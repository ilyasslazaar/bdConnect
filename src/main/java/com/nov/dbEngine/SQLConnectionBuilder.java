package com.nov.dbEngine;


import com.nov.domain.Connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public Connection build() throws SQLException, ClassNotFoundException {

        return DriverManager.getConnection(connexion.getHostname()+connexion.getPort()+"/"
                             +connexion.getCurrentDatabase(),connexion.getUser(),
                                                         connexion.getPassword());
    }

    public Connection reset(String database) throws SQLException, ClassNotFoundException {
        connexion.setCurrentDatabase(database);
        return this.build();
    }

}
