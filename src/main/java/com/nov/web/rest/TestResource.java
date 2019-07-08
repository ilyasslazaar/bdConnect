package com.nov.web.rest;


import com.nov.dbEngine.SQLConnectionBuilder;
import com.nov.domain.Connexion;
import com.nov.domain.Query;
import com.nov.repository.ConnexionRepository;
import com.nov.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private ConnexionRepository repository;
    @Autowired
    private QueryRepository queryRepository;

    @GetMapping("/conn")
    public Principal testConnection(Principal principal){


        return principal;
    }

}
