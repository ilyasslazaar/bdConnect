package com.nov.web.rest;

import com.nov.ReportingFullStackApp;

import com.nov.domain.Connexion;
import com.nov.repository.ConnexionRepository;
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
import java.util.List;


import static com.nov.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ConnexionResource REST controller.
 *
 * @see ConnexionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportingFullStackApp.class)
public class ConnexionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SSL = false;
    private static final Boolean UPDATED_SSL = true;

    private static final String DEFAULT_PORT = "AAAAAAAAAA";
    private static final String UPDATED_PORT = "BBBBBBBBBB";

    private static final String DEFAULT_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENT_DATABASE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_DATABASE = "BBBBBBBBBB";

    @Autowired
    private ConnexionRepository connexionRepository;

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

    private MockMvc restConnexionMockMvc;

    private Connexion connexion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConnexionResource connexionResource = new ConnexionResource(connexionRepository);
        this.restConnexionMockMvc = MockMvcBuilders.standaloneSetup(connexionResource)
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
    public static Connexion createEntity(EntityManager em) {
        Connexion connexion = new Connexion()
            .name(DEFAULT_NAME)
            .user(DEFAULT_USER)
            .password(DEFAULT_PASSWORD)
            .ssl(DEFAULT_SSL)
            .port(DEFAULT_PORT)
            .hostname(DEFAULT_HOSTNAME)
            .currentDatabase(DEFAULT_CURRENT_DATABASE);
        return connexion;
    }

    @Before
    public void initTest() {
        connexion = createEntity(em);
    }

    @Test
    @Transactional
    public void createConnexion() throws Exception {
        int databaseSizeBeforeCreate = connexionRepository.findAll().size();

        // Create the Connexion
        restConnexionMockMvc.perform(post("/api/connexions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connexion)))
            .andExpect(status().isCreated());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeCreate + 1);
        Connexion testConnexion = connexionList.get(connexionList.size() - 1);
        assertThat(testConnexion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConnexion.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testConnexion.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testConnexion.isSsl()).isEqualTo(DEFAULT_SSL);
        assertThat(testConnexion.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testConnexion.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
        assertThat(testConnexion.getCurrentDatabase()).isEqualTo(DEFAULT_CURRENT_DATABASE);
    }

    @Test
    @Transactional
    public void createConnexionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = connexionRepository.findAll().size();

        // Create the Connexion with an existing ID
        connexion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnexionMockMvc.perform(post("/api/connexions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connexion)))
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConnexions() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        // Get all the connexionList
        restConnexionMockMvc.perform(get("/api/connexions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(connexion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].ssl").value(hasItem(DEFAULT_SSL.booleanValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.toString())))
            .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME.toString())))
            .andExpect(jsonPath("$.[*].currentDatabase").value(hasItem(DEFAULT_CURRENT_DATABASE.toString())));
    }
    
    @Test
    @Transactional
    public void getConnexion() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        // Get the connexion
        restConnexionMockMvc.perform(get("/api/connexions/{id}", connexion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(connexion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.ssl").value(DEFAULT_SSL.booleanValue()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.toString()))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME.toString()))
            .andExpect(jsonPath("$.currentDatabase").value(DEFAULT_CURRENT_DATABASE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConnexion() throws Exception {
        // Get the connexion
        restConnexionMockMvc.perform(get("/api/connexions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConnexion() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();

        // Update the connexion
        Connexion updatedConnexion = connexionRepository.findById(connexion.getId()).get();
        // Disconnect from session so that the updates on updatedConnexion are not directly saved in db
        em.detach(updatedConnexion);
        updatedConnexion
            .name(UPDATED_NAME)
            .user(UPDATED_USER)
            .password(UPDATED_PASSWORD)
            .ssl(UPDATED_SSL)
            .port(UPDATED_PORT)
            .hostname(UPDATED_HOSTNAME)
            .currentDatabase(UPDATED_CURRENT_DATABASE);

        restConnexionMockMvc.perform(put("/api/connexions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConnexion)))
            .andExpect(status().isOk());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
        Connexion testConnexion = connexionList.get(connexionList.size() - 1);
        assertThat(testConnexion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConnexion.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testConnexion.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testConnexion.isSsl()).isEqualTo(UPDATED_SSL);
        assertThat(testConnexion.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testConnexion.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testConnexion.getCurrentDatabase()).isEqualTo(UPDATED_CURRENT_DATABASE);
    }

    @Test
    @Transactional
    public void updateNonExistingConnexion() throws Exception {
        int databaseSizeBeforeUpdate = connexionRepository.findAll().size();

        // Create the Connexion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnexionMockMvc.perform(put("/api/connexions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connexion)))
            .andExpect(status().isBadRequest());

        // Validate the Connexion in the database
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConnexion() throws Exception {
        // Initialize the database
        connexionRepository.saveAndFlush(connexion);

        int databaseSizeBeforeDelete = connexionRepository.findAll().size();

        // Delete the connexion
        restConnexionMockMvc.perform(delete("/api/connexions/{id}", connexion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Connexion> connexionList = connexionRepository.findAll();
        assertThat(connexionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Connexion.class);
        Connexion connexion1 = new Connexion();
        connexion1.setId(1L);
        Connexion connexion2 = new Connexion();
        connexion2.setId(connexion1.getId());
        assertThat(connexion1).isEqualTo(connexion2);
        connexion2.setId(2L);
        assertThat(connexion1).isNotEqualTo(connexion2);
        connexion1.setId(null);
        assertThat(connexion1).isNotEqualTo(connexion2);
    }
}
