package mv.mossuh.moarmors.EVENTS.Security;

import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeItem(BlockPlaceEvent event) {
        if (event.isCancelled()) { return; }

        ItemStack itemStack = event.getItemInHand();
        Piece piece = Piece.getPiece(itemStack);
        if (piece.isPiece()) {
            event.setCancelled(true);
        }
    }
}
