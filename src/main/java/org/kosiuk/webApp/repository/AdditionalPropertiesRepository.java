package org.kosiuk.webApp.repository;

import org.kosiuk.webApp.entity.AdditionalProperties;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdditionalPropertiesRepository extends CrudRepository<AdditionalProperties, Integer> {

    @Transactional
    @Query(value = "UPDATE additional_properties SET cur_credit_card_order_id = (:cur_credit_card_order_id + 1)" +
            " WHERE cur_credit_card_order_id = :cur_credit_card_order_id", nativeQuery = true)
    @Modifying
    void incCurCreditCardOrderId(@Param("cur_credit_card_order_id") Integer curCreditCardOrderId);

    @Transactional
    @Query(value = "UPDATE additional_properties SET cur_credit_card_id = (:cur_credit_card_id + 1)" +
            " WHERE cur_credit_card_id = :cur_credit_card_id", nativeQuery = true)
    @Modifying
    void incCurCreditCardId (@Param("cur_credit_card_id") Integer curCreditCardId);

    @Transactional
    @Query(value = "UPDATE additional_properties SET cur_visa_card_num = (:cur_visa_card_num + 1)" +
            " WHERE cur_visa_card_num = :cur_visa_card_num", nativeQuery = true)
    @Modifying
    void incCurVisaCardNum(@Param("cur_visa_card_num") Integer curVisaCardNum);

    @Transactional
    @Query(value = "UPDATE additional_properties SET cur_master_card_num = (:cur_master_card_num + 1)" +
            " WHERE cur_master_card_num = :cur_master_card_num", nativeQuery = true)
    @Modifying
    void incCurMasterCardNum(@Param("cur_master_card_num") Integer curMasterCardNum);

    @Transactional
    @Query(value = "UPDATE additional_properties SET cur_money_account_num = (:cur_money_account_num + 1)" +
            " WHERE cur_money_account_num = :cur_money_account_num", nativeQuery = true)
    @Modifying
    void incCurMoneyAccountNum(@Param("cur_money_account_num") Long curMoneyAccountNum);

    @Transactional
    @Query(value = "UPDATE additional_properties SET cur_payment_num = (:cur_payment_num + 1)" +
            " WHERE cur_payment_num = :cur_payment_num", nativeQuery = true)
    @Modifying
    void incCurPaymentNum(@Param("cur_payment_num") Long curPaymentNum);

}
