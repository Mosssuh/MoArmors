package mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo;

import mv.mossuh.moarmors.ENUMS.PieceType;

public class ArmorInfo {
    ItemInfo helmet = new ItemInfo(null, null, null, null, null, null, null, null);
    ItemInfo chestplate = new ItemInfo(null, null, null, null, null, null, null, null);
    ItemInfo leggings = new ItemInfo(null, null, null, null, null, null, null, null);
    ItemInfo boots = new ItemInfo(null, null, null, null, null, null, null, null);

    public ArmorInfo(ItemInfo helmet, ItemInfo chestplate, ItemInfo leggings, ItemInfo boots) {
        if (helmet != null) { this.helmet = helmet; }
        if (chestplate != null) { this.chestplate = chestplate; }
        if (leggings != null) { this.leggings = leggings; }
        if (boots != null) { this.boots = boots; }
    }

    public ArmorInfo() {}

    public ItemInfo getPieceInfo(PieceType pieceType) {
        switch (pieceType) {
            case HELMET:
                return helmet;
            case CHESTPLATE:
                return chestplate;
            case LEGGINGS:
                return leggings;
            case BOOTS:
                return boots;
            default:
                return new ItemInfo(null, null, null, null, null, null, null, null);
        }
    }

    public void setPieceInfo(PieceType pieceType, ItemInfo info) {
        switch (pieceType) {
            case HELMET:
                this.helmet = info;
                break;
            case CHESTPLATE:
                this.chestplate = info;
                break;
            case LEGGINGS:
                this.leggings = info;
                break;
            case BOOTS:
                this.boots = info;
                break;
        }
    }

    public boolean isArmorInfo() {
        return !helmet.getMaterial().equals("AIR") && !chestplate.getMaterial().equals("AIR") && !leggings.getMaterial().equals("AIR") && !boots.getMaterial().equals("AIR");
    }
}
