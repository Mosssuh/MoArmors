package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.CONFIGS.Messages;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ActiveArmorCommands {

    public static void onCommand(CommandSender sender, String[] strings) {


        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("activearmor")) {
                // /moarmors activearmor
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        UUID uuid = player.getUniqueId();

                        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
                        Armor armor = armorPlayer.getArmor();
                        String helmet = armor.getPiece(PieceType.HELMET).getConfigArmor().getArmorIdentifier().getCode();
                        String chestplate = armor.getPiece(PieceType.CHESTPLATE).getConfigArmor().getArmorIdentifier().getCode();
                        String leggings = armor.getPiece(PieceType.LEGGINGS).getConfigArmor().getArmorIdentifier().getCode();
                        String boots = armor.getPiece(PieceType.BOOTS).getConfigArmor().getArmorIdentifier().getCode();

                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&bHelmet: " + helmet).hex().sendMessage(player);
                        UtilString.get("&bChestplate: " + chestplate).hex().sendMessage(player);
                        UtilString.get("&bLeggings: " + leggings).hex().sendMessage(player);
                        UtilString.get("&bBoots: " + boots).hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);

                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cYou can't execute this command in console.").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("check")) {
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        ItemStack itemStack = player.getItemInHand();

                        Piece piece = Piece.getPiece(itemStack);
                        if (!itemStack.getType().equals(Material.AIR)) {
                            ConfigArmor configArmor = piece.getConfigArmor();
                            ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();

                            String code = armorIdentifier.getCode();
                            int level = piece.getLevel();
                            double exp = piece.getExp();
                            double cost = piece.getCost();
                            String tags = armorIdentifier.getTagsAsString();

                            UtilString.get("&r").hex().sendMessage(player);
                            UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                            UtilString.get("&r").hex().sendMessage(player);
                            UtilString.get("&bMaterial: &7" + itemStack.getType().name() + ":" + itemStack.getData().getData()).hex().sendMessage(player);
                            if (piece.isPiece()) {
                                UtilString.get("&bCode: &7" + code).hex().sendMessage(player);
                                UtilString.get("&bLevel: &7" + level).hex().sendMessage(player);
                                UtilString.get("&bExp: &7" + exp).hex().sendMessage(player);
                                UtilString.get("&bCost: &7" + cost).hex().sendMessage(player);
                                UtilString.get("&bTags: &7" + tags).hex().sendMessage(player);
                                if (piece.hasVariables()) {
                                    UtilString.get("&bVariables: " + VariableArg.toString(piece.getVariables())).hex().sendMessage(player);
                                }
                            }
                            UtilString.get("&r").hex().sendMessage(player);
                            UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                            UtilString.get("&r").hex().sendMessage(player);
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cYou can't execute this command in console.").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
