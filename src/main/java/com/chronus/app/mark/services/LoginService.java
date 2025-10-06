package com.chronus.app.mark.services;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import com.chronus.app.user.UserRepository;
import com.chronus.app.utils.HttpResponse;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class LoginService {
    protected UserRepository repository;

    @Autowired
    public LoginService(UserRepository repository) {
        this.repository = repository;
    }

    public LoginService(){}

    public HttpResponse<User> login(User user) {
        User existingUser = repository.findByEmail(user.getEmail());

        if (existingUser != null) {
            if (existingUser.getPassword().equals(user.getPassword())) {
                return new HttpResponse<>(200, "User logged in", existingUser);
            }
            return new HttpResponse<>(401, "Wrong Password", user);
        }

        return new HttpResponse<>(0, "", null);
    }

}
