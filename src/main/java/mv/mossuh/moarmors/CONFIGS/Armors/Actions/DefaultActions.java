package mv.mossuh.moarmors.CONFIGS.Armors.Actions;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ENUMS.EventType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultActions {
    private List<MoAction> actions = new ArrayList<>();
    private Set<EventType> usedEvents = new HashSet<>();
    public DefaultActions(List<MoAction> actions, Set<EventType> usedEvents) {
        if (actions != null) { this.actions = actions; }
        if (usedEvents != null) { this.usedEvents = usedEvents; }
    }

    public List<MoAction> getActions() { return actions; }
    public boolean hasActions() { return !actions.isEmpty(); }
    public MoAction getAction(String actionName) {
        for (MoAction vAction : actions) {
            String name = vAction.getActionName();
            if (name.equalsIgnoreCase(actionName)) {
                return vAction;
            }
        }
        return new MoAction(null, null, null, null, null, null, null);
    }
    public Set<EventType> getUsedEvents() { return usedEvents; }

    public boolean hasEvent(EventType eventType) {
        return usedEvents.contains(eventType);
    }
    public void addEvent(EventType eventType) {
        usedEvents.add(eventType);
    }
}
