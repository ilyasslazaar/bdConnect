package com.nov.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Query.
 */
@Entity
@Table(name = "query")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Query implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "statment")
    private String statment;

    @Column(name = "created_at")
    private LocalDate created_at;

    @OneToMany(mappedBy = "query")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Execution> executions = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "connexion_id")
    @JsonIgnoreProperties("queries")
    private Connexion connexion;

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

    public Query type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Query name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatment() {
        return statment;
    }

    public Query statment(String statment) {
        this.statment = statment;
        return this;
    }

    public void setStatment(String statment) {
        this.statment = statment;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public Query created_at(LocalDate created_at) {
        this.created_at = created_at;
        return this;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Set<Execution> getExecutions() {
        return executions;
    }

    public Query executions(Set<Execution> executions) {
        this.executions = executions;
        return this;
    }

    public Query addExecutions(Execution execution) {
        this.executions.add(execution);
        execution.setQuery(this);
        return this;
    }

    public Query removeExecutions(Execution execution) {
        this.executions.remove(execution);
        execution.setQuery(null);
        return this;
    }

    public void setExecutions(Set<Execution> executions) {
        this.executions = executions;
    }

    public Connexion getConnexion() {
        return connexion;
    }

    public Query connexion(Connexion connexion) {
        this.connexion = connexion;
        return this;
    }

    public void setConnexion(Connexion connexion) {
        this.connexion = connexion;
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
        Query query = (Query) o;
        if (query.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), query.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Query{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", statment='" + getStatment() + "'" +
            ", created_at='" + getCreated_at() + "'" +
            "}";
    }
}
