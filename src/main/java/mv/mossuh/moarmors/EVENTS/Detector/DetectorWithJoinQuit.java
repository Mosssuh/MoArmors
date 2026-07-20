package mv.mossuh.moarmors.EVENTS.Detector;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ENUMS.EquipType;
import mv.mossuh.moarmors.API.Events.PlayerChangePieceEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorGetter;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ENUMS.PieceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class DetectorWithJoinQuit implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byJoining(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Armor armor = ArmorGetter.armor(player);
        ArmorPlayer armorPlayer = new ArmorPlayer(uuid, armor);
        ArmorsAPI.getManager().addPlayer(armorPlayer);

        Piece helmet = armor.getPiece(PieceType.HELMET);
        Piece chestplate = armor.getPiece(PieceType.CHESTPLATE);
        Piece leggings = armor.getPiece(PieceType.LEGGINGS);
        Piece boots = armor.getPiece(PieceType.BOOTS);

        if (helmet.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, helmet, EquipType.EQUIP, DetectorType.PLAYER_JOIN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (chestplate.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, chestplate, EquipType.EQUIP, DetectorType.PLAYER_JOIN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (leggings.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, leggings, EquipType.EQUIP, DetectorType.PLAYER_JOIN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (boots.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, boots, EquipType.EQUIP, DetectorType.PLAYER_JOIN);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void byLeaving(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        Armor armor = armorPlayer.getArmor();

        Piece helmet = armor.getPiece(PieceType.HELMET);
        Piece chestplate = armor.getPiece(PieceType.CHESTPLATE);
        Piece leggings = armor.getPiece(PieceType.LEGGINGS);
        Piece boots = armor.getPiece(PieceType.BOOTS);

        if (helmet.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, helmet, EquipType.UNEQUIP, DetectorType.PLAYER_LEAVE);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (chestplate.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, chestplate, EquipType.UNEQUIP, DetectorType.PLAYER_LEAVE);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (leggings.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, leggings, EquipType.UNEQUIP, DetectorType.PLAYER_LEAVE);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }
        if (boots.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(player, boots, EquipType.UNEQUIP, DetectorType.PLAYER_LEAVE);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        }

        ArmorsAPI.getManager().removePlayer(uuid);
    }
}
