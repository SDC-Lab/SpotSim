package org.cloudbus.spotsim.cloud;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.cloudbus.spotsim.random.MixtureModel;
import org.cloudbus.spotsim.util.SimParameters;

/**
 * Describes the possible instance hardware types
 */
public enum InstanceType {

    // Standard instances
    M1SMALL(1740, 1, 1, 160, 1000, Bits.B32, "m1.small", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 0.085;
		case US_WEST:
			return 0.095;
		case EUROPE:
			return 0.095;
		case APAC:
			return 0.095;
		}
		return 0.0;
	}

	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		24);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.793, 0.164, 0.043 }, new double[] { 1.301,
				    3.582, 13.941 }, new double[] { 0.025, 2.641, 120.277 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 1.062 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis() 
					&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.292 }, 
			    		new double[] {0.022 });
			else						
				return new MixtureModel(3, new double[] { 0.178, 0.028, 0.794 }, new double[] { 3.474,
				    11.536, 1.292 }, new double[] { 2.308, 120.051, 0.022 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.793, 0.164, 0.043 }, new double[] { 1.301,
				    3.582, 13.941 }, new double[] { 0.025, 2.641, 120.277 });
		}
	    return new MixtureModel(3, new double[] { 0.793, 0.164, 0.043 }, new double[] { 1.301,
		    3.582, 13.941 }, new double[] { 0.025, 2.641, 120.277 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.476, 0.048, 0.476 }, new double[] { 3.012,
				    6.009, 3.012 }, new double[] { 0.009, 3.402, 0.009 });
		case US_WEST:
			// new TC
		    return new MixtureModel(3, new double[] { 0.999, 0.001, 0.001 }, new double[] { 3.982,
				    9.667, 9.667 }, new double[] { 0.013, 0.056, 0.056 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.003, 0.003, 0.994 }, new double[] { 5.216, 
		    		5.216, 3.997 }, new double[] { 1.670, 1.670, 0.020 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.476, 0.048, 0.476 }, new double[] { 3.012,
				    6.009, 3.012 }, new double[] { 0.009, 3.402, 0.009 });
		}

	    return new MixtureModel(3, new double[] { 0.476, 0.048, 0.476 }, new double[] { 3.012,
		    6.009, 3.012 }, new double[] { 0.009, 3.402, 0.009 });
	}
    },
    M1LARGE(7680, 4, 2, 850, 1000, Bits.B64, "m1.large", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 0.34;
		case US_WEST:
			return 0.38;
		case EUROPE:
			return 0.38;
		case APAC:
			return 0.38;
		}
		return 0.0;
	}

	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.390, 0.023, 0.587 }, new double[] { 3.986,
				    55.662, 1.277 }, new double[] { 4.862, 12186.399, 0.022 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.895 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
					&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.276 }, 
			    		new double[] {0.022 });
			else			
				return new MixtureModel(3, new double[] { 0.068, 0.126, 0.806 }, new double[] { 6.793,
				    3.040, 1.276 }, new double[] { 13.803, 0.940, 0.022 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.390, 0.023, 0.587 }, new double[] { 3.986,
				    55.662, 1.277 }, new double[] { 4.862, 12186.399, 0.022 });
		}
		
	    return new MixtureModel(3, new double[] { 0.390, 0.023, 0.587 }, new double[] { 3.986,
		    55.662, 1.277 }, new double[] { 4.862, 12186.399, 0.022 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.094, 0.472, 0.434 }, new double[] { 22.345,
				    12.003, 12.015 }, new double[] { 114.856, 0.149, 0.149 });
		case US_WEST:
			// new TC
		    return new MixtureModel(3, new double[] { 0.015, 0.867, 0.118 }, new double[] { 44.060,
				    15.827, 16.661 }, new double[] { 33.061, 0.109, 0.016 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.492, 0.505, 0.003 }, new double[] { 15.556,
				    16.470, 24.401 }, new double[] { 0.059, 0.048, 114.879 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.094, 0.472, 0.434 }, new double[] { 22.345,
				    12.003, 12.015 }, new double[] { 114.856, 0.149, 0.149 });
		}

	    return new MixtureModel(3, new double[] { 0.094, 0.472, 0.434 }, new double[] { 22.345,
		    12.003, 12.015 }, new double[] { 114.856, 0.149, 0.149 });
	}
    },
    M1XLARGE(15360, 8, 4, 1690, 1000, Bits.B64, "m1.xlarge", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 0.68;
		case US_WEST:
			return 0.76;
		case EUROPE:
			return 0.76;
		case APAC:
			return 0.76;
		}
		return 0.0;
	}

	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		23);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
		
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.778, 0.206, 0.016 }, new double[] { 1.278,
				    3.655, 13.907 }, new double[] { 0.022, 3.005, 353.820 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.836 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
				&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.283 }, 
			    		new double[] {0.022 });
			else		
				return new MixtureModel(3, new double[] { 0.793, 0.177, 0.030 }, new double[] { 1.283,
				    3.310, 8.354 }, new double[] { 0.022, 1.864, 29.730 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.778, 0.206, 0.016 }, new double[] { 1.278,
				    3.655, 13.907 }, new double[] { 0.022, 3.005, 353.820 });
		}
		
	    return new MixtureModel(3, new double[] { 0.778, 0.206, 0.016 }, new double[] { 1.278,
		    3.655, 13.907 }, new double[] { 0.022, 3.005, 353.820 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.486, 0.506, 0.008 }, new double[] { 24.034,
				    24.032, 41.510 }, new double[] { 0.592, 0.592, 425.461 });
		 // new TC
		case US_WEST:
		    return new MixtureModel(3, new double[] { 0.113, 0.884, 0.003 }, new double[] { 33.302,
				    31.667, 58.986 }, new double[] { 0.055, 0.411, 756.622 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.457, 0.002, 0.541 }, new double[] { 31.010,
				    53.803, 32.848 }, new double[] { 0.184, 326.523, 0.249 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.486, 0.506, 0.008 }, new double[] { 24.034,
				    24.032, 41.510 }, new double[] { 0.592, 0.592, 425.461 });
		}

	    return new MixtureModel(3, new double[] { 0.486, 0.506, 0.008 }, new double[] { 24.034,
		    24.032, 41.510 }, new double[] { 0.592, 0.592, 425.461 });
	}
    },

    // High-memory instances
    M2XLARGE(17510, 6, 2, 420, 1000, Bits.B64, "m2.xlarge", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 0.50;
		case US_WEST:
			return 0.57;
		case EUROPE:
			return 0.57;
		case APAC:
			return 0.57;
		}
		return 0.0;
	}
	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		13);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
			return new MixtureModel(3, new double[] { 0.814, 0.038, 0.148 }, new double[] { 1.282,
				    9.249, 3.508 }, new double[] { 0.023, 27.356, 1.809 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.877 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
				&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.284 }, 
			    		new double[] {0.022 });
			else				
				return new MixtureModel(3, new double[] { 0.824, 0.066, 0.11 }, new double[] { 1.284,
				    2.506, 5.166 }, new double[] { 0.022, 0.035, 8.192 });
		case APAC:
			// TODO update
			return new MixtureModel(3, new double[] { 0.814, 0.038, 0.148 }, new double[] { 1.282,
				    9.249, 3.508 }, new double[] { 0.023, 27.356, 1.809 });
		}

		return new MixtureModel(3, new double[] { 0.814, 0.038, 0.148 }, new double[] { 1.282,
		    9.249, 3.508 }, new double[] { 0.023, 27.356, 1.809 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.441, 0.035, 0.524 }, new double[] { 17.021,
				    38.735, 17.018 }, new double[] { 0.299, 209.050, 0.299 });
		case US_WEST:
			// new TC
		    return new MixtureModel(3, new double[] { 0.420, 0.420, 0.160 }, new double[] { 23.376,
				    24.342, 24.000 }, new double[] { 0.089, 0.316, 0.000 });
		case EUROPE:
		    return new MixtureModel(2, new double[] { 0.445, 0.001, 0.554 }, new double[] { 23.264,
				    53.500, 24.643}, new double[] { 0.109, 12.960, 0.135 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.441, 0.035, 0.524 }, new double[] { 17.021,
				    38.735, 17.018 }, new double[] { 0.299, 209.050, 0.299 });
		}

	    return new MixtureModel(3, new double[] { 0.441, 0.035, 0.524 }, new double[] { 17.021,
		    38.735, 17.018 }, new double[] { 0.299, 209.050, 0.299 });
	}
    },
    M22XLARGE(35020, 13, 4, 850, 1000, Bits.B64, "m2.2xlarge", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 1.0;
		case US_WEST:
			return 1.14;
		case EUROPE:
			return 1.14;
		case APAC:
			return 1.14;
		}
		return 0.0;
	}

	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		23);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.180, 0.041, 0.779 }, new double[] { 3.230,
				    8.233, 1.278 }, new double[] { 1.355, 29.767, 0.021 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.876 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
				&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(2, new double[] {0.504, 0.496}, new double[] { 1.155, 1.398 }, 
			    		new double[] {0.007, 0.008 });
			else	
				return new MixtureModel(3, new double[] { 0.405, 0.399, 0.196 }, new double[] { 1.155,
				    1.398, 4.044 }, new double[] { 0.007, 0.008, 6.795 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.180, 0.041, 0.779 }, new double[] { 3.230,
				    8.233, 1.278 }, new double[] { 1.355, 29.767, 0.021 });
		}
		
	    return new MixtureModel(3, new double[] { 0.180, 0.041, 0.779 }, new double[] { 3.230,
		    8.233, 1.278 }, new double[] { 1.355, 29.767, 0.021 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.545, 0.449, 0.006 }, new double[] { 43.115,
				    40.726, 61.084 }, new double[] { 0.450, 0.318, 334.571 });
		case US_WEST:
			// new TC
		    return new MixtureModel(3, new double[] { 0.890, 0.109, 0.001 }, new double[] { 55.386,
				    58.202, 88.552 }, new double[] { 1.275, 0.167, 135.209 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.492, 0.252, 0.256 }, new double[] {56.119, 
		    		53.784, 58.100}, new double[] { 1.813, 0.157, 0.216 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.545, 0.449, 0.006 }, new double[] { 43.115,
				    40.726, 61.084 }, new double[] { 0.450, 0.318, 334.571 });
		}
	    return new MixtureModel(3, new double[] { 0.545, 0.449, 0.006 }, new double[] { 43.115,
		    40.726, 61.084 }, new double[] { 0.450, 0.318, 334.571 });
	}
    },
    M24XLARGE(70041, 26, 8, 1690, 1000, Bits.B64, "m2.4xlarge", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 2.0;
		case US_WEST:
			return 2.28;
		case EUROPE:
			return 2.28;
		case APAC:
			return 2.28;
		}
		return 0.0;
	}
	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
		
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.063, 0.151, 0.786 }, new double[] { 7.043,
				    3.065, 1.285 }, new double[] { 11.974, 0.862, 0.022 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.833 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
				&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.279 }, 
			    		new double[] {0.022 });
			else
		    return new MixtureModel(3, new double[] { 0.063, 0.137, 0.80 }, new double[] { 6.705,
				    3.001, 1.279 }, new double[] { 13.525, 0.863, 0.022 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.063, 0.151, 0.786 }, new double[] { 7.043,
				    3.065, 1.285 }, new double[] { 11.974, 0.862, 0.022 });
		}
	    return new MixtureModel(3, new double[] { 0.063, 0.151, 0.786 }, new double[] { 7.043,
			    3.065, 1.285 }, new double[] { 11.974, 0.862, 0.022 });
		
		}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.496, 0.497, 0.007 }, new double[] { 83.979,
				    84.029, 166.257 }, new double[] { 7.067, 7.059, 2457.027 });
		case US_WEST:
			// new TC
		    return new MixtureModel(3, new double[] { 0.126, 0.504, 0.370 }, new double[] { 112.000,
				    113.094, 109.073}, new double[] { 0.000, 8.335, 2.145 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.263, 0.249, 0.488 }, new double[] { 116.126,
				    107.609, 112.183 }, new double[] { 0.898, 0.660, 7.061 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.496, 0.497, 0.007 }, new double[] { 83.979,
				    84.029, 166.257 }, new double[] { 7.067, 7.059, 2457.027 });
		}

	    return new MixtureModel(3, new double[] { 0.496, 0.497, 0.007 }, new double[] { 83.979,
		    84.029, 166.257 }, new double[] { 7.067, 7.059, 2457.027 });
	}
    },

    // High-CPU instances
    C1MEDIUM(1740, 5, 2, 320, 1000, Bits.B32, "c1.medium", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 0.17;
		case US_WEST:
			return 0.19;
		case EUROPE:
			return 0.19;
		case APAC:
			return 0.19;
		}
		return 0.0;
	}

	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
	}
	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
			return new MixtureModel(3, new double[] { 0.780, 0.075, 0.145 }, new double[] { 1.301,
				    7.381, 2.954 }, new double[] { 0.023, 20.605, 0.815 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.982 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
				&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.279 }, 
			    		new double[] {0.022 });
			else	
				return new MixtureModel(2, new double[] { 0.807, 0.090, 0.103 }, new double[] { 1.279,
				    6.452, 2.876 }, new double[] { 0.022, 12.435, 0.528 });
		case APAC:
			// TODO update
			return new MixtureModel(3, new double[] { 0.780, 0.075, 0.145 }, new double[] { 1.301,
				    7.381, 2.954 }, new double[] { 0.023, 20.605, 0.815 });
		}
		
		return new MixtureModel(3, new double[] { 0.780, 0.075, 0.145 }, new double[] { 1.301,
		    7.381, 2.954 }, new double[] { 0.023, 20.605, 0.815 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.978, 0.011, 0.011 }, new double[] { 6.006,
				    8.916, 8.916 }, new double[] { 0.042, 2.743, 2.743 });
		case US_WEST:
			// new TC
			return new MixtureModel(3, new double[] { 0.194, 0.712, 0.094 }, new double[] { 7.710,
				    7.978, 8.349 }, new double[] { 0.005, 0.021, 0.003 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.443, 0.276, 0.281 }, new double[] { 8.018,
				    8.292, 7.703}, new double[] { 0.045, 0.006, 0.006 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.978, 0.011, 0.011 }, new double[] { 6.006,
				    8.916, 8.916 }, new double[] { 0.042, 2.743, 2.743 });
		}

	    return new MixtureModel(3, new double[] { 0.978, 0.011, 0.011 }, new double[] { 6.006,
		    8.916, 8.916 }, new double[] { 0.042, 2.743, 2.743 });
	}
    },
    C1XLARGE(7189, 20, 8, 1690, 1000, Bits.B64, "c1.xlarge", 1) {
	@Override
	public double getOnDemandPrice(Region r, OS os) {
		switch (r){
		case US_EAST:
			return 0.68;
		case US_WEST:
			return 0.76;
		case EUROPE:
			return 0.76;
		case APAC:
			return 0.76;
		}
		return 0.0;
	}

	public GregorianCalendar BreakTime(Region r, OS os){
		switch (r){
		case US_EAST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case US_WEST:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		case EUROPE:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		23);
		case APAC:
			return new GregorianCalendar(2010, Calendar.JULY,
		    		15);
		}
		return new GregorianCalendar(2010, Calendar.JULY,
	    		15);
		
	}

	@Override
	public MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.016, 0.759, 0.225 }, new double[] { 14.300,
				    1.280, 3.651 }, new double[] { 459.734, 0.021, 3.196 });
		case US_WEST:
			// new TC
		    return new MixtureModel(0, new double[] { 1.00 }, new double[] { 0.868 }, new double[] { 0.0 });
		case EUROPE:
			if (start.getTimeInMillis() > BreakTime(r,os).getTimeInMillis()
				&& SimParameters.CLOUD_SPOT_PRICE_MODEL_CALIBRATION.getValue().equals("yes"))
			    return new MixtureModel(1, new double[] {1}, new double[] { 1.286 }, 
			    		new double[] {0.022 });
			else
				return new MixtureModel(3, new double[] { 0.811, 0.187, 0.002 }, new double[] { 1.286,
				    4.048, 84.430}, new double[] { 0.022, 5.341, 15636.817 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.016, 0.759, 0.225 }, new double[] { 14.300,
				    1.280, 3.651 }, new double[] { 459.734, 0.021, 3.196 });
		}
	    return new MixtureModel(3, new double[] { 0.016, 0.759, 0.225 }, new double[] { 14.300,
		    1.280, 3.651 }, new double[] { 459.734, 0.021, 3.196 });
	}

	@Override
	public MixtureModel getPriceMixtureModel(Region r, OS os) {
		switch (r){
		case US_EAST:
		    return new MixtureModel(3, new double[] { 0.449, 0.071, 0.48 }, new double[] { 24.047,
				    51.722, 24.035 }, new double[] { 0.594, 340.122, 0.593 });
		case US_WEST:
			// new TC
		    return new MixtureModel(3, new double[] { 0.480, 0.148, 0.372 }, new double[] { 32.313,
				    32.000, 31.181 }, new double[] { 0.825, 0.000, 0.168 });
		case EUROPE:
		    return new MixtureModel(3, new double[] { 0.261, 0.243, 0.496 }, new double[] { 33.188,
				    30.756, 32.057 }, new double[] { 0.072, 0.058, 0.722 });
		case APAC:
			// TODO update
		    return new MixtureModel(3, new double[] { 0.449, 0.071, 0.48 }, new double[] { 24.047,
				    51.722, 24.035 }, new double[] { 0.594, 340.122, 0.593 });
		}
		
	    return new MixtureModel(3, new double[] { 0.449, 0.071, 0.48 }, new double[] { 24.047,
		    51.722, 24.035 }, new double[] { 0.594, 340.122, 0.593 });
	}
    };

    public static final InstanceType maxCPUUnitsInstance = M24XLARGE;
    public static final InstanceType maxCoresUnitsInstance = M24XLARGE;
    public static final InstanceType maxMemoryInstance = M24XLARGE;

    public enum Bits {
	B32,
	B64;
    }

    public enum PriceModel {
	ON_DEMAND,
	RESERVED,
	SPOT;
    }

    public enum OS {
	LINUX {
	    @Override
	    public String getAmazonName() {
		return "Linux/UNIX";
	    }

	    @Override
	    public String getNameForFile() {
		return "linux";
	    }
	},
	WINDOWS {
	    @Override
	    public String getAmazonName() {
		return "Windows";
	    }

	    @Override
	    public String getNameForFile() {
		return "windows";
	    }
	};

	public abstract String getAmazonName();

	public abstract String getNameForFile();

    }

    public enum Region {
	US_EAST {
	    @Override
	    public String getAmazonName() {
		return "us-east-1";
	    }
	},
	US_WEST {
	    @Override
	    public String getAmazonName() {
		return "us-west-1";
	    }
	},
	EUROPE {
	    @Override
	    public String getAmazonName() {
		return "eu-west-1";
	    }
	},
	APAC {
	    @Override
	    public String getAmazonName() {
		return "ap-southeast-1";
	    }
	};

	public abstract String getAmazonName();
    }

    /** Memory in MB */
    private final int mem;

    /**
     * Total number of EC2 compute units (equivalent to Number of cores * EC2
     * compute units per core
     */
    private final int ec2units;

    /** Number of CPU cores */
    private final int cores;

    /** Local disk storage in GB */
    private final long storage;

    /** 32 or 64 bits */
    private final Bits bits;

    /** Network bandwidth */
    private final long bw;

    private final String name;

    private final double computePowerPerUnit;

    InstanceType(int mem, int ec2units, int cores, long storage, long bw, Bits bits, String name,
	    double gFlopsPerUnit) {
	this.mem = mem;
	this.ec2units = ec2units;
	this.cores = cores;
	this.storage = storage;
	this.bw = bw;
	this.bits = bits;
	this.name = name;
	this.computePowerPerUnit = gFlopsPerUnit;
    }

    public abstract double getOnDemandPrice(Region r, OS os);

    public abstract MixtureModel getPriceMixtureModel(Region r, OS os);

    public abstract MixtureModel getTimeMixtureModel(Region r, OS os, GregorianCalendar start);

    public double getMinimumSpotPrice(Region r, OS os) {
	return getOnDemandPrice(r, os) / 3;
    }

    public static List<String> getAllTypeNames() {
	List<String> allTypes = new ArrayList<String>();
	for (InstanceType type : InstanceType.values()) {
	    allTypes.add(type.getName());
	}
	return allTypes;
    }

    public String getName() {
	return this.name;
    }

    public int getMem() {
	return this.mem;
    }

    public int getEc2units() {
	return this.ec2units;
    }

    public int getCores() {
	return this.cores;
    }

    public long getStorage() {
	return this.storage;
    }

    public Bits getBits() {
	return this.bits;
    }

    public double getComputePowerPerCore() {
	return getComputePower() / getCores();
    }

    public double getComputePower() {
	return getEc2units() * getComputePowerPerUnit();
    }

    public long getBandwidth() {
	return this.bw;
    }

    @Override
    public String toString() {
	return getName();
    }

    public double getComputePowerPerUnit() {
	return this.computePowerPerUnit;
    }

    public double getReservedPrice(Region region) {
	throw new UnsupportedOperationException("Reserved prices are not available");
    }
}