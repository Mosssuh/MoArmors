package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.EVENTS.ArmorEquipEvent.ArmorEquipEvent;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RewardEquipUnEquipArmor implements Listener {
    @EventHandler
    public void playerEquipUnEquipPieceReward(ArmorEquipEvent event) {
        Player player = event.getPlayer();
        ItemStack newPiece = event.getNewArmorPiece();
        EventType eventType = EventType.PLAYER_EQUIP_PIECE;
        String equipType = "EQUIP";
        ItemStack newItem = event.getNewArmorPiece();
        ItemStack oldItem = event.getOldArmorPiece();

        ItemStack selectedItem = null;
        if(newItem == null || newItem.getType().name().equals("AIR")) {
            eventType = EventType.PLAYER_UNEQUIP_PIECE;
            equipType = "UNEQUIP";
            selectedItem = oldItem;
        } else {
            selectedItem = newItem;
        }

        String armorType = "";
        if (event.getType() != null) {
            armorType = event.getType().name();
        }


        if (newPiece != null && !newPiece.getType().equals(Material.AIR)) {
            vArgs args = new vArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, selectedItem);
            args.setRewardArgs(rewardArgs);

            List<VariableArg> variables = new ArrayList<>();
            variables.add(new VariableArg("%piece_type%", armorType));
            variables.add(new VariableArg("%equip_type%", equipType));

            int times = 1;
            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }
}
