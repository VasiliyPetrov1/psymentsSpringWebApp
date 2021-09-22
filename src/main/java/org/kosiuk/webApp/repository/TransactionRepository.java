package org.kosiuk.webApp.repository;

import org.kosiuk.webApp.entity.Transaction;
import org.kosiuk.webApp.entity.TransactionId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, TransactionId> {

}
