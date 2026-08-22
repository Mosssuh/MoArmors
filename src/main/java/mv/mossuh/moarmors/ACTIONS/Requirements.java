package mv.mossuh.moarmors.ACTIONS;

import mv.mossuh.moarmors.UTILITIES.DefaultVariables;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.ENUMS.DebugType;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.moarmors.UTILITIES.MoArgs;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.OtherUtil.MoCooldown;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEvent;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import mv.mossuh.mocore.UTILITIES.REQUIREMENTS.EntityRequirement;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Requirements {
    private List<MoRewards> approvedRewards = new ArrayList<>();

    private Event event;
    private EventType eventType = EventType.NONE;
    private Player player;
    private Armor armor = new Armor();
    private ConfigArmor configArmor = new ConfigArmor();
    private MoArgs args = new MoArgs();
    private ActionResult actionResult = new ActionResult();

    public Requirements(Event event, EventType eventType, Player player, Armor armor, ConfigArmor configArmor, MoArgs args) {
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        this.player = player;
        if (armor != null) { this.armor = armor; }
        if (configArmor != null) { this.configArmor = configArmor; }
        if (args != null) { this.args = args; }
    }

    public Requirements addVariables(VariableArg... variables) {
        this.args.getVariableArgs().addAll(Arrays.asList(variables));
        return this;
    }

    public Requirements addVariables(List<VariableArg> variables) {
        this.args.getVariableArgs().addAll(variables);
        return this;
    }

    public Requirements addPlayerVariables() {
        this.args.getVariableArgs().addAll(DefaultVariables.player(player));
        return this;
    }

    public Requirements addItemStackVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ITEMSTACK)) {
            ItemStack itemStack = rewardArgs.getItemStack();
            this.args.getVariableArgs().addAll(DefaultVariables.itemStack(itemStack));
        }
        return this;
    }

    public Requirements addBlockVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.BLOCK)) {
            Block block = rewardArgs.getBlock();
            this.args.getVariableArgs().addAll(DefaultVariables.block(block));
        }
        return this;
    }

    public Requirements addArmorVariables() {
        this.args.getVariableArgs().addAll(DefaultVariables.armor(armor));
        return this;
    }

    public Requirements addEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ENTITY)) {
            Entity entity = rewardArgs.getEntity();
            this.args.getVariableArgs().addAll(DefaultVariables.entity(entity));
        }
        return this;
    }

    public Requirements addLivingEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.LIVING_ENTITY)) {
            LivingEntity entity = rewardArgs.getLivingEntity();
            this.args.getVariableArgs().addAll(DefaultVariables.livingEntity(entity));
        }
        return this;
    }

    public Requirements addCommandArgsVariables() {
        CommandArgs commandArgs = this.args.getCommandArgs();
        if (this.args.getCommandArgs().hasArgs()) {
            int size = commandArgs.getArgs().size();
            for (int i = 0; i < size; i++) {
                this.args.getVariableArgs().add(new VariableArg("%args_" + (i+1)  + "%", commandArgs.getArg(i)));
            }
        }
        return this;
    }


    public Requirements check() {
        List<MoAction> actionList = configArmor.getActions().getDefaultActions().getActions();
        List<VariableArg> variables = args.getVariableArgs();

        UUID uuid = player.getUniqueId();

        if (!actionList.isEmpty()) {
            for (MoAction vAction : actionList) {
                if (!vAction.isCancelled()) {
                    String code = configArmor.getArmorIdentifier().getCode();
                    String actionName = vAction.getActionName();
                    MoCooldown cooldown = vAction.getCooldown();
                    EventType requirementEventType = vAction.getEventType();
                    if (requirementEventType == eventType) {
                        MoRequirements requirements = vAction.getRequirements();
                        List<MoRequirement> requirementList = requirements.getRequirements();

                        List<VariableArg> actionVariables = new ArrayList<>();
                        String cooldownCode = Config.PLUGIN_NAME+"::"+uuid+"::"+code+"::"+actionName;
                        long cooldownInSeconds = cooldown.getCooldown();
                        actionVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownInSeconds(cooldownCode, cooldownInSeconds)));
                        actionVariables.add(new VariableArg("%cooldown_formatted%", Cooldown.showCooldownFormatted(cooldownCode, cooldownInSeconds, Config.TIME_FORMAT)));

                        DebugType debugType = DebugType.ACTIONS;
                        UtilString.get("&8--------------------------------").hex().sendMessageInConsole(debugType);
                        UtilString.get("&bType: Normal").hex().sendMessageInConsole(debugType);
                        UtilString.get("&bAction name: " + actionName).hex().sendMessageInConsole(debugType);

                        if (cooldown.isCooldown()) {
                            if (Cooldown.startAndIsOnCooldown(cooldownCode, cooldownInSeconds)) {
                                if (!cooldown.isByPass()) {
                                    UtilString.get("&bStatus: &cIn Cooldown").hex().sendMessageInConsole(debugType);
                                    UtilString.get(cooldown.getMessage()).setVariables(variables).setVariables(actionVariables)
                                            .setPlaceholders(uuid).setTimeFormatter().hex().sendMessage(player);
                                    continue;
                                }
                            }
                        }

                        boolean isAccepted = true;
                        if (!requirementList.isEmpty()) {
                            for (MoRequirement moRequirement : requirementList) {
                                boolean requirementAccepted = false;

                                if (moRequirement.isRequirement(RequirementType.EVENT)) {
                                    RequirementEvent requirementEvent = (RequirementEvent) moRequirement.getRequirement();
                                    List<EntityRequirement> entityRequirementList = requirementEvent.getRequirements();

                                    if (requirementEventType.hasEntity()) {
                                        if (requirementEvent.hasRequirements()) {
                                            String entity = VariableArg.getValue(variables, "%event_entity%");
                                            String data = VariableArg.getValue(variables, "%event_data%");

                                            boolean containsEntity = EntityRequirement.containsEntity(entityRequirementList, entity, data);
                                            requirementAccepted = containsEntity;
                                            UtilString.get("&bEvent: &7" + eventType.name() + " -> " + entity + ":" + data + " &8 | &a" + containsEntity).hex().sendMessageInConsole(debugType);
                                        } else {
                                            requirementAccepted = true;
                                            UtilString.get("&bEvent: &7No requirements | &a" + true).hex().sendMessageInConsole(debugType);
                                        }
                                    } else {
                                        boolean isEvent = requirementEventType == eventType;
                                        requirementAccepted = isEvent;
                                        UtilString.get("&bEvent: &7No requirements | &a" + isEvent).hex().sendMessageInConsole(debugType);
                                    }
                                } else if (moRequirement.isRequirement(RequirementType.EVAL)) {
                                    RequirementEval requirement = (RequirementEval) moRequirement.getRequirement();
                                    for (String eval : requirement.getRequirements()) {
                                        boolean condition = UtilString.get(eval).setVariables(variables).setVariables(actionVariables)
                                                .setPlaceholders(uuid).setTimeFormatter().hex().evaluateString();

                                        if (condition) {
                                            requirementAccepted = true;
                                            break;
                                        }
                                    }
                                    UtilString.get("&bEval: " + (requirementAccepted ? "&atrue" : "&cfalse")).hex().sendMessageInConsole(debugType);
                                }

                                if (!requirementAccepted) {
                                    isAccepted = false;
                                    break;
                                }
                            }
                        }


                        MoRewards moRewards;
                        if (isAccepted) {
                            moRewards = new MoRewards(vAction.getRewards().getRewards(), null);
                        } else {
                            moRewards = new MoRewards(vAction.getElseRewards().getRewards(), null);
                        }
                        moRewards.addVariables(actionVariables);
                        approvedRewards.add(moRewards);
                        UtilString.get("&bStatus: " + (isAccepted ? "&aApproved" : "&cDisapproved")).hex().sendMessageInConsole(debugType);
                    }
                }
            }
        }
        actionResult = new ActionResult(event, eventType, player, armor, configArmor, args, approvedRewards);
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
