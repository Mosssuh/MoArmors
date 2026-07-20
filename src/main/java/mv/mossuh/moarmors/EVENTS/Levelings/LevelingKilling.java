package mv.mossuh.moarmors.EVENTS.Levelings;

import mv.mossuh.moarmors.ENUMS.ExpType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class LevelingKilling implements Listener {
    @EventHandler
    public void playerKillsReward(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        LivingEntity dead = event.getEntity();

        if (player != null) {
            String type = String.valueOf(dead.getType());
            short data = -1;

            ExpType expType = ExpType.PLAYER_KILLS;
            LevelingExecutor executor = new LevelingExecutor(player, expType, type, data, null);
            executor.execute();
        }
    }
}
