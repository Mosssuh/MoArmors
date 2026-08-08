package mv.mossuh.moarmors.EVENTS.Levelings;

import mv.mossuh.moarmors.ENUMS.ExpType;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LevelingBreaking implements Listener {

    @EventHandler
    public void breaking(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String type = String.valueOf(block.getType());
        short data = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? block.getData() : -1;

        ExpType expType = ExpType.BLOCK_BREAK;
        LevelingExecutor executor = new LevelingExecutor(player, expType, type, data, null);
        executor.execute();
    }
}
