package owenwang.game.levels;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;
import owenwang.game.entity.enemy.GolemEnemy;
import owenwang.game.entity.enemy.SkullEnemy;
import owenwang.game.entity.enemy.WitchEnemy;

import java.util.List;

public class WitchLevel implements Level {
    @Override
    public List<Enemy> getEnemies() {
        return List.of(
            new WitchEnemy()
        );
    }

    @Override
    public String getArt() {
        return Assets.level2;
    }
}
