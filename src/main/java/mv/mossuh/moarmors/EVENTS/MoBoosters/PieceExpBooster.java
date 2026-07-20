package mv.mossuh.moarmors.EVENTS.MoBoosters;

import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;

import java.util.UUID;

public class PieceExpBooster implements Listener {
    @EventHandler
    public void expBoost(PieceChangeExpEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Piece piece = event.getPiece();
        ConfigArmor configArmor = piece.getConfigArmor();
        ArmorIdentifier identifier = configArmor.getArmorIdentifier();
        String boosterIdentifier = identifier.getBoosterIdentifier();

        double personal = 0;
        double global = 0;
        double superiorSkyblock2 = 0;

        if (boosterIdentifier != null) {
            if (identifier.isBoosterIdentifier()) {
                personal = BoostersAPI.getManager().getBoost(new PersonalBooster(uuid, new BoosterIdentifier(boosterIdentifier, BoosterType.PERSONAL, ApplicatorType.MOARMORS, "exp")), false);
                global = BoostersAPI.getManager().getBoost(new GlobalBooster(new BoosterIdentifier(boosterIdentifier, BoosterType.GLOBAL, ApplicatorType.MOARMORS, "exp")), false);
                superiorSkyblock2 = BoostersAPI.getManager().getBoost(new SuperiorSkyblock2Booster(UtilMethods.getIslandUUID(player), new BoosterIdentifier(boosterIdentifier, BoosterType.SUPERIORSKYBLOCK2, ApplicatorType.MOARMORS, "exp")), false);
            }
        } else {
            personal = BoostersAPI.getManager().getBoost(new PersonalBooster(uuid, new BoosterIdentifier(null, BoosterType.PERSONAL, ApplicatorType.MOARMORS, "exp")), true);
            global = BoostersAPI.getManager().getBoost(new GlobalBooster(new BoosterIdentifier(null, BoosterType.GLOBAL, ApplicatorType.MOARMORS, "exp")), true);
            superiorSkyblock2 = BoostersAPI.getManager().getBoost(new SuperiorSkyblock2Booster(UtilMethods.getIslandUUID(player), new BoosterIdentifier(null, BoosterType.SUPERIORSKYBLOCK2, ApplicatorType.MOARMORS, "exp")), true);
        }

        double total = personal + global + superiorSkyblock2;
        event.addBoost(total);
    }
}
