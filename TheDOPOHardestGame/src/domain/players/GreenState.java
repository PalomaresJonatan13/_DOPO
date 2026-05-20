package domain.players;

public class GreenState implements PlayerState {
    private boolean hasShield;

    @Override
    public void onEnterState(Player player) {
        this.hasShield = true;
        player.setWidth(player.getSideLength());
        player.setHeight(player.getSideLength());
        player.setSpeed(player.getBaseSpeed() * 2);
    }

    @Override
    public void handleDie(Player player) {
        if (player.isAlive()) {
            if (this.hasShield) {
                this.hasShield = false;
                player.setSpeed(player.getBaseSpeed() * 0.7);
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
        player.setSpeed(player.getBaseSpeed() * 2);
    }

    public boolean hasShield() {
        return this.hasShield;
    }
}
