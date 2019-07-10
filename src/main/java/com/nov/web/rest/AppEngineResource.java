package com.nov.web.rest;


import com.nov.dbEngine.SQLConnectionBuilder;
import com.nov.dbEngine.models.SQLTable;
import com.nov.domain.Connexion;
import com.nov.domain.Execution;
import com.nov.domain.Query;
import com.nov.service.AppEngineService;
import com.nov.service.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppEngineResource {

    @Autowired
    private  ConnexionService connexionService;

    @Autowired
    private AppEngineService appEngineService;
    //Recieve a connection and statment and return dynamique  result

    @PostMapping("/statment")
    public SQLTable executeStatment(@RequestBody Map<String, Object> payload) throws Exception {
        Connexion  conn;
        Long connexion_id;
        String statment = null;
        SQLTable table = null;
        System.out.println(payload);
        try{
            statment = (String)payload.get("statement");
            connexion_id = Long.valueOf((String)(payload.get("connection")));
            conn = connexionService.getConnexionById(connexion_id);
            if(conn != null && statment !=null){
                // execute statment
                 table = appEngineService.executeQuery(conn, statment);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return table;
    }
    @GetMapping("/statment/{query_id}")
    public SQLTable executeStatmentWithId(@PathVariable Long query_id) throws Exception {
        SQLTable table = null;

        try{

            Query query = connexionService.getConnextionQueryById(query_id);
            if(query !=null){
                // execute statment
                table = appEngineService.executeQuery(query.getConnexion(), query);

            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return table;
    }

    @GetMapping("/databases/{id}")
    public SQLTable getAllConnectionCatalog(@PathVariable Long id){

        return appEngineService.getAllDatabases(connexionService.getConnexionById(id));
    }



}
