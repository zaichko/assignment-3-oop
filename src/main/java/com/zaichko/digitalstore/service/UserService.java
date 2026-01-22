package com.zaichko.digitalstore.service;

import com.zaichko.digitalstore.exception.DuplicateResourceException;
import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.User;
import com.zaichko.digitalstore.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public void createUser(User user) {
        user.validate();

        if (userRepo.getByEmail(user.getEmail()) != null) {
            throw new DuplicateResourceException("User with this email already exist");
        }

        userRepo.create(user);
    }

    public List<User> getAllUsers(){
        return userRepo.getAll();
    }

    public User getUserById(int id){

        User user = userRepo.getById(id);

        if(user == null){
            throw new ResourceNotFoundException("User not found with ID " + id);
        }

        return user;

    }

    public void updateUser(int id, User user){

        user.validate();

        if(userRepo.getById(id) == null){
            throw new ResourceNotFoundException("User not found");
        }

        userRepo.update(id, user);

    }

    public void deleteUser(int id){

        if(userRepo.getById(id) == null){
            throw new ResourceNotFoundException("User not found");
        }

        userRepo.delete(id);

    }

}
