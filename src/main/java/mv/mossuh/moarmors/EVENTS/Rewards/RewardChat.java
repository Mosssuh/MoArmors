package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class RewardChat implements Listener {
    @EventHandler
    public void playerCommandReward(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        EventType eventType = EventType.PLAYER_COMMAND;

        vArgs args = new vArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.STRING, command);
        args.setRewardArgs(rewardArgs);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%command%", command));
        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void playerChatReward(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        EventType eventType = EventType.PLAYER_CHAT;

        vArgs args = new vArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.STRING, message);
        args.setRewardArgs(rewardArgs);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%message%", message));
        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
