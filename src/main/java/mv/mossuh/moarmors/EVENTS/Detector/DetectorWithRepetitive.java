package mv.mossuh.moarmors.EVENTS.Detector;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorGetter;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.MoArmors;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class DetectorWithRepetitive {

    private static BukkitTask task;

    public static void start(MoArmors main) {
        task = Bukkit.getScheduler().runTaskTimer(main, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                Armor newArmor = ArmorGetter.armor(player);
                ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
                Armor oldArmor = armorPlayer.getArmor();

                DetectorType detectorType = DetectorType.REPETITIVE;
                UtilMethods.executeChangePieceEvent(uuid, newArmor.getPiece(PieceType.HELMET), oldArmor.getPiece(PieceType.HELMET), detectorType);
                UtilMethods.executeChangePieceEvent(uuid, newArmor.getPiece(PieceType.CHESTPLATE), oldArmor.getPiece(PieceType.CHESTPLATE), detectorType);
                UtilMethods.executeChangePieceEvent(uuid, newArmor.getPiece(PieceType.LEGGINGS), oldArmor.getPiece(PieceType.LEGGINGS), detectorType);
                UtilMethods.executeChangePieceEvent(uuid, newArmor.getPiece(PieceType.BOOTS), oldArmor.getPiece(PieceType.BOOTS), detectorType);

                armorPlayer.setArmor(newArmor);
            }
        }, 600L, Config.CHECK_ARMOR_INTERVAL * 20L);
    }

    public static void cancel() {
        if (task != null) {
            task.cancel();
        }
    }
}
