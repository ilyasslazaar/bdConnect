package com.nov.dbEngine;



import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.nov.domain.Connexion;


public class SQLConnectionBuilder {
    private  Connexion connexion;

    public SQLConnectionBuilder(Connexion connexion){
        this.connexion = connexion;

    }
    public Connexion getConnetionParams(){
        return connexion;
    }
    public void setConnetionParams(Connexion conn){
        this.connexion = conn;
    }

    public JdbcTemplate build()  {

        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource(this.connexion.getConnector().getType()));
        return template;
    }

    public DataSource dataSource(String SGBD) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        String baseUrl = null;
        switch (SGBD){
            case Constants.MYSQL:
                baseUrl= Constants.MYSQL_BASE_URL;
                break;
            case Constants.ORACLE:
                baseUrl = Constants.ORACLE_BASE_URL;
                break;
            case Constants.H2:

                baseUrl = Constants.H2_BASE_URL;
                break;
            case Constants.POSTGRESQL:
                baseUrl = Constants.POSTGRE_SQL_BASE_URL;
                break;
            default:
                break;
        }
        ds.setDriverClassName(connexion.getConnector().getDriver());
        if(connexion.getConnector().getType().equals("oracle")){
            ds.setUrl(baseUrl + connexion.getHostname() + ":" + connexion.getPort() + ":"
                + connexion.getCurrentDatabase());
            ds.setUsername(connexion.getUser());
        }else {
            ds.setUrl(baseUrl + connexion.getHostname() + ":" + connexion.getPort() +"/"
                + connexion.getCurrentDatabase() );
            ds.setUsername(connexion.getUser());
        }
        ds.setPassword(connexion.getPassword());

        return ds;
    }




}
