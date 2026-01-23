package com.zaichko.digitalstore.service;

import com.zaichko.digitalstore.exception.ResourceNotFoundException;
import com.zaichko.digitalstore.model.Creator;
import com.zaichko.digitalstore.model.Game;
import com.zaichko.digitalstore.repository.GameRepository;

import java.util.List;

public class GameService {

    private final CreatorService creatorService;
    private final GameRepository gameRepo;

    public GameService(CreatorService creatorService,
                        GameRepository gameRepo){
        this.creatorService = creatorService;
        this.gameRepo = gameRepo;
    }

    public void createGame(Game game) {
        game.validate();

        Creator creator = creatorService.getCreatorById(game.getCreator().getId());

        if (creator == null) {
            throw new ResourceNotFoundException("Creator with id " + game.getCreator().getId() + " does not exist");
        }

        gameRepo.create(game);
    }

    public List<Game> getAllGames(){
        return gameRepo.getAll();
    }

    public Game getGameById(int id){

        Game game = gameRepo.getById(id);

        if(game == null){
            throw new ResourceNotFoundException("Game not found with ID " + id);
        }

        return game;

    }

    public void updateGame(int id, Game game){

        game.validate();

        if(gameRepo.getById(id) == null){
            throw new ResourceNotFoundException("Game not found");
        }

        gameRepo.update(id, game);

    }

    public void deleteGame(int id){

        if(gameRepo.getById(id) == null){
            throw new ResourceNotFoundException("Game not found");
        }

        gameRepo.delete(id);

    }
    
}
