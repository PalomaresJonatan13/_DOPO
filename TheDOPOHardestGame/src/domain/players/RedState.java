package domain.players;

public class RedState implements PlayerState {
    @Override
    public void onEnterState(Player player) {
        player.setWidth(player.getSideLength());
        player.setHeight(player.getSideLength());
        player.setSpeed(player.getBaseSpeed());
    }

    @Override
    public void handleDie(Player player) {
        if (player.isAlive()) {
            player.performDie();
        }
    }

    @Override
    public void handleResetPosition(Player player) {
        player.performResetPosition();
    }
}
