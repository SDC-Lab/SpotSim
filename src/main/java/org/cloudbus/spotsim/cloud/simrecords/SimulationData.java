package org.cloudbus.spotsim.cloud.simrecords;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.util.workload.Job;
import org.cloudbus.cloudsim.util.workload.Job.JobStatus;
import org.cloudbus.spotsim.cloud.Instance;
import org.cloudbus.spotsim.cloud.InstanceType;
import org.cloudbus.spotsim.cloud.broker.SchedulingDecision;

/**
 * For result analysis purpose
 */
public class SimulationData {
    
    private GregorianCalendar startDate;

    private double estimatedCost = 0D;

    private double estimatedRunTime = 0D;

    private double actualCost = 0D;

    private double actualRuntime = 0D;
    
    private double waitingTime = 0;
    
    private int jobsSubmitted = 0;
    
    private int jobsCompleted = 0;
    
    private int jobsFailed = 0;

    private int instancesRequested = 0;
    
    private int instancesReceived = 0 ;

    private int instancesTerminated = 0;

    private double totalIdleTime  = 0;

    private double totalInstanceTime  = 0D;

    private double utilization;

    private int deadlineBreach = 0;
    
    private Map<InstanceType,Integer> instanceTypesUsed;

    private double wasted = 0D;
    
    private LinkedHashMap<Integer, JobSummary> jobs;

    
    public GregorianCalendar getStartDate() {
		return startDate;
	}

	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	public double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public double getEstimatedRunTime() {
		return estimatedRunTime;
	}

	public void setEstimatedRunTime(double estimatedRunTime) {
		this.estimatedRunTime = estimatedRunTime;
	}

	public double getActualCost() {
		return actualCost;
	}

	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}

	public double getActualRuntime() {
		return actualRuntime;
	}

	public void setActualRuntime(double actualRuntime) {
		this.actualRuntime = actualRuntime;
	}

	public double getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}

	public int getJobsSubmitted() {
		return jobsSubmitted;
	}

	public void setJobsSubmitted(int jobsSubmitted) {
		this.jobsSubmitted = jobsSubmitted;
	}

	public int getJobsCompleted() {
		return jobsCompleted;
	}

	public void setJobsCompleted(int jobsCompleted) {
		this.jobsCompleted = jobsCompleted;
	}

	public int getJobsFailed() {
		return jobsFailed;
	}

	public void setJobsFailed(int jobsFailed) {
		this.jobsFailed = jobsFailed;
	}

	public int getInstancesRequested() {
		return instancesRequested;
	}

	public void setInstancesRequested(int instancesRequested) {
		this.instancesRequested = instancesRequested;
	}

	public int getInstancesReceived() {
		return instancesReceived;
	}

	public void setInstancesReceived(int instancesReceived) {
		this.instancesReceived = instancesReceived;
	}

	public int getInstancesTerminated() {
		return instancesTerminated;
	}

	public void setInstancesTerminated(int instancesTerminated) {
		this.instancesTerminated = instancesTerminated;
	}

	public double getTotalIdleTime() {
		return totalIdleTime;
	}

	public void setTotalIdleTime(double totalIdleTime) {
		this.totalIdleTime = totalIdleTime;
	}

	public double getTotalInstanceTime() {
		return totalInstanceTime;
	}

	public void setTotalInstanceTime(double totalInstanceTime) {
		this.totalInstanceTime = totalInstanceTime;
	}

	public double getUtilization() {
		return utilization;
	}

	public void setUtilization(double utilization) {
		this.utilization = utilization;
	}

	public int getDeadlineBreach() {
		return deadlineBreach;
	}

	public void setDeadlineBreach(int deadlineBreach) {
		this.deadlineBreach = deadlineBreach;
	}

	public Map<InstanceType, Integer> getInstanceTypesUsed() {
		return instanceTypesUsed;
	}

	public void setInstanceTypesUsed(Map<InstanceType, Integer> instanceTypesUsed) {
		this.instanceTypesUsed = instanceTypesUsed;
	}

	public double getWasted() {
		return wasted;
	}

	public void setWasted(double wasted) {
		this.wasted = wasted;
	}

	public LinkedHashMap<Integer, JobSummary> getJobs() {
		return jobs;
	}

	public void setJobs(LinkedHashMap<Integer, JobSummary> jobs) {
		this.jobs = jobs;
	}

	public SimulationData() {
	this.instanceTypesUsed = new HashMap<InstanceType, Integer>();
	this.jobs = new LinkedHashMap<Integer, JobSummary>();
	InstanceType[] types = InstanceType.values();
	for (InstanceType instanceType : types) {
	    this.instanceTypesUsed.put(instanceType, 0);
	}
    }
    
    public void incrInstanceTypeUsed(InstanceType type) {
	this.instanceTypesUsed.put(type, this.instanceTypesUsed.get(type)+1);
    }
    
    public void incrEstimatedCost(double add) {
	this.estimatedCost += add;
    }
    
    public void incrEstimatedRunTime(double add) {
	this.estimatedRunTime += add;
    }
    
    public void incrActualCost(double add) {
	this.actualCost += add;
    }
    
    public void incrWastedMoney(double add) {
	this.wasted += add;
    }
    
    
    public void incrActualRuntime(double add) {
	this.actualRuntime += add;
    }
    
    public void incrWaitingTime(double add) {
	this.waitingTime += add;
    }
    
    public void incrInstancesRequested() {
	this.instancesRequested++;
    }
    
    public void incrInstancesReceived() {
	this.instancesReceived++;
    }
    
    public void incrInstancesTerminated() {
	this.instancesTerminated++;
    }
    
    public void incrJobsSubmitted() {
	this.jobsSubmitted++;
    }
    
    public void incrJobsCompleted() {
	this.jobsCompleted++;
    }
    
    public void incrJobsFailed() {
	this.jobsFailed++;
    }
    
    public int getInstancesRunning() {
	return this.instancesReceived - this.instancesTerminated;
    }
    
    public int getJobsRunning() {
	return this.jobsSubmitted - this.jobsCompleted; 
    }
    
    public void incrInstancesIdleTime(long idleTime) {
	this.totalIdleTime += idleTime;
    }
    
    public double computeUtilization() {
	return (this.totalInstanceTime - this.totalIdleTime) / this.totalInstanceTime * 100;
    }
    
    public void incrTotalInstanceTime(double runTimeInHours) {
	this.totalInstanceTime += runTimeInHours;
	this.utilization = computeUtilization();
    }
    
    public void incrDeadlineBreach() {
	    this.deadlineBreach++;
	}
    
    public double getAverageWaitingTime() {
	
	return this.waitingTime / this.jobsCompleted;
    }
    
    public double getAverageRuntime() {
	
	return this.actualRuntime / this.jobsCompleted;
    }
    
    private JobSummary getJob(int id) {
	if (!this.jobs.containsKey(id) ) {
	    throw new RuntimeException("Job summary: " + id + " does not exist");
	}
	return this.jobs.get(id);
    }
    
    public void addJob(Job j) {
	incrJobsSubmitted();
	JobSummary jobSummary = new JobSummary(j);
	this.jobs.put(j.getID(), jobSummary);
    }
    
    public void jobSubmittedToCloud(long clock, Job jobToRun, Instance instance) {
	long jobWaitTime = jobToRun.getActualStartTime() - jobToRun.getSubmitTime();
	incrWaitingTime(jobWaitTime);
	JobSummary jobSummary = getJob(jobToRun.getID());
	jobSummary.setActualStartTime(jobToRun.getActualStartTime());
	jobSummary.setInstanceTypeUsed(instance.getType());
    }
    
    public void jobScheduled(SchedulingDecision dec) {
	
	JobSummary jobSummary = getJob(dec.getJob().getID());
	
	incrEstimatedCost(dec.getCost());
	incrEstimatedRunTime(dec.getEstimatedRuntime());
	jobSummary.setStartedANewInstance(dec.isStartNewInstance());
	jobSummary.setEstimatedRunTime(dec.getEstimatedRuntime());
	jobSummary.setEstimatedCost(dec.getCost());
	jobSummary.setScheduledStartTime(dec.getStartTime());
    }
    
    public void jobFinished(Job finishedJob) {
	JobSummary jobSummary = getJob(finishedJob.getID());
	jobSummary.setActualEndTime(finishedJob.getActualEndTime());
	jobSummary.setFinalStatus(JobStatus.COMPLETED);
	incrActualRuntime(finishedJob.timeTaken());
	incrJobsCompleted();
    }
    
    @Override
    public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append(Log.formatDate(this.startDate.getTime())).append(",")
		.append(this.estimatedCost).append(",").append(this.estimatedRunTime)
		.append(",").append(this.actualCost).append(",")
		.append(this.actualRuntime).append(",").append(this.waitingTime)
		.append(",").append(this.jobsSubmitted).append(",")
		.append(this.jobsCompleted).append(",").append(this.jobsFailed)
		.append(",").append(this.instancesRequested).append(",")
		.append(this.instancesReceived).append(",").append(this.instancesTerminated)
		.append(",").append(this.totalIdleTime).append(",")
		.append(this.totalInstanceTime).append(",").append(this.utilization)
		.append(",").append(this.deadlineBreach).append(",")
		.append(this.instanceTypesUsed).append(",").append(this.wasted);
	    return builder.toString();
	}
    
    public String toString(int i) {
	    StringBuilder builder = new StringBuilder();
//	    String st = "date";
	    builder.append(Log.formatDate(this.startDate.getTime())).append("\nestimatedCost, ")
		.append(this.estimatedCost).append("\nestimatedRunTime, ").append(this.estimatedRunTime)
		.append("\nactualCost, ").append(this.actualCost).append("\nactualRuntime, ")
		.append(this.actualRuntime).append("\nwaitingTime, ").append(this.waitingTime)
		.append("\njobsSubmitted, ").append(this.jobsSubmitted).append("\njobsCompleted, ")
		.append(this.jobsCompleted).append("\njobsFailed, ").append(this.jobsFailed)
		.append("\ninstancesRequested, ").append(this.instancesRequested).append("\ninstancesReceived, ")
		.append(this.instancesReceived).append("\ninstancesTerminated, ").append(this.instancesTerminated)
		.append("\ntotalIdleTime, ").append(this.totalIdleTime).append("\ntotalInstanceTime, ")
		.append(this.totalInstanceTime).append("\nutilization, ").append(this.utilization)
		.append("\ndeadlineBreach, ").append(this.deadlineBreach).append("\ninstanceTypesUsed, ")
		.append(this.instanceTypesUsed).append("\nwasted, ").append(this.wasted);
	    return builder.toString();
	}
}
