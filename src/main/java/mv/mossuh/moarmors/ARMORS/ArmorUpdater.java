package mv.mossuh.moarmors.ARMORS;

import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.API.Events.PieceChangeLevelEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorInfo;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfo;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.Upgrades;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.CONFIGS.Messages;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArmorUpdater {
    private static final int updateItemInfoInterval = (int) Config.UPDATE_ARMOR_INFO_INTERVAL;

    public static void updateArmorInfo(Player player, Armor armor, boolean force) {
        List<Piece> pieces = armor.getPieces();
        if (force) {
            updaterItemInfo(player, pieces);
        } else {
            if (!Cooldown.startAndIsOnCooldown(Config.PLUGIN_NAME+ "-Armor-ArmorUpdater-updateItemInfo-" + player.getUniqueId(), updateItemInfoInterval)) {
                updaterItemInfo(player, pieces);
            }
        }
    }

    public static void verifyArmor(Player player, Armor armor, boolean force) {
        List<Piece> pieces = armor.getPieces();
        if (force) {
            verifierArmor(player, pieces);
        } else {
            if (!Cooldown.startAndIsOnCooldown(Config.PLUGIN_NAME+ "-Armor-ArmorUpdater-verifyPet-" + player.getUniqueId(), updateItemInfoInterval)) {
                verifierArmor(player, pieces);
            }
        }
    }

    public static void updatePieceInfo(Player player, Piece piece, boolean force) {
        List<Piece> pieces = new ArrayList<>();
        pieces.add(piece);
        String code = piece.getConfigArmor().getArmorIdentifier().getCode();
        String pieceType = piece.getPieceType().name();
        if (force) {
            updaterItemInfo(player, pieces);
        } else {
            if (!Cooldown.startAndIsOnCooldown(Config.PLUGIN_NAME+ "-Piece-PetUpdater-updateItemInfo-" + pieceType + "-" + code + "-" + player.getUniqueId(), updateItemInfoInterval)) {
                updaterItemInfo(player, pieces);
            }
        }
    }

    public static void verifyPiece(Player player, Piece piece, boolean force) {
        List<Piece> pieces = new ArrayList<>();
        pieces.add(piece);
        String code = piece.getConfigArmor().getArmorIdentifier().getCode();
        String pieceType = piece.getPieceType().name();
        if (force) {
            verifierArmor(player, pieces);
        } else {
            if (!Cooldown.startAndIsOnCooldown(Config.PLUGIN_NAME+ "-Piece-ArmorUpdater-verifyPet-" + pieceType + "-" + code + "-" + player.getUniqueId(), updateItemInfoInterval)) {
                verifierArmor(player, pieces);
            }
        }
    }

    private static void updaterItemInfo(Player player, List<Piece> pieces) {
        UUID uuid = player.getUniqueId();

        for (Piece piece : pieces) {
            if (piece.isItemStack()) {
                PieceType pieceType = piece.getPieceType();
                ConfigArmor configArmor = piece.getConfigArmor();
                if (configArmor.isConfigArmor()) {
                    int level = piece.getLevel();
                    int maxlevel = configArmor.getUpgrades().getMaxLevel();
                    double exp = piece.getExp();
                    double cost = piece.getCost();
                    String progressPercentage = UtilMethods.progressPercentage(exp, cost);

                    List<VariableArg> variables = new ArrayList<>();
                    variables.add(new VariableArg("%progress_percentage%", progressPercentage));

                    if (level < maxlevel) {
                        ItemStack itemStack = piece.getItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        ItemInfo itemInfo = configArmor.getArmorInfo().getPieceInfo(pieceType);

                        if (itemInfo.hasName()) {
                            String name = UtilString.get(itemInfo.getName()).hex().setRandomNumberVariable().setVariables(variables).setDefaultVariables(piece)
                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply();
                            itemMeta.setDisplayName(name);
                        }

                        if (itemInfo.hasLore()) {
                            List<String> lore = new ArrayList<>();
                            for (String line : itemInfo.getLore()) {
                                if (line.contains("%progress%")) {
                                    List<String> progressMessage = configArmor.getUpgrades().getProgressMessage();
                                    if (!progressMessage.isEmpty()) {
                                        for (String pLine : progressMessage) {
                                            lore.add(UtilString.get(pLine).hex().setRandomNumberVariable().setVariables(variables).setDefaultVariables(piece)
                                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply());
                                        }
                                    }
                                } else {
                                    lore.add(UtilString.get(line).hex().setRandomNumberVariable().setVariables(variables).setDefaultVariables(piece).setPlaceholders(uuid)
                                            .setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply());
                                }
                            }
                            itemMeta.setLore(lore);
                        }

                        itemStack.setItemMeta(itemMeta);
                    } else {
                        ItemStack itemStack = piece.getItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        ItemInfo itemInfo = configArmor.getArmorInfo().getPieceInfo(pieceType);

                        if (itemInfo.hasName()) {
                            String name = UtilString.get(itemInfo.getName()).hex().setRandomNumberVariable().setVariables(variables).setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder()
                                    .setMathPlaceholder().setTimeFormatter().apply();
                            itemMeta.setDisplayName(name);
                        }

                        if (itemInfo.hasLore()) {
                            List<String> lore = new ArrayList<>();
                            for (String line : itemInfo.getLore()) {
                                if (line.contains("%progress%")) {
                                    List<String> progressMaxedMessage = configArmor.getUpgrades().getMaxedProgressMessage();
                                    if (!progressMaxedMessage.isEmpty()) {
                                        for (String pLine : progressMaxedMessage) {
                                            lore.add(UtilString.get(pLine).hex().setRandomNumberVariable().setVariables(variables).setDefaultVariables(piece)
                                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply());
                                        }
                                    }
                                } else {
                                    lore.add(UtilString.get(line).hex().setRandomNumberVariable().setVariables(variables).setDefaultVariables(piece)
                                            .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply());
                                }
                            }
                            itemMeta.setLore(lore);
                        }

                        itemStack.setItemMeta(itemMeta);
                    }
                }
            }
        }
    }
    private static void verifierArmor(Player player, List<Piece> pieces) {

        UUID uuid = player.getUniqueId();

        firstFor:
        for (Piece piece : pieces) {
            if (piece.isPiece()) {
                PieceType pieceType = piece.getPieceType();
                ConfigArmor configArmor = piece.getConfigArmor();
                Upgrades upgrades = configArmor.getUpgrades();
                ArmorInfo armorInfo = configArmor.getArmorInfo();
                ItemInfo itemInfo = armorInfo.getPieceInfo(pieceType);
                String name = UtilString.get(itemInfo.getName()).hex().apply();

                int maxLevel = upgrades.getMaxLevel();
                double costPerLevel = upgrades.getCostPerLevel();
                double obtainedExp = piece.getExp();
                double obtainedCost = piece.getCost();
                int obtainedLevel = piece.getLevel();

                while (obtainedExp >= obtainedCost && obtainedLevel < maxLevel) {
                    PieceChangeLevelEvent event = new PieceChangeLevelEvent(uuid, piece, ExecuteType.NATURAL, ReceiveType.ADD, 1);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) { continue firstFor; }
                    int level = event.getLevel();

                    int newLevel = obtainedLevel + level;
                    double newCost = newLevel * costPerLevel;
                    double newExp = obtainedExp - obtainedCost;

                    piece.setLevel(newLevel);
                    piece.setExp(newExp);
                    piece.setCost(newCost);

                    obtainedLevel = piece.getLevel();
                    obtainedExp = piece.getExp();
                    obtainedCost = piece.getCost();

                    UtilString.get(Messages.PIECE_LEVEL_UP).hex().replaceString("%piece_name%", name)
                            .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().sendMessage(player);
                }
            }
        }
    }
}
