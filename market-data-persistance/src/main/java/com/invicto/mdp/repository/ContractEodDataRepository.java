package com.invicto.mdp.repository;

import com.invicto.mdp.entity.Contract;
import com.invicto.mdp.entity.ContractEodData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractEodDataRepository extends CrudRepository<ContractEodData,Long> {
    public ContractEodData findTop1ByContractOrderByCollectionDateDesc(Contract contract);
}
