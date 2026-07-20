package mv.mossuh.moarmors.CONFIGS.Armors.Armor;

import mv.mossuh.moarmors.CONFIGS.Armors.Actions.Actions;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.ExpType;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorInfo;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.Upgrades;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.EntityExp;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.Exp;

import java.util.ArrayList;
import java.util.List;

public class ConfigArmor {
    private ArmorIdentifier armorIdentifier = new ArmorIdentifier();
    private Upgrades upgrades = new Upgrades();
    private ArmorInfo armorInfo = new ArmorInfo();
    private List<Exp> exps = new ArrayList<>();
    private Actions actions = new Actions();
    public ConfigArmor(ArmorIdentifier armorIdentifier, Upgrades upgrades, ArmorInfo armorInfo, List<Exp> exps, Actions actions) {
        if (armorIdentifier != null) {this.armorIdentifier = armorIdentifier; }
        if (upgrades != null) { this.upgrades = upgrades; }
        if (armorInfo != null) { this.armorInfo = armorInfo; }
        if (exps != null) { this.exps = exps; }
        if (actions != null) { this.actions = actions; }
    }
    public ConfigArmor() {}

    public boolean isConfigArmor() {
        return armorInfo.isArmorInfo();
    }

    public ArmorIdentifier getArmorIdentifier() { return armorIdentifier; }
    public Upgrades getUpgrades() { return upgrades; }
    public ArmorInfo getArmorInfo() { return armorInfo; }
    public List<Exp> getExps() {
        return exps;
    }

    public List<EntityExp> getExp(ExpType expType) {
        if (!exps.isEmpty()) {
            for (Exp e : exps) {
                ExpType type = e.getExpType();
                if (type.equals(expType)) {
                    return e.getExpEntities();
                }
            }
        }
        return new ArrayList<>();
    }
    public Actions getActions() {
        return actions;
    }
}
