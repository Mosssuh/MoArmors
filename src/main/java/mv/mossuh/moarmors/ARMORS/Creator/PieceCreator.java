package mv.mossuh.moarmors.ARMORS.Creator;

import de.tr7zw.nbtapi.NBTItem;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmors;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfo;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.Enchant;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.Enchantments;
import mv.mossuh.moarmors.NBT.NBTPiece;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.VERSION.ServerVersion;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PieceCreator {

    public static ItemStack fromCode(String code, PieceType pieceType, CommandArgs args, UUID uuid, int amount) {
        ConfigArmor configArmor = ConfigArmors.getConfigArmor(code);
        if (configArmor.isConfigArmor()) {
            String material = configArmor.getArmorInfo().getPieceInfo(pieceType).getMaterial();
            if (material.startsWith("basehead-")) {
                return fromHead(configArmor, pieceType, args, uuid, amount);
            } else {
                return fromMaterial(configArmor, pieceType, args, uuid, amount);
            }
        }
        return new ItemStack(Material.AIR, amount);
    }

    public static ItemStack fromConfigPet(ConfigArmor configArmor, PieceType pieceType, CommandArgs args, UUID uuid, int amount) {
        if (configArmor != null && configArmor.isConfigArmor()) {
            String material = configArmor.getArmorInfo().getPieceInfo(pieceType).getMaterial();
            if (material.startsWith("basehead-")) {
                return fromHead(configArmor, pieceType, args, uuid, amount);
            } else {
                return fromMaterial(configArmor, pieceType, args, uuid, amount);
            }
        }
        return new ItemStack(Material.AIR, amount);
    }

    private static ItemStack fromMaterial(ConfigArmor configArmor, PieceType pieceType, CommandArgs args, UUID uuid, int amount) {
        ItemInfo itemInfo = configArmor.getArmorInfo().getPieceInfo(pieceType);
        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        List<VariableArg> defaultVariables = armorIdentifier.getDefaultVariables();
        String material = itemInfo.getMaterial();
        byte data = itemInfo.getMaterialData();
        double cost = configArmor.getUpgrades().getCostPerLevel();

        ItemStack itemStack = new ItemStack(Material.AIR, 1);
        if (material.contains("LEATHER")) {
            String[] itemTypeSeparate = material.replace(" ", "").split("->", 2);
            Material itemMaterial = Material.valueOf(itemTypeSeparate[0]);

            if (itemTypeSeparate.length > 1 && !itemTypeSeparate[1].isEmpty()) {
                String armorColor = itemTypeSeparate[1];

                if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                        itemStack = new ItemStack(itemMaterial, 1);
                } else {
                    if (data != -1) {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, data);
                    } else {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, (byte) 0);
                    }
                }
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                Color color = Color.fromRGB(Integer.parseInt(armorColor, 16));
                leatherArmorMeta.setColor(color);
                itemStack.setItemMeta(leatherArmorMeta);
            } else {
                if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                    itemStack = new ItemStack(itemMaterial, 1);
                } else {
                    if (data != -1) {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, data);
                    } else {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, (byte) 0);
                    }
                }
            }
        } else {
            if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                itemStack = new ItemStack(Material.valueOf(material), 1);
            } else {
                if (data != -1) {
                    itemStack = new ItemStack(Material.valueOf(material), 1, (short) 0, data);
                } else {
                    itemStack = new ItemStack(Material.valueOf(material), 1, (short) 0, (byte) 0);
                }
            }
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        String progressPercentage = UtilMethods.progressPercentage(0, cost);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%progress_percentage%", progressPercentage));

        if (itemInfo.hasName()) {
            String name = UtilString.get(itemInfo.getName()).setArgs(args).setVariables(variables).setVariables(configArmor)
                    .setPlaceholders(uuid).setTimeFormatter().hex().apply();
            itemMeta.setDisplayName(name);
        }
        if (itemInfo.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : itemInfo.getLore()) {
                if (line.contains("%progress%")) {
                    List<String> progressMessage = configArmor.getUpgrades().getProgressMessage();
                    if (!progressMessage.isEmpty()) {
                        for (String pLine : progressMessage) {
                            lore.add(UtilString.get(pLine).setArgs(args).setVariables(variables).setVariables(configArmor)
                                    .setPlaceholders(uuid).setTimeFormatter().hex().apply());
                        }
                    }
                } else {
                    lore.add(UtilString.get(line).setArgs(args).setVariables(variables).setVariables(configArmor)
                            .setPlaceholders(uuid).setTimeFormatter().hex().apply());
                }
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (itemInfo.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        if (itemInfo.isUnbreakable()) {
            NBTItem NBTItem = new NBTItem(itemStack);
            NBTItem.setBoolean("Unbreakable", true);
            itemStack = NBTItem.getItem();
        }

        itemStack.setItemMeta(itemMeta);

        if (itemInfo.isUnique()) {
            NBTPiece.setUnique(itemStack);
        }

        if (armorIdentifier.hasCode()) {
            String code = armorIdentifier.getCode();
            NBTPiece.setUUID(itemStack);
            NBTPiece.setPieceType(itemStack, pieceType);
            NBTPiece.setCode(itemStack, code);
            NBTPiece.setLevel(itemStack, 1);
            NBTPiece.setExp(itemStack, 0.0);
            NBTPiece.setCost(itemStack, cost);

            if (armorIdentifier.hasDefaultVariables()) {
                NBTPiece.setVariables(itemStack, defaultVariables);
            }
            if (armorIdentifier.hasTags()) {
                String tagsAsString  = armorIdentifier.getTagsAsString();
                NBTPiece.setTags(itemStack, tagsAsString);
            }
        }
        itemStack.setAmount(Math.max(amount, 1));
        return itemStack;
    }

    private static ItemStack fromHead(ConfigArmor configArmor, PieceType pieceType, CommandArgs args, UUID uuid, int amount) {
        ItemInfo itemInfo = configArmor.getArmorInfo().getPieceInfo(pieceType);
        ArmorIdentifier armorIdentifier = configArmor.getArmorIdentifier();
        List<VariableArg> defaultVariables = armorIdentifier.getDefaultVariables();
        String material = itemInfo.getMaterial();
        double cost = configArmor.getUpgrades().getCostPerLevel();

        Material skullType;
        if (!ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            skullType = Material.valueOf("SKULL_ITEM");
        } else {
            skullType = Material.PLAYER_HEAD;
        }

        String value = material.replace("basehead-", "");
        ItemStack itemStack;
        if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            itemStack = new ItemStack(skullType, 1);
        } else {
            itemStack =  new ItemStack(skullType, 1, (short) 0, (byte) 3);
        }
        NBTPiece.setHeadTexture(itemStack, value);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();


        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        String progressPercentage = UtilMethods.progressPercentage(0, cost);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%progress_percentage%", progressPercentage));

        if (itemInfo.hasName()) {
            String name = UtilString.get(itemInfo.getName()).setArgs(args).setVariables(variables).setVariables(configArmor)
                    .setPlaceholders(uuid).setTimeFormatter().hex().apply();
            itemMeta.setDisplayName(name);
        }
        if (itemInfo.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : itemInfo.getLore()) {
                if (line.contains("%progress%")) {
                    List<String> progressMessage = configArmor.getUpgrades().getProgressMessage();
                    if (!progressMessage.isEmpty()) {
                        for (String pLine : progressMessage) {
                            lore.add(UtilString.get(pLine).setArgs(args).setVariables(variables).setVariables(configArmor)
                                    .setPlaceholders(uuid).setTimeFormatter().hex().apply());
                        }
                    }
                } else {
                    lore.add(UtilString.get(line).setArgs(args).setVariables(variables).setVariables(configArmor)
                            .setPlaceholders(uuid).setTimeFormatter().hex().apply());
                }
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (itemInfo.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        if (itemInfo.isUnbreakable()) {
            NBTItem NBTItem = new NBTItem(itemStack);
            NBTItem.setBoolean("Unbreakable", true);
            itemStack = NBTItem.getItem();
        }

        itemStack.setItemMeta(itemMeta);

        if (itemInfo.isUnique()) {
            NBTPiece.setUnique(itemStack);
        }

        if (armorIdentifier.hasCode()) {
            String code = armorIdentifier.getCode();
            NBTPiece.setUUID(itemStack);
            NBTPiece.setPieceType(itemStack, pieceType);
            NBTPiece.setCode(itemStack, code);
            NBTPiece.setLevel(itemStack, 1);
            NBTPiece.setExp(itemStack, 0.0);
            NBTPiece.setCost(itemStack, cost);

            if (armorIdentifier.hasDefaultVariables()) {
                NBTPiece.setVariables(itemStack, defaultVariables);
            }
            if (armorIdentifier.hasTags()) {
                String tagsAsString  = armorIdentifier.getTagsAsString();
                NBTPiece.setTags(itemStack, tagsAsString);
            }
        }
        itemStack.setAmount(Math.max(amount, 1));
        return itemStack;
    }

    public static ItemStack fromReward(String itemString, UUID uuid) {
        // HELMET::(code)[amount]
        CommandArgs args = new CommandArgs(null);
        String[] itemStringSplit = itemString.split("::", 2);

        if (itemStringSplit.length == 2) {
            PieceType pieceType = UtilMethods.getPieceType(itemStringSplit[0]);
            String[] itemStringSplit2 = itemStringSplit[1].split("\\[", 2);
            String amountString = "1";
            String code = itemStringSplit2[1];
            if (itemStringSplit2.length == 2) {
                amountString = UtilString.get(itemStringSplit2[1].replaceAll(" ", "").replace("]", "")).setPlaceholders(uuid).apply();
            }

            int amount = (int) Math.round(Double.parseDouble(amountString));

            ConfigArmor configArmor = ConfigArmors.getConfigArmor(code);
            return fromConfigPet(configArmor, pieceType, args, uuid, amount);
        }
        return new ItemStack(Material.AIR);
    }
}
