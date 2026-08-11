package mv.mossuh.moarmors.EVENTS.Levelings;

import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ENUMS.ExecuteType;
import mv.mossuh.moarmors.ENUMS.ReceiveType;
import mv.mossuh.moarmors.API.Events.PieceChangeExpEvent;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.ARMORS.ArmorUpdater;
import mv.mossuh.moarmors.CONFIGS.Armors.Armor.ConfigArmor;
import mv.mossuh.moarmors.ENUMS.ExpType;
import mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil.EntityExp;
import mv.mossuh.moarmors.MoArmors;
import mv.mossuh.moarmors.ENUMS.DebugType;
import mv.mossuh.moarmors.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LevelingExecutor {
    private static final MoArmors main = MoArmors.getInstance();
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    DebugType debugType = DebugType.LEVELING;
    private Player player;
    private ExpType expType = ExpType.NONE;
    private String type = "";
    private short data = -1;
    private double multiplier = 1;

    public LevelingExecutor(Player player, ExpType expType, String type, Short data, Double multiplier) {
        this.player = player;
        if (expType != null) { this.expType = expType; }
        if (type != null) { this.type = type; }
        if (data != null) { this.data = data; }
        if (multiplier != null) {
            if (multiplier > 1) {
                this.multiplier = multiplier;
            }
        }
    }

    public void execute() {
        UUID uuid = player.getUniqueId();

        UtilString.get("&8--------------------------------").hex().sendMessageInConsole(debugType);
        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        if (!armorPlayer.isPlayer()) {
            UtilString.get("&bPlayer: &cInvalid").hex().sendMessageInConsole(debugType);
            UtilString.get("&8--------------------------------").hex().sendMessageInConsole(debugType);
            return;
        }

        UtilString.get("&bPlayer: &7" + player.getName()).hex().sendMessageInConsole(debugType);

        Armor armor = armorPlayer.getArmor();
        if (!armor.hasPiece()) {
            UtilString.get("&bHas Piece: &cNo").hex().sendMessageInConsole(debugType);
            UtilString.get("&8--------------------------------").hex().sendMessageInConsole(debugType);
            return;
        }

        UtilString.get("&bHas Piece: &aYes").hex().sendMessageInConsole(debugType);
        UtilString.get("&bExp Type: &7" + expType.name()).hex().sendMessageInConsole(debugType);

        List<Piece> pieces = armor.getPieces();
        executor.submit(() -> {
            List<Map.Entry<Piece, Double>> cached = new ArrayList<>();

            for (Piece piece : pieces) {
                String typeString = piece.getPieceType().name();
                ConfigArmor configArmor = piece.getConfigArmor();
                List<EntityExp> exps = configArmor.getExp(expType);
                if (exps.isEmpty()) {
                    UtilString.get("&b" + typeString + ": &cNo Exp Registered").hex().sendMessageInConsole(debugType);
                    continue;
                };

                if (EntityExp.containsEntity(exps, type, data)) {
                    double exp = EntityExp.getExpFromList(exps, type, data) * multiplier;
                    cached.add(new AbstractMap.SimpleEntry<>(piece, exp));
                } else {
                    UtilString.get("&b" + typeString + ": &cNo Same Entity").hex().sendMessageInConsole(debugType);
                }
            }

            if (cached.isEmpty()) return;

            Bukkit.getScheduler().runTask(main, () -> {
                if (!player.isOnline()) return;

                for (Map.Entry<Piece, Double> entry : cached) {
                    Piece piece = entry.getKey();
                    String typeString = piece.getPieceType().name();
                    double exp = entry.getValue();

                    PieceChangeExpEvent expEvent = new PieceChangeExpEvent(uuid, piece, ExecuteType.NATURAL, ReceiveType.ADD, exp);
                    Bukkit.getPluginManager().callEvent(expEvent);

                    if (expEvent.isCancelled()) {
                        UtilString.get("&b" + typeString + ": &cCancelled Event").hex().sendMessageInConsole(debugType);
                        continue;
                    }

                    double newExp = expEvent.getExp();

                    UtilString.get("&b" + typeString + ": &aAdded " + newExp + " exp").hex().sendMessageInConsole(debugType);
                    piece.addExp(newExp, true);
                    ArmorUpdater.verifyPiece(player, piece, false);
                    ArmorUpdater.updatePieceInfo(player, piece, false);
                }
            });
            UtilString.get("&8--------------------------------").hex().sendMessageInConsole(debugType);
        });
    }
}
