package org.cloudbus.spotsim.cloud;


import org.cloudbus.cloudsim.util.workload.Job;

public class RunJobRequest {

    private final Job job;

    private final Instance instance;

	public RunJobRequest(Job job, Instance instance) {
		super();
		this.job = job;
		this.instance = instance;
	}

	public Job getJob() {
		return job;
	}

	public Instance getInstance() {
		return instance;
	}

}
