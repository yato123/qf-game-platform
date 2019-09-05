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
import qf.game.service.CategoryService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game/categories")
public class CategoryController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAll(){
        List<Category> categories = categoryService.findAll();
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public CategoryDto findById(@PathVariable("id") Long id){
        return this.convertToDto(categoryService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody CategoryDto categoryDto){
        Category category = this.convertToEntity(categoryDto);
        return categoryService.create(category);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable( "id" ) Long id, @RequestBody CategoryDto categoryDto) {
        Assert.notNull(categoryService.findById(id),"Category");
        Category category = this.convertToEntity(categoryDto);
        categoryService.update(category);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
    }

    private CategoryDto convertToDto(Category category) {
        Assert.notNull(category,"Category");
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        if(category.getCreateTime() != null){
            categoryDto.setCreateTime(categoryDto.getCreateTimeConverted(category.getCreateTime()));
        }
        if(category.getGames() != null){
            categoryDto.setGames(category.getGames().stream()
                .map(game -> {
                        GameDto gameDto = modelMapper.map(game, GameDto.class);
                        gameDto.setCreateTime(gameDto.getCreateTimeConverted(game.getCreateTime()));
                        return gameDto;
                    }
                )
                .collect(Collectors.toList())
            );
        }
        return categoryDto;
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        Assert.notNull(categoryDto,"Category");
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setCreateTime(
            (categoryDto.getCreateTime() == null) ?
                Timestamp.valueOf(LocalDateTime.now()) : categoryDto.getCreateTimeConverted(categoryDto.getCreateTime())
        );
        if(categoryDto.getGames() != null){
            category.setGames(
                categoryDto.getGames().stream().map(
                    gameDto -> {
                        Game game = modelMapper.map(gameDto, Game.class);
                        game.setCreateTime(
                                (gameDto.getCreateTime() == null) ?
                                        Timestamp.valueOf(LocalDateTime.now()) : gameDto.getCreateTimeConverted(gameDto.getCreateTime())
                        );
                        return game;
                    }
                )
                .collect(Collectors.toList())
            );
        }
        return category;
    }

}
