package qf.game.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import qf.game.dao.entity.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> findAll();

    Category findById(Long id);

    Long create(Category category);

    void update(Category category);

    void delete(Long id);

}
