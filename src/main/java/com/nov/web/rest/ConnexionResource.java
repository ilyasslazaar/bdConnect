package com.nov.web.rest;
import com.nov.domain.Connexion;
import com.nov.repository.ConnexionRepository;
import com.nov.web.rest.errors.BadRequestAlertException;
import com.nov.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

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
    public ResponseEntity<Connexion> createConnexion(@Valid @RequestBody Connexion connexion) throws URISyntaxException {
        log.debug("REST request to save Connexion : {}", connexion);
        if (connexion.getId() != null) {
            throw new BadRequestAlertException("A new connexion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Connexion result = connexionRepository.save(connexion);
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
    public ResponseEntity<Connexion> updateConnexion(@Valid @RequestBody Connexion connexion) throws URISyntaxException {
        log.debug("REST request to update Connexion : {}", connexion);
        if (connexion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Connexion result = connexionRepository.save(connexion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, connexion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /connexions : get all the connexions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of connexions in body
     */
    @GetMapping("/connexions")
    public List<Connexion> getAllConnexions() {
        log.debug("REST request to get all Connexions");
        return connexionRepository.findAll();
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
}
