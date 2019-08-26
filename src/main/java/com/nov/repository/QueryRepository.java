package com.nov.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


/**
 * Spring Data  repository for the Query entity.
 */
@SuppressWarnings("unused")
@Repository
@Transactional
public interface QueryRepository extends JpaRepository<com.nov.domain.Query, Long> {

    @Modifying
    @Query(value = "delete from Query where id in ?1")
    void deleteListQueries(List<Long> ids);
}
