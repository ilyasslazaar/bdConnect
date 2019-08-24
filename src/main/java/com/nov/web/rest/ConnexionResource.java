package com.nov.web.rest;
import com.nov.dbEngine.models.ConnexionId;
import com.nov.domain.*;
import com.nov.repository.ConnectorRepository;
import com.nov.repository.ConnexionRepository;
import com.nov.security.SecurityUtils;
import com.nov.service.ConnexionService;
import com.nov.service.QueryService;
import com.nov.web.rest.errors.BadRequestAlertException;
import com.nov.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Connexion.
 */
@RestController
@RequestMapping("/api")
public class ConnexionResource {

    private final Logger log = LoggerFactory.getLogger(ConnexionResource.class);

    private static final String ENTITY_NAME = "connexion";

    private final ConnexionRepository connexionRepository;

    @Autowired
    private ConnexionService connexionService;

    @Autowired
    private ConnectorRepository connectorRepository;

    public ConnexionResource(ConnexionRepository connexionRepository) {
        this.connexionRepository = connexionRepository;
    }

    /**
     * POST  /connexions : Create a new connexion.
     *
     * @param connexion the connexion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new connexion, or with status 400 (Bad Request) if the connexion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/connexions")
    public ResponseEntity<Connexion> createConnexion(@RequestBody Connexion connexion,@RequestParam Long connectorId) throws URISyntaxException {
        // this code is externalized because its used twice
        AnalyseRequestAndassociateConnector(connexion, connectorId);

        Connexion result = connexionService.saveConnexion(connexion);
     // this code is externalized because its used twice
        return getConnexionResponseEntity(result);
    }

    private ResponseEntity<Connexion> getConnexionResponseEntity(Connexion result) throws URISyntaxException {
        return ResponseEntity.created(new URI("/api/connexions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /connexions : Updates an existing connexion.
     *
     * @param connexion the connexion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated connexion,
     * or with status 400 (Bad Request) if the connexion is not valid,
     * or with status 500 (Internal Server Error) if the connexion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/connexions")
    public ResponseEntity<Connexion> updateConnexion(@RequestBody Connexion connexion) throws URISyntaxException {
        log.debug("REST request to update Connexion : {}", connexion);
        return SaveConnection(connexion);
    }

    /**
     * GET  /connexions : get all the connexions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of connexions in body
     */
    @GetMapping("/connexions")
    public List<Connexion> getAllConnexions() {
        log.debug("REST request to get all Connexions");
        return (List<Connexion>) connexionRepository.findAll();
    }

    /**
     * GET  /connexions/:id : get the "id" connexion.
     *
     * @param id the id of the connexion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the connexion, or with status 404 (Not Found)
     */
    @GetMapping("/connexions/{id}")
    public ResponseEntity<Connexion> getConnexion(@PathVariable Long id) {
        log.debug("REST request to get Connexion : {}", id);
        Optional<Connexion> connexion = connexionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(connexion);
    }

    /**
     * DELETE  /connexions/:id : delete the "id" connexion.
     *
     * @param id the id of the connexion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/connexions/{id}")
    public ResponseEntity<Void> deleteConnexion(@PathVariable Long id) {
        log.debug("REST request to delete Connexion : {}", id);
        connexionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //getConnections by userId

    @GetMapping("/currentUserConnections/{byUser}")
    public Page<Connexion> getConnexionsByUser(@RequestParam Integer currentPage, @RequestParam Integer pageSize,
                                               @RequestParam String search, @RequestParam String orderBy,@PathVariable Integer byUser){


        return connexionService.getConnexionsByUserId(currentPage,pageSize,search,orderBy,byUser);
    }

    @PostMapping("/connexions/delete")
    public ResponseEntity<Void> deleteConnexions(@RequestBody List<Long> ids) {
        log.debug("REST request to delete Connexion : {}", ids);
        connexionService.deleteListOfConnctions(ids);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, ids.toString())).build();
    }


    @GetMapping("/connexions/updateDatabase/{conn}/{database}")
    public void  updateDtatabase(@PathVariable String database,@PathVariable Long conn){

        Connexion connexion = connexionRepository.findById(conn).get();
        if(connexion == null){
            throw new RuntimeException("Error : no connection with id = "+conn);

        }
        connexion.setCurrentDatabase(database);
        connexionRepository.save(connexion);
    }


    @PutMapping("/connexions/{connectorId}")
    public ResponseEntity<Connexion> updateConn(@RequestBody Connexion connexion,@PathVariable String connectorId) throws URISyntaxException {

        log.debug("REST request to update Connexion with connector : {}", connexion);
        connexion.setConnector(connectorRepository.getOne(Long.valueOf(connectorId)));
        return SaveConnection(connexion);
    }
// extracted this code  because it is used twice in put connection Actions
    // created new one to keep back Office  working correctly
    private ResponseEntity<Connexion> SaveConnection(@RequestBody Connexion connexion) {
        if (connexion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id is null");
        }
        Connexion result = connexionService.saveConnexion(connexion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, connexion.getId().toString()))
            .body(result);
    }


    @PostMapping("/connexions/{userId}/{connectorId}")
    public ResponseEntity<Connexion> createConnexionForUser(@RequestBody Connexion connexion,
                                                            @PathVariable Long connectorId,@PathVariable Long userId ) throws URISyntaxException {

        AnalyseRequestAndassociateConnector(connexion, connectorId);
        Connexion result = connexionService.saveConnexion(connexion,userId); // add connection and associate it to a user
        return getConnexionResponseEntity(result);
    }

    private void AnalyseRequestAndassociateConnector(@RequestBody Connexion connexion, @PathVariable Long connectorId) {
        log.debug("REST request to save Connexion : {}", connexion);
        if (connexion.getId() != null) {
            throw new BadRequestAlertException("A new connexion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Connector connector = connectorRepository.findById(connectorId).get();
        connexion.setConnector(connector);
    }


}
