package owenwang.game.levels;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;
import owenwang.game.entity.enemy.GolemEnemy;

import java.util.List;

public class GolemLevel implements Level {
    @Override
    public List<Enemy> getEnemies() {
        return List.of(new GolemEnemy());
    }

    @Override
    public String getArt() {
        return Assets.level3;
    }
}
