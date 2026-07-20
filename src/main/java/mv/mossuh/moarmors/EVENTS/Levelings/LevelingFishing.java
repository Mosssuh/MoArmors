package mv.mossuh.moarmors.EVENTS.Levelings;

import mv.mossuh.moarmors.ENUMS.ExpType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class LevelingFishing implements Listener {

    @EventHandler
    public void building(PlayerFishEvent event) {
        Player player = event.getPlayer();

        Entity caught = event.getCaught();
        String type = String.valueOf(((org.bukkit.entity.Item) caught).getItemStack().getType());
        short data = ((org.bukkit.entity.Item) caught).getItemStack().getDurability();

        ExpType expType = ExpType.PLAYER_FISH;
        LevelingExecutor executor = new LevelingExecutor(player, expType, type, data, null);
        executor.execute();
    }
}
