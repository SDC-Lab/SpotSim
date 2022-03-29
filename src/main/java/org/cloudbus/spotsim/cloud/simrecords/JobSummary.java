package org.cloudbus.spotsim.cloud.simrecords;


import org.cloudbus.cloudsim.util.workload.Job;
import org.cloudbus.cloudsim.util.workload.Job.JobStatus;
import org.cloudbus.spotsim.cloud.InstanceType;

/**
 * For simulation statistics only. Adds some fields that are not contained in CloudSim's Job class
 */
public class JobSummary {

    private int jobID;
    private long submitTime;
    private long jobSize;
    private long scheduledStartTime;
    private long actualStartTime;
    private long actualEndTime;
    private long deadline;
    private double budget;
    private InstanceType instanceTypeUsed;
    private boolean startedANewInstance;
    private long estimatedRunTime;
    private JobStatus finalStatus;
    private double estimatedCost;
    private double actualCost;
    
    public JobSummary(Job j) {
	this.jobID = j.getCloudlet().getCloudletId();
	this.submitTime = j.getSubmitTime();
	this.jobSize = j.getCloudlet().getCloudletLength();
	this.deadline = j.getDeadline();
	this.budget = j.getBudget();
    }
  
    
    public int getJobID() {
		return jobID;
	}


	public void setJobID(int jobID) {
		this.jobID = jobID;
	}


	public long getSubmitTime() {
		return submitTime;
	}


	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}


	public long getJobSize() {
		return jobSize;
	}


	public void setJobSize(long jobSize) {
		this.jobSize = jobSize;
	}


	public long getScheduledStartTime() {
		return scheduledStartTime;
	}


	public void setScheduledStartTime(long scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}


	public long getActualStartTime() {
		return actualStartTime;
	}


	public void setActualStartTime(long actualStartTime) {
		this.actualStartTime = actualStartTime;
	}


	public long getActualEndTime() {
		return actualEndTime;
	}


	public void setActualEndTime(long actualEndTime) {
		this.actualEndTime = actualEndTime;
	}


	public long getDeadline() {
		return deadline;
	}


	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}


	public double getBudget() {
		return budget;
	}


	public void setBudget(double budget) {
		this.budget = budget;
	}


	public InstanceType getInstanceTypeUsed() {
		return instanceTypeUsed;
	}


	public void setInstanceTypeUsed(InstanceType instanceTypeUsed) {
		this.instanceTypeUsed = instanceTypeUsed;
	}


	public boolean isStartedANewInstance() {
		return startedANewInstance;
	}


	public void setStartedANewInstance(boolean startedANewInstance) {
		this.startedANewInstance = startedANewInstance;
	}


	public long getEstimatedRunTime() {
		return estimatedRunTime;
	}


	public void setEstimatedRunTime(long estimatedRunTime) {
		this.estimatedRunTime = estimatedRunTime;
	}


	public JobStatus getFinalStatus() {
		return finalStatus;
	}


	public void setFinalStatus(JobStatus finalStatus) {
		this.finalStatus = finalStatus;
	}


	public double getEstimatedCost() {
		return estimatedCost;
	}


	public void setEstimatedCost(double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}


	public double getActualCost() {
		return actualCost;
	}


	public void setActualCost(double actualCost) {
		this.actualCost = actualCost;
	}


	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.jobID).append(",").append(this.submitTime)
    	.append(",").append(this.jobSize).append(",")
    	.append(this.scheduledStartTime).append(",").append(this.actualStartTime)
    	.append(",").append(this.actualEndTime).append(",").append(this.deadline)
    	.append(",").append(this.budget).append(",").append(this.instanceTypeUsed)
    	.append(",").append(this.startedANewInstance).append(",")
    	.append(this.estimatedRunTime).append(",").append(this.finalStatus)
    	.append(",").append(this.estimatedCost).append(",")
    	.append(this.actualCost);
        return builder.toString();
    }
}
