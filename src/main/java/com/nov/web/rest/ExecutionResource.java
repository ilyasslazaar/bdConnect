package com.nov.web.rest;
import com.nov.domain.Execution;
import com.nov.repository.ExecutionRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Execution.
 */
@RestController
@RequestMapping("/api")
public class ExecutionResource {

    private final Logger log = LoggerFactory.getLogger(ExecutionResource.class);

    private static final String ENTITY_NAME = "execution";

    private final ExecutionRepository executionRepository;

    public ExecutionResource(ExecutionRepository executionRepository) {
        this.executionRepository = executionRepository;
    }

    /**
     * POST  /executions : Create a new execution.
     *
     * @param execution the execution to create
     * @return the ResponseEntity with status 201 (Created) and with body the new execution, or with status 400 (Bad Request) if the execution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/executions")
    public ResponseEntity<Execution> createExecution(@RequestBody Execution execution) throws URISyntaxException {
        log.debug("REST request to save Execution : {}", execution);
        if (execution.getId() != null) {
            throw new BadRequestAlertException("A new execution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Execution result = executionRepository.save(execution);
        return ResponseEntity.created(new URI("/api/executions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /executions : Updates an existing execution.
     *
     * @param execution the execution to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated execution,
     * or with status 400 (Bad Request) if the execution is not valid,
     * or with status 500 (Internal Server Error) if the execution couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/executions")
    public ResponseEntity<Execution> updateExecution(@RequestBody Execution execution) throws URISyntaxException {
        log.debug("REST request to update Execution : {}", execution);
        if (execution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Execution result = executionRepository.save(execution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, execution.getId().toString()))
            .body(result);
    }

    /**
     * GET  /executions : get all the executions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of executions in body
     */
    @GetMapping("/executions")
    public ResponseEntity<List<Execution>> getAllExecutions(Pageable pageable) {
        log.debug("REST request to get a page of Executions");
        Page<Execution> page = executionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/executions");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /executions/:id : get the "id" execution.
     *
     * @param id the id of the execution to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the execution, or with status 404 (Not Found)
     */
    @GetMapping("/executions/{id}")
    public ResponseEntity<Execution> getExecution(@PathVariable Long id) {
        log.debug("REST request to get Execution : {}", id);
        Optional<Execution> execution = executionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(execution);
    }

    /**
     * DELETE  /executions/:id : delete the "id" execution.
     *
     * @param id the id of the execution to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/executions/{id}")
    public ResponseEntity<Void> deleteExecution(@PathVariable Long id) {
        log.debug("REST request to delete Execution : {}", id);
        executionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
