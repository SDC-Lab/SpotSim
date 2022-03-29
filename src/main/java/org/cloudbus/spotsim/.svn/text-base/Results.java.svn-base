package org.cloudbus.spotsim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.cloudbus.spotsim.cloud.InstanceType.Region;
import org.cloudbus.spotsim.cloud.simrecords.JobSummary;
import org.cloudbus.spotsim.cloud.simrecords.SimulationData;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;

public class Results {

    public static final String RESULTS_DIR = "simulationResults";

    private static Map<String, SimulationData> simDB;

    private static File dir;
    
    private static String[] Methods ={"history","model"};
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

	//printJobSummariesCSV();
	printSimulationDataCSV();
    }

    static void persistResults(String key, SimulationData data, Region region, String method) {

	load(region, method);
	simDB.put(key, data);
    }

    private static void load(Region region, String method) {
    	String destDir = null;
    	
    	destDir = RESULTS_DIR + "/"+ region.getAmazonName() + "/" + method;
    	dir = new File(destDir);
    	if (!dir.exists()) {
	    dir.mkdirs();
    	}
	XStream xstream = new XStream();
	xstream.processAnnotations(SimulationData.class);

	simDB = new XmlMap(new FilePersistenceStrategy(dir, xstream));
    }

    // XML 2 CSV
    private static void printJobSummariesCSV() throws IOException {
    	for(String method: Methods){
    		for (Region region: Region.values()){
   		load(region, method);

	Set<String> entries = simDB.keySet();

	for (String entry : entries) {

	    File f = new File(dir, entry + ".csv");

	    if (!f.exists()) {

		SimulationData simulationData = simDB.get(entry);

		System.out.println("Printing: " + entry);

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));

		LinkedHashMap<Integer, JobSummary> jobs = simulationData.getJobs();
		for (Integer j : jobs.keySet()) {
		    JobSummary jobSummary = jobs.get(j);
		    out.println(jobSummary.toString());
		}
	    }
	}}
    	}
    }
    private static void printSimulationDataCSV() throws IOException {
    	for(String method: Methods){
    		for (Region region: Region.values()){
    	   		load(region, method);

        Set<String> entries = simDB.keySet();

        for (String entry : entries) {

            File f = new File(dir, entry + "-summary.csv");

            if (!f.exists()) {

                SimulationData simulationData = simDB.get(entry);
                
                System.out.println("Printing summary: " + entry + ", " + simulationData.toString());
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
                String st = simulationData.toString(0);
                out.println(st);
                out.close();
            }
        }
    	}    }
    }
}
