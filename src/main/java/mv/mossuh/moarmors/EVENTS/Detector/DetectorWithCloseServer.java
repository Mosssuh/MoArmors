package mv.mossuh.moarmors.EVENTS.Detector;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.API.Events.PlayerChangePieceEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorGetter;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ENUMS.EquipType;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.MANAGER.ArmorsManager;
import mv.mossuh.moarmors.MoArmors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.Iterator;
import java.util.UUID;

public class DetectorWithCloseServer implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byRespawning(PluginDisableEvent event) {
        if (!event.getPlugin().equals(MoArmors.getInstance())) return;

        Iterator<ArmorPlayer> players = ArmorsAPI.getManager().getPlayers().iterator();
        while (players.hasNext()) {
            ArmorPlayer player = players.next();
            UUID uuid = player.getUUID();

            Armor armor = player.getArmor();
            Piece helmet = armor.getPiece(PieceType.HELMET);
            Piece chestplate = armor.getPiece(PieceType.CHESTPLATE);
            Piece leggings = armor.getPiece(PieceType.LEGGINGS);
            Piece boots = armor.getPiece(PieceType.BOOTS);

            if (helmet.isPiece()) {
                PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, helmet, EquipType.UNEQUIP, DetectorType.CLOSE_SERVER);
                Bukkit.getPluginManager().callEvent(changePieceEvent);
            }
            if (chestplate.isPiece()) {
                PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, chestplate, EquipType.UNEQUIP, DetectorType.CLOSE_SERVER);
                Bukkit.getPluginManager().callEvent(changePieceEvent);
            }
            if (leggings.isPiece()) {
                PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, leggings, EquipType.UNEQUIP, DetectorType.CLOSE_SERVER);
                Bukkit.getPluginManager().callEvent(changePieceEvent);
            }
            if (boots.isPiece()) {
                PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, boots, EquipType.UNEQUIP, DetectorType.CLOSE_SERVER);
                Bukkit.getPluginManager().callEvent(changePieceEvent);
            }

            players.remove();
        }
    }
}
