package mv.mossuh.moarmors.UTILITIES;

import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.ENUMS.DebugType;
import mv.mossuh.moarmors.DEBUG.Debugs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UtilString extends UsefulString<UtilString> {

    protected UtilString(String string) {
        super(string);
    }

    public static UtilString get(String string) { return new UtilString(string); }

    public UtilString setTimeFormatter() {
        setTimeFormatter(Config.TIME_FORMAT);
        return this;
    }

    public UtilString setVariables(Player player) {
        if (!isString()) return this;
        if (player == null || !player.isOnline()) return this;

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%player%", player.getName()));
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(Armor armor) {
        if (!isString() || armor == null) return this;

        List<VariableArg> variables = DefaultVariables.armor(armor);
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(Piece piece) {
        if (!isString() || piece == null) { return this; }
        List<VariableArg> variables = DefaultVariables.piece(piece);
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(ConfigArmor config) {
        if (!isString() || config == null) return this;

        List<VariableArg> variables = DefaultVariables.configArmor(config);
        setVariables(variables);
        return this;
    }


    public void sendMessageInConsole(DebugType debugType) {
        if (isString()) {
            if (Debugs.isActive(debugType)) {
                Bukkit.getConsoleSender().sendMessage(apply());
            }
        }
    }


}
