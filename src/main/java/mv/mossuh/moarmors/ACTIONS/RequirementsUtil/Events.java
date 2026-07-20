package mv.mossuh.moarmors.ACTIONS.RequirementsUtil;

import mv.mossuh.mocore.ENUMS.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Events {

    private static List<EventType> eventTypeWithEntitiesList = new ArrayList<>(Arrays.asList(EventType.BLOCK_BREAK, EventType.BLOCK_PLACE, EventType.PLAYER_CAUGHT_FISH,
            EventType.PLAYER_CAUGHT_ENTITY, EventType.PLAYER_KILLS, EventType.PLAYER_DIE, EventType.PLAYER_ATTACK, EventType.PLAYER_ATTACKED, EventType.ITEM_INTERACT,
            EventType.BLOCK_INTERACT, EventType.ENTITY_INTERACT, EventType.ITEM_CONSUME, EventType.ITEM_BREAK,
            EventType.ITEM_PICKUP, EventType.ITEM_DROP, EventType.ITEM_HELD, EventType.PLAYER_EQUIP_PIECE, EventType.PLAYER_UNEQUIP_PIECE, EventType.ITEM_SELECT,
            EventType.ITEM_UNSELECT, EventType.ITEM_ENCHANT, EventType.ITEM_CRAFT));

    private static List<EventType> eventTypeWithoutArgs = new ArrayList<>(Arrays.asList(EventType.EDPRISON_BLOCK_BREAK, EventType.PLAYER_BED_ENTER, EventType.PLAYER_BED_LEAVE,
            EventType.PLAYER_JOIN, EventType.PLAYER_LEAVE, EventType.PLAYER_RESPAWN, EventType.PLAYER_FLY, EventType.PLAYER_UNFLY, EventType.PLAYER_SNEAK, EventType.PLAYER_UNSNEAK,
            EventType.PLAYER_SPRINT, EventType.PLAYER_UNSPRINT, EventType.PLAYER_LEVELUP, EventType.DRAG_AND_DROP_CLAIM, EventType.RIGHT_CLICK_CLAIM));

    private static List<EventType> eventTypeWithArgs = new ArrayList<>(Arrays.asList(EventType.PLAYER_CHANGE_WORLD, EventType.PLAYER_COMMAND, EventType.PLAYER_CHAT));


    private EventType eventType = EventType.NONE;
    private Events(EventType eventType) {
        if (eventType != null) { this.eventType = eventType; }

    }

    public static Events from(EventType eventType) { return new Events(eventType); }

    public boolean containEntity() {
        for (EventType e : eventTypeWithEntitiesList) {
            if (e.equals(eventType)) { return true; }
        }
        return false;
    }

    public boolean noContainArgument() {
        for (EventType e : eventTypeWithoutArgs) {
            if (e.equals(eventType)) { return true; }
        }
        return false;
    }

    public boolean containArgument() {
        for (EventType e : eventTypeWithArgs) {
            if (e.equals(eventType)) { return true; }
        }
        return false;
    }
}
