package mv.mossuh.moarmors.CONFIGS.Armors.Actions;


import mv.mossuh.moarmors.CONFIGS.Armors.Actions.MoBoosters.LocalBooster;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;

import java.util.ArrayList;
import java.util.List;

public class Actions {

    private DefaultActions defaultActions = new DefaultActions(null, null);
    private List<LocalBooster> boosters = new ArrayList<>();

    public Actions(DefaultActions defaultActions, List<LocalBooster> boosters) {
        if (defaultActions != null) { this.defaultActions = defaultActions; }
        if (boosters != null) { this.boosters = boosters; }
    }
    public Actions() {}

    public DefaultActions getDefaultActions() { return defaultActions; }
    public List<LocalBooster> getBoosters() { return boosters; }

    public LocalBooster getBooster(BoosterIdentifier identifier) {
        if (!boosters.isEmpty()) {
            for (LocalBooster booster : boosters) {
                if (booster.getIdentifier().equals(identifier)) {
                    return booster;
                }
            }
        }
        return new LocalBooster(null, null, null, null);
    }
    public boolean hasBooster(BoosterIdentifier identifier) {
        if (!boosters.isEmpty()) {
            for (LocalBooster booster : boosters) {
                if (booster.getIdentifier().equals(identifier)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasActions() {
        return defaultActions.hasActions() || !boosters.isEmpty();
    }
}
