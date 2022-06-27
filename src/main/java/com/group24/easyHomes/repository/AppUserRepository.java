package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);


    @Query("SELECT t FROM AppUser t WHERE t.email = ?1")
    AppUser findByUserEmail(String email);

    @Query("SELECT t FROM AppUser t WHERE t.id = ?1")
    AppUser findByUserId(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Transactional
    @Modifying
    @Query(" update AppUser user set user.password=?2 where user.email=?1")
    int setNewPassword(String email, String password);

}
