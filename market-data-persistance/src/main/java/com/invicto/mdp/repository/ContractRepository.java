package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.Symbol;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends CrudRepository<Contract,Long> {
    List<Contract> findAllBySymbol(Symbol symbol);
    Optional<Contract> findBySymbolAndExpiryDate(Symbol symbol, LocalDate expiry);
}
