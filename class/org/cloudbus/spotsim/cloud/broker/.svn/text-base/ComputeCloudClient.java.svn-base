package org.cloudbus.spotsim.cloud.broker;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.util.workload.Job;
import org.cloudbus.cloudsim.util.workload.Job.JobStatus;
//import org.cloudbus.replica.vm.ec2.broker.ComputeCloudTest;
import org.cloudbus.spotsim.cloud.Accounting;
import org.cloudbus.spotsim.cloud.ComputeCloud;
import org.cloudbus.spotsim.cloud.ComputeCloudTags;
import org.cloudbus.spotsim.cloud.Instance;
import org.cloudbus.spotsim.cloud.InstanceFactory;
import org.cloudbus.spotsim.cloud.InstanceStatus;
import org.cloudbus.spotsim.cloud.InstanceType;
import org.cloudbus.spotsim.cloud.PriceForecastingMethod;
import org.cloudbus.spotsim.cloud.RunJobRequest;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.PriceModel;
import org.cloudbus.spotsim.cloud.InstanceType.Region;
import org.cloudbus.spotsim.cloud.payloads.InstanceCreatedNotification;
import org.cloudbus.spotsim.cloud.payloads.InstanceTerminatedNotification;
import org.cloudbus.spotsim.cloud.payloads.RunInstanceRequest;
import org.cloudbus.spotsim.cloud.payloads.TerminateInstancesRequest;
import org.cloudbus.spotsim.cloud.simrecords.SimulationData;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceHistory;
import org.cloudbus.spotsim.util.SimParameters;

/**
 * @see ComputeCloudTest
 * @author William Voorsluys - williamvoor@gmail.com
 * 
 */
public class ComputeCloudClient extends SimEntity {

    private static final OS DEFAULT_OS = OS.LINUX;
    private static final Region DEFAULT_REGION = Region.US_EAST;
    private static final long VM_INIT_TIME = 0;
    private static final PriceForecastingMethod PRICING_METHOD = PriceForecastingMethod.AVERAGE;
	protected boolean requestPersistent = false;

    private ComputeCloud provider;
    static Random r = new Random();

    private enum DeadlineStrictness {
	STRICT {
	    @Override
	    double multiplier() {
		return r.nextDouble(); // 0-1
	    }
	},
	MEDIUM {
	    @Override
	    double multiplier() {
		return r.nextDouble() * 2 + 3; // 3-5
	    }
	},
	LOOSE {
	    @Override
	    double multiplier() {
		return r.nextDouble() * 5 + 5; // 5-10
	    }
	};

	abstract double multiplier();

	static DeadlineStrictness rnd() {
	    return DeadlineStrictness.values()[new Random()
		.nextInt(DeadlineStrictness.values().length)];
	}
    }

    /**
     * Contains all jobs
     */
    protected final Map<Integer, Job> jobList;

    /**
     * Contains jobs that have failed
     */
    protected final Map<Integer, Job> failedList;

    protected int jobsToDo;
    
    public SimulationData data = new SimulationData();

    public ComputeCloudClient(String name, ComputeCloud provider) throws Exception {
	super(name);
	this.provider = provider;
	this.jobList = new LinkedHashMap<Integer, Job>();
	this.failedList = new LinkedHashMap<Integer, Job>();
	this.data.setStartDate(SpotPriceHistory.getSimPeriodStart());
    if (SimParameters.WORKLOAD_PERSISTENT.getValue().equals("yes")){
    	this.requestPersistent = true;
    }

    }

    @Override
    public void startEntity() {

	Log.logger.info("Starting simulation at date: "
		+ Log.formatDate(SpotPriceHistory.getSimPeriodStart().getTime()));
	generateBudgetAndDeadlines();
	submit();
    }

    protected void generateBudgetAndDeadlines() {
	for (Job j : this.jobList.values()) {
	    j.setDeadline(computeDeadline(j));
	    j.setBudget(computeBudget(j));
	}
    }

    private double computeBudget(Job j) {
	double hours = j.getCloudlet().getCloudletLength() / 60;
	double onDemandPrice = InstanceType.M1SMALL.getOnDemandPrice(DEFAULT_REGION, OS.LINUX);
	double minimumSpotPrice = InstanceType.M1SMALL.getMinimumSpotPrice(DEFAULT_REGION, OS.LINUX);

	return hours * ((onDemandPrice - minimumSpotPrice) * r.nextDouble() + minimumSpotPrice);
    }

    /*
     * Generates random deadlines
     */
    private long computeDeadline(Job j) {
	long minDeadline = 600; // 10 minutes

	double delta = j.getCloudlet().getCloudletLength() * DeadlineStrictness.rnd().multiplier();

	return (long) (j.getSubmitTime() + Math.max(minDeadline, j.getCloudlet()
	    .getCloudletLength() + delta));
    }

    /*
     * Decides, based on the workload trace, when to submit which job and sends
     * an event to itself to schedule the job
     */
    protected void submit() {

	Collection<Job> jobs = this.jobList.values();
	for (Job job : jobs) {
	    send(getId(), job.getSubmitTime(), ComputeCloudTags.NEW_JOB_ARRIVED.tag(), job);
	}
    }

    protected void schedule(Job j) {

	SchedulingDecision dec = sched(j);

	dec.checkSanity();

	this.data.jobScheduled(dec);
	j.setEstimatedRunTime(dec.getEstimatedRuntime());
	j.setScheduledStartTime(dec.getStartTime());

	Instance instance = null;

	if (dec.isStartNewInstance()) {
	    requestNewInstance(dec);
	} else {
	    instance = dec.getInstance();
	    if (instance.getStatus().equals(InstanceStatus.MARKED_FOR_TERMINATION)) {
		// instance is idle. submit job immediately
		submitJobToCloud(instance, j);
	    } else {
		// instance is busy, put job in the queue
		instance.scheduleJob(j);
	    }
	}
	Log.logger.info(Log.clock() + " Scheduling Decision: " + dec + ", total cost: "
		+ this.data.getEstimatedCost() + ", total runtime: "
		+ this.data.getEstimatedRunTime() + ", instanceID " + instance);
    }

    protected void requestNewInstance(SchedulingDecision dec) {

	this.data.incrInstancesRequested();

	RunInstanceRequest req = new RunInstanceRequest(dec.getChosenInstanceType(), dec.getJob(),
	    PriceModel.SPOT, DEFAULT_OS, dec.getBidPrice(), getId(), DEFAULT_REGION);

	sendNow(this.provider.getId(), ComputeCloudTags.RUN_INSTANCE.tag(), req);
    }

    /*
     * Does the scheduling Firstly: tries to fit the job in an existing idle
     * instance. Secondly: tries to queue the job in an instance that will be
     * idle soon. Finally, allocates a brand new instance for this job
     */
    protected SchedulingDecision sched(Job job) {
	long startTime = 0;
	double bidPrice;

//	InstanceType[] values = InstanceType.values();
	
	// only one instances
	InstanceType[] values = new InstanceType[] { InstanceType.M1SMALL };

	double minCost = Double.MAX_VALUE;
	InstanceType chosenInstanceType = InstanceType.M1SMALL;

	long longestEstimatedRunTime = (long) (job.getCloudlet().getCloudletLength() / InstanceType.M1SMALL
	    .getComputePower());
	long shortestEstimatedRunTime = (long) (job.getCloudlet().getCloudletLength() / InstanceType.maxCPUUnitsInstance
	    .getComputePower());

	long maxStartTime = job.getDeadline() - longestEstimatedRunTime - VM_INIT_TIME;

	final long increment = 120;

	if (job.getSubmitTime() > maxStartTime) {
	    // unlikely (for debugging purposes)
	    throw new Error("Job cannot be scheduled");
	}

	// search resources among active instances
	Instance suitableInstance = null;
	double recyclePrice = Double.MAX_VALUE;
	double earliestCompletionTime = Double.MAX_VALUE;
	List<Instance> instancesByClient = InstanceFactory.singleton()
	    .getInstancesByClient(getId());
	Log.logger.info(Log.clock()
		+ " New job arrived: "
		+ job.getID()
		+ ", length: "
		+ job.getCloudlet().getCloudletLength()
		+ ", idle instances: "
		+ InstanceFactory.singleton().countInstancesByStatus(
		    InstanceStatus.MARKED_FOR_TERMINATION, getId()) + ", total: "
		+ instancesByClient.size());
	for (Instance instance : instancesByClient) {
	    if (instance.getStatus().isUsable()) {
		double estimatedTime = job.getCloudlet().getCloudletLength()
			/ instance.getType().getComputePower();
		long expectedIdleTime = instance.getExpectedIdleTime();
		long nextFullHour = instance.getNextFullHour();
		long gratisSeconds = instance.getIdleSecondsToNextHour();

		Log.logger.finer(Log.clock() + "Trying to reuse: " + instance.getId()
			+ ", expected idle: " + expectedIdleTime + ", idle seconds: "
			+ gratisSeconds + ", status: " + instance.getStatus());

		if (expectedIdleTime <= maxStartTime) {
		    if (estimatedTime <= gratisSeconds) {
			/*
			 * Job can be fit in less than one hour that has already
			 * been paid (execution is thus free, gratis)
			 */
			recyclePrice = 0;
			double complTime = expectedIdleTime + estimatedTime;
			if (complTime < earliestCompletionTime) {
			    earliestCompletionTime = complTime;
			    suitableInstance = instance;
			}
		    } else {
			double p = Double.MAX_VALUE;
			switch (PRICING_METHOD) {
			case OPTIMAL:
			    p = Accounting.billSpot(nextFullHour,
				(long) (nextFullHour + estimatedTime), instance.getType(),
				InstanceStatus.TERMINATED_BY_USER, instance.getOs(),
				instance.getRegion());
			    break;
			case AVERAGE:
//			    double defaultForecast = SpotPriceHistory.defaultForecast(
//				instance.getType(), instance.getOs(), instance.getRegion(),
//				nextFullHour);
//			    double payableTime = estimatedTime - gratisSeconds;
//			    double estimatedPayableHours = Math.ceil(payableTime / 3600D);
//			    p = estimatedPayableHours * defaultForecast;
			    break;
			}
			if (p < recyclePrice) {
			    recyclePrice = p;
			    suitableInstance = instance;
			}
		    }
		}
	    }
	}

	if (suitableInstance != null && recyclePrice == 0) {
	    Log.logger.fine(Log.clock() + "Reusing instance " + suitableInstance.getId()
		    + " for job " + job.getID() + ", idle time: "
		    + suitableInstance.getExpectedIdleTime() + ", status: "
		    + suitableInstance.getStatus());
	    return new SchedulingDecision(job, suitableInstance.getType(), suitableInstance, false,
		suitableInstance.getExpectedIdleTime(), 0D, 0D);
	}

	for (InstanceType instanceType : values) {
	    double estimatedTime = job.getCloudlet().getCloudletLength()
		    / instanceType.getComputePower();
	    long estimatedHours = (long) Math.ceil(estimatedTime / 3600D);

	    double costForANew = Double.MAX_VALUE;
	    switch (PRICING_METHOD) {
	    case OPTIMAL:
		// swipes future prices to find a good spot to fit the job
		for (long i = job.getSubmitTime(); i < maxStartTime; i += increment) {
		    costForANew = Accounting.billSpot(i, (long) (i + estimatedTime), instanceType,
			InstanceStatus.TERMINATED_BY_USER, DEFAULT_OS, DEFAULT_REGION);
		    System.out.println("Type: " + instanceType + ", time : " + estimatedHours
			    + ", cost for a new: " + costForANew);
		}
		break;
	    case AVERAGE:
//		double forecast = SpotPriceHistory.defaultForecast(instanceType, DEFAULT_OS,
//		    DEFAULT_REGION, job.getSubmitTime());
//		costForANew = estimatedHours * forecast;
		break;
	    }

	    if (costForANew < minCost) {
		minCost = costForANew;
		chosenInstanceType = instanceType;
		startTime = job.getSubmitTime();
	    }
	}

	if (suitableInstance != null && recyclePrice <= minCost) {
	    Log.logger.fine(Log.clock() + " Reusing instance " + suitableInstance.getId()
		    + " for job " + job.getID() + ", idle time: "
		    + suitableInstance.getExpectedIdleTime() + ", status: "
		    + suitableInstance.getStatus());
	    job.setScheduledStartTime(suitableInstance.getExpectedIdleTime());
	    return new SchedulingDecision(job, suitableInstance.getType(), suitableInstance, false,
		suitableInstance.getExpectedIdleTime(), recyclePrice, 0D);
	}

//	double bidPrice = chosenInstanceType.getOnDemandPrice(DEFAULT_REGION, OS.LINUX);
	bidPrice = Double.parseDouble(SimParameters.CLOUD_SPOT_BID.getValue());

	if (bidPrice==0) 
		// user bid as high as on-demand instance 
		bidPrice = chosenInstanceType.getOnDemandPrice(DEFAULT_REGION, OS.LINUX);

	return new SchedulingDecision(job, chosenInstanceType, null, true, startTime, minCost,
	    bidPrice);
    }

    @Override
    public void processEvent(SimEvent ev) {

	int tag = ev.getTag();
	if (tag >= ComputeCloudTags.BASE) {
	    ComputeCloudTags ec2EventTag = ComputeCloudTags.fromTag(tag);
	    switch (ec2EventTag) {
	    case INSTANCE_CREATED:
		InstanceCreatedNotification createNotif = (InstanceCreatedNotification) ev
		    .getData();
		instanceCreated(createNotif.getInstance());
		break;
	    case INSTANCE_TERMINATED:
		InstanceTerminatedNotification termNotif = (InstanceTerminatedNotification) ev
		    .getData();
		instanceTerminated(termNotif.getInstance());
		break;
	    case NEW_JOB_ARRIVED:
		Job j = (Job) ev.getData();
		newJobArrived(j);
		break;
	    case JOB_FINISHED:
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		jobFinished(cloudlet);
		break;
		
	    default:
	    	//Bahman
	    	processOtherEvent(ev);
	    break;
	    }
	} else {
	    switch (tag) {
	    case CloudSimTags.CLOUDLET_RETURN:
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		jobFinished(cloudlet);
		break;
	    case CloudSimTags.END_OF_SIMULATION:
	    	shutdownEntity();
	    break;
	    default:
		throw new RuntimeException("Unexpected event " + tag
			+ " cannot be processed by the client");
	    }
	}
    }

    protected void instanceTerminated(Instance instance) {
	Log.logger.warning(Log.clock()
		+ " Instances: "
		+ this.data.getInstancesRunning()
		+ ", Idle: "
		+ InstanceFactory.singleton().countInstancesByStatus(
		    InstanceStatus.MARKED_FOR_TERMINATION, getId()) + ", Jobs running: "
		+ this.data.getJobsRunning() + ", to do: " + this.jobsToDo);
	this.data.incrInstancesTerminated();

	if (instance.getStatus().equals(InstanceStatus.OUT_OF_BID)) {
	    List<Job> failedJobs = instance.getRunningJobs();
	    for (Job failedJob : failedJobs) {
		this.data.incrJobsFailed();
		this.failedList.put(failedJob.getID(), failedJob);
		failedJob.setStatus(JobStatus.FAILED);
		this.data.incrWastedMoney(instance.getCost());
		// for persistent workloads
		if (this.requestPersistent){
			PriorityQueue<Job> scheduledJobs = instance.getScheduledJobs();
			// send the request to create a new instance with a job at the head of queue
//			sendNow(getId(), ComputeCloudTags.NEW_JOB_ARRIVED.tag(), scheduledJobs.poll());
			sendNow(getId(), ComputeCloudTags.CREATE_NEW_INSTANCE.tag(), scheduledJobs.poll());
			for (Job job : scheduledJobs) {
				send(getId(), 0.01, ComputeCloudTags.NEW_JOB_ARRIVED.tag(), job);
				
			}
	    	}
	    }
	}
	this.data.incrActualCost(instance.getCost());
	this.data.incrInstancesIdleTime(instance.getIdleTime());
	this.data.incrTotalInstanceTime(instance.runTime());
	Log.logger.info(Log.clock() + "Instance terminated: " + instance.getId() + ", cost: "
		+ instance.getCost() + ", run time: " + instance.runTimeInHours() + "( from "
		+ instance.getAccStart() + " to " + instance.getAccEnd() + "), idle time: "
		+ instance.getIdleTime());
    }

    private void instanceCreated(Instance instance) {
	this.data.incrInstancesReceived();
	this.data.incrInstanceTypeUsed(instance.getType());
	instance.setStatus(InstanceStatus.RUNNING_IDLE);
	Job nextScheduledJob = instance.getNextScheduledJob();
	submitJobToCloud(instance, nextScheduledJob);
	Log.logger.fine(Log.clock() + "Instance received by client. ID:" + instance.getId()
		+ ". Job waiting: " + nextScheduledJob.getID());
    }

    protected void newJobArrived(Job j) {
	this.data.addJob(j);
	schedule(j);
	j.setStatus(JobStatus.SCHEDULED);
    }

    protected void submitJobToCloud(Instance instance, Job jobToRun) {
    
	jobToRun.getCloudlet().setVmId(instance.getId());
	jobToRun.setStatus(JobStatus.RUNNING);
	jobToRun.setActualStartTime((long) CloudSim.clock());
	this.data.jobSubmittedToCloud((long) CloudSim.clock(), jobToRun, instance);
	instance.runJob(jobToRun);
	Log.logger.info(Log.clock() + getName() + ": Sending cloudlet "
		+ jobToRun.getCloudlet().getCloudletId() + " to VM #" + instance.getId()
		+ ". Expected completion time: "
		+ (CloudSim.clock() + jobToRun.getEstimatedRunTime())
		+ ", was scheduled to start: " + jobToRun.getScheduledStartTime());
	RunJobRequest runJobRequest = new RunJobRequest(jobToRun, instance);
	sendNow(this.provider.getId(), ComputeCloudTags.RUN_JOB_ON_INSTANCE.tag(), runJobRequest);
    }

    @Override
    public void shutdownEntity() {
    	System.out.println("Summary: " + this.data);
	// System.out.println(this.jobList);

    }

    public void addJobs(Collection<Job> jobs) {
	Job[] j = new Job[jobs.size()];
	this.addJobs(jobs.toArray(j));
	generateBudgetAndDeadlines();
    }

    public void addJobs(Job... jobs) {
	for (Job job : jobs) {
	    job.getCloudlet().setUserId(getId());
	    this.jobList.put(job.getID(), job);
	}
	this.jobsToDo += this.jobList.size();
    }

    protected void jobFinished(Cloudlet cloudlet) {
	Instance instance = InstanceFactory.singleton().getInstanceById(cloudlet.getVmId());
	Job finishedJob = this.jobList.get(cloudlet.getCloudletId());
	finishedJob.setStatus(JobStatus.COMPLETED);
	finishedJob.setActualEndTime((long) CloudSim.clock());
	finishedJob.checkSanity();
	int remainingJobs = instance.jobDone(finishedJob);
	if (finishedJob.getActualEndTime() > finishedJob.getDeadline()) {
	    this.data.incrDeadlineBreach();
	}

	long secondsToNextFullHour = instance.getSecondsToNextFullHour();
	if (remainingJobs == 0) {
	    // will be terminated at next full hour if no job reuses it
	    instance.setStatus(InstanceStatus.MARKED_FOR_TERMINATION);
	    terminateInstance(instance, secondsToNextFullHour);
	} else {
	    Job nextJob = instance.getNextScheduledJob();
	    submitJobToCloud(instance, nextJob);
	}

	this.data.jobFinished(finishedJob);

	Log.logger.info(Log.clock() + "Job " + cloudlet.getCloudletId()
		+ " finished. Submitted at " + finishedJob.getScheduledStartTime()
		+ ", Time taken: " + finishedJob.timeTaken() + ", expected was: "
		+ finishedJob.getEstimatedRunTime() + ", jobs in queue: " + remainingJobs);

	this.jobsToDo--;
//	if (getJobList().size() == 0 && this.jobsToDo == 0) {
	if (this.jobsToDo == 0) {
	    Log.logger.info(Log.clock() + " All Cloudlets executed. Finishing...");
	    // send a finish message to simulator (cool down)
//	    sendNow(getId(),CloudSimTags.END_OF_SIMULATION,instance);

	}

    }

    protected void terminateInstance(Instance instance, long delay) {
	Log.logger.info(Log.clock() + " Requesting termination of instance " + instance.getId()
		+ " in " + delay + " seconds");
	TerminateInstancesRequest req = new TerminateInstancesRequest(instance);
	send(this.provider.getId(), delay, ComputeCloudTags.TERMINATE_INSTANCES.tag(), req);
    }

    public Map<Integer, Job> getJobList() {
	return this.jobList;
    }

    public SimulationData getData() {
	return this.data;
    }
    
    protected void processOtherEvent(SimEvent ev)
    {
        if (ev == null)
        {
            System.out.println(super.getName() + ".processOtherEvent(): " +
                    "Error - an event is null.");
            return;
        }
    }
}