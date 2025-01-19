package owenwang.game.card;

import com.badlogic.gdx.Gdx;
import owenwang.game.Util;

import java.util.ArrayList;
import java.util.List;

public class Cards {
    public static final List<Card> commonCards = List.of(
        new BreakCard(),
        new ThousandCutsCard(),
        new AnticipateCard(),
        new WhirlCard(),
        new WalkItOffCard(),
        new SlamCard(),
        new WarCryCard(),
        new ConcludeCard(),
        new SeverSoulCard(),
        new FortitudeCard(),
        new AdrenalineCard(),
        new FortifyCard(),
        new CriticalStrikeCard(),
        new FuryCard(),
        new ParryCard(),
        new StrengthCard(),
        new BulwarkCard()
    );

    public static List<Card> randomCards(int amount) {
        List<Card> cards = new ArrayList<>();
        Card c;
        int i = 0;
        while (true) {
            c = commonCards.get(Util.RANDOM.nextInt(commonCards.size()));
            if (cards.contains(c)) continue;
            cards.add(c);
            if (++i == amount) break;
        }
        return cards;
    }

    public static float width() {
        return Gdx.graphics.getWidth() * CardActor.CARD_WIDTH_SW_PROPORTION;
    }

    public static float height() {
        return Gdx.graphics.getWidth() * CardActor.CARD_HEIGHT_SW_PROPORTION;
    }
}
