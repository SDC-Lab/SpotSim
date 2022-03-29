package org.cloudbus.spotsim.cloud.payloads;


import org.cloudbus.spotsim.cloud.Instance;

public class InstanceTerminatedNotification {

    private Instance instance;

	public InstanceTerminatedNotification(Instance instance) {
		super();
		this.instance = instance;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

}
