package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ARMORS.ArmorUpdater;
import mv.mossuh.moarmors.ENUMS.PieceType;
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

public class ExpCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("addexp")) {
                // /moarmors addexp <piece type> <amount> <player>
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
                        double amount = Double.parseDouble(itemAmount);

                        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
                        if (armorPlayer.isPlayer()) {
                            Armor armor = armorPlayer.getArmor();
                            if (itemPiece.equalsIgnoreCase("all")) {
                                List<PieceType> pieces = new ArrayList<>(Arrays.asList(PieceType.HELMET, PieceType.CHESTPLATE, PieceType.LEGGINGS, PieceType.BOOTS));
                                for (PieceType pieceType : pieces) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeExpEvent event = new PieceChangeExpEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.ADD, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        double exp = event.getExp();

                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_EXP_RECEIVER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_EXP_SENDER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(sender);

                                        piece.addExp(exp);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                }
                            } else {
                                PieceType pieceType = mv.mossuh.moarmors.UTILITIES.UtilMethods.getPieceType(itemPiece);
                                if (pieceType != PieceType.NONE) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeExpEvent event = new PieceChangeExpEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.ADD, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        double exp = event.getExp();

                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_EXP_RECEIVER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_ADD_EXP_SENDER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(sender);

                                        piece.addExp(exp);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                } else {
                                    UtilString.get(Messages.INVALID_PIECE).hex().setPlaceholders(sender).sendMessage(sender);
                                }
                            }
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /moarmors addexp <piece type> <amount> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("setexp")) {
                // /moarmors setexp <piece type> <amount> <player>
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
                        double amount = Double.parseDouble(itemAmount);

                        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
                        if (armorPlayer.isPlayer()) {
                            Armor armor = armorPlayer.getArmor();
                            if (itemPiece.equalsIgnoreCase("all")) {
                                List<PieceType> pieces = new ArrayList<>(Arrays.asList(PieceType.HELMET, PieceType.CHESTPLATE, PieceType.LEGGINGS, PieceType.BOOTS));
                                for (PieceType pieceType : pieces) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeExpEvent event = new PieceChangeExpEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.SET, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        double exp = event.getExp();

                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_SET_EXP_RECEIVER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_SET_EXP_SENDER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(sender);

                                        piece.setExp(exp);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                }
                            } else {
                                PieceType pieceType = mv.mossuh.moarmors.UTILITIES.UtilMethods.getPieceType(itemPiece);
                                if (pieceType != PieceType.NONE) {
                                    Piece piece = armor.getPiece(pieceType);
                                    if (piece.isPiece()) {
                                        PieceChangeExpEvent event = new PieceChangeExpEvent(uuid, piece, ExecuteType.COMMAND, ReceiveType.SET, amount);
                                        Bukkit.getPluginManager().callEvent(event);

                                        if (event.isCancelled()) { return; }

                                        double exp = event.getExp();

                                        String name = UtilString.get(piece.getConfigArmor().getArmorInfo().getPieceInfo(pieceType).getName()).hex().apply();
                                        UtilString.get(Messages.COMMAND_PIECE_SET_EXP_RECEIVER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(player);
                                        UtilString.get(Messages.COMMAND_PIECE_SET_EXP_SENDER).hex().replaceString("%piece_name%", name).replaceString("%new_exp%", exp + "")
                                                .setRandomNumberVariable().setDefaultVariables(piece).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                                .setDefaultVariables(player).sendMessage(sender);

                                        piece.setExp(exp);
                                        ArmorUpdater.verifyPiece(player, piece, true);
                                        ArmorUpdater.updatePieceInfo(player, piece, true);
                                    }
                                } else {
                                    UtilString.get(Messages.INVALID_PIECE).hex().setPlaceholders(sender).sendMessage(sender);
                                }
                            }
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /moarmors setexp <piece type> <amount> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
