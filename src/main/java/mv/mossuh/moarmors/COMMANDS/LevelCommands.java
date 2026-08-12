package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.API.Events.PieceChangeLevelEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ARMORS.ArmorUpdater;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.Upgrades;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.CONFIGS.Messages;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LevelCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("addlevel")) {
                // /moarmors addlevel <piece type> <amount> <player>
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemPiece = strings[1];
                        String itemAmount = strings[2];
                        String itemPlayer = strings[3];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }
                        if (!UsefulMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();
                        int amount = Integer.parseInt(itemAmount);

                        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
                        if (armorPlayer.isPlayer()) {
                            Armor armor = armorPlayer.getArmor();
                            if (itemPiece.equalsIgnoreCase("all")) {
                                List<PieceType> pieces = new ArrayList<>(Arrays.asList(PieceType.HELMET, PieceType.CHESTPLATE, PieceType.LEGGINGS, PieceType.BOOTS));
                                for (PieceType pieceType : pieces) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeLevelEvent event = new PieceChangeLevelEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.ADD, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        Upgrades upgrades = piece.getConfigArmor().getUpgrades();
                                        int maxLevel = upgrades.getMaxLevel();
                                        double cost = upgrades.getCostPerLevel();

                                        int level = Math.min(event.getLevel(), maxLevel);
                                        if (level < 0) { level = 0; }

                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_LEVEL_RECEIVER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_LEVEL_SENDER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(sender);

                                        piece.addLevel(level);
                                        piece.setCost(cost*level);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                }
                            } else {
                                PieceType pieceType = mv.mossuh.moarmors.UTILITIES.UtilMethods.getPieceType(itemPiece);
                                if (pieceType != PieceType.NONE) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeLevelEvent event = new PieceChangeLevelEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.ADD, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        Upgrades upgrades = piece.getConfigArmor().getUpgrades();
                                        int maxLevel = upgrades.getMaxLevel();
                                        double cost = upgrades.getCostPerLevel();

                                        int level = Math.min(event.getLevel(), maxLevel);
                                        if (level < 0) { level = 0; }

                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_LEVEL_RECEIVER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_LEVEL_SENDER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(sender);

                                        piece.addLevel(level);
                                        piece.setCost(cost*level);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                } else {
                                    UtilString.get(Messages.INVALID_PIECE).hex().setPlaceholders(sender).sendMessage(sender);
                                }
                            }
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /moarmors addlevel <piece type> <amount> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("setlevel")) {
                // /moarmors setlevel <piece type> <amount> <player>
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemPiece = strings[1];
                        String itemAmount = strings[2];
                        String itemPlayer = strings[3];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }
                        if (!UsefulMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();
                        int amount = Integer.parseInt(itemAmount);

                        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
                        if (armorPlayer.isPlayer()) {
                            Armor armor = armorPlayer.getArmor();
                            if (itemPiece.equalsIgnoreCase("all")) {
                                List<PieceType> pieces = new ArrayList<>(Arrays.asList(PieceType.HELMET, PieceType.CHESTPLATE, PieceType.LEGGINGS, PieceType.BOOTS));
                                for (PieceType pieceType : pieces) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeLevelEvent event = new PieceChangeLevelEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.SET, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        Upgrades upgrades = piece.getConfigArmor().getUpgrades();
                                        int maxLevel = upgrades.getMaxLevel();
                                        double cost = upgrades.getCostPerLevel();

                                        int level = Math.min(event.getLevel(), maxLevel);
                                        if (level < 1) { level = 1; }


                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_SET_LEVEL_RECEIVER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_SET_LEVEL_SENDER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(sender);

                                        piece.setLevel(level);
                                        piece.setCost(cost*level);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                }
                            } else {
                                PieceType pieceType = mv.mossuh.moarmors.UTILITIES.UtilMethods.getPieceType(itemPiece);
                                if (pieceType != PieceType.NONE) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeLevelEvent event = new PieceChangeLevelEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.SET, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        Upgrades upgrades = piece.getConfigArmor().getUpgrades();
                                        int maxLevel = upgrades.getMaxLevel();
                                        double cost = upgrades.getCostPerLevel();

                                        int level = Math.min(event.getLevel(), maxLevel);
                                        if (level < 1) { level = 1; }


                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_SET_LEVEL_RECEIVER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_SET_LEVEL_SENDER).replaceString("%piece_name%", name).replaceString("%new_level%", level+"")
                                                .setVariables(piece).setVariables(player).setPlaceholders(uuid).hex().sendMessage(sender);

                                        piece.setLevel(level);
                                        piece.setCost(cost*level);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                } else {
                                    UtilString.get(Messages.INVALID_PIECE).hex().setPlaceholders(sender).sendMessage(sender);
                                }
                            }
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /moarmors setlevel <piece type> <amount> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
