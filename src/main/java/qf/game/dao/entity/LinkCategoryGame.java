package qf.game.dao.entity;

import java.io.Serializable;

public class LinkCategoryGame implements Serializable {

    private Long gameInfoId;
    private Long categoryInfoId;

    public LinkCategoryGame(Long gameInfoId, Long categoryInfoId) {
        this.gameInfoId = gameInfoId;
        this.categoryInfoId = categoryInfoId;
    }

    public Long getGameInfoId() {
        return gameInfoId;
    }

    public void setGameInfoId(Long gameInfoId) {
        this.gameInfoId = gameInfoId;
    }

    public Long getCategoryInfoId() {
        return categoryInfoId;
    }

    public void setCategoryInfoId(Long categoryInfoId) {
        this.categoryInfoId = categoryInfoId;
    }

}

