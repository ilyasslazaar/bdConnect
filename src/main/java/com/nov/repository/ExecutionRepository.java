package com.nov.repository;

import com.nov.domain.Execution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Execution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    @Query(value = "SELECT e from  Execution e join e.query  q where q.id = ?1 ")
     List<Execution> getExecutionByQueryId(Long id);

}
