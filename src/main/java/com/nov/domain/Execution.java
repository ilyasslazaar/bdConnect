package com.nov.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Execution.
 */
@Entity
@Table(name = "execution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Execution implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ex_date")
    private LocalDate exDate;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    private Query query;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExDate() {
        return exDate;
    }

    public Execution exDate(LocalDate exDate) {
        this.exDate = exDate;
        return this;
    }

    public void setExDate(LocalDate exDate) {
        this.exDate = exDate;
    }

    public Boolean isStatus() {
        return status;
    }

    public Execution status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Query getQuery() {
        return query;
    }

    public Execution query(Query query) {
        this.query = query;
        return this;
    }

    public void setQuery(Query query) {
        this.query = query;
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
        Execution execution = (Execution) o;
        if (execution.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), execution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Execution{" +
            "id=" + getId() +
            ", exDate='" + getExDate() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
