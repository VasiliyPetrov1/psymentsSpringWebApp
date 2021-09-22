package org.kosiuk.webApp.config;

import org.kosiuk.webApp.entity.AdditionalProperties;
import org.kosiuk.webApp.entity.Role;
import org.kosiuk.webApp.entity.User;
import org.kosiuk.webApp.repository.AdditionalPropertiesRepository;
import org.kosiuk.webApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class DataLoader implements ApplicationRunner {

    private final AdditionalPropertiesRepository addPropRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataLoader(AdditionalPropertiesRepository addPropRepository, UserRepository userRepository) {
        this.addPropRepository = addPropRepository;
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {
        Iterable<AdditionalProperties> addProps = addPropRepository.findAll();
        if (!addProps.iterator().hasNext()) { // if no addProp tuple in database
            addPropRepository.save(new AdditionalProperties(0, 0, 0, 0, 0, 0L, 0L));
            // we add it with default values
        }
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()) {
            User user = new User("Oleksii", "oleksii_kosiuk@outlook.com", "20032002");
            user.setActive(true);
            user.setHasBlockedAccount(false);
            user.setRoles(Collections.singleton(Role.ADMIN));
            userRepository.save(user);
        }
    }
}