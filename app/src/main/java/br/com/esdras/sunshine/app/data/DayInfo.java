package br.com.esdras.sunshine.app.data;

import java.io.Serializable;

/**
 * Created by esdras on 28/08/15.
 */
public class DayInfo implements Serializable{

    public int    weatherConditionId;
    public long   dateTime;
    public String description;
    public double maxTemperature;
    public double minTemperature;
    public double pressure;
    public float  humidity;
    public double windSpeed;
    public double windDirection;
    public String icon;

}
