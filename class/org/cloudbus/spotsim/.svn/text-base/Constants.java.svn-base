package org.cloudbus.spotsim;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.cloudbus.spotsim.cloud.PriceForecastingMethod;
import org.cloudbus.spotsim.cloud.InstanceType.OS;
import org.cloudbus.spotsim.cloud.InstanceType.Region;

/*
 * Heaps of hard-coded simulation parameters
 * TODO: Move to a configuration file
 */
public interface Constants {

    public static final double RUN_INSTANCE_BASE_DELAY = 0.0;
    public static final GregorianCalendar START_TIME = new GregorianCalendar(2010, Calendar.MARCH,
	1);
    public static final GregorianCalendar END_TIME = new GregorianCalendar(2010, Calendar.NOVEMBER,
    		1);
//    public static final GregorianCalendar BREAK_TIME = new GregorianCalendar(2010, Calendar.JULY,
//    		15);
    public static final int MIN_INTERVAL_UNIT = Calendar.HOUR;
    public static final int MIN_INTERVAL_VALUE = 1;
    public static final String ACCESS_KEY = "0AR2GGRFRVFNTJ43BG02";
    public static final String SECRET_KEY = "jjcScXKzEKt9qgBqY2cxe+pqsQ1n2SsXcnu0ey6w";
    public static final OS DEFAULT_OS = OS.LINUX;
    public static final Region DEFAULT_REGION = Region.US_WEST;
    public static final long VM_INIT_TIME = 0;
    public static final PriceForecastingMethod PRICE_FORECASTING_METHOD = PriceForecastingMethod.AVERAGE;
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
