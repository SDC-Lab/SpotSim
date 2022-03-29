package org.cloudbus.spotsim.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.util.workload.Job;
//import org.cloudbus.replica.vm.ec2.broker.ComputeCloudTest;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.Region;
import org.cloudbus.spotsim.cloud.payloads.InstanceCreatedNotification;
import org.cloudbus.spotsim.cloud.payloads.InstanceTerminatedNotification;
import org.cloudbus.spotsim.cloud.payloads.RunInstanceRequest;
import org.cloudbus.spotsim.cloud.payloads.TerminateInstancesRequest;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceHistory;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceRecord;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceHistory.PriceTraceMethod;
import org.cloudbus.spotsim.util.SimParameters;

/**
 * @author William Voorsluys - williamvoor@gmail.com
 * 
 * @see ComputeCloudTest
 * 
 *      Simulates a compute cloud, much like Amazon's EC2. Main methods:
 *      {@link #runInstance(RunInstanceRequest)} (creates an instance)
 *      {@link #terminateInstances(TerminateInstancesRequest)} (terminates one
 *      or more instances)
 * 
 */
public class ComputeCloud extends SimEntity {

    private Map<Integer, Datacenter> datacenters;

    private InstanceFactory instanceDB = InstanceFactory.singleton();

    private Accounting accounting;
   
	public Region region = null;
	public OS os = null;
	public InstanceType type = null;
	protected boolean requestPersistent = false;
	private int BrokerId;
	private double bidPrice;
	protected boolean outofBid;
	static boolean NextPrice = true;
	
    public ComputeCloud() throws Exception {
	this(2000);
   }

    public ComputeCloud(int hostsPerDatacenter) throws Exception {
	super("Cloud");
	this.datacenters = new HashMap<Integer, Datacenter>();
	Datacenter dc1 = createDatacenter("data_center_1", hostsPerDatacenter);
	this.datacenters.put(dc1.getId(), dc1);
	this.accounting = new Accounting();
	this.outofBid = false;
    if (SimParameters.WORKLOAD_PERSISTENT.getValue().equals("yes")){
    	this.requestPersistent = true;
    }
    }

    @Override
    public void startEntity() {
    	
    	NextPrice = true;
    	
    	if (SimParameters.CLOUD_REGION.getValue().equals("us-east"))
    		region = Region.US_EAST;
    	else if (SimParameters.CLOUD_REGION.getValue().equals("us-west"))
    		region = Region.US_WEST;
    	else if (SimParameters.CLOUD_REGION.getValue().equals("eu-west"))
    		region = Region.EUROPE;
    	else if (SimParameters.CLOUD_REGION.getValue().equals("ap-southeast"))
    		region = Region.APAC;

    	if (SimParameters.CLOUD_INSTANCE_OS.getValue().equals("linux"))
    		os = OS.LINUX;
    	else if (SimParameters.CLOUD_INSTANCE_OS.getValue().equals("windows"))
    		os = OS.WINDOWS;
    	
    	// kicks off price variation according to price history logs or random
    	// model
//    	for (InstanceType type : InstanceType.values()) {

    	if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("m1.small"))
    		type = InstanceType.M1SMALL;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("c1.medium"))
    		type = InstanceType.C1MEDIUM;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("m1.large"))
    		type = InstanceType.M1LARGE;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("m2.xlarge"))
    		type = InstanceType.M2XLARGE;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("m1.xlarge"))
    		type = InstanceType.M1XLARGE;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("c1.xlarge"))
    		type = InstanceType.C1XLARGE;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("m2.2xlarge"))
    		type = InstanceType.M22XLARGE;
    	else if (SimParameters.CLOUD_INSTANCE_TYPE.getValue().equals("m2.4xlarge"))
    		type = InstanceType.M24XLARGE;

    	bidPrice = Double.parseDouble(SimParameters.CLOUD_SPOT_BID.getValue());
    	if (bidPrice==0) 
    		// user bid as high as on-demand instance 
    		bidPrice = type.getOnDemandPrice(region, os)*10;

	    SpotPriceRecord nextPriceChange = SpotPriceHistory.getNextPriceChange(type, os,	region);
	    Log.logger.info(Log.clock() + "Scheduling price change for type " + type + ": "
		    + nextPriceChange);
	    sendNow(getId(), ComputeCloudTags.CHANGE_INSTANCE_PRICE.tag(), new PriceChangeEvent(
		type, nextPriceChange));
	}

    @Override
    public void processEvent(SimEvent ev) {

	final int tag = ev.getTag();
	final SimEntity sourceEntity = CloudSim.getEntity(ev.getSource());

	if (tag >= ComputeCloudTags.BASE) {
	    // Handles ComputeCloud specific events
	    switch (ComputeCloudTags.fromTag(tag)) {
	    case RUN_INSTANCE:
		RunInstanceRequest runRequest = (RunInstanceRequest) ev.getData();
		Log.logger.info(Log.clock() + " Instance requested with broker ID: " + runRequest.getBrokerID()
			+ ", " + runRequest.getType());
		runInstance(runRequest);
		break;
	    case TERMINATE_INSTANCES:
		TerminateInstancesRequest terminateRequest = (TerminateInstancesRequest) ev.getData();
		terminateInstances(terminateRequest);
		break;
	    case RUN_JOB_ON_INSTANCE:
		RunJobRequest request = (RunJobRequest) ev.getData();
		runJob(request);
		break;
	    case CHANGE_INSTANCE_PRICE:
		/*
		 * updates price of a spot instance type and schedules the next
		 * price change (if needed)
		 */
	    	if (NextPrice){
	    		PriceChangeEvent changeEvent = (PriceChangeEvent) ev.getData();
	    		SpotPriceRecord price = changeEvent.getPriceRecord();
	    		this.accounting.updatePrice(changeEvent.getType(), changeEvent.getPriceRecord().getPrice());
	    		terminateOutOfBidInstances(changeEvent.getType(), price);
	    		// for persistent workload
	    		checkPendingSpotRequests(changeEvent.getType(), price);
	    		scheduleNextChange(changeEvent.getType(), price);
	    	}
		break;
	    default:
		throw new RuntimeException("Event " + tag + " cannot be processed by the server");
	    }
	} else {
	    // Handles original CloudSim tags
	    switch (tag) {
	    case CloudSimTags.VM_CREATE_ACK:
		// VM was successfully created
		int[] data = (int[]) ev.getData();
		int datacenterId = data[0];
		int vmId = data[1];
		int result = data[2];
		vmCreated(datacenterId, vmId, result == CloudSimTags.TRUE);
		break;
	    case CloudSimTags.CLOUDLET_RETURN:
		Cloudlet c = (Cloudlet) ev.getData();
		Instance inst = InstanceFactory.singleton().getInstanceById(c.getVmId());
		sendNow(inst.getBrokerID(), ComputeCloudTags.JOB_FINISHED.tag(), c);
		break;
	    case CloudSimTags.VM_DESTROY_ACK:
		    int[] dat = (int[]) ev.getData();
			int vmID = dat[1];
			InstanceFactory.singleton().destroyInstance(vmID);
			break;
	    case CloudSimTags.END_OF_SIMULATION:
	    	NextPrice = false;    	
			break;
	    default:
		throw new RuntimeException("Event " + tag + " cannot be processed by the server");
	    }
	}
    }
/**
 * check pending instances
 * @param type
 * @param price
 */
    private void checkPendingSpotRequests(InstanceType type, SpotPriceRecord price) {

    	Set<Instance> pendingSpotRequests = InstanceFactory.singleton()
    	    .getPendingSpotRequests(type);

    	for (Instance instance : pendingSpotRequests) {
    	    if (instance.getBidPrice() >= price.getPrice()) {
    		startInstance(instance);
    	    }
    	}
        }

    private void terminateOutOfBidInstances(InstanceType type, SpotPriceRecord price) {
    	
	Set<Instance> spotInstancesHeadSet = this.instanceDB.getSpotInstancesHeadSet(type, price.getPrice());
	for (Instance instance : spotInstancesHeadSet) {
			instanceOutOfBid(instance);
	}
    }

    private void scheduleNextChange(InstanceType type, SpotPriceRecord price) {
	SpotPriceRecord nextPriceChange = SpotPriceHistory.getNextPriceChange(type, os,
			region);
	if (nextPriceChange != null
		&& nextPriceChange.getDate().before(SpotPriceHistory.getSimPeriodEnd())) {
	    PriceChangeEvent nextChangeEvent = new PriceChangeEvent(type, nextPriceChange);
	    long delay = nextPriceChange.secondsSinceStart() - price.secondsSinceStart();
	    Log.logger.warning(Log.clock() + " Next price change in time: " + CloudSim.clock()+ delay + ", current price: " + price.getPrice());
	    send(getId(), delay, ComputeCloudTags.CHANGE_INSTANCE_PRICE.tag(), nextChangeEvent);
	}
    }

    private void runJob(RunJobRequest request) {

	Cloudlet cloudlet = request.getJob().getCloudlet();
	cloudlet.setUserId(getId());
	sendNow(request.getInstance().getDatacenterID(), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
    }

    private void vmCreated(int datacenterId, int vmId, boolean result) {
	if (result) {
	    Instance instance = this.instanceDB.getInstanceById(vmId);
	    instance.startAccounting();
	    Log.logger.info(Log.clock() + ": " + getName() + ": VM #" + vmId
		    + " has been created in Datacenter #" + datacenterId + ", Host #"
		    + instance.getHost().getId());
	    // notify client
	    send(instance.getBrokerID(),
		computeRunInstanceDelay(instance.getType(), instance.getOs()),
		ComputeCloudTags.INSTANCE_CREATED.tag(), new InstanceCreatedNotification(instance));
	} else {
	    this.instanceDB.getInstanceById(vmId).setStatus(InstanceStatus.FAILED);
	    Log.logger.info(Log.clock() + ": " + getName() + ": Creation of VM #" + vmId
		    + " failed in Datacenter #" + datacenterId);
	}
    }

    @Override
    public void shutdownEntity() {
    	System.out.println(Log.clock() + " End of Simulation");
//    	NextPrice = false;
    }

    public void runInstance(RunInstanceRequest runRequest) {

	Instance instance = InstanceFactory.singleton().newInstance(runRequest.getType(),
	    runRequest.getOs(), runRequest.getPriceModel(), getId(), runRequest.getBrokerID(),
	    runRequest.getBidPrice(), runRequest.getRegion());
	instance.setBrokerID(runRequest.getBrokerID());
    instance.scheduleJob(runRequest.getJob());
	Log.logger.warning(Log.clock() + " Instances Id: " + instance.getId() +" is created.");

	if (isInBid(instance)) {
//	    instance.scheduleJob(runRequest.getJob());
	    startInstance(instance);
	} else {
//	    instanceOutOfBid(instance);
	}
    }

    private void instanceOutOfBid(Instance instance) {
    	
	Log.logger.warning(Log.clock() + " ******** Instance " + instance.getId() + " is out of bid");
	//Bahman
	if (instance.getStatus().isUsable()) {
	    sendNow(instance.getDatacenterID(), CloudSimTags.VM_DESTROY_ACK, instance);
	}
	
	instance.setStatus(InstanceStatus.OUT_OF_BID);
	sendNow(instance.getBrokerID(), ComputeCloudTags.INSTANCE_TERMINATED.tag(),
				new InstanceTerminatedNotification(instance));
    }

    private boolean isInBid(Instance instance) {
	if (instance.isSpot()) {
		Log.logger.info(Log.clock() + " Instance " + instance.getId() + " current price: " + SpotPriceHistory.getPriceAtTime(instance.getType(),
				(long) CloudSim.clock(), os, region));

	    return instance.getBidPrice() > SpotPriceHistory.getPriceAtTime(instance.getType(),
		(long) CloudSim.clock(), os, region);
	}
	return true;
    }

    private void startInstance(Instance instance) {
	Datacenter targetDC = chooseDataCenter();
	instance.setDatacenterID(targetDC.getId());
	instance.setStatus(InstanceStatus.BOOTING);
	instance.setUserId(getId());
	sendNow(targetDC.getId(), CloudSimTags.VM_CREATE_ACK, instance);
    }

    private Datacenter chooseDataCenter() {
	return this.datacenters.values().iterator().next();
    }

    public void terminateInstances(TerminateInstancesRequest terminateRequest) {
	List<Instance> toTerminate = terminateRequest.getToTerminate();
	for (Instance instance : toTerminate) {
	    /*
	     * A termination request of an instance not marked for termination
	     * is ignored because the instance has been reused before the hour
	     * boundary
	     */
	    if (instance.getStatus().equals(InstanceStatus.MARKED_FOR_TERMINATION)) {
		instance.setStatus(InstanceStatus.TERMINATED_BY_USER);
		instance.stopAccounting();
		instance.bill();
		sendNow(instance.getDatacenterID(), CloudSimTags.VM_DESTROY_ACK, instance);
		sendNow(instance.getBrokerID(), ComputeCloudTags.INSTANCE_TERMINATED.tag(),
		    new InstanceTerminatedNotification(instance));
	    } else {
		Log.logger.fine(Log.clock() + " Instance " + instance.getId()
			+ " was reused. It will not be terminated");
	    }
	}
    }

    private Datacenter createDatacenter(String name, int hosts) {
	List<Host> hostList = new ArrayList<Host>();

	final double computePower = InstanceType.maxCPUUnitsInstance.getComputePower();
	final int ram = InstanceType.maxMemoryInstance.getMem();
	final int cores = InstanceType.maxCoresUnitsInstance.getCores();

	for (int j = 0; j < hosts; j++) {
	    hostList.add(new Host(j, cores, ram, computePower, false));
	}

	String arch = "x86";
	String os = SimParameters.CLOUD_INSTANCE_OS.getValue();
	String vmm = "Xen";
	double time_zone = 0;
	double cost = 0.0;
	double costPerMem = 0.0;
	double costPerStorage = 0.0;
	double costPerBw = 0.0;

	DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm,
	    hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

	Datacenter dc = null;
	try {
	    dc = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList),
		null, 10);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return dc;
    }

    private double computeRunInstanceDelay(InstanceType type, OS os) {
	return Constants.RUN_INSTANCE_BASE_DELAY;
    }
}
