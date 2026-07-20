package mv.mossuh.moarmors.API;

import mv.mossuh.moarmors.MANAGER.ArmorsManager;

public class ArmorsAPI {
    private static final ArmorsManager armorsManager = new ArmorsManager();

    public static ArmorsManager getManager() { return armorsManager; }
}
