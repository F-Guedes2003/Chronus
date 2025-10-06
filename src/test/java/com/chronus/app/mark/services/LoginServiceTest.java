package com.chronus.app.mark.services;

import com.chronus.app.user.User;
import com.chronus.app.user.UserRepository;
import com.chronus.app.utils.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    LoginService sut = new LoginService();
    @Mock UserRepository userRepository;

    @BeforeEach
    public void setup() {
        sut.repository = userRepository;;
    }

    @Test
    @DisplayName("Should return 200 if the user exists")
    public void shouldReturn200IfUserExists(){
        User loginUser = new User("Vitor Roque","tigrinho","vitorroque@palmeiras.com.br");
        User mockUser = new User("Vitor Roque","tigrinho","vitorroque@palmeiras.com.br");
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        assertThat(sut.login(loginUser)).isEqualTo(new HttpResponse<User>(200, "User logged in", loginUser));
    }

    @Test
    @DisplayName("Should return 401 if the password is wrong")
    public void shouldReturn401IfPasswordWrong(){
        User loginUser = new User("Vitor Roque","tigrinho1","vitorroque@palmeiras.com.br");
        User mockUser = new User("Vitor Roque","tigrinho","vitorroque@palmeiras.com.br");
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        assertThat(sut.login(loginUser)).isEqualTo(new HttpResponse<User>(401, "Wrong Password", loginUser));
    }
}