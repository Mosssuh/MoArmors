package mv.mossuh.moarmors.NBT;

import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import mv.mossuh.mocore.NBT.NBTMethods;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NBTPiece {
    public static void setHeadTexture(ItemStack itemStack, String value) {
        NBTMethods.setHeadTexture(itemStack, value);
    }
    public static void setUUID(ItemStack itemStack) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "UUID", UUID.randomUUID().toString());
    }
    public static void setPieceType(ItemStack itemStack, PieceType pieceType) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "PieceType", pieceType.name());
    }
    public static void setCode(ItemStack itemStack, String code) {
        NBTMethods.setString(itemStack, Config.PLUGIN_NAME + "Code", code);
    }
    public static void setLevel(ItemStack itemStack, Integer level) {
        NBTMethods.setInteger(itemStack, "vItemLevel", level);
    }
    public static void setExp(ItemStack itemStack, Double exp) {
        NBTMethods.setDouble(itemStack, "vItemExp", exp);
    }
    public static void setCost(ItemStack itemStack, Double cost) {
        NBTMethods.setDouble(itemStack, "vItemCost", cost);
    }

    public static void setVariables(ItemStack itemStack, List<VariableArg> variables) {
        NBTMethods.setString(itemStack, "vItemVariables", VariableArg.toString(variables));
    }
    public static String getArgs(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, "vItemArgs");
    }
    public static void setArgs(ItemStack itemStack, String args) {
        NBTMethods.setString(itemStack, "vItemArgs", args);
    }
    public static void setUnique(ItemStack itemStack) {
        NBTMethods.setString(itemStack, "vItemUniqueID", UUID.randomUUID().toString());
    }
    public static void setTags(ItemStack itemStack, String tags) {
        NBTMethods.setString(itemStack, "vItemTags", tags);
    }


    public static UUID getUUID(ItemStack itemStack) {
        String uuidString = NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "UUID");
        if (uuidString == null) { return null; }
        return UUID.fromString(uuidString);
    }
    public static PieceType getPieceType(ItemStack itemStack) {
        return UtilMethods.getPieceType(NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "PieceType"));
    }
    public static String getCode(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, Config.PLUGIN_NAME + "Code");
    }
    public static Integer getLevel(ItemStack itemStack) {
        return NBTMethods.getInteger(itemStack, "vItemLevel");
    }
    public static Double getExp(ItemStack itemStack) {
        return NBTMethods.getDouble(itemStack, "vItemExp");
    }
    public static Double getCost(ItemStack itemStack) {
        return NBTMethods.getDouble(itemStack, "vItemCost");
    }
    public static List<VariableArg> getVariables(ItemStack itemStack) {
        String variablesAsString = NBTMethods.getString(itemStack, "vItemVariables");
        if (variablesAsString != null) { return VariableArg.fromString(variablesAsString); }
        return new ArrayList<>();
    }
    public static Boolean isUnique(ItemStack itemStack) {
        String id = NBTMethods.getString(itemStack, "vItemUniqueID");
        return id != null;
    }
    public static String getTags(ItemStack itemStack) {
        return NBTMethods.getString(itemStack, "vItemTags");
    }
}
