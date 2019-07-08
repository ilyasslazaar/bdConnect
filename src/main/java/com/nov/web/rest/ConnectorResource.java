package com.nov.web.rest;
import com.nov.domain.Connector;
import com.nov.repository.ConnectorRepository;
import com.nov.web.rest.errors.BadRequestAlertException;
import com.nov.web.rest.util.HeaderUtil;
import com.nov.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Connector.
 */
@RestController
@RequestMapping("/api")
public class ConnectorResource {

    private final Logger log = LoggerFactory.getLogger(ConnectorResource.class);

    private static final String ENTITY_NAME = "connector";

    private final ConnectorRepository connectorRepository;

    public ConnectorResource(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    /**
     * POST  /connectors : Create a new connector.
     *
     * @param connector the connector to create
     * @return the ResponseEntity with status 201 (Created) and with body the new connector, or with status 400 (Bad Request) if the connector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/connectors")
    public ResponseEntity<Connector> createConnector(@Valid @RequestBody Connector connector) throws URISyntaxException {
        log.debug("REST request to save Connector : {}", connector);
        if (connector.getId() != null) {
            throw new BadRequestAlertException("A new connector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Connector result = connectorRepository.save(connector);
        return ResponseEntity.created(new URI("/api/connectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /connectors : Updates an existing connector.
     *
     * @param connector the connector to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated connector,
     * or with status 400 (Bad Request) if the connector is not valid,
     * or with status 500 (Internal Server Error) if the connector couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/connectors")
    public ResponseEntity<Connector> updateConnector(@Valid @RequestBody Connector connector) throws URISyntaxException {
        log.debug("REST request to update Connector : {}", connector);
        if (connector.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Connector result = connectorRepository.save(connector);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, connector.getId().toString()))
            .body(result);
    }

    /**
     * GET  /connectors : get all the connectors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of connectors in body
     */
    @GetMapping("/connectors")
    public ResponseEntity<List<Connector>> getAllConnectors(Pageable pageable) {
        log.debug("REST request to get a page of Connectors");
        Page<Connector> page = connectorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/connectors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /connectors/:id : get the "id" connector.
     *
     * @param id the id of the connector to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the connector, or with status 404 (Not Found)
     */
    @GetMapping("/connectors/{id}")
    public ResponseEntity<Connector> getConnector(@PathVariable Long id) {
        log.debug("REST request to get Connector : {}", id);
        Optional<Connector> connector = connectorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(connector);
    }

    /**
     * DELETE  /connectors/:id : delete the "id" connector.
     *
     * @param id the id of the connector to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/connectors/{id}")
    public ResponseEntity<Void> deleteConnector(@PathVariable Long id) {
        log.debug("REST request to delete Connector : {}", id);
        connectorRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
