package mv.mossuh.moarmors.CONFIGS.Armors.Actions.MoBoosters;

import mv.mossuh.moarmors.ENUMS.MultiplierType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;

public class LocalBooster {
    private MultiplierType multiplierType = MultiplierType.NONE;
    private BoosterIdentifier identifier = new BoosterIdentifier(Config.PLUGIN_NAME, null, null, null);
    private MoRequirements requirements = new MoRequirements(null, null);
    private double boost = 0;

    public LocalBooster(BoosterIdentifier identifier, MoRequirements requirements, MultiplierType multiplierType, Double boost) {
        if (multiplierType != null) { this.multiplierType = multiplierType; }
        if (requirements != null) { this.requirements = requirements; }
        if (identifier != null) { this.identifier = identifier; }
        if (boost != null) {
            if (boost < 0) {
                this.boost = 0;
            } else {
                this.boost = boost;
            }
        }
    }

    public MultiplierType getMultiplierType() { return multiplierType; }
    public BoosterIdentifier getIdentifier() { return identifier; }
    public MoRequirements getRequirements() { return requirements; }
    public double getBoost() { return boost; }
    public double getBoost(int multiplier) {
        if (multiplierType == MultiplierType.BOOST_PER_LEVEL) {
            return boost * multiplier;
        }
        return boost;
    }

    public boolean isValid() {
        return identifier.getBoosterType() != BoosterType.NONE
                && identifier.getApplicatorType() != ApplicatorType.NONE
                && !identifier.getBoosted().isEmpty()
                && multiplierType != MultiplierType.NONE;
    }
}
