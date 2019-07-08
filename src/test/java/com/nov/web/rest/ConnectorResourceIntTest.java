package com.nov.web.rest;

import com.nov.ReportingFullStackApp;

import com.nov.domain.Connector;
import com.nov.repository.ConnectorRepository;
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
 * Test class for the ConnectorResource REST controller.
 *
 * @see ConnectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportingFullStackApp.class)
public class ConnectorResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DRIVER = "AAAAAAAAAA";
    private static final String UPDATED_DRIVER = "BBBBBBBBBB";

    @Autowired
    private ConnectorRepository connectorRepository;

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

    private MockMvc restConnectorMockMvc;

    private Connector connector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConnectorResource connectorResource = new ConnectorResource(connectorRepository);
        this.restConnectorMockMvc = MockMvcBuilders.standaloneSetup(connectorResource)
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
    public static Connector createEntity(EntityManager em) {
        Connector connector = new Connector()
            .type(DEFAULT_TYPE)
            .driver(DEFAULT_DRIVER);
        return connector;
    }

    @Before
    public void initTest() {
        connector = createEntity(em);
    }

    @Test
    @Transactional
    public void createConnector() throws Exception {
        int databaseSizeBeforeCreate = connectorRepository.findAll().size();

        // Create the Connector
        restConnectorMockMvc.perform(post("/api/connectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connector)))
            .andExpect(status().isCreated());

        // Validate the Connector in the database
        List<Connector> connectorList = connectorRepository.findAll();
        assertThat(connectorList).hasSize(databaseSizeBeforeCreate + 1);
        Connector testConnector = connectorList.get(connectorList.size() - 1);
        assertThat(testConnector.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testConnector.getDriver()).isEqualTo(DEFAULT_DRIVER);
    }

    @Test
    @Transactional
    public void createConnectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = connectorRepository.findAll().size();

        // Create the Connector with an existing ID
        connector.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnectorMockMvc.perform(post("/api/connectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connector)))
            .andExpect(status().isBadRequest());

        // Validate the Connector in the database
        List<Connector> connectorList = connectorRepository.findAll();
        assertThat(connectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = connectorRepository.findAll().size();
        // set the field null
        connector.setType(null);

        // Create the Connector, which fails.

        restConnectorMockMvc.perform(post("/api/connectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connector)))
            .andExpect(status().isBadRequest());

        List<Connector> connectorList = connectorRepository.findAll();
        assertThat(connectorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConnectors() throws Exception {
        // Initialize the database
        connectorRepository.saveAndFlush(connector);

        // Get all the connectorList
        restConnectorMockMvc.perform(get("/api/connectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(connector.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].driver").value(hasItem(DEFAULT_DRIVER.toString())));
    }
    
    @Test
    @Transactional
    public void getConnector() throws Exception {
        // Initialize the database
        connectorRepository.saveAndFlush(connector);

        // Get the connector
        restConnectorMockMvc.perform(get("/api/connectors/{id}", connector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(connector.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.driver").value(DEFAULT_DRIVER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConnector() throws Exception {
        // Get the connector
        restConnectorMockMvc.perform(get("/api/connectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConnector() throws Exception {
        // Initialize the database
        connectorRepository.saveAndFlush(connector);

        int databaseSizeBeforeUpdate = connectorRepository.findAll().size();

        // Update the connector
        Connector updatedConnector = connectorRepository.findById(connector.getId()).get();
        // Disconnect from session so that the updates on updatedConnector are not directly saved in db
        em.detach(updatedConnector);
        updatedConnector
            .type(UPDATED_TYPE)
            .driver(UPDATED_DRIVER);

        restConnectorMockMvc.perform(put("/api/connectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConnector)))
            .andExpect(status().isOk());

        // Validate the Connector in the database
        List<Connector> connectorList = connectorRepository.findAll();
        assertThat(connectorList).hasSize(databaseSizeBeforeUpdate);
        Connector testConnector = connectorList.get(connectorList.size() - 1);
        assertThat(testConnector.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testConnector.getDriver()).isEqualTo(UPDATED_DRIVER);
    }

    @Test
    @Transactional
    public void updateNonExistingConnector() throws Exception {
        int databaseSizeBeforeUpdate = connectorRepository.findAll().size();

        // Create the Connector

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnectorMockMvc.perform(put("/api/connectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connector)))
            .andExpect(status().isBadRequest());

        // Validate the Connector in the database
        List<Connector> connectorList = connectorRepository.findAll();
        assertThat(connectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConnector() throws Exception {
        // Initialize the database
        connectorRepository.saveAndFlush(connector);

        int databaseSizeBeforeDelete = connectorRepository.findAll().size();

        // Delete the connector
        restConnectorMockMvc.perform(delete("/api/connectors/{id}", connector.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Connector> connectorList = connectorRepository.findAll();
        assertThat(connectorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Connector.class);
        Connector connector1 = new Connector();
        connector1.setId(1L);
        Connector connector2 = new Connector();
        connector2.setId(connector1.getId());
        assertThat(connector1).isEqualTo(connector2);
        connector2.setId(2L);
        assertThat(connector1).isNotEqualTo(connector2);
        connector1.setId(null);
        assertThat(connector1).isNotEqualTo(connector2);
    }
}
