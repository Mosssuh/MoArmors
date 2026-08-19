package mv.mossuh.moarmors.ACTIONS;

import mv.mossuh.moarmors.ACTIONS.RewardsUtil.RewardMethods;
import mv.mossuh.moarmors.ACTIONS.RewardsUtil.VariableSeparator;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.Actions;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.MoArmors;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.moarmors.UTILITIES.MoArgs;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.ENUMS.RewardReceiverType;
import mv.mossuh.mocore.ENUMS.RewardType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.REWARDS.RewardReceiver;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Rewards {
    private final MoArmors instance = MoArmors.getInstance();
    private boolean executeDefaultRewards = false;
    private boolean executeArmorRewards = false;
    private boolean executeVariableRewards = false;
    private boolean cancelEvent = false;
    private boolean cancelDrops = false;
    private boolean cancelMessage = false;

    private ActionResult actionResult = new ActionResult(null, null, null, null, null, null, null, null);
    private int times = 1;


    public Rewards(ActionResult actionResult) {
        if (actionResult != null) { this.actionResult = actionResult; }
    }

    public Rewards(ActionResult actionResult, Integer times) {
        if (actionResult != null) { this.actionResult = actionResult; }
        if (times != null) { this.times = times; }
    }

    public boolean cancelDrops() { return cancelDrops; }

    public boolean cancelEvent() { return cancelEvent; }
    public boolean cancelMessage() { return cancelMessage; }


    public Rewards executeDefault() {
        this.executeDefaultRewards = true;
        return this;
    }

    public Rewards executeArmor() {
        this.executeArmorRewards = true;
        return this;
    }

    public Rewards executeVariables() {
        this.executeVariableRewards = true;
        return this;
    }

    public Rewards check() {
        if(!Bukkit.isPrimaryThread()){
            new BukkitRunnable(){
                @Override
                public void run() {
                    for (int i = 0; i < times; i++) {
                        rewards();
                    }
                }
            }.runTask(instance);
        } else{
            for (int i = 0; i < times; i++) {
                rewards();
            }
        }
        return this;
    }

    private void rewards() {
        Event event = actionResult.getEvent();
        EventType eventType = actionResult.getEventType();
        Player player = actionResult.getPlayer();
        Armor armor = actionResult.getArmor();
        List<MoRewards> approvedRewards = actionResult.getApprovedRewards();
        ConfigArmor configArmor = actionResult.getConfigArmor();
        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        Actions actions = configArmor.getActions();

        MoArgs args = actionResult.getArgs();
        List<VariableArg> variables = actionResult.getVariables();

        RewardArgs rewardArgs = args.getRewardArgs();
        RewardArgsType rewardArgsType = rewardArgs.getArgumentType();

        String code = armorIdentifier.getCode();
        List<Piece> pieces = armor.getPieces(code);
        int level = Armor.getTotalLevel(pieces);

        if (actionResult.hasApprovedRewards()) {
            for (MoRewards moRewards : approvedRewards) {
                List<VariableArg> actionVariables = moRewards.getVariables();
                List<MoReward> rewardList = moRewards.getRewards();
                for (MoReward moReward : rewardList) {

                    double rewardChance = Double.parseDouble(moReward.getChance());
                    ChanceType chanceType = moReward.getChanceType();
                    double chance = UtilMethods.transformChance(chanceType, rewardChance, level);
                    double random = UsefulMethods.randomNumber();

                    if (random >= chance) {
                        List<AvailableRewards> availableRewardsList = moReward.getAvailableRewards();
                        if (!availableRewardsList.isEmpty()) {
                            for (AvailableRewards availableRewards : availableRewardsList) {
                                if (availableRewards.hasReward()) {
                                    LivingEntity entityReceiver = null;
                                    Location location = null;
                                    SelectedReward randomSelectedReward = availableRewards.getRandomReward();
                                    RewardReceiver rewardReceiver = randomSelectedReward.getRewardReceiver();
                                    RewardReceiverType rewardReceiverType = rewardReceiver.getRewardReceiverType();
                                    String reward = null;
                                    if (rewardReceiverType.equals(RewardReceiverType.TARGET)) {
                                        if (rewardArgsType.equals(RewardArgsType.LIVING_ENTITY)) {
                                            entityReceiver = rewardArgs.getLivingEntity();
                                        }
                                        location = rewardArgs.getLocation();
                                    } else if (rewardReceiverType.equals(RewardReceiverType.PLACEHOLDER)) {
                                        if (rewardReceiver.hasRewardReceiver()) {
                                            String rewardReceiverString = UtilString.get(rewardReceiver.getRewardReceiver()).setVariables(variables)
                                                    .setVariables(actionVariables).setPlaceholders(player).setTimeFormatter().apply();
                                            Player rewardPlayer = Bukkit.getPlayer(rewardReceiverString);
                                            if (rewardPlayer != null && rewardPlayer.isOnline()) {
                                                entityReceiver = rewardPlayer;
                                                location = entityReceiver.getLocation();
                                            }
                                        }
                                    } else {
                                        entityReceiver = player;
                                        location = player.getLocation();
                                    }
                                    reward = UtilString.get(randomSelectedReward.getReward()).setVariables(variables).setVariables(actionVariables)
                                            .setPlaceholders(player).setTimeFormatter().hex().apply();

                                    EntityType entityReceiverType = EntityType.UNKNOWN;
                                    if (entityReceiver != null) {
                                        entityReceiverType = entityReceiver.getType();
                                    }

                                    RewardType rewardType = randomSelectedReward.getRewardType();
                                    if (reward == null) {
                                        continue;
                                    }

                                    if (executeDefaultRewards) {
                                        if (rewardType.equals(RewardType.CONSOLE_COMMAND)) {
                                            RewardMethods.consoleCommand(reward);
                                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND)) {
                                            RewardMethods.playerCommand(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND_AS_OP)) {
                                            RewardMethods.playerCommandAsOP(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.MESSAGE)) {
                                            RewardMethods.playerMessage(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.TITLE)) {
                                            RewardMethods.playerTitle(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.SOUND)) {
                                            RewardMethods.playerSound(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.BROADCAST_MESSAGE)) {
                                            Bukkit.broadcastMessage(reward);
                                        } else if (rewardType.equals(RewardType.BROADCAST_TITLE)) {
                                            RewardMethods.broadcastTitle(reward);
                                        } else if (rewardType.equals(RewardType.JSON)) {
                                            RewardMethods.json(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.JSON_BROADCAST)) {
                                            RewardMethods.jsonBroadcast(reward);
                                        } else if (rewardType.equals(RewardType.EFFECT)) {
                                            RewardMethods.effect(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.WORLD_DROP)) {
                                            RewardMethods.worldDrop(entityReceiver, location, reward);
                                        } else if (rewardType.equals(RewardType.GIVE_ITEM)) {
                                            RewardMethods.giveItem(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.CANCEL_DROPS)) {
                                            cancelDrops = true;
                                        } else if (rewardType.equals(RewardType.CANCEL_EVENT)) {
                                            cancelEvent = true;
                                        } else if (rewardType.equals(RewardType.EXECUTE_ACTION)) {
                                            if (entityReceiverType.equals(EntityType.PLAYER)) {
                                                MoAction rewardAction = actions.getDefaultActions().getAction(reward);
                                                ExecuteAction executeAction = new ExecuteAction(rewardAction, event, eventType, (Player) entityReceiver, armor, configArmor, args)
                                                        .addVariables(variables).check();
                                                if (executeAction.cancelEvent()) {
                                                    this.cancelEvent = true;
                                                }
                                                if (executeAction.cancelDrops()) {
                                                    this.cancelDrops = true;
                                                }
                                                if (executeAction.cancelMessage()) {
                                                    this.cancelMessage = true;
                                                }
                                            }
                                        } else if (rewardType.equals(RewardType.CANCEL_MESSAGE)) {
                                            cancelMessage = true;
                                        } else if (rewardType.equals(RewardType.convert("SET_DAMAGE"))) {
                                            RewardMethods.setDamage(entityReceiver, Double.parseDouble(reward));
                                        }
                                    }

                                    if (executeArmorRewards || executeVariableRewards) {
                                        Piece pieceReceiver = new Piece(null, null, null, null, null, null, null, null);
                                        if (rewardReceiverType.equals(RewardReceiverType.PLACEHOLDER)) {
                                            PieceType pieceType = UtilMethods.getPieceType(UtilString.get(rewardReceiver.getRewardReceiver()).setVariables(variables)
                                                    .setVariables(actionVariables).setPlaceholders(player).setTimeFormatter().apply());
                                            switch (pieceType) {
                                                case HELMET:
                                                    pieceReceiver = armor.getPiece(PieceType.HELMET);
                                                    break;
                                                case CHESTPLATE:
                                                    pieceReceiver = armor.getPiece(PieceType.CHESTPLATE);
                                                    break;
                                                case LEGGINGS:
                                                    pieceReceiver = armor.getPiece(PieceType.LEGGINGS);
                                                    break;
                                                case BOOTS:
                                                    pieceReceiver = armor.getPiece(PieceType.BOOTS);
                                                    break;
                                            }
                                        }

                                        if (pieceReceiver.isPiece()) {
                                            if (executeArmorRewards) {
                                                if (rewardType.equals(RewardType.ADD_EXP)) {
                                                    if (UtilString.get(reward).isNumeric()) {
                                                        double exp = Double.parseDouble(reward);
                                                        RewardMethods.addExp(player, pieceReceiver, exp);
                                                    }
                                                } else if (rewardType.equals(RewardType.SET_EXP)) {
                                                    if (UtilString.get(reward).isNumeric()) {
                                                        double exp = Double.parseDouble(reward);
                                                        RewardMethods.setExp(player, pieceReceiver, exp);
                                                    }
                                                } else if (rewardType.equals(RewardType.ADD_LEVEL)) {
                                                    if (UtilString.get(reward).isNumeric()) {
                                                        int levelReward = Integer.parseInt(reward);
                                                        RewardMethods.addLevel(player, pieceReceiver, levelReward);
                                                    }
                                                } else if (rewardType.equals(RewardType.SET_LEVEL)) {
                                                    if (UtilString.get(reward).isNumeric()) {
                                                        int levelReward = Integer.parseInt(reward);
                                                        RewardMethods.setLevel(player, pieceReceiver, levelReward);
                                                    }
                                                } else if (rewardType.equals(RewardType.REMOVE_EXP)) {
                                                    if (UtilString.get(reward).isNumeric()) {
                                                        double exp = Double.parseDouble(reward);
                                                        RewardMethods.removeExp(player, pieceReceiver, exp);
                                                    }
                                                } else if (rewardType.equals(RewardType.REMOVE_LEVEL)) {
                                                    if (UtilString.get(reward).isNumeric()) {
                                                        int levelReward = Integer.parseInt(reward);
                                                        RewardMethods.removeLevel(player, pieceReceiver, levelReward);
                                                    }
                                                }
                                            }

                                            if (executeVariableRewards) {
                                                if (pieceReceiver.isPiece()) {
                                                    if (rewardType.equals(RewardType.SET_VARIABLE)) {
                                                        VariableSeparator vs = new VariableSeparator(reward, true);
                                                        VariableArg variable = vs.getVariable();
                                                        if (variable.isVariable() && variable.isValue()) {
                                                            RewardMethods.setVariable(player, pieceReceiver, variable);
                                                        }
                                                    } else if (rewardType.equals(RewardType.REMOVE_VARIABLE)) {
                                                        VariableSeparator vs = new VariableSeparator(reward, false);
                                                        VariableArg variable = vs.getVariable();
                                                        if (variable.isVariable()) {
                                                            RewardMethods.removeVariable(player, pieceReceiver, variable.getVariable());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
