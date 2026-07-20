package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.ACTIONS.ActionResult;
import mv.mossuh.moarmors.ACTIONS.Requirements;
import mv.mossuh.moarmors.ACTIONS.Rewards;
import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.DefaultActions;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RewardExecutor {
    private boolean cancelEvent = false;
    private boolean cancelDrops = false;
    private boolean cancelMessage = false;



    private Player player = null;
    private Event event = null;
    private EventType eventType = EventType.INVALID;
    private vArgs args = new vArgs();
    private List<VariableArg> variables = new ArrayList<>();
    private int times = 1;
    public RewardExecutor(Player player, Event event, EventType eventType, vArgs args, List<VariableArg> variables, Integer times) {
        this.player = player;
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        if (args != null) { this.args = args; }
        if (variables != null) { this.variables = variables; }
        if (times != null) { this.times = times; }
    }

    public void execute() {
        UUID uuid = player.getUniqueId();
        RewardArgs rewardArgs = args.getRewardArgs();
        RewardArgsType rewardArgsType = rewardArgs.getArgumentType();

        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        if (armorPlayer.isPlayer() && armorPlayer.hasArmor()) {
            Armor armor = armorPlayer.getArmor();
            List<ConfigArmor> uniqueConfigs = armor.getUniqueConfigArmors();

            for (ConfigArmor uniqueConfig : uniqueConfigs) {
                DefaultActions defaultActions = uniqueConfig.getActions().getDefaultActions();
                if (defaultActions.hasEvent(eventType)) {
                    Requirements requirements = new Requirements(event, eventType, player, armor, uniqueConfig, args)
                            .addPlayerVariables().addArmorVariables();

                    if (!variables.isEmpty()) {
                        requirements.addVariables(variables);
                    }

                    switch (rewardArgsType) {
                        case ITEMSTACK:
                            requirements.addItemStackVariables();
                            break;
                        case BLOCK:
                            requirements.addBlockVariables();
                            break;
                        case ENTITY:
                            requirements.addEntityVariables();
                            break;
                        case LIVING_ENTITY:
                            requirements.addLivingEntityVariables();
                            break;
                        case ITEM:
                            break;
                    }

                    requirements.check();
                    ActionResult result = requirements.getActionResult();
                    if (result.hasApprovedRewards()) {
                        Rewards rewards = new Rewards(result, times).executeDefault().executeArmor().executeVariables().check();
                        if (!this.cancelDrops && rewards.cancelDrops()) {
                            this.cancelDrops = true;
                        }
                        if (!this.cancelEvent && rewards.cancelEvent()) {
                            this.cancelEvent = true;
                        }
                        if (!this.cancelMessage && rewards.cancelMessage()) {
                            this.cancelMessage = true;
                        }
                    }
                }
            }
        }
    }


    public boolean isCancelledDrops() { return cancelDrops; }
    public boolean isCancelledEvent() { return cancelEvent; }
    public boolean isCancelledMessage() { return cancelMessage; }
}
