package domain;

public class GreenState implements PlayerState {
    private boolean hasShield;

    @Override
    public void onEnterState(Player player) {
        this.hasShield = true;
        player.setWidth(player.getBaseWidth());
        player.setHeight(player.getBaseHeight());
        player.setSpeedLimit(player.getBaseSpeedLimit());
    }

    @Override
    public void handleDie(Player player) {
        if (player.isAlive()) {
            if (this.hasShield) {
                this.hasShield = false;
                player.setSpeedLimit(player.getBaseSpeedLimit() * 0.7);
            } else {
                this.resetShield(player);
                player.performDie();
            }
        }
    }

    @Override
    public void handleResetPosition(Player player) {
        player.performResetPosition();
        this.resetShield(player);
    }

    private void resetShield(Player player) {
        this.hasShield = true;
        player.setSpeedLimit(player.getBaseSpeedLimit()); 
    }
}
