package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday1HSnap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolIntraday1hSnapRepository extends CrudRepository<SymbolIntraday1HSnap, Long> {
    Optional<SymbolIntraday1HSnap> findBySymbolAndCollectionTime(Symbol symbol, LocalDateTime localDateTime);
    List<SymbolIntraday1HSnap> findBySymbol(Symbol symbol);
}
