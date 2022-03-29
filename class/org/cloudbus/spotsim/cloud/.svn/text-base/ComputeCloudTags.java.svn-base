package org.cloudbus.spotsim.cloud;

public enum ComputeCloudTags {

    RUN_INSTANCE,
    TERMINATE_INSTANCES,
    INSTANCE_CREATED,
    INSTANCE_TERMINATED,
    NEW_JOB_ARRIVED,
    JOB_FINISHED,
    RUN_JOB_ON_INSTANCE,
    CHANGE_INSTANCE_PRICE,
    WORKLOAD_PERSISTENT,
    CREATE_NEW_INSTANCE,
    ADD_JOB_TO_INSTANCE;
    // start from 300 to avoid conflict with existing CloudSim tags
    public static final int BASE = 300;

    public int tag() {
	return BASE + ordinal();
    }

    public static ComputeCloudTags fromTag(int tag) {
	return values()[tag - BASE];
    }
}
