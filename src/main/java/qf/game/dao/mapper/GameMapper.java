package qf.game.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import qf.game.dao.entity.Game;
import qf.game.dao.entity.LinkCategoryGame;

import java.util.List;

@Mapper
public interface GameMapper {

    List<Game> findAll();

    Game findById(Long id);

    List<Game> findAllByCategoryId(Long categoryId);

    Long create(Game game);

    void update(Game game);

    void delete(Long id);

    void createCategoryRelation(List<LinkCategoryGame> links);

}
