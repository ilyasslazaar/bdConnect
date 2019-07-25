package com.nov.repository;


import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Query entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QueryRepository extends JpaRepository<com.nov.domain.Query, Long> {

   @Query(value = "select query.id,query.jhi_type,query.statment,query.name from query  " +
                   "join connexion " +
                    "WHERE connexion.id = query.connexion_id " +
            "and connexion.current_database = query.jhi_database and connexion.id = 10",nativeQuery = true)
    List<com.nov.domain.Query> getAllByConnexionId(Long id);
}
