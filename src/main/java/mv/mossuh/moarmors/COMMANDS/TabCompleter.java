package mv.mossuh.moarmors.COMMANDS;

import mv.mossuh.moarmors.MANAGER.ConfigsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("give", "giveall", "addexp", "setexp", "addlevel", "setlevel", "reload", "activearmor", "check");
        } else if (args.length > 1) {
            String arg = args[0].toLowerCase();
            if (args.length == 2) {
                switch (arg) {
                    case "give":
                    case "giveall":
                        return ConfigsManager.getCodes();
                    case "addexp":
                    case "setexp":
                    case "addlevel":
                    case "setlevel":
                        return getPieces();
                }
            } else if (args.length == 3) {
                switch (arg) {
                    case "give":
                    case "giveall":
                        return getPieces();
                    case "addexp":
                    case "setexp":
                    case "addlevel":
                    case "setlevel":
                        return Arrays.asList("1", "2", "3", "4", "5");
                }
            } else if (args.length == 4) {
                switch (arg) {
                    case "give":
                    case "addexp":
                    case "setexp":
                    case "addlevel":
                    case "setlevel":
                        return getPlayers();
                }
            }
        }
        return Collections.emptyList();
    }

    public List<String> getPieces() {
        return Arrays.asList("helmet", "chestplate", "leggings", "boots", "all");
    }

    public List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName());
        }

        return players;
    }
}
