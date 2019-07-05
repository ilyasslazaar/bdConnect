package com.nov.repository;

import com.nov.domain.Execution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Execution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

}
