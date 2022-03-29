package org.cloudbus.spotsim.cloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.PriceModel;
import org.cloudbus.spotsim.cloud.InstanceType.Region;

/**
 * Creates virtual machine instances and keeps them organised
 * 
 * @author William Voorsluys - williamvoor@gmail.com
 * 
 */
public class InstanceFactory {

    private static InstanceFactory singleton = null;

    public static InstanceFactory singleton() {
	if (singleton == null) {
	    singleton = new InstanceFactory();
	}
	return singleton;
    }

    /* All instances */
    private final Map<Integer, Instance> byInstanceID;

    private final Map<PriceModel, List<Instance>> byPriceModel;

    private final Map<Integer, List<Instance>> byClient;

    /* Only spot instances */
    private final Map<InstanceType, SortedSet<Instance>> spotByType;

    private InstanceFactory() {

	this.byInstanceID = new LinkedHashMap<Integer, Instance>();
	this.byPriceModel = new LinkedHashMap<PriceModel, List<Instance>>();
	this.spotByType = new LinkedHashMap<InstanceType, SortedSet<Instance>>();
	this.byClient = new LinkedHashMap<Integer, List<Instance>>();
	PriceModel[] models = PriceModel.values();
	for (PriceModel m : models) {
	    this.byPriceModel.put(m, new ArrayList<Instance>());
	}
	InstanceType[] types = InstanceType.values();
	for (InstanceType t : types) {
	    this.spotByType.put(t, new TreeSet<Instance>(bidComparator()));
	}
    }

    private Comparator<Instance> bidComparator() {
	return new Comparator<Instance>() {

	    @Override
	    public int compare(Instance o1, Instance o2) {
		return Double.compare(o1.getBidPrice(), o2.getBidPrice());
	    }
	};
    }

    public Instance getInstanceById(int id) {
	return this.byInstanceID.get(id);
    }

    public List<Instance> getInstancesByPriceModel(PriceModel type) {
	return this.byPriceModel.get(type);
    }

    /**
     * Returns a set containing spot instances that have a bid price less then
     * <code>price</code>
     * 
     * @param price
     * @return
     */
    public Set<Instance> getSpotInstancesHeadSet(InstanceType type, double price) {
    	SortedSet<Instance> sortedSet = this.spotByType.get(type);
    	SortedSet<Instance> headSet = sortedSet.headSet(new Instance(price));

    	for (Iterator<Instance> iterator = headSet.iterator(); iterator.hasNext();) {
    	    Instance instance = iterator.next();
    	    if (instance.getStatus().equals(InstanceStatus.PENDING)) {
    		iterator.remove();
    	    }
    	}

    	return headSet;
        }

    public Set<Instance> getPendingSpotRequests(InstanceType type) {

	Set<Instance> ret = new HashSet<Instance>();

	List<Instance> spotInstances = getInstancesByPriceModel(PriceModel.SPOT);
	for (Instance instance : spotInstances) {
	    if (instance.getType().equals(type)
		    && instance.getStatus().equals(InstanceStatus.PENDING)) {
		ret.add(instance);
	    }
	}

	return ret;
    }

    public void destroyInstance(int instanceID) {
	Instance toRemove = this.byInstanceID.remove(instanceID);
	getInstancesByPriceModel(toRemove.getPricing()).remove(toRemove);
	this.byClient.get(toRemove.getBrokerID()).remove(toRemove);
	if (toRemove.isSpot()) {
	    this.spotByType.get(toRemove.getType()).remove(toRemove);
	}
    }

    public Instance newInstance(InstanceType instanceType, OS os, PriceModel priceModel,
	    int cloudID, int brokerID, double bidPrice, Region region) {
	Instance instance = new Instance(instanceType, os, priceModel, cloudID, brokerID, bidPrice,
	    region);
	this.byInstanceID.put(instance.getId(), instance);
	List<Instance> clientInstances = this.byClient.get(brokerID);
	if (clientInstances == null) {
	    clientInstances = new ArrayList<Instance>();
	    this.byClient.put(brokerID, clientInstances);
	}
	clientInstances.add(instance);
	this.byPriceModel.get(instance.getPricing()).add(instance);
	if (instance.isSpot()) {
	    this.spotByType.get(instance.getType()).add(instance);
	}
	return instance;
    }

    public Collection<Instance> getAllInstances() {
	return this.byInstanceID.values();
    }

    public List<Instance> getInstancesByClient(int id) {
	List<Instance> clientInstances = this.byClient.get(id);
	if (clientInstances == null) {
	    return new ArrayList<Instance>();
	}
	return clientInstances;
    }

    public int countInstancesByStatus(InstanceStatus status, int brokerID) {
	Collection<Instance> l = getInstancesByClient(brokerID);
	int c = 0;
	for (Instance instance : l) {
	    if (instance.getStatus().equals(status)) {
		c++;
	    }
	}
	return c;
    }
}