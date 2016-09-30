package org.partner.model;

/**
 * Created by Dhruv on 12/9/2015.
 */
public class FieldValue {
    private String fieldType;
    private String fieldInputType;
    private ValidationField validation;
    public FieldValue(){}

    public FieldValue(String fieldType,String fieldInputType,ValidationField validation){
        this.fieldType=fieldType;
        this.fieldInputType=fieldInputType;
        this.validation=validation;
    }
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldInputType() {
        return fieldInputType;
    }

    public void setFieldInputType(String fieldInputType) {
        this.fieldInputType = fieldInputType;
    }

    public ValidationField getValidation() {
        return validation;
    }

    public void setValidation(ValidationField validation) {
        this.validation = validation;
    }
}
