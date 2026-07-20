package mv.mossuh.moarmors.ARMORS.Armor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArmorGetter {

    public static Armor armor(Player player) {
        Piece helmet = null;
        Piece chestplate = null;
        Piece leggings = null;
        Piece boots = null;

        try {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemHelmet = inventory.getHelmet();
            ItemStack itemChestplate = inventory.getChestplate();
            ItemStack itemLeggings = inventory.getLeggings();
            ItemStack itemBoots = inventory.getBoots();

            Piece pieceHelmet = Piece.getPiece(itemHelmet);
            Piece pieceChestplate = Piece.getPiece(itemChestplate);
            Piece pieceLeggings = Piece.getPiece(itemLeggings);
            Piece pieceBoots = Piece.getPiece(itemBoots);

            if (pieceHelmet.isPiece()) { helmet = pieceHelmet; }
            if (pieceChestplate.isPiece()) { chestplate = pieceChestplate; }
            if (pieceLeggings.isPiece()) { leggings = pieceLeggings; }
            if (pieceBoots.isPiece()) { boots = pieceBoots; }

        } catch (NoSuchMethodError | Exception ignored) {

        }
        return new Armor(helmet, chestplate, leggings, boots);
    }

    public static Piece helmet(Player player) {
        Piece piece = new Piece(null, null, null, null, null, null, null, null);
        try {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getHelmet();

            Piece p = Piece.getPiece(itemStack);
            if (p.isPiece()) { piece = p; }

        } catch (NoSuchMethodError | Exception ignored) {

        }
        return piece;
    }

    public static Piece chestplate(Player player) {
        Piece piece = new Piece(null, null, null, null, null, null, null, null);
        try {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getChestplate();

            Piece p = Piece.getPiece(itemStack);
            if (p.isPiece()) { piece = p; }

        } catch (NoSuchMethodError | Exception ignored) {

        }
        return piece;
    }

    public static Piece leggings(Player player) {
        Piece piece = new Piece(null, null, null, null, null, null, null, null);
        try {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getLeggings();

            Piece p = Piece.getPiece(itemStack);
            if (p.isPiece()) { piece = p; }

        } catch (NoSuchMethodError | Exception ignored) {

        }
        return piece;
    }

    public static Piece boots(Player player) {
        Piece piece = new Piece(null, null, null, null, null, null, null, null);
        try {
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getBoots();

            Piece p = Piece.getPiece(itemStack);
            if (p.isPiece()) { piece = p; }

        } catch (NoSuchMethodError | Exception ignored) {

        }
        return piece;
    }
}
