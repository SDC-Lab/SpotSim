package org.cloudbus.spotsim.cloud;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.Region;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceHistory;
import org.cloudbus.spotsim.cloud.spothistory.SpotPriceRecord;

/**
 * 
 * Manages usage accounting and billing of virtual machine instances
 * 
 * @author William Voorsluys - williamvoor@gmail.com
 * 
 */
public class Accounting {

    private Map<InstanceType, Double> currentSpotPrices;

    public Accounting() {
	this.currentSpotPrices = new HashMap<InstanceType, Double>();
    }

    public void updatePrice(InstanceType type, Double newPrice) {
	this.currentSpotPrices.put(type, newPrice);
	Log.logger.info(Log.clock() + " Updating spot price for " + type + " to " + newPrice);
    }

    public double getCurrentPrice(InstanceType type) {
	return this.currentSpotPrices.get(type);
    }

    public static double billInstance(Instance instance) {
	double cost = 0.0;
	long periodStart = instance.getAccStart();
	long periodEnd = instance.getAccEnd();
	InstanceType type = instance.getType();
	InstanceStatus status = instance.getStatus();
	OS os = instance.getOs();
	Region region = instance.getRegion();

	switch (instance.getPricing()) {
	case ON_DEMAND:
	    // NOT YET IMPLEMENTED
	    break;
	case RESERVED:
	    // NOT YET IMPLEMENTED
	    break;
	case SPOT:
	    cost = billSpot(periodStart, periodEnd, type, status, os, region);
	    break;
	}
	return cost;
    }

    public static double billSpot(long from, long to, InstanceType type, InstanceStatus status,
	    OS os, Region region) {
	String from1 = Log.formatDate(SpotPriceHistory.toCalendar(from).getTime());
	String to1 = Log.formatDate(SpotPriceHistory.toCalendar(to).getTime());
	Log.logger.info(Log.clock() + "Billing spot instance from: " + from1 + " to " + to1);
	System.out.println("Billing spot instance from: " + from1 + " to " + to1);
	Iterator<SpotPriceRecord> it = SpotPriceHistory.getIteratorAtTime(type, from, os,
	    region);

	return computeCost(from, to, status, it);
    }

    /**
     * 
     * @param from
     *        period start (simulation time)
     * @param to
     *        period end (simulation time)
     * @param status
     * @param l
     * @return
     */
    public static double computeCost(long from, long to, InstanceStatus status,
	    Iterator<SpotPriceRecord> it) {
	SpotPriceRecord rec = it.next();
	SpotPriceRecord currentPrice = rec;
	long prev = from;
	SpotPriceRecord nextChange = it.next();
	long nextTimestamp = (nextChange.getDate().getTimeInMillis() - SpotPriceHistory
	    .getSimPeriodStart().getTimeInMillis()) / 1000;
	double cost = 0.0;
	while (true) {
	    prev += 3600;
	    if (prev >= to) {
		if (status.equals(InstanceStatus.TERMINATED_BY_USER)) {
		    cost += currentPrice.getPrice();
		    Log.logger.fine(Log.clock() + "Adding: " + currentPrice + ", Total: " + cost
			    + ", Hours: " + (prev - from) / 3600);
		}
		break;
	    }
	    cost += currentPrice.getPrice();
	    Log.logger.fine(Log.clock() + "Adding: " + currentPrice + ", Total: " + cost
		    + ", Hours: " + (prev - from) / 3600);

	    while (prev > nextTimestamp) {
		currentPrice = nextChange;
		nextChange = it.next();
		nextTimestamp = (nextChange.getDate().getTimeInMillis() - SpotPriceHistory
		    .getSimPeriodStart().getTimeInMillis()) / 1000;
	    }

	}
	return cost;
    }
}