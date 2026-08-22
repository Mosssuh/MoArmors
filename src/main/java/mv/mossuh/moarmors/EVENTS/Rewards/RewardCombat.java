package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.MoArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import java.util.UUID;

public class RewardCombat implements Listener {

    @EventHandler
    public void playerKillsReward(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        LivingEntity dead = event.getEntity();
        if (player != null) {
            EventType eventType = EventType.PLAYER_KILLS;

            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, dead);
            args.setRewardArgs(rewardArgs);

            int times = 1;
            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
            executor.execute();
            if (executor.isCancelledDrops()) { event.getDrops().clear(); }
        }
    }


    @EventHandler
    public void playerDieReward(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent causeEvent = player.getLastDamageCause();

        int times = 1;
        EventType eventType = EventType.PLAYER_DIE;
        MoArgs args = new MoArgs();

        if (causeEvent != null) {
            EntityDamageEvent.DamageCause cause = causeEvent.getCause();
            args.addVariableArg(
                    new VariableArg("%cause%", cause.name()),
                    new VariableArg("%base_damage%", causeEvent.getDamage()+""),
                    new VariableArg("%final_damage%", causeEvent.getFinalDamage()+"")
            );
        }

        if (causeEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent byEntityEvent = (EntityDamageByEntityEvent) causeEvent;
            Entity entity = byEntityEvent.getDamager();
            RewardArgs arg = new RewardArgs(RewardArgsType.ENTITY, entity);
            args.setRewardArgs(arg);
        } else if (causeEvent instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent byBlockEvent = (EntityDamageByBlockEvent) causeEvent;
            Block entity = byBlockEvent.getDamager();
            RewardArgs arg = new RewardArgs(RewardArgsType.BLOCK, entity);
            args.setRewardArgs(arg);
        }

        RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
        executor.execute();
        if (executor.isCancelledDrops()) { event.getDrops().clear(); }
    }

    @EventHandler
    public void playerAttackReward(EntityDamageByEntityEvent event) {
        Entity attackerEntity =  event.getDamager();
        Entity attackedEntity = event.getEntity();
        if (attackerEntity instanceof Player) {
            Player player = (Player) attackerEntity;
            EventType eventType = EventType.PLAYER_ATTACK;

            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, attackedEntity);
            args.setRewardArgs(rewardArgs);
            args.addVariableArg(
                    new VariableArg("%cause%", event.getCause().name()),
                    new VariableArg("%base_damage%", event.getDamage()+""),
                    new VariableArg("%final_damage%", event.getFinalDamage()+"")
            );

            int times = 1;
            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerAttackedReward(EntityDamageByEntityEvent event) {
        Entity attackerEntity =  event.getDamager();
        Entity attackedEntity = event.getEntity();
        if (attackedEntity instanceof Player) {
            Player player = (Player) attackedEntity;
            EventType eventType = EventType.convert("PLAYER_DAMAGED", true, false);

            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, attackerEntity);
            args.setRewardArgs(rewardArgs);
            args.addVariableArg(
                    new VariableArg("%cause%", event.getCause().name()),
                    new VariableArg("%base_damage%", event.getDamage()+""),
                    new VariableArg("%final_damage%", event.getFinalDamage()+"")
            );

            int times = 1;
            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }
}
