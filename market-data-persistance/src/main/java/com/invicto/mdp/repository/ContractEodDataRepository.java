package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContractEodDataRepository extends CrudRepository<ContractEodData,Long> {
    public ContractEodData findTop1ByContractOrderByCollectionDateDesc(Contract contract);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM ContractEodData e WHERE e.collectionDate = (SELECT MAX(e2.collectionDate) FROM ContractEodData e2)")
    public List<ContractEodData> findPrevDayData();
}
