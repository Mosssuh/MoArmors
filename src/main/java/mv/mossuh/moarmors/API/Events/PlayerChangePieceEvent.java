package mv.mossuh.moarmors.API.Events;

import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ENUMS.EquipType;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangePieceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Piece piece = new Piece(null, null, null, null, null, null, null, null);
    private EquipType equipType = EquipType.NONE;
    private DetectorType detectorType = DetectorType.NONE;

    public PlayerChangePieceEvent(Player player, Piece piece, EquipType equipType, DetectorType detectorType) {
        this.player = player;
        if (piece != null) { this.piece = piece; }
        if (equipType != null) { this.equipType = equipType; }
        if (detectorType != null) { this.detectorType = detectorType; }
    }

    public Player getPlayer() {
        return player;
    }

    public Piece getPiece() {
        return piece;
    }

    public EquipType getEquipType() { return equipType; }
    public DetectorType getDetectorType() { return  detectorType; }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
