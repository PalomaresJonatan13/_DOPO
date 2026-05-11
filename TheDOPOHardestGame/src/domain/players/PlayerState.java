package domain.players;

import java.io.Serializable;

public interface PlayerState extends Serializable {
    void onEnterState(Player player);
    void handleDie(Player player);
    void handleResetPosition(Player player);
}
