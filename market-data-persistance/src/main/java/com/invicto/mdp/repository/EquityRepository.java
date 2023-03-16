package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Equity;
import com.invicto.mdp.entity.Symbol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquityRepository extends CrudRepository<Equity,Long> {
    Optional<Equity> findBySymbol(Symbol symbol);
}
