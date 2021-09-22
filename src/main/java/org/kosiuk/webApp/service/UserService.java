package org.kosiuk.webApp.service;

import org.kosiuk.webApp.dto.UserCreationDto;
import org.kosiuk.webApp.dto.UserEditionDto;
import org.kosiuk.webApp.dto.UserLimitedEditionDto;
import org.kosiuk.webApp.dto.UserRegistrationDto;
import org.kosiuk.webApp.entity.CreditCardOrder;
import org.kosiuk.webApp.entity.Role;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.exceptions.NotCompatibleRolesException;
import org.kosiuk.webApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User createUser(UserCreationDto userCreationDto) throws NotCompatibleRolesException {
        Set<Role> roleSet = userCreationDto.getCheckedRoles();

        User user = new User(userCreationDto.getUsername(), userCreationDto.getEmail(), userCreationDto.getPassword(),
                true, roleSet);


        try {
            save(user);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }

        return user;
    }

    public User registerUser(UserRegistrationDto userRegDto) {
        User user = new User(userRegDto.getUsername(), userRegDto.getEmail(), userRegDto.getPassword());
        user.setActive(true);
        user.setHasBlockedAccount(false);
        user.setRoles(Collections.singleton(Role.USER));
        save(user);
        return user;
    }


    public UserEditionDto convertUserToDTO(User user) {
        return new UserEditionDto(user.getId(), user.getUsername(), user.getEmail(), user.isActive(),
                user.getRoles().contains(Role.USER), user.getRoles().contains(Role.ADMIN));
    }

    public UserLimitedEditionDto convertUserToLimDto(User user) {
        return new UserLimitedEditionDto(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
                user.isHasOrderOnCheck());
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).get(); // findById returns Optional container here so we need to use get method to retrieve the value
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User updateUser(UserEditionDto userEditionDto) throws NotCompatibleRolesException{

        Set<Role> roleSet = userEditionDto.getCheckedRoles();

        userRepository.deleteRoles(userEditionDto.getId());

        roleSet.forEach(role -> {userRepository.insertRole(userEditionDto.getId(), role.name());});

        userRepository.updateUser(userEditionDto.getId(), userEditionDto.getUsername(), userEditionDto.getEmail(),
                userEditionDto.isActive());

        return new User(userEditionDto.getId(), userEditionDto.getUsername(), userEditionDto.getEmail(),
                userEditionDto.isActive(), roleSet);
    }

    public User updateUserLimited(UserLimitedEditionDto userLimEditionDto){

        userRepository.updateUserLimited(userLimEditionDto.getId(), userLimEditionDto.getUsername(),
                userLimEditionDto.getEmail(), userLimEditionDto.getPassword());

        return new User(userLimEditionDto.getId(), userLimEditionDto.getUsername(),
                userLimEditionDto.getEmail(), userLimEditionDto.getPassword());
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }


}
