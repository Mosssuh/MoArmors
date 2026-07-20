package mv.mossuh.moarmors.MANAGER;

import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ArmorsManager {
    private static final Set<ArmorPlayer> players = new HashSet<>();

    public ArmorPlayer getPlayer(UUID uuid) {
        if (uuid != null) {
            for (ArmorPlayer player : players) {
                if (player.getUUID() == uuid) {
                    return player;
                }
            }

            ArmorPlayer player = new ArmorPlayer(uuid, null);
            players.add(player);

            return player;
        }
        return new ArmorPlayer(null, null);
    }

    public void addPlayer(ArmorPlayer player) {
        UUID uuid = player.getUUID();
        for (ArmorPlayer p : players) {
            if (p.getUUID() == uuid) {
                players.remove(p);
                break;
            }
        }

        players.add(player);
    }

    public void removePlayer(UUID uuid) {
        for (ArmorPlayer p : players) {
            if (p.getUUID() == uuid) {
                players.remove(p);
                break;
            }
        }
    }
}
