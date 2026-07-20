package mv.mossuh.moarmors.UTILITIES;


import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;

public class vArgs {
    private RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
    private CommandArgs commandArgs = new CommandArgs(null);

    public void setRewardArgs(RewardArgs args) {
        if (args != null) { this.rewardArgs = args; }
    }
    public void setCommandArgs(CommandArgs args) {
        if (args != null) { this.commandArgs = args; }
    }

    public RewardArgs getRewardArgs() { return rewardArgs; }
    public CommandArgs getCommandArgs() { return commandArgs; }
}
