package mv.mossuh.moarmors.ARMORS.Armor;

import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmors;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.NBT.NBTPiece;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Piece {
    private UUID uuid;
    private ItemStack itemStack = new ItemStack(Material.AIR);
    private boolean isItemStack = false;
    private PieceType pieceType = PieceType.NONE;
    private ConfigArmor configArmor = new ConfigArmor(null, null, null, null, null);
    private int level = 0;
    private double exp = 0;
    private double cost = 0;
    private List<VariableArg> variables = new ArrayList<>();
    private String variablesAsString = "";


    public Piece(UUID uuid, ItemStack itemStack, PieceType pieceType, ConfigArmor configArmor, Integer level, Double exp, Double cost, List<VariableArg> variables) {
        this.uuid = uuid;
        if (itemStack != null) {
            this.itemStack = itemStack;
            this.isItemStack = !itemStack.getType().equals(Material.AIR);
        }
        if (pieceType != null) { this.pieceType = pieceType; }
        if (configArmor != null) { this.configArmor = configArmor; }
        if (level != null) { this.level = level; }
        if (exp != null) { this.exp = exp; }
        if (cost != null) { this.cost = cost; }
        if (variables != null) {
            this.variables = variables;
            this.variablesAsString = VariableArg.toString(variables);
        }
    }

    public boolean isPiece() {
        return uuid != null && pieceType != PieceType.NONE && isItemStack && !configArmor.getArmorIdentifier().getCode().equals("invalid");
    }

    public boolean isSimilar(Piece piece) {
        return piece.getConfigArmor().getArmorIdentifier().getCode().equalsIgnoreCase(getConfigArmor().getArmorIdentifier().getCode()) && pieceType == piece.getPieceType();
    }

    public UUID getPieceUUID() { return uuid; }
    public ItemStack getItemStack() { return itemStack; }
    public boolean isItemStack() {
        return isItemStack;
    }
    public PieceType getPieceType() { return pieceType; }
    public ConfigArmor getConfigArmor() { return configArmor; }
    public int getLevel() { return level; }
    public double getExp() { return exp; }
    public double getCost() { return cost; }


    public void setLevel(Integer level) {
        if (level != null) {
            NBTPiece.setLevel(itemStack, level);
            this.level = level;
        }
    }
    public void setExp(Double exp) {
        if (exp != null) {
            NBTPiece.setExp(itemStack, exp);
            this.exp = exp;
        }
    }
    public void setCost(Double cost) {
        if (cost != null) {
            NBTPiece.setCost(itemStack, cost);
            this.cost = cost;
        }
    }


    public void addLevel(Integer level) {
        if (level != null) {
            NBTPiece.setLevel(itemStack, this.level + level);
            this.level = this.level + level;
        }
    }
    public void addExp(Double exp) {
        if (exp != null) {
            NBTPiece.setExp(itemStack, this.exp + exp);
            this.exp = this.exp + exp;
        }
    }


    private static final Map<UUID, Map<String, Double>> expMap = new ConcurrentHashMap<>();

    public void addExp(double exp, boolean cooldown) {
        if (!cooldown) {
            addExp(exp);
            return;
        }

        String code = Config.PLUGIN_NAME+"-"+pieceType+"-"+uuid;
        Map<String, Double> playerExpMap = expMap.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>());
        playerExpMap.compute(code, (key, value) -> (value == null) ? exp : value + exp);

        if (Cooldown.startAndIsOnCooldown(code, 3)) {
            return;
        }

        Map<String, Double> playerExpMapTimer = new ConcurrentHashMap<>(playerExpMap);
        for (Map.Entry<String, Double> entry : playerExpMapTimer.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();

            if (key.equals(code)) {
                addExp(value);
                playerExpMap.remove(key);
                return;
            }
        }
    }

    public boolean hasVariables() { return !variables.isEmpty(); }

    public boolean hasVariable(String variable) {
        for (VariableArg v : variables) {
            if (v.getVariable().equalsIgnoreCase(variable)) {
                return true;
            }
        }
        return false;
    }

    public void setVariable(VariableArg variable) {
        if (variable.isVariable()) {
            variables.removeIf(ve -> ve.getVariable().equalsIgnoreCase(variable.getVariable()));
            variables.add(variable);
            NBTPiece.setVariables(itemStack, variables);
        }
    }
    public void removeVariable(String variable) {
        variables.removeIf(v -> v.getVariable().equalsIgnoreCase(variable));
    }
    public List<VariableArg> getVariables() { return variables; }
    public VariableArg getVariable(String variable) {
        for (VariableArg v : variables) {
            if (v.getVariable().equalsIgnoreCase(variable)) {
                return v;
            }
        }
        return new VariableArg(null, null);
    }
    public String getVariablesAsString() { return variablesAsString; }


    public static Piece getPiece(ItemStack itemStack) {
        if (itemStack != null) {
            UUID uuid = NBTPiece.getUUID(itemStack);
            if (uuid != null) {
                String code = NBTPiece.getCode(itemStack);
                PieceType pieceType = NBTPiece.getPieceType(itemStack);
                ConfigArmor configArmorByCode = ConfigArmors.getConfigArmor(code);
                Integer level = NBTPiece.getLevel(itemStack);
                Double exp = NBTPiece.getExp(itemStack);
                Double cost = NBTPiece.getCost(itemStack);
                List<VariableArg> variables = NBTPiece.getVariables(itemStack);

                return new Piece(uuid, itemStack, pieceType, configArmorByCode, level, exp, cost, variables);
            }
        }
        return new Piece(null, null, null, null, null, null, null, null);
    }
}
