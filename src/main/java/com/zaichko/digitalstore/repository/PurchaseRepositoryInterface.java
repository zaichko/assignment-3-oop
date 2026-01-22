package com.zaichko.digitalstore.repository;

import com.zaichko.digitalstore.model.Purchase;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseRepositoryInterface {

    void create(Purchase entity) throws SQLException;

    List<Purchase> getAll() throws SQLException;

    Purchase getById(Integer id) throws SQLException;

    boolean existsByUserAndContent(int userId, int contentId) throws SQLException;

}
