package domain;

public interface PlayerState {
    void onEnterState(Player player);
    void handleDie(Player player);
    void handleResetPosition(Player player);
}
