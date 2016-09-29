package com.cordon;

/**
 * Created by sami on 23/06/16.
 *
 * @author sami
 */
@SuppressWarnings("ALL")
public class Step {

    /**
     * Step name
     */

    private String Name;

    /**
     * Step measure value
     */

    private String Value;

    /**
     * Step measure min
     */

    private String Min;

    /**
     * Step measure max
     */

    private String Max;

    /**
     * Measure unit
     */

    private String Unit;

    /**
     * Step status
     */

    private String Status;

    /**
     * Step date
     */

    private String Date;

    /**
     * Step delay
     */

    private String StepDuration;


    /**
     * Constructor
     */

    public Step(String name) {
        Name = name;
    }


    /**
     * Constructor
     */

    public Step() {
    }


    /**
     * Constructor
     */

    public Step(String name, String date, String status, String max, String min, String unit, String value,String duration) {
        StepDuration=duration;
        Name = name;
        Value = value;
        Min = min;
        Max = max;
        Unit = unit;
        Status = status;
        Date = date;
    }


    /**
     * Getter
     */

    public String getDate() {
        return Date;
    }

    /**
     * Setter
     */

    public void setDate(String date) {
        Date = date;
    }

    /**
     * Getter
     */

    public String getName() {
        return Name;
    }

    /**
     * Setter
     */

    public void setName(String name) {
        Name = name;
    }

    /**
     * Getter
     */

    public String getValue() {
        return Value;
    }

    /**
     * Setter
     */

    public void setValue(String value) {
        Value = value;
    }

    /**
     * Getter
     */

    public String getMin() {
        return Min;
    }

    /**
     * Setter
     */

    public void setMin(String min) {
        Min = min;
    }

    /**
     * Getter
     */

    public String getMax() {
        return Max;
    }

    /**
     * Setter
     */

    public void setMax(String max) {
        Max = max;
    }

    /**
     * Getter
     */

    public String getUnit() {
        return Unit;
    }

    /**
     * Setter
     */

    public void setUnit(String unit) {
        Unit = unit;
    }

    /**
     * Getter
     */

    public String getStatus() {
        return Status;
    }

    /**
     * Setter
     */

    public void setStatus(String status) {
        Status = status;
    }

    /**
     * Getter
     */

    public String getStepDuration() {
        return StepDuration;
    }

    /**
     * Setter
     */

    public void setStepDuration(String delay) {
        StepDuration = delay;
    }
}


