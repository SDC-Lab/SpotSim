package org.cloudbus.spotsim.cloud.broker;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
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
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.PriceModel;
import org.cloudbus.spotsim.cloud.InstanceType.Region;
import org.cloudbus.spotsim.cloud.payloads.RunInstanceRequest;
import org.cloudbus.spotsim.cloud.simrecords.SimulationData;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceHistory;
import org.cloudbus.spotsim.util.SimParameters;

/**
 * simple broker with only one VM for all jobs
 * 
 * @see ComputeCloudTest
 *   
 * @author Bahman Javadi
 * 
 */

public class ComputeCloudClientSimple extends ComputeCloudClient {
    
    private ComputeCloud provider;
    private double BidPrice;
    private int NoVM;		
    private int NoReqVM;	// number of requested virtual machines
	private PriorityQueue<Job> scheduledJobs;   // scheduledJobs for a terminated instance 

    public ComputeCloudClientSimple(String name, ComputeCloud provider) throws Exception {
	super(name, provider);
	this.provider = provider;
	// we just consider on VM
	this.NoReqVM = 1;
	this.NoVM = 0;
	
 }

	@Override
    public void startEntity() {
	Log.logger.info("Starting simulation at date: "
		+ Log.formatDate(SpotPriceHistory.getSimPeriodStart().getTime()));
	
	this.BidPrice = Double.parseDouble(SimParameters.CLOUD_SPOT_BID.getValue());

	if (this.BidPrice==0) 
		// user bid as high as on-demand instance 
		this.BidPrice = this.provider.type.getOnDemandPrice(this.provider.region, this.provider.os) *10;

	submit();
    }

    @Override
	protected void schedule(Job j) {

    	j.setEstimatedRunTime(getEstimatedRuntime(j,this.provider.type));
    	j.setScheduledStartTime((long) CloudSim.clock());

    	List<Instance> instancesByClient = InstanceFactory.singleton()
	    .getInstancesByClient(getId());

    	if (instancesByClient.isEmpty())
    		// no instance, create one
        	requestNewInstance(j);
    	else{
    		for (Instance instance : instancesByClient) {

    			if (instance.getStatus().equals(InstanceStatus.MARKED_FOR_TERMINATION)) {
    				// instance is idle. submit job immediately
    				submitJobToCloud(instance, j);
    			} else {
    				// instance is busy, put job in the queue
    				instance.scheduleJob(j);
    	    }
    	Log.logger.info(Log.clock() + ", total cost: "
    		+ this.data.getEstimatedCost() + ", total runtime: "
    		+ this.data.getEstimatedRunTime() + ", instanceID " + instance);
        }
    	}
   }
   
    @Override
	protected void generateBudgetAndDeadlines() {
    	for (Job j : this.jobList.values()) {
    	    j.setDeadline(Long.MAX_VALUE);
    	    j.setBudget(Long.MAX_VALUE);
    	}
        }
/*
 * request a new instance
 */
   protected void requestNewInstance(Job job) {

    	this.data.incrInstancesRequested();
    	this.NoVM++;
    	
    	RunInstanceRequest req = new RunInstanceRequest(provider.type, job,
    	    PriceModel.SPOT, provider.os, BidPrice, getId(), provider.region);

    	sendNow(provider.getId(), ComputeCloudTags.RUN_INSTANCE.tag(), req);
        }
   

   public long getEstimatedRuntime(Job job, InstanceType type) {
		return (long) Math.ceil(job.getCloudlet().getCloudletLength() / type.getComputePower());
}
   
   @Override
protected void instanceTerminated(Instance instance) {
		Log.logger.warning(Log.clock()
			+ " Instances Id: "
			+ instance.getId()
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
		    }
			// for persistent workloads
			if (this.requestPersistent){
				this.scheduledJobs = instance.getScheduledJobs();
				requestNewInstance(failedJobs.get(0));
				sendNow(getId(), ComputeCloudTags.ADD_JOB_TO_INSTANCE.tag(),0);
		    }
		}

		this.data.incrActualCost(instance.getCost());
		this.data.incrInstancesIdleTime(instance.getIdleTime());
		this.data.incrTotalInstanceTime(instance.runTime());
		Log.logger.warning(Log.clock() + "Instance terminated, Id: " + instance.getId() + ", cost: "
			+ instance.getCost() + ", run time: " + instance.runTimeInHours() + "( from "
			+ instance.getAccStart() + " to " + instance.getAccEnd() + "), idle time: "
			+ instance.getIdleTime());
		// cool down
		if (super.jobsToDo ==0){ 
			sendNow(provider.getId(), CloudSimTags.END_OF_SIMULATION, instance);
		}
	    }
   
   
 @Override
   protected void processOtherEvent(SimEvent ev){
	int tag = ev.getTag();
	if (tag >= ComputeCloudTags.BASE) {
	    ComputeCloudTags ec2EventTag = ComputeCloudTags.fromTag(tag);
	    switch (ec2EventTag) {
//   	    case WORKLOAD_PERSISTENT:
//			Instance instance = (Instance) ev.getData();
//			remainingJobs = instance.getScheduledJobs();
//	    break;
	    case ADD_JOB_TO_INSTANCE:
	    	List<Instance> instancesByClient = InstanceFactory.singleton().getInstancesByClient(getId());
    		for (Instance instance : instancesByClient){
    			for (Job job : this.scheduledJobs)
    				instance.scheduleJob(job);
    		}
	    break;
	    }
	}
}
   
}