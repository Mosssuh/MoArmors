package mv.mossuh.moarmors.API.Events;

import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PieceChangeExpEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private UUID uuid;
    private Piece piece = new Piece(null, null, null, null, null, null, null, null);
    private double exp = 0;
    private ExecuteType executeType = ExecuteType.NONE;
    private ReceiveType receiveType = ReceiveType.NONE;
    private boolean isCancelled;

    public PieceChangeExpEvent(UUID uuid, Piece piece, ExecuteType executeType, ReceiveType receiveType, Double exp) {
        this.uuid = uuid;
        if (piece != null) { this.piece = piece; }
        if (executeType != null) { this.executeType = executeType; }
        if (receiveType != null) { this.receiveType = receiveType; }
        if (exp != null) { this.exp = exp; }
        this.isCancelled = false;
    }

    public UUID getUUID() {
        return uuid;
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
