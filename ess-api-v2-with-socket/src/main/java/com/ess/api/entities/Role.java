package com.ess.api.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
public class Role {

    public enum SecurityClearance {
        LEVEL_0,LEVEL_1,LEVEL_2,LEVEL_3;
    }
    
    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name =  "role_id")
    private long id;

    @Column(name =  "role_name", nullable=false, unique = true)
    private String name;

    @Column(name = "role_description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role_clearance", nullable = false ,columnDefinition = "VARCHAR(20) DEFAULT 'LEVEL_1'")
    private SecurityClearance clearance;

    @JsonCreator
    public Role(@JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("clearance") SecurityClearance clearance) {
        this.name = name;
        this.description = (description != null) ? description : name.trim().toUpperCase() + "_DESCRIPTION";
        this.clearance = (clearance != null) ? clearance : SecurityClearance.LEVEL_1;
    }

    public Role(String name, SecurityClearance clearance) {
        this.name = name;
        this.description = name.trim().toUpperCase()+"_DESCRIPTION";
        this.clearance = clearance;
    }

    public Role(String name) {
        this.name = name;
        this.description = name.trim().toUpperCase()+"_DESCRIPTION";
    }

    public Role() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SecurityClearance getClearance() {
        return clearance;
    }

    public void setClearance(SecurityClearance clearance) {
        this.clearance = clearance;
    }

    @Override
    public String toString() {
        System.out.println(3);
        return "Role [id=" + id + ", name=" + name + ", description=" + description + ", clearance=" + clearance + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (clearance != other.clearance)
            return false;
        return true;
    }
    
}
