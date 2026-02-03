package com.zaichko.digitalstore.service;

import com.zaichko.digitalstore.exception.InvalidInputException;
import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.repository.CreatorRepository;

import java.util.List;

public class CreatorService {

    private final CreatorRepository creatorRepo;

    public CreatorService(CreatorRepository creatorRepo){
        this.creatorRepo = creatorRepo;
    }

    public void createCreator(Creator creator) {
        creator.validate();

        creatorRepo.create(creator);
    }

    public List<Creator> getAllCreators(){
        return creatorRepo.getAll();
    }

    public Creator getCreatorById(int id){

        Creator creator = creatorRepo.getById(id);

        if(creator == null){
            throw new ResourceNotFoundException("Creator not found with ID " + id);
        }

        return creator;

    }

    public Creator getTopEarningCreator() {
        Creator creator = creatorRepo.findCreatorWithMaxRevenue();

        if (creator == null) {
            throw new ResourceNotFoundException("No revenue data available");
        }

        return creator;
    }

    public double getTopEarnings(){
        return creatorRepo.findMaxRevenue();
    }

    public void updateCreator(int id, Creator creator){

        if(creatorRepo.getById(id) == null){
            throw new ResourceNotFoundException("Creator not found");
        }

        creatorRepo.update(id, creator);

    }

    public void deleteCreator(int id){

        if(creatorRepo.getById(id) == null){
            throw new ResourceNotFoundException("Creator not found");
        }

        if(creatorRepo.hasContentByCreatorId(id)){
            throw new InvalidInputException("Cannot delete creator with existing content");
        }

        creatorRepo.delete(id);

    }

}
