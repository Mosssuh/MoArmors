package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class RewardRespawn implements Listener {
    @EventHandler
    public void playerRespawnReward(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        vArgs args = new vArgs();
        RewardArgs rewardArgs = new RewardArgs(RewardArgsType.NONE);
        args.setRewardArgs(rewardArgs);
        EventType eventType = EventType.PLAYER_RESPAWN;

        int times = 1;
        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
        executor.execute();
    }
}
