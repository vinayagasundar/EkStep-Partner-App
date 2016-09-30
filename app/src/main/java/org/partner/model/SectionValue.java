package org.partner.model;

/**
 * Created by Dhruv on 12/9/2015.
 */
public class SectionValue {
    private String SectionHeading;
    private InstructionsField instructionsField;
    private FieldModel[] fieldModels ;

    public FieldModel[] getFieldModels() {
        return fieldModels;
    }

    public void setFieldModels(FieldModel[] fieldModels) {
        this.fieldModels = fieldModels;
    }

    public String getSectionHeading() {
        return SectionHeading;
    }

    public void setSectionHeading(String sectionHeading) {
        SectionHeading = sectionHeading;
    }

    public InstructionsField getInstructionsField() {
        return instructionsField;
    }

    public void setInstructionsField(InstructionsField instructionsField) {
        this.instructionsField = instructionsField;
    }
}
