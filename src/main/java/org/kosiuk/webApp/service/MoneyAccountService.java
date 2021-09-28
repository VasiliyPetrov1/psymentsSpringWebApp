package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.MoneyAccountConfirmationDto;
import org.kosiuk.webApp.dto.MoneyAccountDto;
import org.kosiuk.webApp.dto.MoneyAccountWithUserDto;
import org.kosiuk.webApp.entity.CreditCard;
import org.kosiuk.webApp.entity.MoneyAccount;
import org.kosiuk.webApp.entity.MoneyAccountActStatus;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.exceptions.UnsafeMoneyAccCreationException;
import org.kosiuk.webApp.repository.MoneyAccountRepository;
import org.kosiuk.webApp.util.concurrent.MoneyAccountMonitor;
import org.kosiuk.webApp.util.sumConversion.MoneyIntDecToStringAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoneyAccountService {

    private final MoneyAccountRepository moneyAccountRepository;
    private final AdditionalPropertiesService addPropService;
    private final UserService userService;
    @Value("${application.thisSystemAccountsCode}")
    public long ACCOUNT_CODE;
    @Value("${application.moneyAccountPageSize}")
    public int pageSize;
    public boolean isAnyMoneyAccOnCreation = false;
    public final static String NO_OWNER = "No owner registered in this payment system";
    private final Map<Integer, MoneyAccountMonitor> moneyAccountMonitorsMap;

    @Autowired
    public MoneyAccountService(MoneyAccountRepository moneyAccountRepository,
                               AdditionalPropertiesService addPropService, UserService userService) {
        this.moneyAccountRepository = moneyAccountRepository;
        this.addPropService = addPropService;
        this.userService = userService;
        moneyAccountMonitorsMap = new HashMap<>();
        List<Integer> moneyAccountIds = ((List<MoneyAccount>)moneyAccountRepository.findAll()).stream().
                map(moneyAccount -> moneyAccount.getId()).collect(Collectors.toList());
        for (Integer curMoneyAccountId : moneyAccountIds) {
            moneyAccountMonitorsMap.put(curMoneyAccountId, new MoneyAccountMonitor(curMoneyAccountId));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MoneyAccount createMoneyAccount(Long number, String name) {
        MoneyAccount moneyAccount = MoneyAccount.builder()
                .number(number)
                .name(name)
                .sumInt(0L)
                .sumDec(0)
                .sumAvailableInt(0L)
                .sumAvailableDec(0)
                .active(MoneyAccountActStatus.ACTIVE)
                .build();

        //MoneyAccount moneyAccount = new MoneyAccount(number, name, 0L, 0, 0L, 0, MoneyAccountActStatus.ACTIVE);
        moneyAccountRepository.save(moneyAccount);
        addPropService.incCurMoneyAccountNum();
        isAnyMoneyAccOnCreation = false;
        moneyAccount = moneyAccountRepository.findByNumber(number);
        moneyAccountMonitorsMap.put(moneyAccount.getId(), new MoneyAccountMonitor(moneyAccount.getId()));
        return moneyAccount;
    }

    public MoneyAccountConfirmationDto getNewMoneyAccountConfDto() throws UnsafeMoneyAccCreationException {
        if (isAnyMoneyAccOnCreation) {
            throw new UnsafeMoneyAccCreationException();
        }
        long number = addPropService.getNextCurMoneyAccountNum() + ACCOUNT_CODE;
        isAnyMoneyAccOnCreation = true;
        return new MoneyAccountConfirmationDto(number, "Money Account");
    }

    public void cancelMoneyAccountCreation() {
        isAnyMoneyAccOnCreation = false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteMoneyAccount(Integer id) {
        MoneyAccount deletedMoneyAccount = getMoneyAccountById(id);
        if (deletedMoneyAccount.getCreditCard() != null &&
                !(deletedMoneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE))) {
            // if account we are going to delete has owner and if it's blocked
            User owner = deletedMoneyAccount.getCreditCard().getUser();
            owner.setHasBlockedAccount(false); // we drop flag to show that after deletion of that account
            // the user won't have any blocked accounts
            userService.save(owner);
        }

        moneyAccountRepository.deleteById(id);
    }

    public MoneyAccount save(MoneyAccount moneyAccount) {
        moneyAccountMonitorsMap.put(moneyAccount.getId(), new MoneyAccountMonitor(moneyAccount.getId()));
        moneyAccountRepository.save(moneyAccount);
        return moneyAccountRepository.findById(moneyAccount.getId()).get();
    }

    public MoneyAccount getMoneyAccountById(Integer id) {
        return moneyAccountRepository.findById(id).get();
    }

    public List<MoneyAccount> getAllMoneyAccounts() {
        return (List<MoneyAccount>) moneyAccountRepository.findAll();
    }

    public Page<MoneyAccount> getAllMoneyAccountsPage(int pageNumber, String sortParameter) {
        Pageable pageable;
        if (sortParameter.equals("none")) {
            pageable = PageRequest.of(pageNumber - 1, pageSize);
        } else if (sortParameter.equals("sum")) {
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "sumInt", "sumDec");
        } else {
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, sortParameter);
        }
        return moneyAccountRepository.findAll(pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MoneyAccountDto getMoneyAccountDtoById(Integer id) {
        MoneyAccount moneyAccount = getMoneyAccountById(id);
        return convertMoneyAccountToDto(moneyAccount, moneyAccount.getCreditCard().getUser().isHasBlockedAccount());
    }

    public MoneyAccount getMoneyAccountByNumber(Long number) {
        MoneyAccount moneyAccount = moneyAccountRepository.findByNumber(number);
        return moneyAccount;
    }

    public List<MoneyAccountDto> getAllUsersMoneyAccountDtos(User user) {
        List<CreditCard> creditCards = user.getCreditCards();
        return creditCards.stream().map(creditCard ->
                convertMoneyAccountToDto(creditCard.getMoneyAccount(), creditCard.getUser().isHasBlockedAccount())).
                collect(Collectors.toList());
    }

    public List<MoneyAccountDto> getAllSortedUsersMoneyAccountDtos(User user, String sortParameter) {
        List<MoneyAccountDto> moneyAccountDtos = getAllUsersMoneyAccountDtos(user);
        if (sortParameter.equals("number")) {
            return sortMoneyAccountDtosByNumber(moneyAccountDtos);
        } else if (sortParameter.equals("name")) {
            return sortMoneyAccountDtosByName(moneyAccountDtos);
        } else if (sortParameter.equals("sum")) {
            return sortMoneyAccountDtosByRemainedSum(moneyAccountDtos);
        }
        return moneyAccountDtos;
    }

    public List<MoneyAccountWithUserDto> getAllMoneyAccountDtos() {
        List<MoneyAccount> moneyAccounts = getAllMoneyAccounts();
        List<MoneyAccountWithUserDto> moneyAccountWithUserDtos = new ArrayList<>();
        for (MoneyAccount curMoneyAccount : moneyAccounts) {
            if (curMoneyAccount.getCreditCard() != null) {
                User owner = curMoneyAccount.getCreditCard().getUser();
                moneyAccountWithUserDtos.add(convertMoneyAccountToDtoWithUser(
                        convertMoneyAccountToDto(curMoneyAccount, !(owner.isHasBlockedAccount())),
                        owner.getUsername()));
            } else {
                moneyAccountWithUserDtos.add(convertMoneyAccountToDtoWithUser(
                        convertMoneyAccountToDto(curMoneyAccount, false),
                        NO_OWNER));
            }

        }
        return moneyAccountWithUserDtos;
    }

    public List<MoneyAccountDto> sortMoneyAccountDtosByNumber(List<MoneyAccountDto> moneyAccountDtos) {

        moneyAccountDtos.sort((moneyAccountOne, moneyAccountTwo) -> {
            if(moneyAccountOne.getNumber() > moneyAccountTwo.getNumber()) {
                return 1;
            } else if (moneyAccountOne.getNumber() < moneyAccountOne.getNumber()) {
                return -1;
            } else {
                return 0;
            }
        });

        return moneyAccountDtos;

    }

    public List<MoneyAccountDto> sortMoneyAccountDtosByName(List<MoneyAccountDto> moneyAccountDtos) {

        moneyAccountDtos.sort(Comparator.comparing(MoneyAccountDto::getName));

        return moneyAccountDtos;
    }

    public List<MoneyAccountDto> sortMoneyAccountDtosByRemainedSum(List<MoneyAccountDto> moneyAccountDtos) {

        moneyAccountDtos.sort(Comparator.comparing(MoneyAccountDto::getSumString));

        return moneyAccountDtos;
    }

    public MoneyAccountDto convertMoneyAccountToDto(MoneyAccount moneyAccount, boolean hasBlockedAccount) {

        MoneyAccountDto moneyAccountDto = new MoneyAccountDto(moneyAccount.getId(), moneyAccount.getNumber(),
                moneyAccount.getName(), new MoneyIntDecToStringAdapter(moneyAccount).getOperatedSumString(),
                moneyAccount.getActive().equals(MoneyAccountActStatus.ACTIVE),
                moneyAccount.getActive().equals(MoneyAccountActStatus.BLOCKED),
                moneyAccount.getActive().equals(MoneyAccountActStatus.UNLOCK_REQUESTED), !hasBlockedAccount);

        return moneyAccountDto;
    }

    public MoneyAccountWithUserDto convertMoneyAccountToDtoWithUser(MoneyAccountDto moneyAccountDto, String userName) {

        MoneyAccountWithUserDto moneyAccountWithUserDto = new MoneyAccountWithUserDto(moneyAccountDto, userName);

        return moneyAccountWithUserDto;
    }

    public List<MoneyAccountWithUserDto> convertMoneyAccountsToMoneyAccountDtosWithUser
            (List<MoneyAccount> moneyAccounts) {
        List<MoneyAccountWithUserDto> moneyAccountWithUserDtos = new ArrayList<>();
        for (MoneyAccount curMoneyAccount : moneyAccounts) {
            if (curMoneyAccount.getCreditCard() != null) {
                User owner = curMoneyAccount.getCreditCard().getUser();
                moneyAccountWithUserDtos.add(convertMoneyAccountToDtoWithUser(
                        convertMoneyAccountToDto(curMoneyAccount, !(owner.isHasBlockedAccount())),
                        owner.getUsername()));
            } else {
                moneyAccountWithUserDtos.add(convertMoneyAccountToDtoWithUser(
                        convertMoneyAccountToDto(curMoneyAccount, false),
                        NO_OWNER));
            }
        }
        return moneyAccountWithUserDtos;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MoneyAccount blockMoneyAccount(Integer id) {

        MoneyAccount moneyAccount = getMoneyAccountById(id);
        moneyAccount.setActive(MoneyAccountActStatus.BLOCKED);
        User user = moneyAccount.getCreditCard().getUser();
        user.setHasBlockedAccount(true);
        moneyAccountRepository.save(moneyAccount);

        return getMoneyAccountById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MoneyAccount askToUnlockMoneyAccount(Integer id) {

        MoneyAccount moneyAccount = getMoneyAccountById(id);
        moneyAccount.setActive(MoneyAccountActStatus.UNLOCK_REQUESTED);
        moneyAccountRepository.save(moneyAccount);
        return getMoneyAccountById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public MoneyAccount unlockMoneyAccount(Integer id) {

        MoneyAccount moneyAccount = getMoneyAccountById(id);
        moneyAccount.setActive(MoneyAccountActStatus.ACTIVE);
        User user = moneyAccount.getCreditCard().getUser();
        user.setHasBlockedAccount(false);
        moneyAccountRepository.save(moneyAccount);

        return getMoneyAccountById(id);
    }

    public void cancelCreation() {
        isAnyMoneyAccOnCreation = false;
    }

    public Map<Integer, MoneyAccountMonitor> getMoneyAccountMonitorsMap() {
        return moneyAccountMonitorsMap;
    }
}
