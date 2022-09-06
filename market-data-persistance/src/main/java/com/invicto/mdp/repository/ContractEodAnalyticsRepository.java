package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodAnalytics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ContractEodAnalyticsRepository extends CrudRepository<ContractEodAnalytics,Long> {
    Optional<ContractEodAnalytics> findTop1ByContractOrderByAnalyticsDateDesc(Contract contract);
    List<ContractEodAnalytics> findTop50ByContractOrderByAnalyticsDateDesc(Contract contract);

}
