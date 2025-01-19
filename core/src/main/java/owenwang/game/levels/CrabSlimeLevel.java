package owenwang.game.levels;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.CrabEnemy;
import owenwang.game.entity.enemy.Enemy;
import owenwang.game.entity.enemy.OrcEnemy;
import owenwang.game.entity.enemy.SlimeEnemy;

import java.util.List;

public class CrabSlimeLevel implements Level {
    @Override
    public List<Enemy> getEnemies() {
        return List.of(
            new SlimeEnemy(),
            new CrabEnemy(),
            new SlimeEnemy(),
            new CrabEnemy()
        );
    }

    @Override
    public String getArt() {
        return Assets.level1;
    }
}
