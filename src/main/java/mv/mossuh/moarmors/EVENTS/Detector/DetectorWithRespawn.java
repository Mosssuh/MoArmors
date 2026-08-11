package mv.mossuh.moarmors.EVENTS.Detector;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ENUMS.EquipType;
import mv.mossuh.moarmors.API.Events.PlayerChangePieceEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorGetter;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ENUMS.PieceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class DetectorWithRespawn implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byRespawning(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Armor armor = ArmorGetter.armor(player);
        Piece helmet = armor.getPiece(PieceType.HELMET);
        Piece chestplate = armor.getPiece(PieceType.CHESTPLATE);
        Piece leggings = armor.getPiece(PieceType.LEGGINGS);
        Piece boots = armor.getPiece(PieceType.BOOTS);

        if (helmet.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, helmet, EquipType.EQUIP, DetectorType.RESPAWN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (chestplate.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, chestplate, EquipType.EQUIP, DetectorType.RESPAWN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (leggings.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, leggings, EquipType.EQUIP, DetectorType.RESPAWN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (boots.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, boots, EquipType.EQUIP, DetectorType.RESPAWN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }

        ArmorsAPI.getManager().getPlayer(uuid).setArmor(armor);
    }
}
