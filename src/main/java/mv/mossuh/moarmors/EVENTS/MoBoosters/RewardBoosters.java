package mv.mossuh.moarmors.EVENTS.MoBoosters;

import mv.mossuh.moarmors.ACTIONS.Booster.EvalRequirements;
import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.Actions;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.MoBoosters.LocalBooster;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class RewardBoosters implements Listener {

    @EventHandler
    public void onApplyBooster(PlayerApplyBoostEvent event) {
        UUID uuid = event.getUUID();

        ApplicatorType applicatorType = event.getApplicatorType();
        String boosted = event.getBoosted();


        double personal = 0;
        double global = 0;
        double superiorSkyblock2 = 0;

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        if (armorPlayer.isPlayer() && armorPlayer.hasArmor()) {
            Armor armor = armorPlayer.getArmor();
            List<ConfigArmor> configs = armor.getUniqueConfigArmors();

            for (ConfigArmor config : configs) {
                String code = config.getArmorIdentifier().getCode();
                Actions actions = config.getActions();
                List<LocalBooster> boosters = actions.getBoosters();

                List<Piece> pieces = armor.getPieces(code);
                int level = Armor.getTotalLevel(pieces);

                for (LocalBooster booster : boosters) {
                    MoRequirements requirements = booster.getRequirements();
                    EvalRequirements eval = new EvalRequirements(player, armor, requirements).addPlayerVariables().addArmorVariables().check();
                    if (eval.isApproved()) {

                        LocalBooster personalBooster = actions.getBooster(new BoosterIdentifier(Config.PREFIX, BoosterType.PERSONAL, applicatorType, boosted));
                        LocalBooster globalBooster = actions.getBooster(new BoosterIdentifier(Config.PREFIX, BoosterType.GLOBAL, applicatorType, boosted));
                        LocalBooster boosterSuperiorSkyblock2 = actions.getBooster(new BoosterIdentifier(Config.PREFIX, BoosterType.SUPERIORSKYBLOCK2, applicatorType, boosted));

                        personal += personalBooster.getBoost(level);
                        global += globalBooster.getBoost(level);
                        superiorSkyblock2 += boosterSuperiorSkyblock2.getBoost(level);
                    }
                }
            }
        }

        double total = personal+global+superiorSkyblock2;
        event.addBoost(total);
    }
}
