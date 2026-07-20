package mv.mossuh.moarmors.ACTIONS;

import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.UTILITIES.vArgs;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private Event event;
    private EventType eventType = EventType.NONE;
    private Player player;
    private Armor armor = new Armor(null, null, null, null);
    private ConfigArmor configArmor = new ConfigArmor(null, null, null, null, null);
    private vArgs args = new vArgs();
    private List<VariableArg> variables = new ArrayList<>();
    private List<MoRewards> approvedRewards = new ArrayList<>();
    private boolean hasRewards = false;
    public ActionResult(Event event, EventType eventType, Player player, Armor armor, ConfigArmor configArmor, vArgs args, List<VariableArg> variables, List<MoRewards> approvedRewards) {
        this.event = event;
        if (eventType != null) { this.eventType = eventType; }
        this.player = player;
        if (armor != null) { this.armor = armor; }
        if (configArmor != null) { this.configArmor = configArmor; }
        if (args != null) { this.args = args; }
        if (variables != null) { this.variables = variables; }
        if (approvedRewards != null) { this.approvedRewards = approvedRewards; }
        if (approvedRewards != null && !approvedRewards.isEmpty()) { this.hasRewards = true; }
    }

    public Event getEvent() { return event; }
    public EventType getEventType() { return eventType; }
    public Player getPlayer() { return player; }
    public Armor getArmor() { return armor; }
    public ConfigArmor getConfigArmor() { return configArmor; }
    public vArgs getArgs() { return args; }
    public List<VariableArg> getVariables() { return variables; }
    public List<MoRewards> getApprovedRewards() { return approvedRewards; }
    public boolean hasApprovedRewards() { return hasRewards; }
}
