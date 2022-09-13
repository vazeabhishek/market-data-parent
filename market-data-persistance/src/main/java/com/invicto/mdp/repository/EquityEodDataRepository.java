package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Equity;
import com.invicto.mdp.entity.EquityEodData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EquityEodDataRepository extends CrudRepository<EquityEodData, Long> {


    Optional<EquityEodData> findTop1ByEquityOrderByCollectionDateDesc(Equity equity);

    Optional<EquityEodData> findFirstByEquityAndHighGreaterThanAndCollectionDateBeforeOrderByCollectionDateDesc(Equity equity, double high, LocalDate date);
    Optional<EquityEodData> findFirstByEquityAndLowLessThanAndCollectionDateBeforeOrderByCollectionDateDesc(Equity equity, double low, LocalDate date);
}
