package domain.players;

public class BlueState implements PlayerState {
    @Override
    public void onEnterState(Player player) {
        player.setWidth(player.getSideLength() * 1.5);
        player.setHeight(player.getSideLength() * 1.5);
        player.setSpeed(player.getBaseSpeed() * 1.5);
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
