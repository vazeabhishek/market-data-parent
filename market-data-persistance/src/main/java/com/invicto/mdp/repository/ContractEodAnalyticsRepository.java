package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodAnalytics;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ContractEodAnalyticsRepository extends CrudRepository<ContractEodAnalytics,Long> {
    Optional<ContractEodAnalytics> findTop1ByContractOrderByAnalyticsDateDesc(Contract contract);
}
