package com.nov.repository;


import com.nov.domain.Execution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Spring Data  repository for the Query entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueryRepository extends JpaRepository<com.nov.domain.Query, Long> {
  @Query(value = "select* from query where connexion_id = ?1 ",nativeQuery = true)
     List<com.nov.domain.Query>getAllByConnexionId(Long id);


}
