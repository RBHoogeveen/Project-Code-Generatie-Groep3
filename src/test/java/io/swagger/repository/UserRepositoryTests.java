package io.swagger.repository;

import io.swagger.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {
  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUsernameShouldReturnUser(){
    User testUser = userRepository.findByUsername("User");

    assertEquals(testUser.getClass(), User.class);
    assertEquals("User", testUser.getUsername());
  }

  @Test
  public void getUserIdByUsernameShouldReturnInt(){
    Integer testUserId = userRepository.getUserIdByUsername("User");

    assertEquals(testUserId.getClass(), Integer.class);
    assertEquals( Integer.valueOf(3), testUserId);
  }

  @Test
  public void getUserBySearchTermShouldReturnUserList(){
    assertEquals(ArrayList.class ,userRepository.getUserBySearchterm("User").getClass());
  }
}
