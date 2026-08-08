package mv.mossuh.moarmors.UTILITIES;

import mv.mossuh.moarmors.CONFIGS.Config.Config;
import mv.mossuh.moarmors.EVENTS.MoBoosters.RewardBoosters;
import mv.mossuh.moarmors.MoArmors;
import mv.mossuh.moarmors.PAPI;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.EVENTS.PluginCheckerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PluginChecker implements Listener {
    private MoArmors instance = MoArmors.getInstance();

    @EventHandler
    public void onPluginChecker(PluginCheckerEvent event) {
        PluginType pluginType = event.getPluginType();

        switch (pluginType) {
            case PlaceholderAPI:
                new PAPI().register();
                UtilString.get("&8[" + Config.PREFIX + "&8] &aDetected PlaceholderAPI, used as soft-depend.").hex().sendMessageInConsole();
                break;
            case MoBoosters:
                if (Config.ACTIONS) {
                    instance.getServer().getPluginManager().registerEvents(new RewardBoosters(), instance);
                }
                UtilString.get("&8[" + Config.PREFIX + "&8] &aDetected MoBoosters, used as soft-depend. Enabling classes.").hex().sendMessageInConsole();
                break;
        }
    }
}
