package owenwang.game.levels;

import owenwang.game.entity.enemy.Enemy;

import java.util.List;

public interface Level {
    List<Enemy> getEnemies();
    String getArt();
}
