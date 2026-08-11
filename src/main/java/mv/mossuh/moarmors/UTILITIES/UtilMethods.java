package mv.mossuh.moarmors.UTILITIES;

import mv.mossuh.moarmors.ENUMS.DetectorType;
import mv.mossuh.moarmors.ENUMS.EquipType;
import mv.mossuh.moarmors.API.Events.PlayerChangePieceEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ENUMS.MultiplierType;
import mv.mossuh.moarmors.ENUMS.ExpType;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.EventType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.*;

public class UtilMethods {

    public static List<String> separateString(String utilString) {
        if (utilString != null) {
            String[] utilStrings = utilString.replaceAll(" ", "").split("\\|\\|");
            return new ArrayList<>(Arrays.asList(utilStrings));
        }
        return new ArrayList<>();
    }

    public static double transformChance(ChanceType chanceType, double chance, int level) {
        double chanceFormat = chance * 100;
        if (chanceType.equals(ChanceType.CHANCE_PER_LEVEL)) {
            chanceFormat = (chance * level) * 100;
        }
        return 10000 - chanceFormat;
    }

    public static MultiplierType getMultiplierType(String multiplierTypeString) {
        MultiplierType multiplierType = MultiplierType.BASE;
        if (multiplierTypeString != null) {
            switch (multiplierTypeString.toLowerCase()) {
                case "base":
                case "default":
                    return MultiplierType.BASE;
                case "boost_per_level":
                case "multiplier_per_level":
                    return MultiplierType.BOOST_PER_LEVEL;
            }
        }
        return multiplierType;
    }


    public static EventType getEventType(String eventTypeString) {
        EventType eventType = EventType.NONE;

        if (eventTypeString != null) {
            for (EventType et : EventType.values()) {
                if (et.name().equalsIgnoreCase(eventTypeString)) {
                    return et;
                }
            }
            return EventType.INVALID;
        }
        return eventType;
    }

    public static Set<EventType> getEventTypeList(List<String> eventTypesString) {
        Set<EventType> eventTypes = new HashSet<>();

        if (eventTypesString != null) {
            for (String eventTypeString : eventTypesString) {
                EventType eventType = getEventType(eventTypeString);
                if (!eventType.equals(EventType.NONE) && !eventType.equals(EventType.INVALID)) {
                    eventTypes.add(eventType);
                }
            }
        }
        return eventTypes;
    }

    public static ExpType getExpType(String expTypeString) {
        ExpType expType = ExpType.NONE;

        if (expTypeString != null) {
            for (ExpType et : ExpType.values()) {
                if (et.name().equalsIgnoreCase(expTypeString)) {
                    return et;
                }
            }
            return ExpType.NONE;
        }
        return expType;
    }

    public static Set<ExpType> getExpTypeList(List<String> expTypesString) {
        Set<ExpType> expTypes = new HashSet<>();

        if (expTypesString != null) {
            for (String expTypeString : expTypesString) {
                ExpType expType = getExpType(expTypeString);
                if (!expType.equals(ExpType.NONE)) {
                    expTypes.add(expType);
                }
            }
        }
        return expTypes;
    }

    public static PieceType getPieceType(String string) {
        if (string != null) {
            switch (string.toLowerCase()) {
                case "helmet":
                case "h":
                    return PieceType.HELMET;
                case "chestplate":
                case "c":
                    return PieceType.CHESTPLATE;
                case "leggings":
                case "l":
                    return PieceType.LEGGINGS;
                case "boots":
                case "b":
                    return PieceType.BOOTS;
                default:
                    return PieceType.NONE;
            }
        }
        return PieceType.NONE;
    }

    public static void executeChangePieceEvent(UUID uuid, Piece newPiece, Piece oldPiece, DetectorType detectorType) {
        if (newPiece.isPiece() && !oldPiece.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, newPiece, EquipType.EQUIP, detectorType);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        } else if (!newPiece.isPiece() && oldPiece.isPiece()) {
            PlayerChangePieceEvent changePieceEvent = new PlayerChangePieceEvent(uuid, oldPiece, EquipType.UNEQUIP, detectorType);
            Bukkit.getPluginManager().callEvent(changePieceEvent);
        } else if (newPiece.isPiece() && oldPiece.isPiece()) {
            if (newPiece.getPieceUUID() != oldPiece.getPieceUUID()) {
                PlayerChangePieceEvent changePieceEventEquip = new PlayerChangePieceEvent(uuid, newPiece, EquipType.EQUIP, detectorType);
                Bukkit.getPluginManager().callEvent(changePieceEventEquip);

                PlayerChangePieceEvent changePieceEventUnEquip = new PlayerChangePieceEvent(uuid, oldPiece, EquipType.UNEQUIP, detectorType);
                Bukkit.getPluginManager().callEvent(changePieceEventUnEquip);
            }
        }
    }

    public static String progressPercentage(double exp, double cost) {
        Locale locale = new Locale("en", "US");

        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

        double percentage = (exp / cost) * 100;
        return numberFormat.format(percentage).replace("$", "");
    }
}
