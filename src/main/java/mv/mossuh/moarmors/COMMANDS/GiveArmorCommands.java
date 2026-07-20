package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.ARMORS.Creator.PieceCreator;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmors;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.CONFIGS.Messages;
import mv.mossuh.moarmors.NBT.NBTPiece;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GiveArmorCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("give")) {
                // /moarmors give <code> <piece type> <player>
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemCode = strings[1];
                        String itemPiece = strings[2];
                        String itemPlayer = strings[3];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!ConfigArmors.exist(itemCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }
                        if (!UsefulMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();
                        CommandArgs args = new CommandArgs(strings, 4);
                        String argsAsString = args.getArgsAsString();

                        if (itemPiece.equalsIgnoreCase("all")) {
                            List<PieceType> pieces = new ArrayList<>(Arrays.asList(PieceType.HELMET, PieceType.CHESTPLATE, PieceType.LEGGINGS, PieceType.BOOTS));
                            for (PieceType piece : pieces) {
                                ItemStack itemStack = PieceCreator.fromCode(itemCode, piece, args, uuid, 1);
                                NBTPiece.setArgs(itemStack, argsAsString);

                                player.getInventory().addItem(itemStack);
                                UtilString.get(Messages.COMMAND_GIVE_PIECE_SENDER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                        .replaceString("%piece_type%", piece.name()).replaceString("%player%", itemPlayer).setPlaceholders(sender).sendMessage(sender);
                                UtilString.get(Messages.COMMAND_GIVE_PIECE_RECEIVER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                        .replaceString("%piece_type%", piece.name()).setPlaceholders(player).sendMessage(player);
                            }
                        } else {
                            PieceType piece = mv.mossuh.moarmors.UTILITIES.UtilMethods.getPieceType(itemPiece);
                            if (piece != PieceType.NONE) {
                                ItemStack itemStack = PieceCreator.fromCode(itemCode, piece, args, uuid, 1);
                                NBTPiece.setArgs(itemStack, argsAsString);

                                player.getInventory().addItem(itemStack);
                                UtilString.get(Messages.COMMAND_GIVE_PIECE_SENDER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                        .replaceString("%piece_type%", piece.name()).replaceString("%player%", itemPlayer).setPlaceholders(sender).sendMessage(sender);
                                UtilString.get(Messages.COMMAND_GIVE_PIECE_RECEIVER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                        .replaceString("%piece_type%", piece.name()).setPlaceholders(player).sendMessage(player);
                            } else {
                                UtilString.get(Messages.INVALID_PIECE).hex().setPlaceholders(sender).sendMessage(sender);
                            }
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /moarmors give <code> <piece type> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("giveall")) {
                // /moarmors giveall <code> <piece type>
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (strings.length >= 3) {
                        String itemCode = strings[1];
                        String itemPiece = strings[2];
                        if (!ConfigArmors.exist(itemCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }

                        CommandArgs args = new CommandArgs(strings, 3);
                        String argsAsString = args.getArgsAsString();

                        if (itemPiece.equalsIgnoreCase("all")) {
                            List<PieceType> pieces = new ArrayList<>(Arrays.asList(PieceType.HELMET, PieceType.CHESTPLATE, PieceType.LEGGINGS, PieceType.BOOTS));

                            for (Player player : Bukkit.getOnlinePlayers()) {
                                UUID uuid = player.getUniqueId();
                                for (PieceType piece : pieces) {
                                    ItemStack itemStack = PieceCreator.fromCode(itemCode, piece, args, uuid, 1);
                                    NBTPiece.setArgs(itemStack, argsAsString);

                                    player.getInventory().addItem(itemStack);
                                    UtilString.get(Messages.COMMAND_GIVE_PIECE_RECEIVER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                            .replaceString("%piece_type%", piece.name()).setPlaceholders(player).sendMessage(player);
                                }
                            }
                            UtilString.get(Messages.COMMAND_GIVE_ALL_PIECE_SENDER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                    .replaceString("%piece_type%", "ALL").setPlaceholders(sender).sendMessage(sender);
                        } else {
                            PieceType piece = mv.mossuh.moarmors.UTILITIES.UtilMethods.getPieceType(itemPiece);
                            if (piece != PieceType.NONE) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    UUID uuid = player.getUniqueId();
                                    ItemStack itemStack = PieceCreator.fromCode(itemCode, piece, args, uuid, 1);
                                    NBTPiece.setArgs(itemStack, argsAsString);

                                    player.getInventory().addItem(itemStack);
                                    UtilString.get(Messages.COMMAND_GIVE_PIECE_RECEIVER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                            .replaceString("%piece_type%", piece.name()).setPlaceholders(player).sendMessage(player);
                                }
                                UtilString.get(Messages.COMMAND_GIVE_PIECE_SENDER).hex().replaceString("%amount%", "1").replaceString("%code%", itemCode)
                                        .replaceString("%piece_type%", piece.name()).setPlaceholders(sender).sendMessage(sender);
                            } else {
                                UtilString.get(Messages.INVALID_PIECE).hex().setPlaceholders(sender).sendMessage(sender);
                            }
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /moarmors giveall <code> <piece type>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
