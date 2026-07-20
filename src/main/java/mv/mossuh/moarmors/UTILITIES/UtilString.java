package mv.mossuh.moarmors.UTILITIES;

import me.clip.placeholderapi.PlaceholderAPI;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.Upgrades;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.ENUMS.DebugType;
import mv.mossuh.moarmors.DEBUG.Debugs;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilString {

    private void replacePattern(Pattern pattern, Function<Matcher, String> replacementFunction) {
        Matcher matcher = pattern.matcher(builder);
        while (matcher.find()) {
            String replacement = replacementFunction.apply(matcher);
            builder.replace(matcher.start(), matcher.end(), replacement);
            matcher.reset(builder); // Reset matcher to avoid index inconsistency
        }
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    private StringBuilder builder;
    private boolean isString = false;
    private UtilString(String string) {
        if (string != null && !string.isEmpty()) { this.isString = true; }
        this.builder = new StringBuilder(string != null ? string : "");
    }

    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString hex() {
        replacePattern(HEX_PATTERN, matcher -> {
            String hexCode = matcher.group();
            String replaceSharp = hexCode.replace('#', 'x');
            StringBuilder hexBuilder = new StringBuilder();
            for (char c : replaceSharp.toCharArray()) {
                hexBuilder.append("&").append(c);
            }
            return hexBuilder.toString();
        });
        builder = new StringBuilder(ChatColor.translateAlternateColorCodes('&', builder.toString()));
        return this;
    }

    public UtilString replaceString(String target, String replacement) {
        UsefulMethods.replacePlaceholder(builder, target, replacement);
        return this;
    }

    public UtilString setPlaceholders(Player player) {
        if (isString) {
            if (player != null && player.isOnline()) {
                String string = builder.toString();
                builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
            }
        }
        return this;
    }

    public UtilString setPlaceholders(CommandSender sender) {
        if (isString) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String string = builder.toString();
                builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
            }
        }
        return this;
    }

    public UtilString setPlaceholders(OfflinePlayer player) {
        if (isString) {
            if (player != null) {
                String string = builder.toString();
                builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
            }
        }
        return this;
    }

    public UtilString setPlaceholders(UUID uuid) {
        if (isString) {
            if (uuid != null) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player player = Bukkit.getPlayer(uuid);
                    String string = builder.toString();
                    builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
                } else {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    String string = builder.toString();
                    builder = new StringBuilder(PlaceholderAPI.setPlaceholders(player, string));
                }
            }
        }
        return this;
    }

    public UtilString setArgs(CommandArgs args) {
        if (args != null && args.hasArgs()) {
            List<String> argList = args.getArgs();
            for (int i = 0; i < argList.size(); i++) {
                UsefulMethods.replacePlaceholder(builder, "%args_" + (i + 1) + "%", argList.get(i));
            }
        }
        return this;
    }

    public UtilString setVariables(List<VariableArg> variables) {
        if (variables != null && !variables.isEmpty()) {
            for (VariableArg variable : variables) {
                if (variable.isVariable() && variable.isValue()) {
                    UsefulMethods.replacePlaceholder(builder, variable.getVariable(), variable.getValue());
                }
            }
        }
        return this;
    }

    public UtilString setRandomNumberVariable() {
        String string = builder.toString();
        builder = new StringBuilder(UsefulMethods.setRandomNumber(string));
        return this;
    }

    public UtilString setMathPlaceholder() {
        if (isString) {
            String string = builder.toString();
            builder = new StringBuilder(UsefulMethods.setMathPlaceholder(string));
        }
        return this;
    }

    public UtilString setChangeOutputPlaceholder() {
        if (isString) {
            String string = builder.toString();
            builder = new StringBuilder(UsefulMethods.setChangeOutputPlaceholder(string));
        }
        return this;
    }

    public UtilString setTimeFormatter() {
        if (isString) {
            String string = builder.toString();
            builder = new StringBuilder(UsefulMethods.setTimeFormatter(string, Config.TIME_FORMAT));
        }
        return this;
    }

    public UtilString removeColors() {
        String string = builder.toString();
        builder = new StringBuilder(ChatColor.stripColor(string));
        return this;
    }

    public boolean evaluateString() {
        return UsefulMethods.evaluateString(builder.toString());
    }

    public boolean isNumeric() {
        return UsefulMethods.isNumeric(builder.toString());
    }

    public UtilString setDefaultVariables(Armor armor) {
        if (!isString || armor == null) {
            return this;
        }
        List<Piece> pieces = armor.getPieces();
        for (Piece piece : pieces) {
            if (piece.isPiece()) {
                ConfigArmor configArmor = piece.getConfigArmor();
                ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
                String code = armorIdentifier.getCode();
                String tags = armorIdentifier.getTagsAsString();
                List<VariableArg> variables = piece.getVariables();

                String level = piece.getLevel() + "";
                String exp = UsefulMethods.formatNumber(piece.getExp(), 2);
                String cost = UsefulMethods.formatNumber(piece.getCost(), 2);
                String maxLevel = configArmor.getUpgrades().getMaxLevel() + "";

                String pieceType = piece.getPieceType().name().toLowerCase();

                UsefulMethods.replacePlaceholder(builder, "%" + pieceType + "_level%", level);
                UsefulMethods.replacePlaceholder(builder, "%" + pieceType + "_exp%", exp);
                UsefulMethods.replacePlaceholder(builder, "%" + pieceType + "_cost%", cost);
                UsefulMethods.replacePlaceholder(builder, "%" + pieceType + "_max_level%", maxLevel);
                UsefulMethods.replacePlaceholder(builder, "%" + pieceType + "_tags%", tags);
                UsefulMethods.replacePlaceholder(builder, "%" + pieceType + "_code%", code);

                for (VariableArg variable : variables) {
                    String placeholder = "%" + pieceType + "_variable_{" + variable.getVariable() + "}%";
                    UsefulMethods.replacePlaceholder(builder, placeholder, variable.getValue());
                }
            }
        }
        return this;
    }

    public UtilString setDefaultVariables(Piece piece) {
        if (!isString || piece == null || !piece.isPiece()) { return this; }

        ConfigArmor configArmor = piece.getConfigArmor();
        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        String code = armorIdentifier.getCode();
        String tags = armorIdentifier.getTagsAsString();
        List<VariableArg> variables = piece.getVariables();

        String level = piece.getLevel()+"";
        String exp = UsefulMethods.formatNumber(piece.getExp(), 2);
        String cost = UsefulMethods.formatNumber(piece.getCost(), 2);
        String maxLevel = configArmor.getUpgrades().getMaxLevel()+"";

        UsefulMethods.replacePlaceholder(builder, "%level%", level);
        UsefulMethods.replacePlaceholder(builder, "%exp%", exp);
        UsefulMethods.replacePlaceholder(builder, "%cost%", cost);
        UsefulMethods.replacePlaceholder(builder, "%max_level%", maxLevel);
        UsefulMethods.replacePlaceholder(builder, "%tags%", tags);
        UsefulMethods.replacePlaceholder(builder, "%code%", code);
        for (VariableArg variable : variables) {
            UsefulMethods.replacePlaceholder(builder, "%variable_{" + variable.getVariable() + "}", variable.getValue());
        }
        return this;
    }

    public UtilString setDefaultVariables(ConfigArmor configArmor) {
        if (!isString || configArmor == null || !configArmor.isConfigArmor()) {
            return this;
        }

        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        Upgrades upgrades = configArmor.getUpgrades();
        String code = armorIdentifier.getCode();
        String tags = armorIdentifier.getTagsAsString();
        List<VariableArg> variables = armorIdentifier.getDefaultVariables();
        String maxLevel = upgrades.getMaxLevel()+"";
        String costPerLevel = upgrades.getCostPerLevel()+"";

        UsefulMethods.replacePlaceholder(builder, "%level%", "1");
        UsefulMethods.replacePlaceholder(builder, "%exp%", "0");
        UsefulMethods.replacePlaceholder(builder, "%cost%", costPerLevel);
        UsefulMethods.replacePlaceholder(builder, "%max_level%", maxLevel);
        UsefulMethods.replacePlaceholder(builder, "%tags%", tags);
        UsefulMethods.replacePlaceholder(builder, "%code%", code);
        for (VariableArg variable : variables) {
            UsefulMethods.replacePlaceholder(builder, "%variable_{" + variable.getVariable() + "}%", variable.getValue());
        }
        return this;
    }

    public String apply() {
        return builder.toString();
    }

    public void sendMessage(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(builder.toString());
        } else {
            Bukkit.getConsoleSender().sendMessage(builder.toString());
        }
    }

    public void sendMessage(Player player) {
        if (isString) {
            if (player != null && player.isOnline()) {
                player.sendMessage(builder.toString());
            }
        }
    }

    public void sendMessage(UUID uuid) {
        if (isString) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.sendMessage(builder.toString());
            }
        }
    }

    public void sendMessageInConsole() {
        Bukkit.getConsoleSender().sendMessage(builder.toString());
    }

    public void sendMessageToOnlinePlayers() {
        if (isString) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(builder.toString());
            }
        }
    }

    public void sendMessageInConsole(DebugType debugType) {
        if (isString) {
            if (Debugs.isActive(debugType)) {
                Bukkit.getConsoleSender().sendMessage(builder.toString());
            }
        }
    }

    public boolean hasPermission(CommandSender sender) {
        if (isString) {
            if (sender != null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    return player.hasPermission(builder.toString());
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasPermission(Player player) {
        if (isString) {
            if (player != null) {
                return player.hasPermission(builder.toString());
            }
        }
        return false;
    }

    public boolean hasPermission(UUID uuid) {
        if (isString) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                return player.hasPermission(builder.toString());
            }
        }
        return false;
    }

    public UtilString setDefaultVariables(Player player) {
        if (isString) {
            if (player != null && player.isOnline()) {
                UsefulMethods.replacePlaceholder(builder, "%player%", player.getName());
            }
        }
        return this;
    }

}
