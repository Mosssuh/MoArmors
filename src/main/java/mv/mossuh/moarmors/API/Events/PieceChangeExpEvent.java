package mv.mossuh.moarmors.API.Events;

import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PieceChangeExpEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Piece piece = new Piece(null, null, null, null, null, null, null, null);
    private double exp = 0;
    private double boost = 1;
    private ExecuteType executeType = ExecuteType.NONE;
    private ReceiveType receiveType = ReceiveType.NONE;
    private boolean isCancelled;

    public PieceChangeExpEvent(Player player, Piece piece, ExecuteType executeType, ReceiveType receiveType, Double exp) {
        this.player = player;
        if (piece != null) { this.piece = piece; }
        if (executeType != null) { this.executeType = executeType; }
        if (receiveType != null) { this.receiveType = receiveType; }
        if (exp != null) { this.exp = exp; }
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Piece getPiece() {
        return piece;
    }

    public ExecuteType getExecuteType() {
        return executeType;
    }

    public ReceiveType getReceiveType() {
        return receiveType;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void addExp(double exp) {
        this.exp = this.exp + exp;
    }

    public double getBoost() {
        return boost;
    }
    public double getBoost(boolean ignoreBase) {
        return boost - 1;
    }
    public void setBoost(double boost) {
        if (boost < 1) {
            boost = 1;
        }

        this.boost = boost;
    }
    public void addBoost(double boost) {
        double newBoost = this.boost + boost;
        if (newBoost < 1) {
            newBoost = 1;
        }

        this.boost = newBoost;
    }
    public void removeBoost(double boost) {
        double newBoost = this.boost - boost;
        if (newBoost < 1) {
            newBoost = 1;
        }
        this.boost = newBoost;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }


}
