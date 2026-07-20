package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class RewardBed implements Listener {
    @EventHandler
    public void bedEnterReward(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        EventType eventType = EventType.PLAYER_BED_ENTER;

        vArgs args = new vArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void bedLeaveReward(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();

        EventType eventType = EventType.PLAYER_BED_LEAVE;

        vArgs args = new vArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);

        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
    }
}
