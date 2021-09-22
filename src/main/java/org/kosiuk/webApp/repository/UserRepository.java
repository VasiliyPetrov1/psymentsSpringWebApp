package org.kosiuk.webApp.repository;

import org.kosiuk.webApp.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    @Transactional
    @Query("UPDATE User SET username = :username, email = :email, active = :active WHERE id = :id")
    @Modifying
    void updateUser(@Param("id") Integer id, @Param("username") String username, @Param("email") String email,
                    @Param("active") boolean active);

    @Transactional
    @Query("UPDATE User SET username = :username, email = :email, password = :password WHERE id = :id")
    @Modifying
    void updateUserLimited(@Param("id") Integer id, @Param("username") String username, @Param("email") String email,
                           @Param("password") String password);

    /*@Transactional
    @Query(value = "UPDATE role SET roles = :roles WHERE account_id = :account_id", nativeQuery = true)
    @Modifying
    void updateRoles(@Param("account_id") Integer account_id, @Param("roles") String roles);
    */

    @Transactional
    @Query(value = "INSERT INTO role (account_id, roles) VALUES(:account_id, :roles) ", nativeQuery = true)
    @Modifying
    void insertRole(@Param("account_id") Integer account_id, @Param("roles") String roles);

    @Transactional
    @Query(value = "DELETE FROM role WHERE account_id = :account_id", nativeQuery = true)
    @Modifying
    void deleteRoles(@Param("account_id") Integer account_id);

    @Transactional
    @Query(value = "UPDATE account set has_order_on_check = 0 WHERE id = :id", nativeQuery = true)
    @Modifying
    void dropOrderOnCheckFlag (@Param("id") Integer id);

}
