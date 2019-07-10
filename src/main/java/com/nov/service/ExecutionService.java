package com.nov.service;

import com.nov.domain.Execution;
import com.nov.repository.ExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExecutionService {

    @Autowired
    private ExecutionRepository executionRepository;
    private final Logger log = LoggerFactory.getLogger(ExecutionService.class);
    public List<Execution> getExecutionsByQueryId(Long id) {

        return executionRepository.getExecutionByQueryId(id);
    }

}
