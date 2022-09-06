package com.invicto.mdp.repository;

import com.invicto.mdp.entity.SymbolEodPrediction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolEodPredictionRepository  extends CrudRepository<SymbolEodPrediction,Long> {

}
