package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.MoArmors;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    /*

    /moarmors give <code> <piece type> <player>
    /moarmors giveall <code> <piece type>

    /moarmors addexp <piece type> <amount> <player>
    /moarmors setexp <piece type> <amount> <player>

    /moarmors addlevel <piece type> <amount> <player>
    /moarmors setlevel <piece type> <amount> <player>

    /moarmors reload

    /moarmors check
    /moarmors activearmor

     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length > 0) {
            switch (strings[0].toLowerCase()) {
                case "activearmor":
                case "check":
                    ActiveArmorCommands.onCommand(sender, strings);
                    break;
                case "give":
                case "giveall":
                    GiveArmorCommands.onCommand(sender, strings);
                    break;
                case "addexp":
                case "setexp":
                    ExpCommands.onCommand(sender, strings);
                    break;
                case "addlevel":
                case "setlevel":
                    LevelCommands.onCommand(sender, strings);
                    break;
                case "reload":
                    MoArmors.getConfigs().reload();
                    UtilString.get("&8[" + Config.PREFIX + "&8] &aConfiguration reloaded!").hex().sendMessage(sender);
                    break;
                case "debug":
                case "events":
                    DebugCommands.onCommand(sender, strings);
                    break;
            }
        } else {
            if (UsefulMethods.hasPermission(sender, "moarmors.admin")) {
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/moarmors give <code> <piece type> <player>").hex().sendMessage(sender);
                UtilString.get("&b/moarmors giveall <code> <piece type>").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/moarmors addexp <piece type> <amount> <player>").hex().sendMessage(sender);
                UtilString.get("&b/moarmors setexp <piece type> <amount> <player>").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/moarmors addlevel <piece type> <amount> <player>").hex().sendMessage(sender);
                UtilString.get("&b/moarmors setlevel <piece type> <amount> <player>").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/moarmors activearmor").hex().sendMessage(sender);
                UtilString.get("&b/moarmors check").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
            }
        }
        return false;
    }
}
