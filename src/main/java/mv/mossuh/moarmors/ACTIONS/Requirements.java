package mv.mossuh.moarmors.ACTIONS;

import mv.mossuh.moarmors.ACTIONS.RequirementsUtil.DefaultVariables;
import mv.mossuh.moarmors.ACTIONS.RequirementsUtil.Events;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.ENUMS.DebugType;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.moarmors.UTILITIES.vArgs;
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
    private Armor armor = new Armor(null, null, null, null);
    private ConfigArmor configArmor = new ConfigArmor(null, null, null, null, null);
    private vArgs args = new vArgs();
    private List<VariableArg> variables = new ArrayList<>();
    private ActionResult actionResult = new ActionResult(null, null, null, null, null, null, null, null);

    public Requirements(Event event, EventType eventType, Player player, Armor armor, ConfigArmor configArmor, vArgs args) {
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        this.player = player;
        if (armor != null) { this.armor = armor; }
        if (configArmor != null) { this.configArmor = configArmor; }
        if (args != null) { this.args = args; }
    }

    public Requirements addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public Requirements addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public Requirements addPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player));
        return this;
    }

    public Requirements addItemStackVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ITEMSTACK)) {
            ItemStack itemStack = rewardArgs.getItemStack();
            this.variables.addAll(DefaultVariables.itemStack(itemStack));
        }
        return this;
    }

    public Requirements addBlockVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.BLOCK)) {
            Block block = rewardArgs.getBlock();
            this.variables.addAll(DefaultVariables.block(block));
        }
        return this;
    }

    public Requirements addArmorVariables() {
        this.variables.addAll(DefaultVariables.armor(armor));
        return this;
    }

    public Requirements addEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.ENTITY)) {
            Entity entity = rewardArgs.getEntity();
            this.variables.addAll(DefaultVariables.entity(entity));
        }
        return this;
    }

    public Requirements addLivingEntityVariables() {
        RewardArgs rewardArgs = this.args.getRewardArgs();
        if (rewardArgs.getArgumentType().equals(RewardArgsType.LIVING_ENTITY)) {
            LivingEntity entity = rewardArgs.getLivingEntity();
            this.variables.addAll(DefaultVariables.livingEntity(entity));
        }
        return this;
    }

    public Requirements addCommandArgsVariables() {
        CommandArgs commandArgs = this.args.getCommandArgs();
        if (this.args.getCommandArgs().hasArgs()) {
            int size = commandArgs.getArgs().size();
            for (int i = 0; i < size; i++) {
                this.variables.add(new VariableArg("%args_" + (i+1)  + "%", commandArgs.getArg(i)));
            }
        }
        return this;
    }


    public Requirements check() {
        List<MoAction> actionList = configArmor.getActions().getDefaultActions().getActions();

        UUID uuid = player.getUniqueId();

        if (!actionList.isEmpty()) {
            for (MoAction vAction : actionList) {
                if (!vAction.isCancelled()) {
                    String code = configArmor.getArmorIdentifier().getCode();
                    String actionName = vAction.getActionName();
                    MoCooldown cooldown = vAction.getCooldown();
                    EventType requirementEventType = vAction.getEventType();
                    if (requirementEventType.equals(eventType) || requirementEventType.equals(EventType.NONE)) {
                        MoRequirements requirements = vAction.getRequirements();
                        List<MoRequirement> requirementList = requirements.getRequirements();

                        int requirementsAmount = requirementList.size();
                        int requirementsAccepted = 0;

                        List<VariableArg> actionVariables = new ArrayList<>();
                        String cooldownCode = Config.PLUGIN_NAME+"::"+uuid+"::"+code+"::"+actionName;
                        long cooldownInSeconds = cooldown.getCooldown();
                        actionVariables.add(new VariableArg("%cooldown%", Cooldown.showCooldownInSeconds(cooldownCode, cooldownInSeconds)));
                        actionVariables.add(new VariableArg("%cooldown_formatted%", Cooldown.showCooldownFormatted(cooldownCode, cooldownInSeconds, Config.TIME_FORMAT)));

                        DebugType debugType = DebugType.ACTIONS;
                        UtilString.get("&8--------------------------------").hex().sendMessageInConsole(debugType);
                        UtilString.get("&bType: Normal").hex().sendMessageInConsole(debugType);
                        UtilString.get("&bAction name: " + actionName).hex().sendMessageInConsole(debugType);

                        if (!requirementList.isEmpty()) {
                            for (MoRequirement moRequirement : requirementList) {
                                if (moRequirement.isRequirement(RequirementType.EVENT)) {
                                    RequirementEvent requirementEvent = (RequirementEvent) moRequirement.getRequirement();
                                    List<EntityRequirement> entityRequirementList = requirementEvent.getRequirements();

                                    if (Events.from(requirementEventType).containEntity()) {
                                        if (requirementEvent.hasRequirements()) {
                                            String entity = VariableArg.getValue(variables, "%event_entity%");
                                            String data = VariableArg.getValue(variables, "%event_data%");
                                            if (EntityRequirement.containsEntity(entityRequirementList, entity, data)) {
                                                requirementsAccepted = requirementsAccepted + 1;
                                                UtilString.get("&bEvent: &7" + eventType.name() + " -> " + entity + ":" + data + " &8 | &a" + true).hex().sendMessageInConsole(debugType);
                                            } else {
                                                UtilString.get("&bEvent: &7" + eventType.name() + " -> " + entity + ":" + data + " &8 | &c" + false).hex().sendMessageInConsole(debugType);
                                            }
                                        } else {
                                            requirementsAccepted = requirementsAccepted + 1;
                                            UtilString.get("&bEvent: &7No requirements | &a" + true).hex().sendMessageInConsole(debugType);
                                        }
                                    } else {
                                        requirementsAccepted = requirementsAccepted + 1;
                                        UtilString.get("&bEvent: &7No requirements | &a" + true).hex().sendMessageInConsole(debugType);
                                    }
                                } else if (moRequirement.isRequirement(RequirementType.EVAL)) {
                                    RequirementEval requirement = (RequirementEval) moRequirement.getRequirement();
                                    for (String eval : requirement.getRequirements()) {
                                        boolean condition = UtilString.get(eval).hex().setVariables(variables).setVariables(actionVariables)
                                                .setRandomNumberVariable().setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setTimeFormatter().evaluateString();

                                        String value = UtilString.get(eval).hex().setVariables(variables).setVariables(actionVariables)
                                                .setRandomNumberVariable().setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setTimeFormatter().apply();

                                        if (condition) {
                                            UtilString.get(value + " &8| &a" + true).hex().sendMessageInConsole(debugType);
                                            requirementsAccepted = requirementsAccepted + 1;
                                            break;
                                        } else {
                                            UtilString.get(value + " &8| &c" + false).hex().sendMessageInConsole(debugType);
                                        }
                                    }
                                }
                            }
                        }

                        if (cooldown.isCooldown()) {
                            if (Cooldown.startAndIsOnCooldown(cooldownCode, cooldownInSeconds)) {
                                if (!cooldown.isByPass()) {
                                    UtilString.get("&bStatus: &cIn Cooldown").hex().sendMessageInConsole(debugType);
                                    UtilString.get(cooldown.getMessage()).hex().setVariables(variables).setVariables(actionVariables).setRandomNumberVariable().setPlaceholders(uuid)
                                            .setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().sendMessage(player);
                                    continue;
                                }
                            }
                        }

                        if (requirementsAccepted == requirementsAmount) {
                            MoRewards moRewards = new MoRewards(vAction.getRewards().getRewards(), null);
                            moRewards.addVariables(actionVariables);
                            approvedRewards.add(moRewards);
                            UtilString.get("&bStatus: &aApproved").hex().sendMessageInConsole(debugType);
                        } else {
                            UtilString.get("&bStatus: &cDisapproved").hex().sendMessageInConsole(debugType);
                            MoRewards moRewards = new MoRewards(vAction.getElseRewards().getRewards(), null);
                            moRewards.addVariables(actionVariables);
                            approvedRewards.add(moRewards);
                        }
                    }
                }
            }
        }
        actionResult = new ActionResult(event, eventType, player, armor, configArmor, args, variables, approvedRewards);
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
