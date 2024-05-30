package org.linhart.ppj.sem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="state")
public class State{

    @Id
    @Column(unique = true, name = "iso_code")
    private String isoCode;

    @Column(name = "state_name")
    private String stateName;

    public State() {
    }

    public State(String isoCode, String stateName) {
        this.isoCode = isoCode;
        this.stateName = stateName;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "Jmeno:"  + stateName + "  IsoCode:"+ isoCode;
    }
}
