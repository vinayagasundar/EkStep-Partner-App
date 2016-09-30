package org.partner.model;

import java.util.ArrayList;

/**
 * Created by Dhruv on 12/9/2015.
 */
public class FieldModel implements Comparable<FieldModel> {
    private String fieldName;
    private FieldValue fieldValue ;
    private String fieldHint;
    private int displayOrder;
    private ArrayList fieldValues=new ArrayList();
    public FieldModel(){}
    public FieldModel(String fieldName,FieldValue fieldValue,
                      String fieldHint,
                      int displayOrder,ArrayList fieldValues){
        this.fieldName=fieldName;
        this.fieldValue=fieldValue;
        this.fieldHint=fieldHint;
        this.displayOrder=displayOrder;
        this.fieldValues=fieldValues;


    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldValue getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(FieldValue fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldHint() {
        return fieldHint;
    }

    public void setFieldHint(String fieldHint) {
        this.fieldHint = fieldHint;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ArrayList getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(ArrayList fieldValues) {
        this.fieldValues = fieldValues;
    }

    @Override
    public int compareTo(FieldModel fieldModel) {
        int compareDisplayorder=((FieldModel)fieldModel).getDisplayOrder();
        //ascending order
        return this.displayOrder - compareDisplayorder;

        //descending order
        //return compareDisplayorder - this.displayOrder;
    }
}
