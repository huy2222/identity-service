package com.devteria.identityservice.service;

import com.devteria.identityservice.dto.request.UserCreationRequest;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.AppException;
import com.devteria.identityservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(1990, 1, 1);
        userCreationRequest = UserCreationRequest.builder()
                .username("join12345")
                .firstName("Test")
                .lastName("User")
                .password("123456781111")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cd5151e0-1c2b-4f3a-9d5e-123456789abc")
                .username("join12345")
                .firstName("Test")
                .lastName("User")
                .dob(dob)
                .build();
        user = User.builder()
                .id("cd5151e0-1c2b-4f3a-9d5e-123456789abc")
                .username("join12345")
                .firstName("Test")
                .lastName("User")
                .dob(dob)
                .build();
    }

    @Test
    void creatUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(false);
        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        // WHEN
        var response = userService.createUser(userCreationRequest);

        // THEN
        Assertions.assertThat(response.getId())
                .isEqualTo("cd5151e0-1c2b-4f3a-9d5e-123456789abc");
        Assertions.assertThat(response.getUsername())
                .isEqualTo("join12345");
    }

    @Test
    void creatUser_userExist_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString()))
                .thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));
        assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1002);
    }

    @Test
    @WithMockUser(username = "join12345" )
    void getMyInfo_valid_success() {
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));
        var response = userService.getMyInfo();
        Assertions.assertThat(response.getUsername())
                .isEqualTo("join12345");
        Assertions.assertThat(response.getId())
                .isEqualTo("cd5151e0-1c2b-4f3a-9d5e-123456789abc");
    }


}
