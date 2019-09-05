package qf.game.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import qf.game.dao.entity.Category;
import qf.game.dao.entity.Game;
import qf.game.dto.CategoryDto;
import qf.game.dto.GameDto;
import qf.game.service.GameService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<GameDto> findAll(){
        List<Game> categories = gameService.findAll();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public GameDto findById(@PathVariable("id") Long id){
        return this.convertToDto(gameService.findById(id));
    }

    @GetMapping(value = "/category/{categoryId}")
    public List<GameDto> findAllByCategoryId(@PathVariable("categoryId") Long categoryId){
        List<Game> categories = gameService.findAllByCategoryId(categoryId);
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody GameDto gameDto) throws Exception {
        Game game = this.convertToEntity(gameDto);
        return gameService.create(game);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody GameDto gameDto) {
        Assert.notNull(gameService.findById(id),"Game");
        Game game = this.convertToEntity(gameDto);
        gameService.update(game);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        gameService.deleteById(id);
    }

    private GameDto convertToDto(Game game) {
        Assert.notNull(game,"Game");
        GameDto gameDto = modelMapper.map(game, GameDto.class);
        if(gameDto.getCreateTime() != null){
            gameDto.setCreateTime(gameDto.getCreateTimeConverted(game.getCreateTime()));
        }

        if(game.getCategories() != null) {
            gameDto.setCategories(game.getCategories().stream()
                .map(category -> {
                        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
                        categoryDto.setCreateTime(categoryDto.getCreateTimeConverted(category.getCreateTime()));
                        return categoryDto;
                    }
                )
                .collect(Collectors.toList())
            );
        }
        return gameDto;
    }

    private Game convertToEntity(GameDto gameDto) {
        Assert.notNull(gameDto,"Game");
        Game game = modelMapper.map(gameDto, Game.class);
        game.setCreateTime(
            (gameDto.getCreateTime() == null) ?
                Timestamp.valueOf(LocalDateTime.now()) : gameDto.getCreateTimeConverted(gameDto.getCreateTime())
        );

        if(gameDto.getCategories() != null){
            game.setCategories(
                gameDto.getCategories().stream().map(
                    categoryDto -> {
                        Category category = modelMapper.map(categoryDto, Category.class);
                        category.setCreateTime(
                            (categoryDto.getCreateTime() == null) ?
                                Timestamp.valueOf(LocalDateTime.now()) : categoryDto.getCreateTimeConverted(categoryDto.getCreateTime())
                        );
                        return category;
                    }
                )
                .collect(Collectors.toList())
            );
        }
        return game;
    }

}
