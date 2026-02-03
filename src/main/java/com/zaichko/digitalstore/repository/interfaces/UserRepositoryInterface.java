package com.zaichko.digitalstore.repository.interfaces;

import com.zaichko.digitalstore.model.User;

public interface UserRepositoryInterface extends CrudRepository<User> {
    User getByEmail(String email);
}
