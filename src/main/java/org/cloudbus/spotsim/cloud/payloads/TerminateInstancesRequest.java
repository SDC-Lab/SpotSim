package org.cloudbus.spotsim.cloud.payloads;

import java.util.ArrayList;
import java.util.List;


import org.cloudbus.spotsim.cloud.Instance;

public class TerminateInstancesRequest {

    private final List<Instance> toTerminate;
    public TerminateInstancesRequest(List<Instance> toTerminate) {
	super();
	this.toTerminate = toTerminate;
    }
    
    public TerminateInstancesRequest(Instance... toTerminate) {
	super();
	this.toTerminate = new ArrayList<Instance>();
	for (Instance instance : toTerminate) {
	    this.toTerminate.add(instance);
	}
    }

	public List<Instance> getToTerminate() {
		return toTerminate;
	}

}
