package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.MoArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class RewardFishing implements Listener {

    @EventHandler
    public void caughtFishReward(PlayerFishEvent event) {
        Player player = event.getPlayer();
        Entity caught = event.getCaught();
        PlayerFishEvent.State state = event.getState();
        EventType eventType = EventType.NONE;
        if (state.equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            eventType = EventType.PLAYER_CAUGHT_FISH;
        } else if (state.equals(PlayerFishEvent.State.CAUGHT_ENTITY)){
            eventType = EventType.PLAYER_CAUGHT_ENTITY;
        } else {
            eventType = EventType.convert("PLAYER_CAUGHT", true, false);
        }

        int times = 1;
        MoArgs args = new MoArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ENTITY, caught);
        args.setRewardArgs(rewardArgs);

        args.addVariableArg(
                new VariableArg("%state%", state.name())
        );

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }
}
