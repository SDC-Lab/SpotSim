package org.cloudbus.spotsim.cloud.spothistory;

import static org.cloudbus.spotsim.Constants.ACCESS_KEY;
import static org.cloudbus.spotsim.Constants.END_TIME;
import static org.cloudbus.spotsim.Constants.MIN_INTERVAL_UNIT;
import static org.cloudbus.spotsim.Constants.MIN_INTERVAL_VALUE;
import static org.cloudbus.spotsim.Constants.SECRET_KEY;
import static org.cloudbus.spotsim.Constants.START_TIME;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.spotsim.Constants;
import org.cloudbus.spotsim.cloud.InstanceType;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.Region;
import org.cloudbus.spotsim.random.MixtureOfGaussians;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeSpotPriceHistoryRequest;
import com.amazonaws.services.ec2.model.DescribeSpotPriceHistoryResult;
import com.amazonaws.services.ec2.model.SpotPrice;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;

/**
 * Manages the trace of spot prices
 * 
 * @author William Voorsluys - williamvoor@gmail.com
 * 
 */
public class SpotPriceHistory {

    public static final String DATE = "date";
    private static Map<String, SortedSet<SpotPriceRecord>> pricesDB = null;
    private static Map<String, SortedSet<SpotPriceRecord>> pricesInMem = null;

    private static Random RNG = null;

    /*
     * Defines how price variation traces will serve as input to the simulator
     */
    public enum PriceTraceMethod {
	// Will use actual traces collected from Amazon EC2
	HISTORY,

	// Will use a random model
	RANDOM;
    }

    public static Map<String, SortedSet<SpotPriceRecord>> getPrices() {

	checkIfLoaded();

	return pricesInMem;
    }

    private static Map<InstanceType, PriorityQueue<SpotPriceRecord>> priceQueue;
    private static File dir;
    private static boolean loaded = false;
    private static GregorianCalendar simPeriodStart;
    private static GregorianCalendar simPeriodEnd;

//    public static void init() throws IOException {
//	init(false, PriceTraceMethod.HISTORY, Constants.START_TIME, Constants.END_TIME,seed);
//    }

    public static void init(boolean toclear, PriceTraceMethod traceMethod, GregorianCalendar start,
	    GregorianCalendar end, long seed) throws IOException {
	SpotPriceHistory.simPeriodStart = start;
	SpotPriceHistory.simPeriodEnd = end;
	RNG = new Random(seed);
	MixtureOfGaussians.init();
	priceQueue = null;
	pricesDB = null;
	pricesInMem = null;
	loaded = false;
	if (toclear) {
	    clear();
	}
	switch (traceMethod) {
	case HISTORY:
	    loadEC2Traces();
	    break;
	case RANDOM:
	    loadRandomTraces();
	    break;
	}
    }

    private static void loadRandomTraces() {
	pricesInMem = new HashMap<String, SortedSet<SpotPriceRecord>>();

	InstanceType[] values = InstanceType.values();

	for (InstanceType type : values) {

	    SortedSet<SpotPriceRecord> l = randomPricePeriod(type, getSimPeriodStart(),
		getSimPeriodEnd());
	    pricesInMem.put(genKey(Constants.DEFAULT_REGION, type, Constants.DEFAULT_OS), l);
	}

	loaded = true;
    }

    public static SortedSet<SpotPriceRecord> randomPricePeriod(InstanceType type, long from, long to) {

	return randomPricePeriod(type, toCalendar(from), toCalendar(to));
    }

    public static SortedSet<SpotPriceRecord> randomPricePeriod(InstanceType type,
	    GregorianCalendar from, GregorianCalendar to) {
	GregorianCalendar movingDate = (GregorianCalendar) from.clone();

	SortedSet<SpotPriceRecord> l = new TreeSet<SpotPriceRecord>(SpotPriceRecord.dateCompare());
	double initialPrice = randomPrice(type);
	l.add(new SpotPriceRecord(movingDate, initialPrice));

	do {
	    SpotPriceRecord randomRecord = randomPriceIncrement(type, movingDate);
	    l.add(randomRecord);
	    movingDate = randomRecord.getDate();
	} while (movingDate.before(to));
	return l;
    }

    private static double randomPrice(InstanceType type) {

	return MixtureOfGaussians.nextGaussian(RNG, type.getPriceMixtureModel(
	    Constants.DEFAULT_REGION, Constants.DEFAULT_OS)) / 100;
    }

    public static SpotPriceRecord randomPriceIncrement(InstanceType type,
	    GregorianCalendar previousChange) {
	double newPrice = randomPrice(type);
	int timeIncrement = (int) (MixtureOfGaussians.nextGaussian(RNG, type.getTimeMixtureModel(
	    Constants.DEFAULT_REGION, Constants.DEFAULT_OS,SpotPriceHistory.simPeriodStart)) * 3600);

	GregorianCalendar newDate = (GregorianCalendar) previousChange.clone();
	newDate.add(Calendar.SECOND, timeIncrement);
	return new SpotPriceRecord(newDate, newPrice);
    }

    public static void clear() throws IOException {
	dir = new File("pricesMap");
	if (dir.exists()) {
	    FileUtils.deleteDirectory(dir);
	}
    }

    @SuppressWarnings("unchecked")
    public static void loadEC2Traces() {

	dir = new File("pricesMap");
	if (!dir.exists()) {
	    dir.mkdirs();
	}
	XStream xstream = new XStream();
	xstream.processAnnotations(SpotPriceRecord.class);
	pricesDB = new XmlMap(new FilePersistenceStrategy(dir, xstream));
	pricesInMem = new HashMap<String, SortedSet<SpotPriceRecord>>();
	pricesInMem.putAll(pricesDB);
	loaded = true;
    }

    /**
     * @return a record of when will be the next price change and how much. Null
     *         if there are no price changes anymore
     */
    public static SpotPriceRecord getNextPriceChange(InstanceType type, OS os, Region region) {

	checkIfLoaded();

	if (priceQueue == null) {
	    priceQueue = new HashMap<InstanceType, PriorityQueue<SpotPriceRecord>>();
	}
	PriorityQueue<SpotPriceRecord> queue = priceQueue.get(type);
	if (queue == null) {
	    queue = getPriceQueue(type, getSimPeriodStart(), getSimPeriodEnd(), os, region);
	    if (queue.isEmpty()) {
		Log.logger.warning(Log.clock() + "There are no price variation for type: " + type
			+ ", " + os + ", " + region + " at date "
			+ Log.formatDate(getSimPeriodStart().getTime()));
		return getPriceRecordAtTime(type, getSimPeriodStart(), os, region);
	    }
	    priceQueue.put(type, queue);
	}
	return queue.poll();
    }

    public static SpotPriceRecord getNextPriceChange(InstanceType type) {
	return getNextPriceChange(type, Constants.DEFAULT_OS, Constants.DEFAULT_REGION);

    }

    private static void checkIfLoaded() {
    }

    protected static List<SpotPriceRecord> getEvenlyDistributedPriceList(InstanceType type, OS os,
	    Region region) {
	return getEvenlyDistributedPriceList(type, START_TIME, END_TIME, os, region);
    }

    protected static List<SpotPriceRecord> getEvenlyDistributedPriceList(InstanceType type,
	    GregorianCalendar start, GregorianCalendar end, OS os, Region region) {

	checkIfLoaded();

	List<SpotPriceRecord> ret = new ArrayList<SpotPriceRecord>();

	Iterator<SpotPriceRecord> listIterator = getIteratorAtTime(type, start, os, region);
	GregorianCalendar movingDate = (GregorianCalendar) start.clone();

	while (listIterator.hasNext()) {
	    SpotPriceRecord next = listIterator.next();
	    SpotPriceRecord next2 = null;
	    if (listIterator.hasNext()) {
		next2 = listIterator.next();
	    }

	    if (next.getDate().after(end)) {
		break;
	    }
	    SpotPriceRecord ad1 = new SpotPriceRecord(movingDate, next.getPrice());
	    ret.add(ad1);

	    if (next2 != null && next2.getDate().before(end)) {
		movingDate = (GregorianCalendar) movingDate.clone();
		movingDate.add(MIN_INTERVAL_UNIT, MIN_INTERVAL_VALUE);
		while (movingDate.before(next2.getDate())) {
		    SpotPriceRecord ad2 = new SpotPriceRecord(movingDate, next.getPrice());
		    ret.add(ad2);
		    movingDate = (GregorianCalendar) movingDate.clone();
		    movingDate.add(MIN_INTERVAL_UNIT, MIN_INTERVAL_VALUE);
		}
	    }
	}
	return ret;
    }

    public static PriorityQueue<SpotPriceRecord> getPriceQueue(InstanceType type,
	    GregorianCalendar from, GregorianCalendar to, OS os, Region region) {
	PriorityQueue<SpotPriceRecord> queue = new PriorityQueue<SpotPriceRecord>(100,
	    SpotPriceRecord.dateCompare());
	queue.addAll(getPriceSubset(type, from, to, os, region));
	return queue;
    }

    public static List<SpotPriceRecord> getPriceSubset(InstanceType type, long from, long to,
	    OS os, Region region) {
	return getPriceSubset(type, toCalendar(from), toCalendar(to), os, region);
    }

    public static List<SpotPriceRecord> getPriceSubset(InstanceType type, GregorianCalendar from,
	    GregorianCalendar to, OS os, Region region) {
	List<SpotPriceRecord> l = new ArrayList<SpotPriceRecord>();

	Iterator<SpotPriceRecord> listIterator = getIteratorAtTime(type, from, os, region);
	while (listIterator.hasNext()) {
	    SpotPriceRecord next = listIterator.next();
	    if (next.getDate().after(to)) {
		break;
	    }
	    l.add(next);
	}
	return l;
    }

    private static int searchDate(List<SpotPriceRecord> listOfPrices, GregorianCalendar periodStart) {
	int dateFound = Collections.binarySearch(listOfPrices, new SpotPriceRecord(periodStart,
	    Double.NaN), SpotPriceRecord.dateCompare());
	if (dateFound >= 0) {
	    return dateFound;
	}
	return Math.abs(dateFound) - 2;
    }

    /**
     * This method will contact Amazon EC2 to obtain up-to-date prices for all
     * instances
     * 
     * WARNING: It is slow and will delete currently stored prices. Needs to be
     * called only once.
     */
    public static void fetchAllTypes() {

	checkIfLoaded();

	for (InstanceType type : InstanceType.values()) {
	    for (OS os : OS.values()) {
		for (Region region : Region.values()) {
		    fetchSpotHistory(type, os, region);
		}
	    }
	}
    }

    public static void fetchSpotHistory(InstanceType type, OS os, Region region) {
	fetchSpotHistory(type, os, region, START_TIME, END_TIME);
    }

    public static void fetchSpotHistory(InstanceType type, OS os, Region region,
	    GregorianCalendar startTime, GregorianCalendar endTime) {

	checkIfLoaded();

	SortedSet<SpotPriceRecord> priceList = fetch(type, os, region, startTime, endTime);
	pricesDB.put(genKey(region, type, os), priceList);
    }

    public static SortedSet<SpotPriceRecord> fetch(InstanceType type, OS os, Region region,
	    GregorianCalendar startTime, GregorianCalendar endTime) {
	AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

	String types = type.getName();
	AmazonEC2Client amazonEC2Client = new AmazonEC2Client(awsCredentials);
	amazonEC2Client.setEndpoint("ec2." + region.getAmazonName() + ".amazonaws.com");
	DescribeSpotPriceHistoryResult history = amazonEC2Client
	    .describeSpotPriceHistory(new DescribeSpotPriceHistoryRequest().withStartTime(
		startTime.getTime()).withEndTime(endTime.getTime()).withInstanceTypes(types)
		.withProductDescriptions(os.getAmazonName()));

	SortedSet<SpotPriceRecord> priceList = new TreeSet<SpotPriceRecord>(SpotPriceRecord.dateCompare());

	for (SpotPrice spotPrice : history.getSpotPriceHistory()) {

	    Date timestamp = spotPrice.getTimestamp();
	    priceList.add(new SpotPriceRecord(timestamp, Double.parseDouble(spotPrice
		.getSpotPrice())));
	}

	return priceList;
    }

    public static String genKey(Region region, InstanceType type, OS os) {
	return region.getAmazonName() + '.' + os.getAmazonName() + '.' + type.getName();
    }

    public static double getPriceAtTime(InstanceType type, long timestampInSeconds, OS os,
	    Region region) {

	GregorianCalendar calendar = toCalendar(timestampInSeconds);
	return getPriceAtTime(type, calendar, os, region);
    }

    /*
     * Converts a time in milliseconds to a calendar formal. All times in
     * milliseconds are counter since the start the simulation
     * (SIM_PERIOD_START)
     */
    public static GregorianCalendar toCalendar(long timestampInSeconds) {
	GregorianCalendar calendar = new GregorianCalendar();
	calendar.setTimeInMillis(getSimPeriodStart().getTimeInMillis() + timestampInSeconds * 1000);
	return calendar;
    }

    public static double getPriceAtTime(InstanceType type, GregorianCalendar timestamp, OS os,
	    Region region) {

	checkIfLoaded();

	SpotPriceRecord spotPriceRecord = getPriceRecordAtTime(type, timestamp, os, region);
	return spotPriceRecord.getPrice();
    }

    private static SpotPriceRecord getPriceRecordAtTime(InstanceType type,
	    GregorianCalendar timestamp, OS os, Region region) {
    	NavigableSet<SpotPriceRecord> priceList = (NavigableSet<SpotPriceRecord>) getPricesForType(
    		    type, os, region);

    		return priceList.floor(new SpotPriceRecord(timestamp, 0));
    }

    public static SortedSet<SpotPriceRecord> getPricesForType(InstanceType type, OS os, Region region) {

	checkIfLoaded();

	SortedSet<SpotPriceRecord> priceList = getPrices().get(genKey(region, type, os));

	if (priceList == null) {
	    throw new RuntimeException("There are no prices for type: " + type);
	}

	return priceList;
    }

    public static Iterator<SpotPriceRecord> getIteratorAtTime(InstanceType type,
	    long timeStamp, OS os, Region region) {
	GregorianCalendar calendar = toCalendar(timeStamp);
	return getIteratorAtTime(type, calendar, os, region);
    }

    public static Iterator<SpotPriceRecord> getIteratorAtTime(InstanceType type,
	    GregorianCalendar start, OS os, Region region) {
    	String key = genKey(region, type, os);
    	NavigableSet<SpotPriceRecord> priceList = (NavigableSet<SpotPriceRecord>) pricesInMem
    	    .get(key);
    	System.out.println("Key: " + key);
    	SortedSet<SpotPriceRecord> subSet = priceList.subSet(new SpotPriceRecord(start, 0), priceList.last());
		return subSet.iterator();
    }

    public static GregorianCalendar getSimPeriodStart() {
	return simPeriodStart;
    }

    public static GregorianCalendar getSimPeriodEnd() {
	return simPeriodEnd;
    }
}
