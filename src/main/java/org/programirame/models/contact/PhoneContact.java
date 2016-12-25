package org.programirame.models.contact;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.programirame.models.Organization;

import javax.persistence.*;

@Entity
public class PhoneContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PhoneContactType type = PhoneContactType.MAIN;

    @ManyToOne
    @JsonIgnore
    private Organization organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneContactType getType() {
        return type;
    }

    public void setType(PhoneContactType type) {
        this.type = type;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
