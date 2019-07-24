package com.nov.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Connexion.
 */
@Entity
@Table(name = "connexion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Connexion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_user")
    private String user;

    @Column(name = "jhi_password")
    private String password;

    @Column(name = "jhi_ssl")
    private Boolean ssl;

    @Column(name = "port")
    private String port;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "current_database")
    private String currentDatabase;

    @OneToMany(mappedBy = "connexion",fetch = FetchType.EAGER)
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Query> queries = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("connexions")
    private User conxUser;

    @ManyToOne
    @JsonIgnoreProperties("connexions")
    private Connector connector;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Connexion name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public Connexion user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public Connexion password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isSsl() {
        return ssl;
    }

    public Connexion ssl(Boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public String getPort() {
        return port;
    }

    public Connexion port(String port) {
        this.port = port;
        return this;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public Connexion hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCurrentDatabase() {
        return currentDatabase;
    }

    public Connexion currentDatabase(String currentDatabase) {
        this.currentDatabase = currentDatabase;
        return this;
    }

    public void setCurrentDatabase(String currentDatabase) {
        this.currentDatabase = currentDatabase;
    }

    public Set<Query> getQueries() {
        return queries;
    }

    public Connexion queries(Set<Query> queries) {
        this.queries = queries;
        return this;
    }

    public Connexion addQueries(Query query) {
        this.queries.add(query);
        query.setConnexion(this);
        return this;
    }

    public Connexion removeQueries(Query query) {
        this.queries.remove(query);
        query.setConnexion(null);
        return this;
    }

    public void setQueries(Set<Query> queries) {
        this.queries = queries;
    }

    public User getConxUser() {
        return conxUser;
    }

    public Connexion conxUser(User user) {
        this.conxUser = user;
        return this;
    }

    public void setConxUser(User user) {
        this.conxUser = user;
    }

    public Connector getConnector() {
        return connector;
    }

    public Connexion connector(Connector connector) {
        this.connector = connector;
        return this;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
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
        Connexion connexion = (Connexion) o;
        if (connexion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), connexion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Connexion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", user='" + getUser() + "'" +
            ", password='" + getPassword() + "'" +
            ", ssl='" + isSsl() + "'" +
            ", port='" + getPort() + "'" +
            ", hostname='" + getHostname() + "'" +
            ", currentDatabase='" + getCurrentDatabase() + "'" +
            "}";
    }
}
