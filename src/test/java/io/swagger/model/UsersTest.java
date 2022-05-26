package io.swagger.model;

import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.UserService;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.BigDecimalConversion;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersTest{

    @Autowired
    private UserService userService;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    @Test
    public void whenLoggingInCorrectCredentialsShouldGiveToken() {
        String username = "Admin";
        String password = "Admin";

        String token = userService.login(username, password);

        assertThat(jwtTokenProvider.validateToken(token)).isEqualTo(true);
    }
    @Test
    public void whenLoggingInIncorrectCredentialsShouldThrowException() {
        String incorrectUsername = "WrongAdmin";
        String incorrectPassword = "WrongAdminPassword";

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.login(incorrectUsername, incorrectPassword);
        });

        assertTrue(exception.getMessage().contains("Username/password invalid"));
    }

    @Test
    public void creatingANewUserSuccessfullyShouldGiveObject() {
        // Create a new test user
        CreateUpdateUserDTO createdUser = new CreateUpdateUserDTO();
        createdUser.setUsername("new-test-user");
        createdUser.setPassword("password");
        createdUser.setFirstname("John");
        createdUser.setLastname("Doe");
        createdUser.setEmail("johndoe@example.com");
        createdUser.setDayLimit(new BigDecimal(1236.75));
        createdUser.setPhonenumber("0612345678");
        createdUser.setTransactionLimit(new BigDecimal(150.00));
        createdUser.setCreateCurrentAccount(true);
        createdUser.setCreateSavingsAccount(true);
        createdUser.setIsActive(true);

        // Add the user
        User response = userService.add(createdUser);

        // Assert that the user has been created
        assertEquals(response.getUsername(), createdUser.getUsername());
        assertThat(response.getIsActive()).isEqualTo(true);
    }

    @Test
    public void creatingANewEmployeeSuccessfullyShouldGiveUser() {
        // Create a new test employee
        CreateUpdateUserDTO createdEmployee = new CreateUpdateUserDTO();
        createdEmployee.setUsername("joham");
        createdEmployee.setPassword("passwordVanJohan");
        createdEmployee.setFirstname("Johan");
        createdEmployee.setLastname("Doedeledokes");
        createdEmployee.setEmail("JohanDoedeledokes@example.com");
        createdEmployee.setDayLimit(new BigDecimal(1236.75));
        createdEmployee.setPhonenumber("0612345678");
        createdEmployee.setTransactionLimit(new BigDecimal(150.00));
        createdEmployee.setRoles(Collections.singletonList(Role.ROLE_USER));
        createdEmployee.setCreateCurrentAccount(true);
        createdEmployee.setCreateSavingsAccount(false);
        createdEmployee.setIsActive(true);

        // Add the employee
        User response = userService.add(createdEmployee);

        // Assert that the user has been created
        assertEquals(response.getUsername(), createdEmployee.getUsername());
        assertThat(response.getRoles()).contains(Role.ROLE_USER);
        assertThat(response.getIsActive()).isEqualTo(true);
    }

    @Test
    public void creatingAUserWithAnUsernameAlreadyInUseShouldGiveAnException() {
        // Create a new test user
        CreateUpdateUserDTO createdUser = new CreateUpdateUserDTO();
        createdUser.setUsername("Admin");
        createdUser.setPassword("Admin");
        createdUser.setFirstname("John");
        createdUser.setLastname("Doe");
        createdUser.setEmail("johndoe@example.com");
        createdUser.setDayLimit(new BigDecimal(1236.75));
        createdUser.setPhonenumber("0612345678");
        createdUser.setTransactionLimit(new BigDecimal(150.00));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.add(createdUser);
        });

        assertTrue(exception.getMessage().contains("Username already in use"));
    }

    @Test
    public void updatingAUserSuccessfullyShouldGiveUser() {
        CreateUpdateUserDTO user = new CreateUpdateUserDTO();
        user.setUsername("Admin");
        user.setPassword("Admin");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("johndoe@example.com");
        user.setDayLimit(new BigDecimal(1236.75));
        user.setTransactionLimit(new BigDecimal(150.00));
        user.setPhonenumber("06 87654321");

        // Add the employee
        User response = userService.updateUser(user);

        // Assert that the birthdate has been updated
        assertEquals(response.getUsername(), user.getUsername());
        assertEquals(response.getPhonenumber(), user.getPhonenumber());
    }

    @Test
    public void gettingAllUsersShouldGiveListOfUsers() {
        List<User> users = userService.getUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUsername()).isEqualTo("Bank");
    }

    @Test
    public void findingAUserSuccessfullyByUsernameShouldGiveUser() {
        String usernameToFind = "Admin";

        List<User> response = userService.getUserBySearchterm(usernameToFind);

        assertNotNull(response);
        assertThat(response.get(0).getUsername()).isEqualTo("Admin");
    }

    @Test
    public void findingAUserWithAnInvalidUsernameShouldGiveException() {
        String notExistingUsername = "DezeUserdoetnietbestaanwantbestaatniet";

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userService.getUserBySearchterm(notExistingUsername);
        });

        assertTrue(exception.getMessage().contains("Username not found"));
    }
}