package org.partner.model;

/**
 * Created by Dhruv on 12/9/2015.
 */
public class ConfigModel {
    private String partnerName;
    private String partnerId;
    private String partnerPublicKey;
    private SectionValue[] sectionValues ;

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerPublicKey() {
        return partnerPublicKey;
    }

    public void setPartnerPublicKey(String partnerPublicKey) {
        this.partnerPublicKey = partnerPublicKey;
    }

    public SectionValue[] getSectionValues() {
        return sectionValues;
    }

    public void setSectionValues(SectionValue[] sectionValues) {
        this.sectionValues = sectionValues;
    }
}
