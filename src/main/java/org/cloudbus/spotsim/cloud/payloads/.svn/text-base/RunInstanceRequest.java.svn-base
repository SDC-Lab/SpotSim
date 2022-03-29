package org.cloudbus.spotsim.cloud.payloads;


import org.cloudbus.cloudsim.util.workload.Job;
import org.cloudbus.spotsim.cloud.InstanceType;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.PriceModel;
import org.cloudbus.spotsim.cloud.InstanceType.Region;

public class RunInstanceRequest {

    private final InstanceType type;

    private final Job job;

    private final PriceModel priceModel;

    private final OS os;

    private final double bidPrice;

    private final int brokerID;

    private final Region region;

	public RunInstanceRequest(InstanceType type, Job job,
			PriceModel priceModel, OS os, double bidPrice, int brokerID,
			Region region) {
		super();
		this.type = type;
		this.job = job;
		this.priceModel = priceModel;
		this.os = os;
		this.bidPrice = bidPrice;
		this.brokerID = brokerID;
		this.region = region;
	}

	public InstanceType getType() {
		return type;
	}

	public Job getJob() {
		return job;
	}

	public PriceModel getPriceModel() {
		return priceModel;
	}

	public OS getOs() {
		return os;
	}

	public double getBidPrice() {
		return bidPrice;
	}

	public int getBrokerID() {
		return brokerID;
	}

	public Region getRegion() {
		return region;
	}

}
