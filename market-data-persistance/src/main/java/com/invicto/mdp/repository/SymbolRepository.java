package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Symbol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SymbolRepository  extends CrudRepository<Symbol, Long> {
    Optional<Symbol> findByTicker(String ticker);

}
