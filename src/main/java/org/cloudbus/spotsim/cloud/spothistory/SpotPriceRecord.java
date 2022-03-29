package org.cloudbus.spotsim.cloud.spothistory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cloudbus.spotsim.Constants;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("price_record")
public class SpotPriceRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Comparator<SpotPriceRecord> priceCompare = new Comparator<SpotPriceRecord>() {
	@Override
	public int compare(SpotPriceRecord o1, SpotPriceRecord o2) {
	    return Double.compare(o1.getPrice(), o2.getPrice());
	}
    };

    private static Comparator<SpotPriceRecord> dateCompare = new Comparator<SpotPriceRecord>() {

	@Override
	public int compare(SpotPriceRecord o1, SpotPriceRecord o2) {
	    return o1.getDate().compareTo(o2.getDate());
	}
    };

    private final double price;

    private GregorianCalendar date;

    public SpotPriceRecord(GregorianCalendar date, double price) {
	this.date = date;
	this.price = price;
    }

    public SpotPriceRecord(Date date, double price) {
	GregorianCalendar d = new GregorianCalendar();
	d.setTime(date);
	this.date = d;
	this.price = price;
    }

    public long secondsSinceStart() {
	return (this.date.getTimeInMillis() - SpotPriceHistory.getSimPeriodStart()
	    .getTimeInMillis()) / 1000;
    }

    public static Comparator<SpotPriceRecord> priceCompare() {
	return priceCompare;
    }

    public static Comparator<SpotPriceRecord> dateCompare() {
	return dateCompare;
    }

    public GregorianCalendar getDate() {
	return this.date;
    }

    public void setDate(GregorianCalendar date) {
	this.date = date;
    }

    public void setDateDate(Date date) {
	this.date.setTime(date);
    }

    @Override
    public String toString() {
	return new SimpleDateFormat(Constants.DATE_FORMAT).format(this.date.getTime()) + ","
		+ new DecimalFormat("#.####").format(getPrice());
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (this.date == null ? 0 : this.date.hashCode());
	long temp;
	temp = Double.doubleToLongBits(getPrice());
	result = prime * result + (int) (temp ^ temp >>> 32);
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	SpotPriceRecord other = (SpotPriceRecord) obj;
	if (this.date == null) {
	    if (other.date != null) {
		return false;
	    }
	} else if (!this.date.equals(other.date)) {
	    return false;
	}
	if (Double.doubleToLongBits(getPrice()) != Double.doubleToLongBits(other.getPrice())) {
	    return false;
	}
	return true;
    }

    public double getPrice() {
	return this.price;
    }
}
