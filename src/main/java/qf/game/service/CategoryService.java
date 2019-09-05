package qf.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qf.game.dao.entity.Category;
import qf.game.dao.mapper.CategoryMapper;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findAll(){
        return categoryMapper.findAll();
    }

    public Category findById(Long id){
        return categoryMapper.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long create(Category category){
        categoryMapper.create(category);
        return category.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public void update(Category category){
        categoryMapper.update(category);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public void deleteById(Long id){
        categoryMapper.delete(id);
    }

}
