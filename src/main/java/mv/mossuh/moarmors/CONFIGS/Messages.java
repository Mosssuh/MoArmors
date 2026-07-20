package mv.mossuh.moarmors.CONFIGS;

import mv.mossuh.moarmors.MoArmors;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Messages {

    private static List<String> messagesConfigList = new ArrayList<>(Arrays.asList("INVALID_CODE", "INVALID_AMOUNT", "INVALID_PLAYER",
            "INVALID_PIECE", "NO_PERMISSION", "COMMAND_GIVE_PIECE_SENDER", "COMMAND_GIVE_ALL_PIECE_SENDER", "COMMAND_GIVE_PIECE_RECEIVER",
            "COMMAND_PIECE_ADD_EXP_RECEIVER", "COMMAND_PIECE_ADD_EXP_SENDER", "COMMAND_PIECE_SET_EXP_RECEIVER", "COMMAND_PIECE_SET_EXP_SENDER",
            "COMMAND_PIECE_ADD_LEVEL_RECEIVER", "COMMAND_PIECE_ADD_LEVEL_SENDER", "COMMAND_PIECE_SET_LEVEL_RECEIVER", "COMMAND_PIECE_SET_LEVEL_SENDER",
            "PIECE_LEVEL_UP"));
    private static Map<String, String> messagesMap = new ConcurrentHashMap<>();

    private String messagePosition;
    public Messages(String messagePosition) {
        this.messagePosition = messagePosition;
    }
    public String get() {
        return messagesMap.get(messagePosition);
    }

    public static String INVALID_CODE;
    public static String INVALID_AMOUNT;
    public static String INVALID_PLAYER;
    public static String INVALID_PIECE;
    public static String NO_PERMISSION;
    public static String COMMAND_GIVE_PIECE_SENDER;
    public static String COMMAND_GIVE_ALL_PIECE_SENDER;
    public static String COMMAND_GIVE_PIECE_RECEIVER;
    public static String COMMAND_PIECE_ADD_EXP_RECEIVER;
    public static String COMMAND_PIECE_ADD_EXP_SENDER;
    public static String COMMAND_PIECE_SET_EXP_RECEIVER;
    public static String COMMAND_PIECE_SET_EXP_SENDER;
    public static String COMMAND_PIECE_ADD_LEVEL_RECEIVER;
    public static String COMMAND_PIECE_ADD_LEVEL_SENDER;
    public static String COMMAND_PIECE_SET_LEVEL_RECEIVER;
    public static String COMMAND_PIECE_SET_LEVEL_SENDER;
    public static String PIECE_LEVEL_UP;


    public static void load() {
        FileConfiguration messagesConfig = MoArmors.getConfigs().getMessagesConfig().getConfig();

        for (String messageConfig : messagesConfigList) {
            String message = messagesConfig.getString(messageConfig);
            if (message != null) {
                messagesMap.put(messageConfig, message);
                continue;
            }
            messagesMap.put(messageConfig, "");
        }

        loadMessages();
    }

    private static void loadMessages() {
        INVALID_CODE = new Messages("INVALID_CODE").get();
        INVALID_AMOUNT = new Messages("INVALID_AMOUNT").get();
        INVALID_PLAYER = new Messages("INVALID_PLAYER").get();
        INVALID_PIECE = new Messages("INVALID_PIECE").get();
        NO_PERMISSION = new Messages("NO_PERMISSION").get();
        COMMAND_GIVE_PIECE_SENDER = new Messages("COMMAND_GIVE_PIECE_SENDER").get();
        COMMAND_GIVE_ALL_PIECE_SENDER = new Messages("COMMAND_GIVE_ALL_PIECE_SENDER").get();
        COMMAND_GIVE_PIECE_RECEIVER = new Messages("COMMAND_GIVE_PIECE_RECEIVER").get();
        COMMAND_PIECE_ADD_EXP_RECEIVER = new Messages("COMMAND_PIECE_ADD_EXP_RECEIVER").get();
        COMMAND_PIECE_ADD_EXP_SENDER = new Messages("COMMAND_PIECE_ADD_EXP_SENDER").get();
        COMMAND_PIECE_SET_EXP_RECEIVER = new Messages("COMMAND_PIECE_SET_EXP_RECEIVER").get();
        COMMAND_PIECE_SET_EXP_SENDER = new Messages("COMMAND_PIECE_SET_EXP_SENDER").get();
        COMMAND_PIECE_ADD_LEVEL_RECEIVER = new Messages("COMMAND_PIECE_ADD_LEVEL_RECEIVER").get();
        COMMAND_PIECE_ADD_LEVEL_SENDER = new Messages("COMMAND_PIECE_ADD_LEVEL_SENDER").get();
        COMMAND_PIECE_SET_LEVEL_RECEIVER = new Messages("COMMAND_PIECE_SET_LEVEL_RECEIVER").get();
        COMMAND_PIECE_SET_LEVEL_SENDER = new Messages("COMMAND_PIECE_SET_LEVEL_SENDER").get();
        PIECE_LEVEL_UP = new Messages("PIECE_LEVEL_UP").get();
    }
}
