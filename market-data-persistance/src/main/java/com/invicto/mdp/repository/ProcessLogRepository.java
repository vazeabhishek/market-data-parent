package com.invicto.mdp.repository;

import com.invicto.mdp.entity.ProcessLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessLogRepository extends CrudRepository<ProcessLog, Long> {
    Optional<ProcessLog> findByFileName(String filename);

}
