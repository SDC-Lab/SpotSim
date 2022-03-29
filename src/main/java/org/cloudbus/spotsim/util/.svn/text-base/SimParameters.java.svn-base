package org.cloudbus.spotsim.util;

/**
 * Reads a set of configuration parameters as items of the enumerator
 * 
 * @author Marcos Dias de Assuncao, Bahman Javadi
 */
public enum SimParameters {
	/**
	 * The path for the simulation config file.
	 */
	USER_PATH("user_path"),
	
	/**
	 * The default simulation seed.
	 */
	SIMULATION_SEED("simulation.seed","1062348"),
	
	/**
	 * The number of simulation rounds.
	 */
	SIMULATION_ROUNDS("simulation.rounds", "1"),
	
	/**
	 * dump the config file (yes/no)
	 */
	SIMULATION_DUMP_CONFIG("simulation.dump_config", "no"),
	
	/**
	 * Warm up period over which no load is measured or forecast
	 * 1 day=86400, 2 days=172800, 7 days=604800, 14 days=1209600
	 */
	WARMUP_PERIOD("warmup.period", "86400"),

	/**
	 * Indicates if it should record the job completion time (yes/no)
	 */
	JOB_COMPLETION("job.completion", "yes"),
	

	/**
	 * What kind of local workload should be used
	 * static = use files provided for each workload
	 * vary = use files in the directory specified in workload.dir
	 * lublin99 = use the Lublin-99 workload model
	 * das2 = use the das2 workload model 
	 */
	WORKLOAD_TYPE("workload.type"),
	
	/**
	 * File containing the site's workload.
	 */
	WORKLOAD_FILE("workload.file"),
	
	/**
	 * Maximum time to consider (in sec.) Do not go further in the logs than this time.
	 */
	WORKLOAD_DURATION("workload.duration", "5184000"),

	/**
	 * Number of generated jobs in the workload 
	 */
	WORKLOAD_JOBS("workload.jobs", "1000"),
	
	/**
	 * Indicates if it should record the workload (yes/no)
	 */
	WORKLOAD_LOG("workload.log", "no"),

	/**
	* are requests persistent? (yes/no)
	 */
	WORKLOAD_PERSISTENT("workload.persistent", "no"),
	
	/**
	 * Region of Cloud
	 */
	CLOUD_REGION("cloud.region", "us-east"),

	/**
	 * OS of Cloud Instance
	 */
	CLOUD_INSTANCE_OS("cloud.instance.os", "linux"),

	/**
	 * Type of Cloud Instance
	 */
	CLOUD_INSTANCE_TYPE("cloud.instance.type", "m1.small"),

	/**
	 * Number of nodes in the provider's cloud
	 */
	CLOUD_NUMBER_NODES("cloud.nodes.number"),
	
	/**
	 * Number of cores per node.
	 */
	CLOUD_CORES_NODE("cloud.nodes.cores", "1"),
	
	/**
	 * Cores' rating in MIPS
	 */
	CLOUD_CORES_MIPS("cloud.cores.mips", "100"),

	/**
	 * The scheduling strategy to be used by the cloud 
	 */
	CLOUD_STRATEGY("cloud.strategy", "unknown"),
	
	/**
	 * The price paid per VM instance/hour in the cloud
	 */
	CLOUD_INSTANCE_PRICE("cloud.instance.price"),

	/**
	 * The price method of spot instances
	 */
	CLOUD_SPOT_PRICE_METHOD("cloud.spot.price.method", "history"),
	/**
	 * Calibration of model (yes/no)
	 */
	CLOUD_SPOT_PRICE_MODEL_CALIBRATION("cloud.spot.price.model_calibration", "no"),

	/**
	 * The start month of price history for spot instances
	 */
	CLOUD_SPOT_PRICE_START_MONTH("cloud.spot.price.start_month", "3"),

	/**
	 * The start year of price history for spot instances
	 */
	CLOUD_SPOT_PRICE_START_YEAR("cloud.spot.price.start_year", "2010"),

	/**
	 * The end month of price history for spot instances
	 */
	CLOUD_SPOT_PRICE_END_MONTH("cloud.spot.price.end_month", "11"),

	/**
	 * The end year of price history for spot instances
	 */
	CLOUD_SPOT_PRICE_END_YEAR("cloud.spot.price.end_year", "2010"),
	
	/**
	 * number of day from price history
	 */
	CLOUD_SPOT_HISTORY_DAY_OF_MONTH("cloud.spot.history.day_of_month", "1"),

	/**
	* The user bid
	 */
	CLOUD_SPOT_BID("cloud.spot.bid", "0"),

	/**
	 * Price of 10,000 GET requests
	 */
	CLOUD_GET_REQUEST_PRICE("cloud.request.get.price"),
	
	/**
	 * Price of 1,000 PUT, COPY, POST and LIST requests
	 */
	CLOUD_OTHER_REQUEST_PRICE("cloud.request.other.price"),
	
	/**
	 * Price per GB/month of storage.
	 */
	CLOUD_STORAGE_MONTH_PRICE("cloud.storage.month"),
	
	/**
	 * The number of VM images the user can select from
	 */
	NUMBER_IMAGES("images.number"),
	
	/**
	 * The size of the VMs in MBs.
	 */
	IMAGES_SIZE("images.size");
	
	
	private final String propertyKey;  
	private final String defaultValue;

	private SimParameters(String propertyKey) {  
	    this(propertyKey, null);  
	}  
	      
	private SimParameters(String propertyKey, String defaultValue) {  
		this.propertyKey = propertyKey;  
	    this.defaultValue = defaultValue;  
	}  
	      
	/**
	 * Returns the key for this property
	 * @return the key for this property
	 */
	public String getPropertyKey() {  
		return this.propertyKey;  
	}  
	      
	/**
	 * Returns the default value for this property
	 * @return the default value 
	 */
	public String getDefaultValue() {  
		return this.defaultValue;  
	}
	    
	/**
	 * Returns the value of this property according to the configuration files
	 * @return the value of this property
	 */
	public String getValue() {
		String value = SimConfiguration.getInstance().getProperty(getPropertyKey());
		return value != null ? value.trim() : getDefaultValue();
	}
}
