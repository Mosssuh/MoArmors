package mv.mossuh.moarmors.UTILITIES;


import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoArgs {
    private RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
    private CommandArgs commandArgs = new CommandArgs(null);
    private List<VariableArg> variableArgs = new ArrayList<>();

    public void setRewardArgs(RewardArgs args) {
        if (args != null) { this.rewardArgs = args; }
    }
    public void setCommandArgs(CommandArgs args) {
        if (args != null) { this.commandArgs = args; }
    }
    public void setVariableArgs(List<VariableArg> args) { if (args != null) { this.variableArgs = args; } }
    public void addVariableArg(VariableArg... arg) {
        if (arg != null) {
            this.variableArgs.addAll(Arrays.asList(arg));
        }
    }

    public RewardArgs getRewardArgs() { return rewardArgs; }
    public CommandArgs getCommandArgs() { return commandArgs; }
    public List<VariableArg> getVariableArgs() { return variableArgs; }
}
