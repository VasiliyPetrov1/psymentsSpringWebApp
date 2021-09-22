package org.kosiuk.webApp.repository;

import org.kosiuk.webApp.entity.Payment;
import org.kosiuk.webApp.entity.PaymentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, PaymentId> {

    @Transactional
    @Query(value = "SELECT * FROM payment WHERE number_ = :number ", nativeQuery = true)
    Payment findByNumber(@Param("number") Long number);

    Page<Payment> findAll(Pageable pageable);

}
