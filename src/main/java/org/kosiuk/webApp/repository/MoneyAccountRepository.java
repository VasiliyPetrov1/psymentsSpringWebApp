package org.kosiuk.webApp.repository;


import org.kosiuk.webApp.entity.CreditCard;
import org.kosiuk.webApp.entity.MoneyAccount;
import org.kosiuk.webApp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MoneyAccountRepository extends CrudRepository<MoneyAccount, Integer> {

    MoneyAccount findByNumber(Long number);

    @Transactional
    @Query(value = "UPDATE money_account SET active = 'BLOCKED' WHERE id = :id", nativeQuery = true)
    @Modifying
    void blockMoneyAccount(@Param("id") Integer orderId);

    Page<MoneyAccount> findAll(Pageable pageable);

}
