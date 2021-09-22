package org.kosiuk.webApp.entity;


import javax.persistence.*;

@Table(name = "additional_properties")
@Entity
public class AdditionalProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "cur_credit_card_order_id")
    private Integer curCreditCardOrderId;

    @Column(name = "cur_credit_card_id")
    private Integer curCreditCardId;

    @Column(name = "cur_visa_card_num")
    private Integer curVisaCardNum;

    @Column(name = "cur_master_card_num")
    private Integer curMasterCardNum;

    @Column(name = "cur_money_account_num")
    private Long curMoneyAccountNum;

    @Column(name = "cur_payment_num")
    private Long curPaymentNum;

    public AdditionalProperties(Integer id, Integer curCreditCardOrderId, Integer curCreditCardId,
                                Integer curVisaCardNum, Integer curMasterCardNum, Long curMoneyAccountNum,
                                Long curPaymentNum) {
        this.id = id;
        this.curCreditCardOrderId = curCreditCardOrderId;
        this.curCreditCardId = curCreditCardId;
        this.curVisaCardNum = curVisaCardNum;
        this.curMasterCardNum = curMasterCardNum;
        this.curMoneyAccountNum = curMoneyAccountNum;
        this.curPaymentNum = curPaymentNum;
    }

    public AdditionalProperties(Integer id, Integer curCreditCardOrderId) {
        this.id = id;
        this.curCreditCardOrderId = curCreditCardOrderId;
    }

    public AdditionalProperties(Integer curCreditCardOrderId) {
        this.curCreditCardOrderId = curCreditCardOrderId;
    }

    public AdditionalProperties() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurCreditCardOrderId() {
        return curCreditCardOrderId;
    }

    public void setCurCreditCardOrderId(Integer curCreditCardOrderId) {
        this.curCreditCardOrderId = curCreditCardOrderId;
    }

    public Integer getCurCreditCardId() {
        return curCreditCardId;
    }

    public void setCurCreditCardId(Integer curCreditCardId) {
        this.curCreditCardId = curCreditCardId;
    }

    public Integer getCurVisaCardNum() {
        return curVisaCardNum;
    }

    public void setCurVisaCardNum(Integer curVisaCardNum) {
        this.curVisaCardNum = curVisaCardNum;
    }

    public Integer getCurMasterCardNum() {
        return curMasterCardNum;
    }

    public void setCurMasterCardNum(Integer curMasterCardNum) {
        this.curMasterCardNum = curMasterCardNum;
    }

    public Long getCurMoneyAccountNum() {
        return curMoneyAccountNum;
    }

    public void setCurMoneyAccountNum(Long curMoneyAccountNum) {
        this.curMoneyAccountNum = curMoneyAccountNum;
    }

    public Long getCurPaymentNum() {
        return curPaymentNum;
    }

    public void setCurPaymentNum(Long curPaymentNum) {
        this.curPaymentNum = curPaymentNum;
    }
}
