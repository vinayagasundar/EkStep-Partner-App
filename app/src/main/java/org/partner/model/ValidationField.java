package org.partner.model;

/**
 * Created by Dhruv on 12/9/2015.
 */
public class ValidationField {
    private boolean flag;
    private int minimum;
    private int maximum;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}
