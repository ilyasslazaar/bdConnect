package com.nov;

import com.nov.config.ApplicationProperties;
import com.nov.config.DefaultProfileUtil;
import com.nov.dbEngine.Constants;
import com.nov.domain.Connector;
import com.nov.repository.ConnectorRepository;
import io.github.jhipster.config.JHipsterConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class ReportingFullStackApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ReportingFullStackApp.class);

    private final Environment env;

    @Autowired
    ConnectorRepository connectorRepository;

    public ReportingFullStackApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes Reporting_FullStack.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReportingFullStackApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    @Override
    public void run(String... args) throws Exception {

        // checking if connectors already exist in database
        if (connectorRepository.findAll().isEmpty()) {
            log.info("[ Connectors ] : adding connectors to Database");
            connectorRepository.saveAll(Arrays.asList(new Connector(Constants.MYSQL, Constants.MYSQL_DRIVER),
                new Connector(Constants.ORACLE, Constants.ORACLE_DRIVER),
                new Connector(Constants.POSTGRESQL, Constants.POSTGRESQL_DRIVER)
            ));
        }
    }
}
