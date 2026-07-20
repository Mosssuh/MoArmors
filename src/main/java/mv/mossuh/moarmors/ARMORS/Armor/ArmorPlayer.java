package mv.mossuh.moarmors.ARMORS.Armor;

import java.util.UUID;

public class ArmorPlayer {
    private UUID uuid;
    private Armor armor = new Armor(null, null, null, null);

    public ArmorPlayer(UUID uuid, Armor armor) {
        this.uuid = uuid;
        if (armor != null) { this.armor = armor; }
    }


    public boolean isPlayer() { return uuid != null; }
    public boolean hasArmor() {
        return armor.hasPiece();
    }
    public UUID getUUID() { return uuid; }

    public Armor getArmor() { return armor; }
    public void setArmor(Armor armor) {
        if (armor != null) { this.armor = armor; }
    }
}
