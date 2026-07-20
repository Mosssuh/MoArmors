package mv.mossuh.moarmors.ARMORS.Armor;

import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil.ArmorIdentifier;
import mv.mossuh.moarmors.ENUMS.PieceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Armor {
    private Piece helmet = new Piece(null, null, null, null, null, null, null, null);
    private Piece chestplate = new Piece(null, null, null, null, null, null, null, null);
    private Piece leggings = new Piece(null, null, null, null, null, null, null, null);
    private Piece boots = new Piece(null, null, null, null, null, null, null, null);

    private List<ConfigArmor> configs = new ArrayList<>();
    public Armor(Piece helmet, Piece chestplate, Piece leggings, Piece boots) {
        if (helmet != null) { this.helmet = helmet; }
        if (chestplate != null) { this.chestplate = chestplate; }
        if (leggings != null) { this.leggings = leggings; }
        if (boots != null) { this.boots = boots; }
        updateConfigs();
    }

    public boolean hasPiece() {
        return helmet.isPiece() || chestplate.isPiece() || leggings.isPiece() || boots.isPiece();
    }

    public List<Piece> getPieces() {
        return new ArrayList<>(Arrays.asList(helmet, chestplate, leggings, boots));
    }
    public Piece getPiece(PieceType pieceType) {
        if (pieceType != null) {
            switch (pieceType) {
                case HELMET:
                    return helmet;
                case CHESTPLATE:
                    return chestplate;
                case LEGGINGS:
                    return leggings;
                case BOOTS:
                    return boots;
            }
        }
        return new Piece(null, null, null, null, null, null, null, null);
    }

    public List<Piece> getPieces(String code) {
        List<Piece> pieces = new ArrayList<>();
        for (Piece piece : getPieces()) {
            if (piece.getConfigArmor().getArmorIdentifier().getCode().equalsIgnoreCase(code)) {
                pieces.add(piece);
            }
        }
        return pieces;
    }

    public void setPiece(Piece piece) {
        if (piece.isPiece()) {
            PieceType pieceType = piece.getPieceType();
            switch (pieceType) {
                case HELMET:
                    this.helmet = piece;
                    updateConfigs();
                    break;
                case CHESTPLATE:
                    this.chestplate = piece;
                    updateConfigs();
                    break;
                case LEGGINGS:
                    this.leggings = piece;
                    updateConfigs();
                    break;
                case BOOTS:
                    this.boots = piece;
                    updateConfigs();
                    break;
            }
        }
    }

    public void removePiece(PieceType pieceType) {
        if (pieceType != null) {
            switch (pieceType) {
                case HELMET:
                    this.helmet = new Piece(null, null, null, null, null, null, null, null);
                    updateConfigs();
                    break;
                case CHESTPLATE:
                    this.chestplate = new Piece(null, null, null, null, null, null, null, null);
                    updateConfigs();
                    break;
                case LEGGINGS:
                    this.leggings = new Piece(null, null, null, null, null, null, null, null);
                    updateConfigs();
                    break;
                case BOOTS:
                    this.boots = new Piece(null, null, null, null, null, null, null, null);
                    updateConfigs();
                    break;
            }
        }
    }

    public void removePiece(Piece piece) {
        PieceType pieceType = piece.getPieceType();
        Piece p = getPiece(pieceType);
        if (p.isSimilar(piece)) {
            removePiece(pieceType);
            updateConfigs();
        }
    }

    public List<ConfigArmor> getUniqueConfigArmors() {
        return configs;
    }



    private void updateConfigs() {
        List<Piece> pieces = getPieces();
        List<String> codes = new ArrayList<>();
        List<ConfigArmor> configArmors = new ArrayList<>();

        firstFor:
        for (Piece piece : pieces) {
            ConfigArmor configArmor = piece.getConfigArmor();
            ArmorIdentifier identifier = configArmor.getArmorIdentifier();
            String code = identifier.getCode();
            for (String c : codes) {
                if (c.equalsIgnoreCase(code)) {
                    continue firstFor;
                }
            }

            codes.add(code);
            configArmors.add(configArmor);
        }
        this.configs = configArmors;
    }

    public static int getTotalLevel(Piece... pieces) {
        int level = 0;
        for (Piece piece : pieces) {
            level += piece.getLevel();
        }
        return level;
    }
    public static int getTotalLevel(List<Piece> pieces) {
        int level = 0;
        for (Piece piece : pieces) {
            level += piece.getLevel();
        }
        return level;
    }
    public static int getTotalLevel(Armor armor) {
        int level = 0;
        if (armor.hasPiece()) {
            List<Piece> pieces = armor.getPieces();
            for (Piece piece : pieces) {
                level += piece.getLevel();
            }
        }
        return level;
    }
}
