package mv.mossuh.moarmors.CONFIGS.Config;

import mv.mossuh.moarmors.ENUMS.ConflictType;
import mv.mossuh.moarmors.UTILITIES.UtilString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Conflict {
    private ConflictType conflictType = ConflictType.NONE;
    private List<String> conflicts = new ArrayList<>();
    private int limit = 1;
    public Conflict(ConflictType conflictType, List<String> conflicts, Integer limit) {
        if (conflictType != null) { this.conflictType = conflictType; }
        if (conflicts != null) { this.conflicts = conflicts; }
        if (limit != null) { this.limit = limit; }
    }

    public ConflictType getConflictType() { return conflictType; }
    public List<String> getConflicts() { return conflicts; }
    public int getLimit() { return limit; }

    public boolean isConflict() {
        return conflictType != ConflictType.NONE && !conflicts.isEmpty();
    }

    public boolean hasConflict(String conflicted) {
        if (!conflicts.isEmpty()) {
            for (String c : conflicts) {
                if (c.equalsIgnoreCase(conflicted)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasConflicted(String conflicted, List<String> conflictedItems) {
        for (String c : conflictedItems) {
            if (c.equalsIgnoreCase(conflicted)) {
                return true;
            }
        }
        return false;
    }

    public static List<Conflict> loadConflicts(List<String> strings) {
        List<Conflict> conflictList = new ArrayList<>();
        if (strings != null) {
            for (String conflictedItems : strings) {
                // Code: [Split] GintraApple, MagicBeef, Antolph -> 3
                String[] conflictedItemsSplit1 = conflictedItems.split(":", 2);
                if (conflictedItemsSplit1.length != 2) {
                    UtilString.get("&8[" + Config.PREFIX + "&8] &cError when loading the conflicted values: " + conflictedItems).hex().sendMessageInConsole();
                    continue;
                }

                String conflictTypeString = conflictedItemsSplit1[0].replace(" ", "").toUpperCase();
                //GintraApple MagicBeef, Antolph in 3
                //                             [split]
                String[] conflictedItemsSplit2 = conflictedItemsSplit1[1].split("->", 2);

                if (conflictedItemsSplit2.length != 2) {
                    UtilString.get("&8[" + Config.PREFIX + "&8] &cError when loading the conflicted values: " + conflictedItems).hex().sendMessageInConsole();
                    continue;
                }

                ConflictType conflictType = ConflictType.NONE;
                if (conflictTypeString.equalsIgnoreCase("code")) {
                    conflictType = ConflictType.CODE;
                } else if (conflictTypeString.equalsIgnoreCase("tags") || conflictTypeString.equalsIgnoreCase("tag")) {
                    conflictType = ConflictType.TAGS;
                } else {
                    UtilString.get("&8[" + Config.PREFIX + "&8] &cError when loading the conflicted values: " + conflictedItems).hex().sendMessageInConsole();
                    continue;
                }

                String[] conflicts = conflictedItemsSplit2[0].replaceAll(" ", "").split(",");
                String limit = conflictedItemsSplit2[1].replaceAll(" ", "");

                Conflict conflict = new Conflict(conflictType, new ArrayList<>(Arrays.asList(conflicts)), Integer.parseInt(limit));
                conflictList.add(conflict);
            }
        }
        return conflictList;
    }
}
