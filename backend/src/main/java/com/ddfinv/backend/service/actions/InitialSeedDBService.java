package com.ddfinv.backend.service.actions;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;

@Component
// only run in dev or test environments
@Profile({"dev", "test"})
public class InitialSeedDBService implements CommandLineRunner{

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;


    public InitialSeedDBService(PasswordEncoder passwordEncoder, UserAccountRepository userAccountRepository){
        this.passwordEncoder = passwordEncoder;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userAccountRepository.count()== 0){
            System.out.println("Beginning DB seeding initialization.");
            seedUsers();
        }
    }
    private void seedUsers(){
        
        if (!userAccountRepository.existsByEmail("admin@example.com")){
            UserAccount adminUserAccount = new UserAccount();
            adminUserAccount.setEmail("admin@example.com");
            adminUserAccount.setHashedPassword(passwordEncoder.encode("password"));
            adminUserAccount.setFirstName("Admin");
            adminUserAccount.setLastName("istrator");
            adminUserAccount.setRole(Role.admin);
            userAccountRepository.save(adminUserAccount);
            System.out.println("User: " + adminUserAccount.getEmail() + " was created.");
        }
        if (!userAccountRepository.existsByEmail("employee@example.com")){
            UserAccount employeeUserAccount = new UserAccount();
            employeeUserAccount .setEmail("employee@example.com");
            employeeUserAccount .setHashedPassword(passwordEncoder.encode("password"));
            employeeUserAccount .setFirstName("Emp");
            employeeUserAccount .setLastName("loyee");
            employeeUserAccount .setRole(Role.employee);
            userAccountRepository.save(employeeUserAccount);
            System.out.println("User: " + employeeUserAccount.getEmail() + " was created.");

        }
        if (!userAccountRepository.existsByEmail("client@example.com")){
            UserAccount clientUserAccount= new UserAccount();
            clientUserAccount.setEmail("client@example.com");
            clientUserAccount.setHashedPassword(passwordEncoder.encode("password"));
            clientUserAccount.setFirstName("Cli");
            clientUserAccount.setLastName("ent");
            clientUserAccount.setRole(Role.client);
            userAccountRepository.save(clientUserAccount);

            System.out.println("User: " + clientUserAccount.getEmail() + " was created.");

        }
        if (!userAccountRepository.existsByEmail("guest@example.com")){
            UserAccount guestUserAccountOne = new UserAccount();
            guestUserAccountOne .setEmail("guestone@example.com");
            guestUserAccountOne .setHashedPassword(passwordEncoder.encode("password"));
            guestUserAccountOne .setFirstName("Gues");
            guestUserAccountOne .setLastName("t One");
            guestUserAccountOne .setRole(Role.guest);
            userAccountRepository.save(guestUserAccountOne);
            System.out.println("User: " + guestUserAccountOne.getEmail() + " was created.");


        }
        if (!userAccountRepository.existsByEmail("guesttwo@example.com")){
            UserAccount guestUserAccountTwo = new UserAccount();
            guestUserAccountTwo .setEmail("guesttwo@example.com");
            guestUserAccountTwo .setHashedPassword(passwordEncoder.encode("password"));
            guestUserAccountTwo .setFirstName("Gues");
            guestUserAccountTwo .setLastName("t Two");
            guestUserAccountTwo .setRole(Role.guest);
            userAccountRepository.save(guestUserAccountTwo);
            System.out.println("User: " + guestUserAccountTwo.getEmail() + " was created.");


        }
    }



}
