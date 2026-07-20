package mv.mossuh.moarmors.ACTIONS.Booster;

import mv.mossuh.moarmors.ACTIONS.RequirementsUtil.DefaultVariables;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EvalRequirements {
    private boolean approved = false;

    private Player player;
    private Armor armor = new Armor(null, null, null, null);
    private MoRequirements requirements = new MoRequirements(null, null);
    private List<VariableArg> variables = new ArrayList<>();

    public EvalRequirements(Player player, Armor armor, MoRequirements requirements) {
        this.player = player;
        if (armor != null) { this.armor = armor; }
        if (requirements != null) { this.requirements = requirements; }
    }

    public EvalRequirements addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public EvalRequirements addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public EvalRequirements addPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player));
        return this;
    }

    public EvalRequirements addArmorVariables() {
        this.variables.addAll(DefaultVariables.armor(armor));
        return this;
    }

    public EvalRequirements check() {
        UUID uuid = player.getUniqueId();

        List<MoRequirement> vRequirementList = requirements.getRequirements();

        int requirementsAmount = vRequirementList.size();
        int requirementsAccepted = 0;

        if (!vRequirementList.isEmpty()) {
            for (MoRequirement vRequirement : vRequirementList) {
                if (vRequirement.isRequirement(RequirementType.EVAL)) {
                    RequirementEval requirement = (RequirementEval) vRequirement.getRequirement();
                    for (String eval : requirement.getRequirements()) {
                        boolean condition = UtilString.get(eval).hex().setVariables(variables)
                                .setRandomNumberVariable().setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                .setTimeFormatter().evaluateString();

                        if (condition) {
                            requirementsAccepted = requirementsAccepted + 1;
                            break;
                        }
                    }
                }
            }
        }

        if (requirementsAccepted == requirementsAmount) {
            approved = true;
        }
        return this;
    }

    public boolean isApproved() { return approved; }
}
