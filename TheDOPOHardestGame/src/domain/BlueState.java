package domain;

public class BlueState implements PlayerState {
    @Override
    public void onEnterState(Player player) {
        player.setWidth(player.getBaseWidth() * 1.5);
        player.setHeight(player.getBaseHeight() * 1.5);
        player.setSpeedLimit(player.getBaseSpeedLimit() * 1.5);
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
