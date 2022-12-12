package com.springKT.apiAssignment.entities;

import jakarta.persistence.Entity;

@Entity
public class TransformerInfo extends CommonDataModel{
    private String lob;
    private String description;
    private String type;

    public TransformerInfo() {
        super();
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TransformerInfo( String lob, String description, String type) {
        this.lob = lob;
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TransformerInfo{" +
                " lob='" + lob + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
