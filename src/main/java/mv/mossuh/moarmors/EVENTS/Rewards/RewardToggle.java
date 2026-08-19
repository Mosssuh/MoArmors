package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.MoArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class RewardToggle implements Listener {
    @EventHandler
    public void playerFlyReward(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        EventType eventType;
        if (!player.isFlying()) {
            eventType = EventType.PLAYER_FLY;
        } else {
            eventType = EventType.PLAYER_UNFLY;
        }

        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void playerSneakReward(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        EventType eventType;
        if (!player.isSneaking()) {
            eventType = EventType.PLAYER_SNEAK;
        } else {
            eventType = EventType.PLAYER_UNSNEAK;
        }

        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void playerSprintReward(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        EventType eventType;
        if (!player.isSprinting()) {
            eventType = EventType.PLAYER_SPRINT;
        } else {
            eventType = EventType.PLAYER_UNSPRINT;
        }

        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
