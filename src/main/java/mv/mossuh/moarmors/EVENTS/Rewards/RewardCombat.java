package mv.mossuh.moarmors.EVENTS.Rewards;

import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class RewardCombat implements Listener {

    @EventHandler
    public void playerKillsReward(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        LivingEntity dead = event.getEntity();
        if (player != null) {
            EventType eventType = EventType.PLAYER_KILLS;

            vArgs args = new vArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, dead);
            args.setRewardArgs(rewardArgs);

            int times = 1;
            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledDrops()) { event.getDrops().clear(); }
        }
    }


    @EventHandler
    public void playerDieReward(EntityDeathEvent event) {
        LivingEntity killer = event.getEntity().getKiller();
        Entity deadEntity = event.getEntity();
        if (deadEntity instanceof Player) {
            Player player = (Player) deadEntity;
            EventType eventType = EventType.PLAYER_DIE;

            vArgs args = new vArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, killer);
            args.setRewardArgs(rewardArgs);

            int times = 1;
            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledDrops()) { event.getDrops().clear(); }
        }
    }

    @EventHandler
    public void playerAttackReward(EntityDamageByEntityEvent event) {
        Entity attackerEntity =  event.getDamager();
        Entity attackedEntity = event.getEntity();
        if (attackerEntity instanceof Player) {
            if (attackedEntity instanceof LivingEntity) {
                LivingEntity attacked = (LivingEntity) attackedEntity;
                Player player = (Player) attackerEntity;
                UUID uuid = player.getUniqueId();
                EventType eventType = EventType.PLAYER_ATTACK;

                vArgs args = new vArgs();
                RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, attacked);
                args.setRewardArgs(rewardArgs);

                int times = 1;
                RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
                executor.execute();
                if (executor.isCancelledEvent()) { event.setCancelled(true); }
            }
        }
    }

    @EventHandler
    public void playerAttackedReward(EntityDamageByEntityEvent event) {
        Entity attackerEntity =  event.getDamager();
        Entity attackedEntity = event.getEntity();
        if (attackedEntity instanceof Player) {
            if (attackerEntity instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) attackerEntity;
                Player player = (Player) attackedEntity;
                EventType eventType = EventType.PLAYER_ATTACKED;

                vArgs args = new vArgs();
                RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, attacker);
                args.setRewardArgs(rewardArgs);

                int times = 1;
                RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
                executor.execute();
                if (executor.isCancelledEvent()) { event.setCancelled(true); }
            }
        }
    }
}
