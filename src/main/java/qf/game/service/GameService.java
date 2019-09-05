package qf.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import qf.game.dao.entity.Category;
import qf.game.dao.entity.Game;
import qf.game.dao.entity.LinkCategoryGame;
import qf.game.dao.mapper.CategoryMapper;
import qf.game.dao.mapper.GameMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Game> findAll(){
        return gameMapper.findAll();
    }

    public List<Game> findAllByCategoryId(Long categoryId){
        return gameMapper.findAllByCategoryId(categoryId);
    }

    public Game findById(Long id){
        return gameMapper.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long create(Game game) throws Exception {
        gameMapper.create(game);
        Long gameId = game.getId();
        List<Long> allCategories = categoryMapper.findAll().stream().map(Category::getId).collect(Collectors.toList());
        Assert.notEmpty(game.getCategories(), "Categories can not be empty");
        List<LinkCategoryGame> links = game.getCategories().stream().map(
                category -> new LinkCategoryGame(gameId, category.getId())
        ).collect(Collectors.toList());
        //Check relation ID
        Assert.notEmpty(allCategories, "Categories can not be empty !");
        for(LinkCategoryGame link : links){
            Long categoryId = link.getCategoryInfoId();
            if(!allCategories.contains(categoryId)){
                throw new Exception(String.format("[ Category ID : %s ] is not exist !", categoryId));
            }
        }
        gameMapper.createCategoryRelation(links);
        return gameId;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public void update(Game game){
        gameMapper.update(game);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public void deleteById(Long id){
        gameMapper.delete(id);
    }

}
