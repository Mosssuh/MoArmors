package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.CONFIGS.Messages;
import mv.mossuh.moarmors.ENUMS.DebugType;
import mv.mossuh.moarmors.DEBUG.Debugs;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import org.bukkit.command.CommandSender;


public class DebugCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("debug")) {
                if (UtilString.get("moarmors.admin").hasPermission(sender)) {
                    if (strings.length >= 2) {
                        if (strings[1].equalsIgnoreCase("actions")) {
                            // /moarmors debug actions
                            Debugs.changeStatus(DebugType.ACTIONS);
                            boolean status = Debugs.getDebug(DebugType.ACTIONS).getStatus();

                            if (status) {
                                UtilString.get("&8[" + Config.PREFIX + "&8] &bActions Debug: &a" + true).hex().sendMessage(sender);
                            } else {
                                UtilString.get("&8[" + Config.PREFIX + "&8] &bActions Debug: &c" + false).hex().sendMessage(sender);
                            }
                        }
                        if (strings[1].equalsIgnoreCase("leveling")) {
                            // /moarmors debug leveling
                            Debugs.changeStatus(DebugType.LEVELING);
                            boolean status = Debugs.getDebug(DebugType.LEVELING).getStatus();

                            if (status) {
                                UtilString.get("&8[" + Config.PREFIX + "&8] &bLeveling Debug: &a" + true).hex().sendMessage(sender);
                            } else {
                                UtilString.get("&8[" + Config.PREFIX + "&8] &bLeveling Debug: &c" + false).hex().sendMessage(sender);
                            }
                        }
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
