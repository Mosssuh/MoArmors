package mv.mossuh.moarmors.ACTIONS.RewardsUtil;

import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.API.Events.PieceChangeLevelEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ARMORS.ArmorUpdater;
import mv.mossuh.moarmors.ARMORS.Creator.PieceCreator;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class RewardMethods {

    public static void consoleCommand(String command) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
    }

    public static void playerCommand(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.performCommand(command);
        }
    }

    public static void playerCommandAsOP(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isOp()) {
                player.performCommand(command);
            } else {
                player.setOp(true);
                player.performCommand(command);
                player.setOp(false);
            }
        }
    }

    public static void playerMessage(LivingEntity entity, String message) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.sendMessage(message);
        }
    }

    public static void playerTitle(LivingEntity entity, String titleSubtitle) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = titleSubtitle.split("::");
            String title = space[0];
            String subtitle = space[1];
            player.sendTitle(title, subtitle);
        }
    }

    public static void playerSound(LivingEntity entity, String soundString) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = soundString.split("::", 3);
            String sound = space[0];
            float volume = Float.parseFloat(space[1]);
            float pitch = Float.parseFloat(space[2].replace(" ", ""));
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void broadcastTitle(String titleSubtitle) {
        String title = "";
        String subtitle = "";
        String[] space;
        space = titleSubtitle.split("::");
        title = space[0];
        subtitle = space[1];

        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            playerOnline.sendTitle(title, subtitle);
        }
    }

    public static void json(LivingEntity entity, String json) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, ("tellraw " + player.getName() + " " + json));
        }
    }

    public static void jsonBroadcast(String json) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            Bukkit.dispatchCommand(console, ("tellraw " + playerOnline.getName() + " " + json));
        }
    }

    public static void effect(LivingEntity entity, String effectString) {
        if (entity != null) {
            String[] effectSeparate = effectString.replace(" ", "").split("::", 3);
            String effect = effectSeparate[0];
            int duration = Integer.parseInt(effectSeparate[1]) * 20;
            int amplifier = Integer.parseInt(effectSeparate[2]);
            PotionEffect poison = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(effect)), duration, amplifier);
            entity.addPotionEffect(poison, true);
        }
    }

    public static void worldDrop(LivingEntity entity, Location location, String code) {
        if (location != null) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                UUID uuid = player.getUniqueId();
                World world = location.getWorld();
                ItemStack itemStack = PieceCreator.fromReward(code, uuid);
                world.dropItemNaturally(location, itemStack);
            } else {
                World world = location.getWorld();
                ItemStack itemStack = PieceCreator.fromReward(code, null);
                world.dropItemNaturally(location, itemStack);
            }
        }
    }

    public static void giveItem(LivingEntity entity, String code) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();
            ItemStack itemStack = PieceCreator.fromReward(code, uuid);
            int amountToGive = itemStack.getAmount();
            ItemStack cloneStack = itemStack.clone();

            player.updateInventory();

            while (amountToGive > 0) {
                if (player.getInventory().firstEmpty() != -1) {
                    int stackSize = Math.min(amountToGive, cloneStack.getMaxStackSize());
                    cloneStack.setAmount(stackSize);

                    HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(cloneStack);
                    if (remaining.isEmpty()) {
                        amountToGive -= stackSize;
                    } else {
                        amountToGive -= (stackSize - remaining.get(0).getAmount());
                        break;
                    }
                } else {
                    break;
                }
            }

            if (amountToGive > 0) {
                cloneStack.setAmount(amountToGive);
                player.getWorld().dropItemNaturally(player.getLocation(), cloneStack);
            }
        }
    }

    public static void addExp(LivingEntity entity, Piece piece, double exp) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PieceChangeExpEvent event = new PieceChangeExpEvent(player, piece, ExecuteType.REWARDS, ReceiveType.ADD, exp);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) { return; }

            double boost = event.getBoost();
            double newExp = event.getExp() * boost;

            piece.addExp(newExp);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void setExp(LivingEntity entity, Piece piece, double exp) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PieceChangeExpEvent event = new PieceChangeExpEvent(player, piece, ExecuteType.REWARDS, ReceiveType.SET, exp);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) { return; }

            double boost = event.getBoost();
            double newExp = event.getExp() * boost;

            piece.setExp(newExp);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void removeExp(LivingEntity entity, Piece piece, double exp) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            double itemExp = piece.getExp();
            double reducedExp = itemExp - exp;
            double newExp = 0;
            if (reducedExp > 0) {
                newExp = reducedExp;
            }
            piece.setExp(newExp);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void addLevel(LivingEntity entity, Piece piece, int level) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PieceChangeLevelEvent event = new PieceChangeLevelEvent(player, piece, ExecuteType.REWARDS, ReceiveType.ADD, level);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) { return; }

            int maxLevel = piece.getConfigArmor().getUpgrades().getMaxLevel();
            int newLevel = Math.min(event.getLevel(), maxLevel);
            if (newLevel < 0) {
                newLevel = 0;
            }

            piece.addLevel(newLevel);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void setLevel(LivingEntity entity, Piece piece, int level) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            PieceChangeLevelEvent event = new PieceChangeLevelEvent(player, piece, ExecuteType.REWARDS, ReceiveType.SET, level);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) { return; }

            int maxLevel = piece.getConfigArmor().getUpgrades().getMaxLevel();
            int newLevel = Math.min(event.getLevel(), maxLevel);
            if (newLevel < 1) {
                newLevel = 1;
            }

            piece.setLevel(newLevel);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void removeLevel(LivingEntity entity, Piece piece, int level) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            int itemLevel = piece.getLevel();
            int reducedLevel = itemLevel - level;
            int newLevel = Math.max(reducedLevel, 1);
            piece.setLevel(newLevel);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void setVariable(LivingEntity entity, Piece piece, VariableArg variable) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            piece.setVariable(variable);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }

    public static void removeVariable(LivingEntity entity, Piece piece, String variable) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            piece.removeVariable(variable);
            ArmorUpdater.verifyPiece(player, piece, false);
            ArmorUpdater.updatePieceInfo(player, piece, false);
        }
    }


}
