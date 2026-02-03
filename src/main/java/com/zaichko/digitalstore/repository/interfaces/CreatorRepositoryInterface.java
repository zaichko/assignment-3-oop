package com.zaichko.digitalstore.repository.interfaces;

import com.zaichko.digitalstore.model.Creator;

public interface CreatorRepositoryInterface extends CrudRepository<Creator> {
    boolean hasContentByCreatorId(int creatorId);
    Creator findCreatorWithMaxRevenue();
    double findMaxRevenue();
}
