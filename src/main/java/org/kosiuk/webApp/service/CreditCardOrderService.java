package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.CreditCardOrderDto;
import org.kosiuk.webApp.dto.CreditCardOrderWithUserDto;
import org.kosiuk.webApp.entity.*;
import org.kosiuk.webApp.exceptions.UnsafeOrderConfirmationException;
import org.kosiuk.webApp.repository.CreditCardOrderRepository;
import org.kosiuk.webApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardOrderService {

    private final CreditCardOrderRepository creditCardOrderRepository;
    private final UserRepository userRepository;
    private final AdditionalPropertiesService addPropService;
    private final CreditCardService creditCardService;
    private final MoneyAccountService moneyAccountService;
    private boolean isAnyOrderOnConfirmation;
    @Value("${application.creditCardOrderPageSize}")
    private int pageSize;

    @Autowired
    public CreditCardOrderService(CreditCardOrderRepository creditCardOrderRepository, UserRepository userRepository,
                                  AdditionalPropertiesService addPropService, CreditCardService creditCardService,
                                  MoneyAccountService moneyAccountService) {
        this.creditCardOrderRepository = creditCardOrderRepository;
        this.userRepository = userRepository;
        this.addPropService = addPropService;
        this.creditCardService = creditCardService;
        this.moneyAccountService = moneyAccountService;
        isAnyOrderOnConfirmation = false;
    }


    public CreditCardOrder getCreditCardOrderById(CreditCardOrderId id) {
        return creditCardOrderRepository.findById(id).get();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreditCardOrder createCreditCardOrder(CreditCardOrderDto creditCardOrderDto, Integer userId) {

        PaymentSystem paymentSystem =
                creditCardOrderDto.isVisa() ? PaymentSystem.VISA :
                        (creditCardOrderDto.isMasterCard() ? PaymentSystem.MASTERCARD : null);

        // Creating composite primary key for order
        CreditCardOrderId orderId = new CreditCardOrderId();
        orderId.setAccountId(userId);
        orderId.setCreditCardOrderId(addPropService.getNextCreditCardOrderIdVal());

        CreditCardOrder order = new CreditCardOrder(orderId,
                OrderStatus.ON_CHECK, creditCardOrderDto.getMessage(),
                paymentSystem);

        User user = userRepository.findById(userId).get();

        user.setHasOrderOnCheck(true);
        order.setUser(user);

        creditCardOrderRepository.save(order);
        return order;
    }

    public List<CreditCardOrderDto> getAllUsersOrderDtos(User user) {
        List<CreditCardOrder> creditCardOrders = user.getCreditCardOrders();
        return creditCardOrders.stream().map(order -> convertCreditCardOrderToDto(order)).collect(Collectors.toList());
    }

    @Deprecated
    public List<CreditCardOrderDto> getAllOrderDtos() {
        List<CreditCardOrder> creditCardOrders = (List<CreditCardOrder>) creditCardOrderRepository.findAll();
        return creditCardOrders.stream().map(order -> convertCreditCardOrderToDto(order)).collect(Collectors.toList());
    }

    public Page<CreditCardOrder> getAllOrdersPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return creditCardOrderRepository.findAll(pageable);
    }

    public List<CreditCardOrder> getAllOrders() {
        return (List<CreditCardOrder>) creditCardOrderRepository.findAll();
    }

    public List<CreditCardOrderWithUserDto> getAllOrderDtosWithUser() {
        List<CreditCardOrder> creditCardOrders = (List<CreditCardOrder>) creditCardOrderRepository.findAll();
        return convertCreditCardOrdersToWithUserDtos(creditCardOrders);
    }

    public CreditCardOrderWithUserDto convertCreditCardOrderToWithUserDto(CreditCardOrder order) {
        return new CreditCardOrderWithUserDto(
                order.getCreditCardOrderId().getCreditCardOrderId(), convertCreditCardOrderToDto(order), order.getUser());
    }

    public List<CreditCardOrderWithUserDto> convertCreditCardOrdersToWithUserDtos(List<CreditCardOrder> orders) {
        return orders.stream().map(order -> convertCreditCardOrderToWithUserDto(order)).collect(Collectors.toList());
    }

    public CreditCardOrderDto convertCreditCardOrderToDto(CreditCardOrder creditCardOrder) {

        PaymentSystem paymentSystem = creditCardOrder.getDesPaymentSystem();

        return new CreditCardOrderDto(creditCardOrder.getOrderStatus(), creditCardOrder.getMessage(),
                paymentSystem.equals(PaymentSystem.VISA), paymentSystem.equals(PaymentSystem.MASTERCARD));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreditCardOrder rejectCreditCardOrder(Integer orderId, Integer ownerId, String rejectMessage) {

        CreditCardOrder creditCardOrder = creditCardOrderRepository.findByOrderId(orderId);
        creditCardOrder.setOrderStatus(OrderStatus.REJECTED);
        String message = creditCardOrder.getMessage() + "\nYOUR ORDER WAS REJECTED." + "\nREASON: " + rejectMessage;
        creditCardOrder.setMessage(message);
        creditCardOrderRepository.save(creditCardOrder);

        userRepository.dropOrderOnCheckFlag(ownerId);

        return creditCardOrderRepository.findByOrderId(orderId);
    }

    public void prepareCreditCardOrderConfirmation() throws UnsafeOrderConfirmationException {
        if (isAnyOrderOnConfirmation) {
            throw new UnsafeOrderConfirmationException();
        }
        isAnyOrderOnConfirmation = true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreditCardOrder confirmCreditCardOrder(Integer orderId, Integer ownerId, Long creditCardNum,
                                                  Integer cvv, String expireDateString, Long moneyAccountNum,
                                                  String moneyAccountName, boolean isVisa, boolean isMasterCard) {

        // create money account
        MoneyAccount moneyAccount = moneyAccountService.createMoneyAccount(moneyAccountNum, moneyAccountName);
        // create credit card and bind money account to it
        CreditCard creditCard = creditCardService.createCreditCard(ownerId, moneyAccount.getId(), creditCardNum,
                cvv, expireDateString, isVisa, isMasterCard);

        CreditCardOrder creditCardOrder = getCreditCardOrderById(new CreditCardOrderId(ownerId, orderId));

        // bind credit card to credit card order and confirm order
        creditCardOrder.setCreditCard(creditCard);
        creditCardOrder.setOrderStatus(OrderStatus.CONFIRMED);
        creditCardOrderRepository.save(creditCardOrder);

        // drop on_check flag to provide availability to create one more order after one was confirmed
        userRepository.dropOrderOnCheckFlag(ownerId);
        isAnyOrderOnConfirmation = false;

        return null;
    }

    public void cancelOrderConfirmation () {
        isAnyOrderOnConfirmation = false;
        moneyAccountService.cancelMoneyAccountCreation();
    }

}
