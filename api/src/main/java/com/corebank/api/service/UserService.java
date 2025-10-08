package com.corebank.api.service;

import com.corebank.api.model.User;
import com.corebank.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // In a real app, you should encrypt the password before saving
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User with id " + id + " does not exist");
        }
    }
}
