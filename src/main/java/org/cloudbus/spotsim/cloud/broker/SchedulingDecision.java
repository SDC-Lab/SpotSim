package org.cloudbus.spotsim.cloud.broker;

import java.util.Comparator;


import org.cloudbus.cloudsim.util.workload.Job;
import org.cloudbus.spotsim.cloud.Instance;
import org.cloudbus.spotsim.cloud.InstanceType;

public class SchedulingDecision {
    
    private final Job job;

    private final InstanceType chosenInstanceType;
    
    private final Instance instance;
    
    private boolean startNewInstance;

    private final long startTime;
    
    private final double cost;
    
    private final double bidPrice;
    

    public SchedulingDecision(Job job, InstanceType chosenInstanceType,
			Instance instance, boolean startNewInstance, long startTime,
			double cost, double bidPrice) {
		super();
		this.job = job;
		this.chosenInstanceType = chosenInstanceType;
		this.instance = instance;
		this.startNewInstance = startNewInstance;
		this.startTime = startTime;
		this.cost = cost;
		this.bidPrice = bidPrice;
	}

    
	public boolean isStartNewInstance() {
		return startNewInstance;
	}

	public void setStartNewInstance(boolean startNewInstance) {
		this.startNewInstance = startNewInstance;
	}

	public static Comparator<SchedulingDecision> getTimeComparator() {
		return timeComparator;
	}

	public static void setTimeComparator(
			Comparator<SchedulingDecision> timeComparator) {
		SchedulingDecision.timeComparator = timeComparator;
	}

	public Job getJob() {
		return job;
	}

	public InstanceType getChosenInstanceType() {
		return chosenInstanceType;
	}

	public Instance getInstance() {
		return instance;
	}

	public long getStartTime() {
		return startTime;
	}

	public double getCost() {
		return cost;
	}

	public double getBidPrice() {
		return bidPrice;
	}


	private static Comparator<SchedulingDecision> timeComparator = new Comparator<SchedulingDecision>() {

	@Override
	public int compare(SchedulingDecision o1, SchedulingDecision o2) {
	    
	    return new Long(o1.getStartTime()).compareTo(o2.getStartTime());
	}
    };
    
    public long getEstimatedRuntime() {
	return (long) (this.job.getCloudlet().getCloudletLength() / this.chosenInstanceType.getComputePower());
    }
    
    public static Comparator<SchedulingDecision> timeComparator() {
	return timeComparator;
    }
    
    /**
     * Check potential inconsistencies (for debugging purposes)
     */
    public void checkSanity() {
	if (this.startTime < 0) {
	    throw new Error("Insane scheduling decision. Start time is negative: " + this.startTime);
	}
	if (this.instance == null && this.chosenInstanceType == null) {
	    throw new Error("Insane scheduling decision: No instance was chosen");
	}
    }
}
