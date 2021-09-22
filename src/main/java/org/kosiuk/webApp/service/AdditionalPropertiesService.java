package org.kosiuk.webApp.service;

import org.kosiuk.webApp.entity.AdditionalProperties;
import org.kosiuk.webApp.repository.AdditionalPropertiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdditionalPropertiesService {

    private final AdditionalPropertiesRepository addPropRepo;

    @Autowired
    public AdditionalPropertiesService(AdditionalPropertiesRepository addPropRepo) {
        this.addPropRepo = addPropRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer getNextCreditCardOrderIdVal () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            int curCreditCardOrderId = curAddProp.getCurCreditCardOrderId();
            addPropRepo.incCurCreditCardOrderId(curCreditCardOrderId);
            return curCreditCardOrderId;
        }
        return 0;
    }

    public Integer getNextCurVisaCardNum () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            return curAddProp.getCurVisaCardNum();
        }
        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer incCurVisaCardNum () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            int curVisaCardNum = curAddProp.getCurVisaCardNum();
            addPropRepo.incCurVisaCardNum(curVisaCardNum);
            return curAddProp.getCurVisaCardNum();
        }
        return 0;
    }

    public Integer getNextCurMasterCardNum () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            return curAddProp.getCurMasterCardNum();
        }
        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer incCurMasterCardNum () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            int curMasterCardNum = curAddProp.getCurMasterCardNum();
            addPropRepo.incCurMasterCardNum(curMasterCardNum);
            return curAddProp.getCurMasterCardNum();
        }
        return 0;
    }

    public Long getNextCurMoneyAccountNum () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            return curAddProp.getCurMoneyAccountNum();
        }
        return 0L;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long incCurMoneyAccountNum () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            long curMoneyAccountNum = curAddProp.getCurMoneyAccountNum();
            addPropRepo.incCurMoneyAccountNum(curMoneyAccountNum);
            return curAddProp.getCurMoneyAccountNum();
        }
        return 0L;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer getNextCreditCardIdVal () {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            int curCreditCardId = curAddProp.getCurCreditCardId();
            addPropRepo.incCurCreditCardId(curCreditCardId);
            return curCreditCardId;
        }
        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long getNextPaymentNum() {
        Iterable<AdditionalProperties> addProps = addPropRepo.findAll();
        if (addProps.iterator().hasNext()) {
            AdditionalProperties curAddProp = addProps.iterator().next();
            long curPaymentNum = curAddProp.getCurPaymentNum();
            addPropRepo.incCurPaymentNum(curPaymentNum);
            return curPaymentNum;
        }

        return 0L;
    }

}
