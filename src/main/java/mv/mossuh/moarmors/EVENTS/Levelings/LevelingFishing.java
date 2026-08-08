package mv.mossuh.moarmors.EVENTS.Levelings;

import mv.mossuh.moarmors.ENUMS.ExpType;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class LevelingFishing implements Listener {

    @EventHandler
    public void building(PlayerFishEvent event) {
        Player player = event.getPlayer();

        Entity caught = event.getCaught();
        if (!(caught instanceof Item)) return;
        Item item = (Item) caught;

        String type = String.valueOf(item.getItemStack().getType());
        short data = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? item.getItemStack().getData().getData() : -1;

        ExpType expType = ExpType.PLAYER_FISH;
        LevelingExecutor executor = new LevelingExecutor(player, expType, type, data, null);
        executor.execute();
    }
}
