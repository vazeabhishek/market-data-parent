package com.invicto.mdp.repository;

import com.invicto.mdp.entity.EquityEodAnalytics;
import com.invicto.mdp.entity.EquityEodData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquityEodAnalyticsRepository extends CrudRepository<EquityEodAnalytics,Long> {


}
