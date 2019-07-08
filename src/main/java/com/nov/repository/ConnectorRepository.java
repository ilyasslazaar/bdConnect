package com.nov.repository;

import com.nov.domain.Connector;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Connector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {

}
