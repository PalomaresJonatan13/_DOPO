package domain;

public class RedState implements PlayerState {
    @Override
    public void onEnterState(Player player) {
        player.setWidth(player.getBaseWidth());
        player.setHeight(player.getBaseHeight());
        player.setSpeedLimit(player.getBaseSpeedLimit());
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
