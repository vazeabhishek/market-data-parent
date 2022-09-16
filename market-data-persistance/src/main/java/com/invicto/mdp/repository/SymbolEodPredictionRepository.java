package com.invicto.mdp.repository;

import com.invicto.mdp.entity.SymbolEodPrediction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SymbolEodPredictionRepository  extends CrudRepository<SymbolEodPrediction,Long> {

    List<SymbolEodPrediction> findAllByPredictionDateAndSignalIsNot(LocalDate predictionDate, String signal);

}
