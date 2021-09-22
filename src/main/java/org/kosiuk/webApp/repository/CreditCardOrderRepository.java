package org.kosiuk.webApp.repository;

import org.kosiuk.webApp.entity.CreditCardOrder;
import org.kosiuk.webApp.entity.CreditCardOrderId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CreditCardOrderRepository extends CrudRepository<CreditCardOrder, CreditCardOrderId> {

    @Transactional
    @Query(value = "UPDATE credit_card_order SET order_status = 'REJECTED' WHERE id = :id", nativeQuery = true)
    @Modifying
    void rejectCreditCardOrder(@Param("id") Integer orderId);

    @Transactional
    @Query(value = "SELECT * FROM credit_card_order WHERE id = :id", nativeQuery = true)
    CreditCardOrder findByOrderId (@Param("id") Integer orderId);

    @Transactional
    @Query(value = "UPDATE credit_card_order SET order_status = 'CONFIRMED' WHERE id = :id", nativeQuery = true)
    @Modifying
    void confirmCreditCardOrder(@Param("id") Integer id);

    Page<CreditCardOrder> findAll(Pageable pageable);
}
