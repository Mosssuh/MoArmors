package mv.mossuh.moarmors.EVENTS.Detector;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorGetter;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class DetectorWithInventory implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byInventoryClose(InventoryCloseEvent event) {
        HumanEntity human = event.getPlayer();
        if (!human.getType().equals(EntityType.PLAYER)) {
            return;
        }

        Player player = (Player) human;
        UUID uuid = player.getUniqueId();

        Armor newArmor = ArmorGetter.armor(player);
        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        Armor oldArmor = armorPlayer.getArmor();

        DetectorType detectorType = DetectorType.CLOSE_INVENTORY;
        UtilMethods.executeChangePieceEvent(player, newArmor.getPiece(PieceType.HELMET), oldArmor.getPiece(PieceType.HELMET), detectorType);
        UtilMethods.executeChangePieceEvent(player, newArmor.getPiece(PieceType.CHESTPLATE), oldArmor.getPiece(PieceType.CHESTPLATE), detectorType);
        UtilMethods.executeChangePieceEvent(player, newArmor.getPiece(PieceType.LEGGINGS), oldArmor.getPiece(PieceType.LEGGINGS), detectorType);
        UtilMethods.executeChangePieceEvent(player, newArmor.getPiece(PieceType.BOOTS), oldArmor.getPiece(PieceType.BOOTS), detectorType);

        ArmorsAPI.getManager().getPlayer(uuid).setArmor(newArmor);
    }

}
