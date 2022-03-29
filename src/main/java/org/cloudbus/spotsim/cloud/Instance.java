package org.cloudbus.spotsim.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;


import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.util.workload.Job;
//import org.cloudbus.replica.vm.ec2.InstanceTest;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.PriceModel;
import org.cloudbus.spotsim.cloud.InstanceType.Region;

/**
 * @see InstanceTest
 * 
 */
public class Instance extends Vm {

    private final OS os;

    private final PriceModel pricing;

    private final Region region;

    private transient InstanceStatus status;

    private static final AtomicInteger uniqueID = new AtomicInteger();

    private int datacenterID;

    private final InstanceType type;

    private final double bidPrice;

    private long accStart = -1;

    private long accEnd = -1;

    private long expectedIdleTime = -1;

    private final PriorityQueue<Job> scheduledJobs;

    private final List<Job> runningJobs;

    private int brokerID;

    private double cost;

    private long idleTime = 0L;

    private long latestIdleStart = 0L;

    public int getDatacenterID() {
		return datacenterID;
	}

	public void setDatacenterID(int datacenterID) {
		this.datacenterID = datacenterID;
	}

	public long getAccStart() {
		return accStart;
	}

	public void setAccStart(long accStart) {
		this.accStart = accStart;
	}

	public long getAccEnd() {
		return accEnd;
	}

	public void setAccEnd(long accEnd) {
		this.accEnd = accEnd;
	}

	public int getBrokerID() {
		return brokerID;
	}

	public void setBrokerID(int brokerID) {
		this.brokerID = brokerID;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public long getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}

	public long getLatestIdleStart() {
		return latestIdleStart;
	}

	public void setLatestIdleStart(long latestIdleStart) {
		this.latestIdleStart = latestIdleStart;
	}

	public int getJobsRun() {
		return jobsRun;
	}

	public void setJobsRun(int jobsRun) {
		this.jobsRun = jobsRun;
	}

	public OS getOs() {
		return os;
	}

	public PriceModel getPricing() {
		return pricing;
	}

	public Region getRegion() {
		return region;
	}

	public InstanceStatus getStatus() {
		return status;
	}

	public static AtomicInteger getUniqueid() {
		return uniqueID;
	}

	public InstanceType getType() {
		return type;
	}

	public double getBidPrice() {
		return bidPrice;
	}

	public PriorityQueue<Job> getScheduledJobs() {
		return scheduledJobs;
	}

	public List<Job> getRunningJobs() {
		return runningJobs;
	}

	public void setExpectedIdleTime(long expectedIdleTime) {
		this.expectedIdleTime = expectedIdleTime;
	}

	private int jobsRun = 0;

    public Instance(InstanceType type, OS os, PriceModel pricing, int clientID, int brokerID,
	    double bidPrice, Region region) {
	this(nextId(), type, os, pricing, clientID, brokerID, bidPrice, region);
    }

    public Instance(int id, InstanceType type, OS os, PriceModel pricing, int cloudID,
	    int brokerID, double bidPrice, Region region) {
	super(id, cloudID, type.getComputePower(), 1, type.getMem(), type.getBandwidth(), type
	    .getStorage(), "Xen", new CloudletSchedulerSpaceShared());
	this.brokerID = brokerID;
	this.type = type;
	this.os = os;
	this.pricing = pricing;
	this.bidPrice = bidPrice;
	this.region = region;
	this.status = InstanceStatus.PENDING;
	this.scheduledJobs = new PriorityQueue<Job>(1, Job.submitTimeCompare());
	this.runningJobs = new ArrayList<Job>();
    }

    /* Used for testing */
    Instance(double bidprice) {
	this(InstanceType.M1SMALL, null, null, -1, -1, bidprice, Region.US_EAST);
    }

    private static int nextId() {
	return uniqueID.getAndIncrement();
    }

    public void startAccounting() {
	if (getAccStart() != -1D) {
	    throw new RuntimeException("Cannot start instance accounting: Already started at "
		    + getAccStart());
	}
	long c = (long) CloudSim.clock();
	setAccStart(c);
	setLatestIdleStart(c);
    }

    public void stopAccounting() {
	if (getAccEnd() != -1D) {
	    throw new RuntimeException("Cannot stop instance accounting: Already stopped at "
		    + getAccEnd());
	}
	long clock = (long) CloudSim.clock();
	setAccEnd(clock);
	this.idleTime += clock - this.latestIdleStart;
    }

    public long getUsedTime() {
	return getAccEnd() - getAccStart();
    }

    public boolean isSpot() {
	return this.pricing.equals(PriceModel.SPOT);
    }

    public boolean isReserved() {
	return this.pricing.equals(PriceModel.RESERVED);
    }

    public boolean isOnDemand() {
	return this.pricing.equals(PriceModel.ON_DEMAND);
    }

    public long getIdleSecondsToNextHour() {
	return getNextFullHourAfterIdle() - getExpectedIdleTime();
    }

    public long getNextFullHourAfterIdle() {
	return ((long) Math.floor(getExpectedIdleTime() / 3600) + 1) * 3600;
    }

    public long getNextFullHour() {
	return getAccStart() + ((long) Math.floor(runTimeInHours()) + 1) * 3600;
    }

    public long getSecondsToNextFullHour(long currentTime) {
	return getNextFullHour() - currentTime;
    }

    public long getSecondsToNextFullHour() {
	return getSecondsToNextFullHour((long) CloudSim.clock());
    }

    public void scheduleJob(Job job) {
	this.scheduledJobs.add(job);
	updateExpectedIdleTime();
    }

    public void runJob(Job job) {
	this.jobsRun++;
	setStatus(InstanceStatus.RUNNING_BUSY);
	this.idleTime += (long) CloudSim.clock() - this.latestIdleStart;
	this.runningJobs.add(job);
	updateExpectedIdleTime();
    }

    public int jobDone(Job job) {
	setLatestIdleStart((long) CloudSim.clock());
	setStatus(InstanceStatus.RUNNING_IDLE);
	this.runningJobs.remove(job);
	return this.scheduledJobs.size();
    }

    private void updateExpectedIdleTime() {

	long latestJobWillEndAt = 0;
	for (Job job : this.runningJobs) {
	    long expectedFinishTime = job.getExpectedFinishTime();
	    if (expectedFinishTime > latestJobWillEndAt) {
		latestJobWillEndAt = expectedFinishTime;
	    }
	}

	for (Job job : this.scheduledJobs) {
	    long expectedFinishTime = job.getExpectedFinishTime();
	    if (expectedFinishTime > latestJobWillEndAt) {
		latestJobWillEndAt = expectedFinishTime;
	    }
	}
	this.expectedIdleTime = latestJobWillEndAt;
	Log.logger.info(Log.clock() + " Expected idle time for instance " + getId() + ", "
		+ this.expectedIdleTime + ", jobs: " + this.scheduledJobs.size());
    }

    public Job getNextScheduledJob() {
	return this.scheduledJobs.poll();
    }

    public void bill() {
	this.cost = Accounting.billInstance(this);
    }

    public long runTime() {
	if (getAccEnd() == -1) {
	    return (long) CloudSim.clock() - getAccStart();
	}
	return getAccEnd() - getAccStart();
    }

    public double runTimeInHours() {
	return (double) runTime() / 3600;
    }

    public void setStatus(InstanceStatus status) {
	Log.logger.fine(Log.clock() + " Instance " + getId() + ". Changing state from "
		+ this.status.toString() + " to " + status.toString() + ". Idle time so far: "
		+ this.idleTime + ", jobs run: " + this.jobsRun + ", queue: "
		+ this.scheduledJobs.size());
	this.status = status;
    }

    public long getExpectedIdleTime() {
	if (getStatus().isIdle()) {
	    return (long) CloudSim.clock();
	}
	return this.expectedIdleTime;
    }
}