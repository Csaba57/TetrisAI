import game.tetris.TetrisAction;
import game.tetris.TetrisGame;
import game.tetris.utils.TetrisState;

import java.util.Random;

public class Test extends game.tetris.TetrisPlayer {
    /**
     * Constructs a player using the specified parameters.
     *
     * @param color player color, not used
     * @param random random number generator
     * @param state game state
     * */

    public Test(int color, Random random, TetrisState state) {
        super(color, random, state);
    }

    @Override
    public TetrisAction getAction(long remainingTime) {
        int action = TetrisGame.actions[random.nextInt(3)];
        state.apply(action);
        return new TetrisAction(action);
    }
}
