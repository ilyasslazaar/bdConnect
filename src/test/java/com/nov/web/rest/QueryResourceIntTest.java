package com.nov.web.rest;

import com.nov.ReportingFullStackApp;

import com.nov.domain.Query;
import com.nov.repository.QueryRepository;
import com.nov.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.nov.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QueryResource REST controller.
 *
 * @see QueryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportingFullStackApp.class)
public class QueryResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATMENT = "AAAAAAAAAA";
    private static final String UPDATED_STATMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DATABASE = "AAAAAAAAAA";
    private static final String UPDATED_DATABASE = "BBBBBBBBBB";

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restQueryMockMvc;

    private Query query;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QueryResource queryResource = new QueryResource(queryRepository);
        this.restQueryMockMvc = MockMvcBuilders.standaloneSetup(queryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Query createEntity(EntityManager em) {
        Query query = new Query()
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .statment(DEFAULT_STATMENT)
            .created_at(DEFAULT_CREATED_AT)
            .database(DEFAULT_DATABASE);
        return query;
    }

    @Before
    public void initTest() {
        query = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuery() throws Exception {
        int databaseSizeBeforeCreate = queryRepository.findAll().size();

        // Create the Query
        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isCreated());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeCreate + 1);
        Query testQuery = queryList.get(queryList.size() - 1);
        assertThat(testQuery.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuery.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuery.getStatment()).isEqualTo(DEFAULT_STATMENT);
        assertThat(testQuery.getCreated_at()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testQuery.getDatabase()).isEqualTo(DEFAULT_DATABASE);
    }

    @Test
    @Transactional
    public void createQueryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = queryRepository.findAll().size();

        // Create the Query with an existing ID
        query.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = queryRepository.findAll().size();
        // set the field null
        query.setType(null);

        // Create the Query, which fails.

        restQueryMockMvc.perform(post("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQueries() throws Exception {
        // Initialize the database
        queryRepository.saveAndFlush(query);

        // Get all the queryList
        restQueryMockMvc.perform(get("/api/queries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(query.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].statment").value(hasItem(DEFAULT_STATMENT.toString())))
            .andExpect(jsonPath("$.[*].created_at").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].database").value(hasItem(DEFAULT_DATABASE.toString())));
    }
    
    @Test
    @Transactional
    public void getQuery() throws Exception {
        // Initialize the database
        queryRepository.saveAndFlush(query);

        // Get the query
        restQueryMockMvc.perform(get("/api/queries/{id}", query.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(query.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.statment").value(DEFAULT_STATMENT.toString()))
            .andExpect(jsonPath("$.created_at").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.database").value(DEFAULT_DATABASE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuery() throws Exception {
        // Get the query
        restQueryMockMvc.perform(get("/api/queries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuery() throws Exception {
        // Initialize the database
        queryRepository.saveAndFlush(query);

        int databaseSizeBeforeUpdate = queryRepository.findAll().size();

        // Update the query
        Query updatedQuery = queryRepository.findById(query.getId()).get();
        // Disconnect from session so that the updates on updatedQuery are not directly saved in db
        em.detach(updatedQuery);
        updatedQuery
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .statment(UPDATED_STATMENT)
            .created_at(UPDATED_CREATED_AT)
            .database(UPDATED_DATABASE);

        restQueryMockMvc.perform(put("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuery)))
            .andExpect(status().isOk());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeUpdate);
        Query testQuery = queryList.get(queryList.size() - 1);
        assertThat(testQuery.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuery.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuery.getStatment()).isEqualTo(UPDATED_STATMENT);
        assertThat(testQuery.getCreated_at()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testQuery.getDatabase()).isEqualTo(UPDATED_DATABASE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuery() throws Exception {
        int databaseSizeBeforeUpdate = queryRepository.findAll().size();

        // Create the Query

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQueryMockMvc.perform(put("/api/queries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(query)))
            .andExpect(status().isBadRequest());

        // Validate the Query in the database
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuery() throws Exception {
        // Initialize the database
        queryRepository.saveAndFlush(query);

        int databaseSizeBeforeDelete = queryRepository.findAll().size();

        // Delete the query
        restQueryMockMvc.perform(delete("/api/queries/{id}", query.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Query> queryList = queryRepository.findAll();
        assertThat(queryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Query.class);
        Query query1 = new Query();
        query1.setId(1L);
        Query query2 = new Query();
        query2.setId(query1.getId());
        assertThat(query1).isEqualTo(query2);
        query2.setId(2L);
        assertThat(query1).isNotEqualTo(query2);
        query1.setId(null);
        assertThat(query1).isNotEqualTo(query2);
    }
}
