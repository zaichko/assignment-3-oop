package kz.aitu.digitalcontent.service;

import kz.aitu.digitalcontent.exception.DuplicateResourceException;
import kz.aitu.digitalcontent.exception.InvalidInputException;
import kz.aitu.digitalcontent.exception.ResourceNotFoundException;
import kz.aitu.digitalcontent.model.User;
import kz.aitu.digitalcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        if (user == null) {
            throw new InvalidInputException("User cannot be null");
        }

        user.validate();

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", user.getEmail());
        }

        return repository.create(user);
    }

    public List<User> getAllUsers() {
        return repository.getAll();
    }

    public User getUserById(int id) {
        return repository.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public User updateUser(int id, User user) {
        if (!repository.exists(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        user.validate();
        return repository.update(id, user);
    }

    public boolean deleteUser(int id) {
        if (!repository.exists(id)) {
            throw new ResourceNotFoundException("User", id);
        }

        return repository.delete(id);
    }
}