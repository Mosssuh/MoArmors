package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.ArrayList;
import java.util.List;

public class RewardWorld implements Listener {
    @EventHandler
    public void playerChangedWorldReward(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String fromWorld = event.getFrom().getName();
        String toWorld = event.getPlayer().getWorld().getName();

        vArgs args = new vArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.STRING, toWorld);
        args.setRewardArgs(rewardArgs);
        EventType eventType = EventType.PLAYER_CHANGE_WORLD;

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%to_world%", toWorld));
        variables.add(new VariableArg("%from_world%", fromWorld));
        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
        executor.execute();
    }
}
