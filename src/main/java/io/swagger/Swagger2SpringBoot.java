package io.swagger;

import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.Role;
import io.swagger.model.User;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"io.swagger", "io.swagger.api", "io.swagger.configuration"})
public class Swagger2SpringBoot implements CommandLineRunner {

    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }

        CreateUpdateUserDTO bank = new CreateUpdateUserDTO();
        bank.setCreateCurrentAccount(true);
        bank.setCreateSavingsAccount(true);
        bank.setIsActive(true);
        bank.setEmail("Bank@bank.nl");
        bank.setFirstname("Bank");
        bank.setLastname("Bank");
        bank.setDayLimit(BigDecimal.valueOf(10000000));
        bank.setPhonenumber("06-121212121");
        bank.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
        bank.setTransactionLimit(BigDecimal.valueOf(10000000));
        bank.setUsername("Bank");
        bank.setPassword("yo");
        userService.add(bank);
    }

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    public static void main(String[] args) throws Exception {
        new SpringApplication(Swagger2SpringBoot.class).run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
