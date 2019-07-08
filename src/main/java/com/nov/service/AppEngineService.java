package com.nov.service;

import com.nov.dbEngine.SQLConnectionBuilder;
import com.nov.domain.Connexion;
import com.nov.repository.ConnexionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Service
@Transactional
public class AppEngineService {

    private final Logger log = LoggerFactory.getLogger(AppEngineService.class);

    @Autowired
    private ConnexionRepository repository;


    public ArrayList<String> getAllDatabases(){

        Connexion conn = repository.findById(1L).get();


        if(conn == null){
            return  null;

        }

        SQLConnectionBuilder builder = new SQLConnectionBuilder(conn);
        Connection connection;
        ResultSet resultset = null;
        ArrayList<String> data = new ArrayList<>();
        try {


            String st = builder.getConnetionParams().getQueries()
                .stream().findFirst().get().getStatment();

            System.out.println(st);
            Statement stm =  builder.build().createStatement();
            resultset = stm.executeQuery(st);


            if (stm.execute(st)) {
                resultset = stm.getResultSet();
                while (resultset.next()) {
                    data.add(resultset.getString("Database"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return data;

    }



}
