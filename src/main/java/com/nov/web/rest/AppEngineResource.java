package com.nov.web.rest;


import com.nov.dbEngine.models.SQLTable;
import com.nov.domain.Connexion;
import com.nov.domain.Query;
import com.nov.service.AppEngineService;
import com.nov.service.ConnexionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppEngineResource {

    @Autowired
    private ConnexionService connexionService;
    @Autowired
    private AppEngineService appEngineService;



    @PostMapping("/statment")
    public SQLTable executeStatment(@RequestBody Map<String, Object> payload) throws Exception {
        Connexion conn;
        Long connexion_id;
        String statment = null;
        SQLTable table = null;
        Integer offset = 0, limit = 9;
        try {
            statment = (String) payload.get("statement");
            connexion_id = Long.valueOf((String) (payload.get("connection")));
            offset = Integer.valueOf((String) (payload.get("offset")));
            limit = Integer.valueOf((String) (payload.get("limit")));

            conn = connexionService.getConnexionById(connexion_id);
            if (conn != null && statment != null) {
                offset = (offset == 1) ? offset : offset * 10 - 10;
                table = appEngineService.executeQuery(conn, statment, offset, limit);
            }
        } catch (Exception e) {
            System.out.println("Statment Error Message : " + e.getMessage());
            throw e;
        }
        return table;
    }

    @GetMapping("/statment/{query_id}")
    public SQLTable executeStatmentWithId(@PathVariable Long query_id, @RequestParam Integer cp, @RequestParam Integer ps) throws Exception {
        SQLTable table = null;
        int currentPage = (cp == 1) ? cp : cp * 10;
        int pageSize = ps;
        try {
            Query query = connexionService.getConnextionQueryById(query_id);
            if (query != null) {
                table = appEngineService.executeQuery(query.getConnexion(), query, currentPage, pageSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    @GetMapping("/databases/{id}")
    public SQLTable getAllConnectionCatalog(@PathVariable Long id) {
        return appEngineService.getAllDatabases(connexionService.getConnexionById(id));
    }


}
