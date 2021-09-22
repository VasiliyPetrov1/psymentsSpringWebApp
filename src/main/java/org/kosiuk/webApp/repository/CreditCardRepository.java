package org.kosiuk.webApp.repository;

import org.kosiuk.webApp.entity.CreditCard;
import org.kosiuk.webApp.entity.CreditCardId;
import org.kosiuk.webApp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CreditCardRepository extends CrudRepository<CreditCard, CreditCardId> {

    @Transactional
    @Query(value = "SELECT * FROM credit_card WHERE id = :id", nativeQuery = true)
    CreditCard findByCardId(@Param("id") Integer id);

    CreditCard findByNumber(Long number);

    Page<CreditCard> findAllByUser(User user, Pageable pageable);

}
