package mv.mossuh.moarmors.MANAGER;

import mv.mossuh.moarmors.CONFIGS.Armors.Actions.Actions;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.DefaultActions;
import mv.mossuh.moarmors.CONFIGS.Messages;
import mv.mossuh.moarmors.ENUMS.MultiplierType;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.MoBoosters.LocalBooster;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmors;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.ExpType;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorInfo;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfo;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.Upgrades;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.Enchantments;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.EntityExp;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.Exp;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.MoArmors;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.OtherUtil.MoCooldown;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEvent;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.CONFIG.FolderConfigs;
import mv.mossuh.mocore.CONFIG.MoConfig;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigsManager {
    private static FolderConfigs petsConfigs;
    private static MoConfig mainConfig;
    private static MoConfig informationConfig;
    private static MoConfig messagesConfig;

    public ConfigsManager(MoArmors main) {
        petsConfigs = new FolderConfigs(true, main, "armors");
        mainConfig = new MoConfig("config.yml", main, null);
        informationConfig = new MoConfig("information.yml", main, null);
        messagesConfig = new MoConfig("messages.yml", main, null);

    }

    public void configure() {
        mainConfig.registerConfig(true);
        informationConfig.registerConfig(true);
        messagesConfig.registerConfig(true);
        petsConfigs.configure("Example1.yml");
        Config.load();
        configureArmors();
    }

    public MoConfig getMainConfig() {
        return mainConfig;
    }
    public MoConfig getMessagesConfig() { return messagesConfig; }

    public List<MoConfig> getConfigs() {
        List<MoConfig> configs = new ArrayList<>();

        configs.add(mainConfig);
        configs.addAll(petsConfigs.getConfigs());

        return configs;
    }
    private static final List<String> codes = new ArrayList<>();
    public static List<String> getCodes() {
        return codes;
    }

    public void reload(){
        mainConfig.reloadConfig();
        petsConfigs.reloadConfigs();
        messagesConfig.reloadConfig();

        ConfigArmors.clearConfigArmors();
        Config.load();
        Messages.load();

        configureArmors();
    }

    private void configureArmors() {
        int savedArmors = 0;
        List<MoConfig> configs = getConfigs();

        for (MoConfig configClass : configs) {
            FileConfiguration config = configClass.getConfig();
            if (config.contains("Armors") && !config.getConfigurationSection("Armors").getKeys(false).isEmpty()) {
                for (String code : config.getConfigurationSection("Armors").getKeys(false)) {
                    codes.add(code);
                    if (!config.contains("Armors." + code + ".code")) {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cThe armor couldn't be loaded in " + code + ", due to possible lack of code.").hex().sendMessageInConsole();
                        continue;
                    }

                    String stringTags = config.getString("Armors." + code + ".tags");
                    List<String> tags = UtilMethods.separateString(stringTags);
                    List<String> defaultVariableList = config.getStringList("Armors." + code + ".variables");
                    List<VariableArg> defaultVariables = VariableArg.fromConfig(defaultVariableList);
                    String boosterAvailable = config.getString("Armors." + code + ".booster-identifier");

                    Integer maxLevel = config.getInt("Armors." + code + ".upgrades.max-level");
                    Double costPerLevel = config.getDouble("Armors." + code + ".upgrades.cost-per-level");
                    List<String> progress = config.getStringList("Armors." + code + ".upgrades.message.progress");
                    List<String> maxedProgress = config.getStringList("Armors." + code + ".upgrades.message.maxed-progress");

                    ArmorIdentifier identifier = new ArmorIdentifier(code, tags, defaultVariables, boosterAvailable);
                    Upgrades upgrades = new Upgrades(maxLevel, costPerLevel, progress, maxedProgress);
                    ArmorInfo armorInfo = transformToArmorInfo(code, config);
                    List<Exp> exps = transformToExps(code, config);
                    DefaultActions defaultActions = transformToDefaultActions(code, config);

                    List<LocalBooster> boosters = new ArrayList<>();
                    if (PluginsChecker.isPluginEnabled(PluginType.MoBoosters)) {
                        if (config.contains("Armors." + code + ".actions.boosters")) {
                            for (String boosterSection : config.getConfigurationSection("Armors." + code + ".actions.boosters").getKeys(false)) {
                                MultiplierType multiplierType = UtilMethods.getMultiplierType(config.getString("Armors." + code + ".actions.boosters." + boosterSection + ".multiplier"));
                                BoosterType boosterType = mv.mossuh.moboosters.UTILITIES.UtilMethods.getBoosterType(config.getString("Armors." + code + ".actions.boosters." + boosterSection + ".type"));
                                ApplicatorType applicatorType = mv.mossuh.moboosters.UTILITIES.UtilMethods.getApplicatorType(config.getString("Armors." + code + ".actions.boosters." + boosterSection + ".applicator"));
                                String boosted = config.getString("Armors." + code + ".actions.boosters." + boosterSection + ".boosted");
                                Double boost = config.getDouble("Armors." + code + ".actions.boosters." + boosterSection + ".boost");

                                MoRequirements requirements = transformToRequirements("Armors." + code + ".actions.boosters." + boosterSection + ".requirements", config);

                                LocalBooster booster = new LocalBooster(new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted), requirements, multiplierType, boost);
                                if (!booster.isValid()) {
                                    continue;
                                }
                                boosters.add(booster);
                            }
                        }
                    }

                    Actions actions = new Actions(defaultActions, boosters);

                    ConfigArmor configArmor = new ConfigArmor(identifier, upgrades, armorInfo, exps, actions);
                    ConfigArmors.addArmor(configArmor);
                    savedArmors += 1;
                }
            }
        }

        UtilString.get("&8[" + Config.PREFIX + "&8] &aLoaded &2" + savedArmors + " &aarmors of the configurations!").hex().sendMessageInConsole();
    }

    private static ArmorInfo transformToArmorInfo(String code, FileConfiguration config) {
        ArmorInfo armorInfo = new ArmorInfo();

        if (config.contains("Armors." + code + ".pieces-info") && !config.getConfigurationSection("Armors." + code + ".pieces-info").getKeys(false).isEmpty()) {
            for (String piece : config.getConfigurationSection("Armors." + code + ".pieces-info").getKeys(false)) {
                String material = config.getString("Armors." + code + ".pieces-info." + piece + ".material");
                byte data = (byte) config.getLong("Armors." + code + ".pieces-info." + piece + ".data");
                String name = config.getString("Armors." + code + ".pieces-info." + piece + ".name");
                List<String> lore = config.getStringList("Armors." + code + ".pieces-info." + piece + ".lore");
                Boolean unbreakable = config.getBoolean("Armors." + code + ".pieces-info." + piece + ".unbreakable");
                Boolean unique = config.getBoolean("Armors." + code + ".pieces-info." + piece + ".unique");
                List<String> enchantments = config.getStringList("Armors." + code + ".pieces-info." + piece + ".enchantments");
                List<String> flags = config.getStringList("Armors." + code + ".pieces-info." + piece + ".flags");

                ItemInfo info = new ItemInfo(material, data, name, lore, new Enchantments(enchantments), flags, unbreakable, unique);
                armorInfo.setPieceInfo(UtilMethods.getPieceType(piece), info);
            }
        }
        return armorInfo;
    }

    private static List<Exp> transformToExps(String code, FileConfiguration config) {
        List<Exp> exps = new ArrayList<>();
        if (config.contains("Armors." + code + ".exp")) {
            for (String expTypeString : config.getConfigurationSection("Armors." + code + ".exp").getKeys(false)) {
                ExpType expType = ExpType.valueOf(expTypeString);
                List<String> entitiesAsString = config.getStringList("Armors." + code + ".exp." + expTypeString);
                List<EntityExp> entityExps = EntityExp.stringToEntitiesExp(entitiesAsString);
                Exp exp = new Exp(expType, entitiesAsString, entityExps);
                exps.add(exp);
            }
        }
        return exps;
    }

    private static DefaultActions transformToDefaultActions(String code, FileConfiguration config) {
        List<MoAction> actionList = new ArrayList<>();
        Set<EventType> usedEvents = new HashSet<>();
        if (config.contains("Armors." + code + ".actions.default") && !config.getString("Armors." + code + ".actions.default").isEmpty()) {
            for (String actionName : config.getConfigurationSection("Armors." + code + ".actions.default").getKeys(false)) {

                List<MoRequirement> requirementList = new ArrayList<>();
                boolean cancelAction = config.getBoolean("Armors." + code + ".actions.default." + actionName + ".cancel_action");
                long cooldown = config.getLong("Armors." + code + ".actions.default." + actionName + ".cooldown");
                boolean cooldownByPass = config.getBoolean("Armors." + code + ".actions.default." + actionName + ".cooldown_bypass");
                String cooldownMessage = config.getString("Armors." + code + ".actions.default." + actionName + ".cooldown_message");
                MoCooldown moCooldown = new MoCooldown(cooldown, cooldownByPass, cooldownMessage);

                EventType eventType = UtilMethods.getEventType(config.getString("Armors." + code + ".actions.default." + actionName + ".event"));

                if (config.contains("Armors." + code + ".actions.default." + actionName + ".requirements")) {
                    List<String> requirementsAsString = config.getStringList("Armors." + code + ".actions.default." + actionName + ".requirements");
                    for (String requirementString : requirementsAsString) {
                        String[] requirementSplit = requirementString.split(" ", 2);
                        String requirementTypeAsString = requirementSplit[0].replace("]", "").replace("[", "").toUpperCase();
                        RequirementType requirementType = RequirementType.valueOf(requirementTypeAsString.toUpperCase());
                        String requirement = requirementSplit[1];
                        MoRequirement moRequirementClass = new MoRequirement(null, null);
                        if (requirementType.equals(RequirementType.EVENT)) {
                            moRequirementClass = new MoRequirement(requirementType, RequirementEvent.getRequirementEvent(requirement));
                        } else if (requirementType.equals(RequirementType.EVAL)){
                            moRequirementClass = new MoRequirement(requirementType, RequirementEval.separateEvals(requirement));
                        }
                        requirementList.add(moRequirementClass);
                    }
                }

                List<MoReward> rewardList = new ArrayList<>();

                if (config.contains("Armors." + code + ".actions.default." + actionName + ".rewards")) {
                    List<String> rewardsAsString = config.getStringList("Armors." + code + ".actions.default." + actionName + ".rewards");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100.0";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        rewardList.add(reward);
                    }
                }

                List<MoReward> elseRewardList = new ArrayList<>();

                if (config.contains("Armors." + code + ".actions.default." + actionName + ".else")) {
                    List<String> rewardsAsString = config.getStringList("Armors." + code + ".actions.default." + actionName + ".else");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100.0";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        elseRewardList.add(reward);
                    }
                }

                MoRequirements requirements = new MoRequirements(requirementList, null);
                MoRewards rewards = new MoRewards(rewardList, null);
                MoRewards elseRewards = new MoRewards(elseRewardList, null);
                MoAction moAction = new MoAction(actionName, cancelAction, moCooldown, eventType, requirements, rewards, elseRewards);
                actionList.add(moAction);
                if (eventType != EventType.INVALID) {
                    usedEvents.add(eventType);
                }
            }
        }
        return new DefaultActions(actionList, usedEvents);
    }

    private static MoRequirements transformToRequirements(String path, FileConfiguration config) {
        List<MoRequirement> requirements = new ArrayList<>();
        if (config.contains(path)) {
            List<String> requirementsAsString = config.getStringList(path);
            for (String requirementString : requirementsAsString) {
                String[] requirementSplit = requirementString.split(" ", 2);
                String requirementTypeAsString = requirementSplit[0].replace("]", "").replace("[", "").toUpperCase();
                RequirementType requirementType = RequirementType.valueOf(requirementTypeAsString.toUpperCase());
                String requirement = requirementSplit[1];
                MoRequirement requirementClass = new MoRequirement(null, null);
                if (requirementType.equals(RequirementType.EVENT)) {
                    requirementClass = new MoRequirement(requirementType, RequirementEvent.getRequirementEvent(requirement));
                } else if (requirementType.equals(RequirementType.EVAL)){
                    requirementClass = new MoRequirement(requirementType, RequirementEval.separateEvals(requirement));
                }
                requirements.add(requirementClass);
            }
        }
        return new MoRequirements(requirements, null);
    }

    @NotNull
    private static List<AvailableRewards> transformRandomRewards(String reward) {
        List<AvailableRewards> availableRewards = new ArrayList<>();

        String[] rewardSplitY = reward.split(" && ");

        for (String rewardOptions : rewardSplitY) {
            String[] separatedRandomReward = rewardOptions.split(" \\|\\| ");
            AvailableRewards randomRewards = new AvailableRewards(SelectedReward.transformToRewards(separatedRandomReward));
            availableRewards.add(randomRewards);
        }
        return availableRewards;
    }

}
