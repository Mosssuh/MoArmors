package mv.mossuh.moarmors;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mv.mossuh.moarmors.API.ArmorsAPI;
import mv.mossuh.moarmors.ARMORS.Armor.Armor;
import mv.mossuh.moarmors.ARMORS.Armor.ArmorPlayer;
import mv.mossuh.moarmors.ARMORS.Armor.Piece;
import mv.mossuh.moarmors.CONFIGS.Armors.Actions.Actions;
import mv.mossuh.moarmors.ENUMS.PieceType;
import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.UTILITIES.UtilMethods;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import org.bukkit.entity.Player;

import java.util.*;

public class PAPI extends PlaceholderExpansion {
    private MoArmors instance = MoArmors.getInstance();

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "Mossuh";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "moarmors";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return instance.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        UUID uuid = player.getUniqueId();
        String placeholder = identifier.toLowerCase();
        ArmorPlayer armorPlayer = ArmorsAPI.getManager().getPlayer(uuid);
        Armor armor = armorPlayer.getArmor();
        List<Piece> pieces = armor.getPieces();

        if (placeholder.startsWith("active_")) {
            String[] placeholderSplit1 = placeholder.split("_", 3);
            PieceType pieceType;
            if (placeholderSplit1.length < 3) {
                return "";
            }
            boolean isALL = placeholderSplit1[1].equalsIgnoreCase("all");
            pieceType = UtilMethods.getPieceType(placeholderSplit1[1]);
            String values = placeholderSplit1[2];
            boolean isPiece = pieceType != PieceType.NONE;

            if (values.startsWith("code")) {
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getConfigArmor().getArmorIdentifier().getCode();
                } else if (isALL) {
                    List<String> codes = new ArrayList<>();
                    for (Piece piece : pieces) {
                        codes.add(piece.getConfigArmor().getArmorIdentifier().getCode());
                    }
                    return String.join(", ", codes);
                }
            } else if (values.startsWith("level")) {
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getLevel()+"";
                } else if (isALL) {
                    int level = 0;
                    for (Piece piece : pieces) {
                        level += piece.getLevel();
                    }
                    return level+"";
                }
            } else if (values.startsWith("exp")) {
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getExp()+"";
                } else if (isALL) {
                    double exp = 0;
                    for (Piece piece : pieces) {
                        exp += piece.getExp();
                    }
                    return exp+"";
                }
            } else if (values.startsWith("cost")) {
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getCost()+"";
                } else if (isALL) {
                    double cost = 0;
                    for (Piece piece : pieces) {
                        cost += piece.getCost();
                    }
                    return cost+"";
                }
            } else if (values.startsWith("max_level")) {
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getConfigArmor().getUpgrades().getMaxLevel()+"";
                } else if (isALL) {
                    List<String> maxLevels = new ArrayList<>();
                    for (Piece piece : pieces) {
                        maxLevels.add(piece.getConfigArmor().getUpgrades().getMaxLevel()+"");
                    }
                    return String.join(", ", maxLevels);
                }
            } else if (values.startsWith("tags")) {
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getConfigArmor().getArmorIdentifier().getTagsAsString();
                } else if (isALL) {
                    Set<String> tags = new HashSet<>();
                    for (Piece piece : pieces) {
                        List<String> t = piece.getConfigArmor().getArmorIdentifier().getTags();
                        tags.addAll(t);
                    }
                    return String.join(", ", tags);
                }
            } else if (values.startsWith("contains_tag_")) {
                String tag = values.replace("contains_tag_", "");

                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getConfigArmor().getArmorIdentifier().hasTag(tag)+"";
                } else if (isALL) {
                    for (Piece piece : pieces) {
                        boolean hasTag = piece.getConfigArmor().getArmorIdentifier().hasTag(tag);
                        if (!hasTag) {
                            return "false";
                        }
                    }
                    return "true";
                }
            } else if (values.startsWith("variable_")) {
                String variableV1 = values.replace("variable_", "");
                String variable = variableV1.replaceFirst("\\{", "").replace("}", "");

                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.getVariable(variable).getValue();
                } else if (isALL) {
                    List<String> valueList = new ArrayList<>();
                    for (Piece piece : pieces) {
                        valueList.add(piece.getVariable(variable).getValue());
                    }
                    return String.join(", ", valueList);
                }
            } else if (values.startsWith("has_variable_")) {
                String variableV1 = values.replace("variable_", "");
                String variable = variableV1.replaceFirst("\\{", "").replace("}", "");

                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    return piece.hasVariable(variable)+"";
                } else if (isALL) {
                    for (Piece piece : pieces) {
                        boolean hasVariable = piece.hasVariable(variable);
                        if (!hasVariable) {
                            return "false";
                        }
                    }
                    return "true";
                }
            } else if (values.startsWith("boost_")) {
                String[] boostV1 = values.replace("boost_", "").split("_", 3);
                if (boostV1.length < 3) {
                    return "0";
                }

                BoosterType boosterType = mv.mossuh.moboosters.UTILITIES.UtilMethods.getBoosterType(boostV1[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(boostV1[1]);
                String boosted = boostV1[2];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    int level = piece.getLevel();
                    Actions actions = piece.getConfigArmor().getActions();
                    return actions.getBooster(boosterIdentifier).getBoost(level)+"";
                } else if (isALL) {
                    double boost = 0;
                    for (Piece piece : pieces) {
                        int level = piece.getLevel();
                        Actions actions = piece.getConfigArmor().getActions();
                        boost += actions.getBooster(boosterIdentifier).getBoost(level);
                    }
                    return boost+"";
                }
            } else if (values.startsWith("has_boost_")) {
                String[] boostV1 = values.replace("has_boost_", "").split("_", 3);
                if (boostV1.length < 3) {
                    return "false";
                }
                BoosterType boosterType = mv.mossuh.moboosters.UTILITIES.UtilMethods.getBoosterType(boostV1[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(boostV1[1]);
                String boosted = boostV1[2];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);
                if (isPiece) {
                    Piece piece = armor.getPiece(pieceType);
                    Actions actions = piece.getConfigArmor().getActions();
                    return actions.hasBooster(boosterIdentifier)+"";
                } else if (isALL) {
                    for (Piece piece : pieces) {
                        Actions actions = piece.getConfigArmor().getActions();
                        boolean hasBooster = actions.hasBooster(boosterIdentifier);
                        if (!hasBooster) {
                            return "false";
                        }
                    }
                    return "true";
                }
            }
        }


        return "";
    }
}
