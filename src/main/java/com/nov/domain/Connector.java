package com.nov.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Connector.
 */
@Entity
@Table(name = "connector")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Connector implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @Column(name = "driver")
    private String driver;

    @OneToMany(mappedBy = "connector")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Connexion> connexions = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Connector type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriver() {
        return driver;
    }

    public Connector driver(String driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Set<Connexion> getConnexions() {
        return connexions;
    }

    public Connector connexions(Set<Connexion> connexions) {
        this.connexions = connexions;
        return this;
    }

    public Connector addConnexions(Connexion connexion) {
        this.connexions.add(connexion);
        connexion.setConnector(this);
        return this;
    }

    public Connector removeConnexions(Connexion connexion) {
        this.connexions.remove(connexion);
        connexion.setConnector(null);
        return this;
    }

    public void setConnexions(Set<Connexion> connexions) {
        this.connexions = connexions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Connector connector = (Connector) o;
        if (connector.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), connector.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Connector{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", driver='" + getDriver() + "'" +
            "}";
    }
}
