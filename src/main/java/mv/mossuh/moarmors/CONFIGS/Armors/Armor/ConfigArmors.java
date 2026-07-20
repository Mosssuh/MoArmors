package mv.mossuh.moarmors.CONFIGS.Armors.Armor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigArmors {
    private static final Set<ConfigArmor> configs = new HashSet<>();

    public static void clearConfigArmors() {
        configs.clear();
    }

    public static List<ConfigArmor> getConfigArmors() {
        return new ArrayList<>(configs);
    }

    public static ConfigArmor getConfigArmor(String code) {

        for (ConfigArmor c : configs) {
            if (c.getArmorIdentifier().getCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return new ConfigArmor();
    }

    public static boolean exist(String code) {
        for (ConfigArmor c : configs) {
            if (c.getArmorIdentifier().getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public static void addArmor(ConfigArmor configArmor) {
        configs.add(configArmor);
    }
}
