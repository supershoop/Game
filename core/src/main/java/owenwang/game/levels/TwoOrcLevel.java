package owenwang.game.levels;

import owenwang.game.Assets;
import owenwang.game.entity.enemy.Enemy;
import owenwang.game.entity.enemy.OrcEnemy;

import java.util.List;

public class TwoOrcLevel implements Level {
    @Override
    public List<Enemy> getEnemies() {
        List<Enemy> l = List.of(
            new OrcEnemy(),
            new OrcEnemy()
        );
        l.get(0).turn++;
        return l;
    }

    @Override
    public String getArt() {
        return Assets.level0;
    }
}
