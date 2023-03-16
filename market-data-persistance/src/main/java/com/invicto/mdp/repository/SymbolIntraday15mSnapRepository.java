package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Symbol;
import com.invicto.mdp.entity.SymbolIntraday15mSnap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolIntraday15mSnapRepository extends CrudRepository<SymbolIntraday15mSnap, Long> {
    Optional<SymbolIntraday15mSnap> findBySymbolAndCollectionTime(Symbol symbol, LocalTime localDateTime);
    List<SymbolIntraday15mSnap> findBySymbolAndCollectionDateOrderByCollectionDateAscCollectionTimeAsc(Symbol symbol, LocalDate localDateTime);
    List<SymbolIntraday15mSnap> findBySymbol(Symbol symbol);
}
