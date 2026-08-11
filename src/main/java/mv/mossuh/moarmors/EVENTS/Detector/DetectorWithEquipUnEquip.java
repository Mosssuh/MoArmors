package mv.mossuh.moarmors.EVENTS.Detector;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ENUMS.EquipType;
import mv.mossuh.moarmors.API.Events.PlayerChangePieceEvent;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.mocore.EVENTS.ArmorEquipEvent.ArmorEquipEvent;
import mv.mossuh.mocore.EVENTS.ArmorEquipEvent.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import java.util.UUID;

public class DetectorWithEquipUnEquip implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byEquip(ArmorEquipEvent event) {
        if (event.isCancelled()) { return; }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ArmorType armorType = event.getType();
        PieceType pieceType = PieceType.NONE;
        switch (armorType) {
            case HELMET:
                pieceType = PieceType.HELMET;
                break;
            case CHESTPLATE:
                pieceType = PieceType.CHESTPLATE;
                break;
            case LEGGINGS:
                pieceType = PieceType.LEGGINGS;
                break;
            case BOOTS:
                pieceType = PieceType.BOOTS;
                break;
        }

        // Equip Piece
        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        ItemStack equipItemStack = event.getNewArmorPiece();
        Piece equipPiece = Piece.getPiece(equipItemStack);
        if (equipPiece.isPiece()) {
            armorPlayer.getArmor().setPiece(equipPiece);
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, equipPiece, EquipType.EQUIP, DetectorType.MANUAL);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        } else {
            armorPlayer.getArmor().removePiece(pieceType);
        }


        // UnEquip Piece
        ItemStack unEquipItemStack = event.getOldArmorPiece();
        Piece unEquipPiece = Piece.getPiece(unEquipItemStack);
        if (unEquipPiece.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, unEquipPiece, EquipType.UNEQUIP, DetectorType.MANUAL);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
    }
}
