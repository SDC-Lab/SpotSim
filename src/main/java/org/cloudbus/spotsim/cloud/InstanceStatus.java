package org.cloudbus.spotsim.cloud;

public enum InstanceStatus {

    PENDING,
    RUNNING_IDLE,
    RUNNING_BUSY,
    SUSPENDED,
    MARKED_FOR_TERMINATION,
    TERMINATED_BY_USER,
    OUT_OF_BID,
    FAILED,
    BOOTING;

    public boolean isUsable() {
	return equals(RUNNING_BUSY) || isIdle();
    }

    public boolean isIdle() {
	return equals(RUNNING_IDLE) || equals(MARKED_FOR_TERMINATION);
    }
}
