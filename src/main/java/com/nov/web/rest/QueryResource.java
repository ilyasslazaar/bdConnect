package com.nov.web.rest;
import com.nov.domain.Execution;
import com.nov.domain.Query;
import com.nov.repository.QueryRepository;
import com.nov.service.QueryService;
import com.nov.web.rest.errors.BadRequestAlertException;
import com.nov.web.rest.util.HeaderUtil;
import com.nov.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing Query.
 */
@RestController
@RequestMapping("/api")
public class QueryResource {

    @Autowired
    private QueryService queryService;

    private final Logger log = LoggerFactory.getLogger(QueryResource.class);

    private static final String ENTITY_NAME = "query";

    private final QueryRepository queryRepository;

    public QueryResource(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    /**
     * POST  /queries : Create a new query.
     *
     * @param query the query to create
     * @return the ResponseEntity with status 201 (Created) and with body the new query, or with status 400 (Bad Request) if the query has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/queries")
    public ResponseEntity<Query> createQuery(@Valid @RequestBody Query query) throws URISyntaxException {
        log.debug("REST request to save Query : {}", query);
        if (query.getId() != null) {
            throw new BadRequestAlertException("A new query cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Query result = queryRepository.save(query);
        return ResponseEntity.created(new URI("/api/queries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /queries : Updates an existing query.
     *
     * @param query the query to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated query,
     * or with status 400 (Bad Request) if the query is not valid,
     * or with status 500 (Internal Server Error) if the query couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/queries")
    public ResponseEntity<Query> updateQuery(@Valid @RequestBody Query query) throws URISyntaxException {
        log.debug("REST request to update Query : {}", query);
        if (query.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Query result = queryRepository.save(query);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, query.getId().toString()))
            .body(result);
    }

    /**
     * GET  /queries : get all the queries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of queries in body
     */
    @GetMapping("/queries")
    public ResponseEntity<List<Query>> getAllQueries(Pageable pageable) {
        log.debug("REST request to get a page of Queries");
        Page<Query> page = queryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/queries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /queries/:id : get the "id" query.
     *
     * @param id the id of the query to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the query, or with status 404 (Not Found)
     */
    @GetMapping("/queries/{id}")
    public ResponseEntity<Query> getQuery(@PathVariable Long id) {
        log.debug("REST request to get Query : {}", id);
        Optional<Query> query = queryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(query);
    }

    /**
     * DELETE  /queries/:id : delete the "id" query.
     *
     * @param id the id of the query to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/queries/{id}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Long id) {
        log.debug("REST request to delete Query : {}", id);
        queryRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    //getting Queries  ByConnexionId
    @GetMapping("/userqueries/{id}")
    public List<Query> getQueriesByConnection(@PathVariable  Long id){

        return queryService.getAllQueriesByConnectionId(id);

    }




}
