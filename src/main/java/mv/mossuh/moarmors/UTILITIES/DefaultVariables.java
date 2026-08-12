package mv.mossuh.moarmors.UTILITIES;

import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.Upgrades;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DefaultVariables {
    
    public static List<VariableArg> player(Player player) {
        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%player%", player.getName()));
        return variables;
    }
    
    public static List<VariableArg> armor(Armor armor) {
        List<VariableArg> variables = new ArrayList<>();
        if (armor == null) return variables;

        List<Piece> pieces = armor.getPieces();
        for (Piece piece : pieces) {
            if (piece.isPiece()) {
                ConfigArmor configArmor = piece.getConfigArmor();
                ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
                String code = armorIdentifier.getCode();
                String tags = armorIdentifier.getTagsAsString();

                String level = piece.getLevel()+"";
                String exp = piece.getExp()+"";
                String cost = piece.getCost()+"";
                String maxLevel = configArmor.getUpgrades().getMaxLevel()+"";

                String pieceType = piece.getPieceType().name().toLowerCase();
                variables.add(new VariableArg("%" + pieceType + "_level%", level));
                variables.add(new VariableArg("%" + pieceType + "_exp%", exp));
                variables.add(new VariableArg("%" + pieceType + "_cost%", cost));
                variables.add(new VariableArg("%" + pieceType + "_max_level%", maxLevel));
                variables.add(new VariableArg("%" + pieceType + "_tags%", tags));
                variables.add(new VariableArg("%" + pieceType + "_code%", code));
                for (VariableArg variable : piece.getVariables()) {
                    variables.add(new VariableArg("%" + pieceType + "_variable_{" + variable.getVariable() + "}%", variable.getValue()));
                }
            }
        }
        return variables;
    }

    public static List<VariableArg> piece(Piece piece) {
        List<VariableArg> variables = new ArrayList<>();
        if (piece == null) return variables;

        ConfigArmor configArmor = piece.getConfigArmor();
        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        String code = armorIdentifier.getCode();
        String tags = armorIdentifier.getTagsAsString();
        String level = piece.getLevel()+"";
        String exp = UsefulMethods.formatNumber(piece.getExp(), 2);
        String cost = UsefulMethods.formatNumber(piece.getCost(), 2);
        String maxLevel = configArmor.getUpgrades().getMaxLevel()+"";

        variables.add(new VariableArg("%level%", level));
        variables.add(new VariableArg("%exp%", exp));
        variables.add(new VariableArg("%cost%", cost));
        variables.add(new VariableArg("%max_level%", maxLevel));
        variables.add(new VariableArg("%tags%", tags));
        variables.add(new VariableArg("%code%", code));
        for (VariableArg v : piece.getVariables()) {
            variables.add(new VariableArg("%variable_{" + v.getVariable() + "}%", v.getValue()));
        }
        return variables;
    }

    public static List<VariableArg> configArmor(ConfigArmor config) {
        List<VariableArg> variables = new ArrayList<>();
        if (config == null) return variables;

        ArmorIdentifier armorIdentifier = config.getArmorIdentifier();
        String level = "1";
        String exp = "0";
        Upgrades upgrades = config.getUpgrades();
        String code = armorIdentifier.getCode();
        String tags = armorIdentifier.getTagsAsString();
        String maxLevel = upgrades.getMaxLevel()+"";
        String cost = upgrades.getCostPerLevel()+"";

        variables.add(new VariableArg("%level%", level));
        variables.add(new VariableArg("%exp%", exp));
        variables.add(new VariableArg("%cost%", cost));
        variables.add(new VariableArg("%max_level%", maxLevel));
        variables.add(new VariableArg("%tags%", tags));
        variables.add(new VariableArg("%code%", code));
        for (VariableArg v : armorIdentifier.getDefaultVariables()) {
            variables.add(new VariableArg("%variable_{" + v.getVariable() + "}%", v.getValue()));
        }
        return variables;
    }

    public static List<VariableArg> itemStack(ItemStack itemStack) {
        List<VariableArg> variables = new ArrayList<>();
        String name = "";
        String material = "";
        byte data = 0;
        String loreString = "";
        short durability = 0;
        int amount = 0;
        if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
            durability = itemStack.getDurability();
            material = itemStack.getType().name();
            data = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? itemStack.getData().getData() : -1;
            amount = itemStack.getAmount();
            if (itemStack.hasItemMeta()) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta.hasDisplayName()) {
                    name = UtilString.get(meta.getDisplayName()).removeColors().apply();
                }
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        String line = lore.get(i);

                        if (i == lore.size() - 1) {
                            loreString = loreString + UtilString.get(line).removeColors().apply();
                        } else {
                            loreString = loreString + UtilString.get(line).removeColors().apply() + " ";
                        }
                    }

                }
            }
        }

        Piece piece = Piece.getPiece(itemStack);
        String isPiece = "false";
        if (piece.isPiece()) { isPiece = "true"; }
        ConfigArmor configArmor = piece.getConfigArmor();
        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        String code = armorIdentifier.getCode();
        String tags = armorIdentifier.getTagsAsString();
        List<VariableArg> pieceVariables = piece.getVariables();

        String level = piece.getLevel()+"";
        String exp = piece.getExp()+"";
        String cost = piece.getCost()+"";
        String maxLevel = configArmor.getUpgrades().getMaxLevel()+"";

        variables.add(new VariableArg("%itemstack_is_piece%", isPiece));
        variables.add(new VariableArg("%itemstack_piece_level%", level));
        variables.add(new VariableArg("%itemstack_piece_exp%", exp));
        variables.add(new VariableArg("%itemstack_piece_cost%", cost));
        variables.add(new VariableArg("%itemstack_piece_max_level%", maxLevel));
        variables.add(new VariableArg("%itemstack_piece_tags%", tags));
        variables.add(new VariableArg("%itemstack_piece_code%", code));
        // %itemstack_vpiece_variable_{<variable}%
        for (VariableArg variable : pieceVariables) {
            variables.add(new VariableArg("%itemstack_vpiece_variable_{" + variable.getVariable() + "}%", variable.getValue()));
        }

        variables.add(new VariableArg("%event_entity%", material));
        variables.add(new VariableArg("%event_data%", data+""));
        variables.add(new VariableArg("%itemstack_material%", material));
        variables.add(new VariableArg("%itemstack_data%", data + ""));
        variables.add(new VariableArg("%itemstack_name%", name));
        variables.add(new VariableArg("%itemstack_lore%", loreString));
        variables.add(new VariableArg("%itemstack_amount%", amount + ""));
        variables.add(new VariableArg("%itemstack_durability%", durability + ""));
        return variables;
    }


    public static List<VariableArg> block(Block block) {
        List<VariableArg> variables = new ArrayList<>();
        if (block != null && !block.getType().equals(Material.AIR)) {
            Location location = block.getLocation();
            String type = block.getType().name();
            String data = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? block.getData()+"" : "-1";
            variables.add(new VariableArg("%event_entity%", type));
            variables.add(new VariableArg("%event_data%", data));
            variables.add(new VariableArg("%block%", type));
            variables.add(new VariableArg("%block_data%", data));
            variables.add(new VariableArg("%block_x%", location.getBlockX() + ""));
            variables.add(new VariableArg("%block_y%", location.getBlockY() + ""));
            variables.add(new VariableArg("%block_z%", location.getBlockZ() + ""));
            variables.add(new VariableArg("%block_world%", location.getWorld().getName()));
        }
        return variables;
    }


    public static List<VariableArg> entity(Entity entity) {
        List<VariableArg> variables = new ArrayList<>();
        if (entity != null) {
            String victimType = entity.getType().name();
            String victimData = "-1";
            String victimName = entity.getName();
            Location location = entity.getLocation();

            variables.add(new VariableArg("%event_entity%", victimType));
            variables.add(new VariableArg("%event_data%", victimData));
            variables.add(new VariableArg("%entity_type%", victimType));
            variables.add(new VariableArg("%entity_name%", victimName));
            variables.add(new VariableArg("%entity_x%", location.getBlockX() + ""));
            variables.add(new VariableArg("%entity_y%", location.getBlockY() + ""));
            variables.add(new VariableArg("%entity_z%", location.getBlockZ() + ""));
            variables.add(new VariableArg("%entity_world%", location.getWorld().getName()));
        }
        return variables;
    }

    public static List<VariableArg> livingEntity(LivingEntity entity) {
        List<VariableArg> variables = new ArrayList<>();
        if (entity != null) {
            String victimType = entity.getType().name();
            String victimData = "-1";
            String victimName = entity.getName();
            Location location = entity.getLocation();

            variables.add(new VariableArg("%event_entity%", victimType));
            variables.add(new VariableArg("%event_data%", victimData));
            variables.add(new VariableArg("%entity_type%", victimType));
            variables.add(new VariableArg("%entity_name%", victimName));
            variables.add(new VariableArg("%entity_x%", location.getBlockX() + ""));
            variables.add(new VariableArg("%entity_y%", location.getBlockY() + ""));
            variables.add(new VariableArg("%entity_z%", location.getBlockZ() + ""));
            variables.add(new VariableArg("%entity_world%", location.getWorld().getName()));
        }
        return variables;
    }
}
