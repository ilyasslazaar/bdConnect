package com.nov.web.rest;


import com.nov.dbEngine.SQLConnectionBuilder;
import com.nov.dbEngine.models.Row;
import com.nov.dbEngine.models.SQLTable;
import com.nov.domain.Connexion;
import com.nov.domain.Query;
import com.nov.repository.ConnexionRepository;
import com.nov.repository.QueryRepository;
import com.nov.service.AppEngineService;
import com.nov.service.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api")
public class TestResource {

    @Autowired
    private AppEngineService service;
    @Autowired
    private ConnexionService connexionService;

    @GetMapping("/conn/{id}")
    public SQLTable testConnection(@PathVariable Long id) throws Exception {

        if(id ==null){
            throw new Exception("id is null"+id);
        }
        Query query =  connexionService.getConnextionQueryById(id);
        service.getAllDatabases(query.getConnexion());

        return service.getAllDatabases(query.getConnexion());
    }

}
