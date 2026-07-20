package mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil;

import java.util.ArrayList;
import java.util.List;

public class Upgrades {

    private int maxLevel = 1;
    private double costPerLevel = 0;

    private List<String> progress = new ArrayList<>();
    private List<String> maxedProgress = new ArrayList<>();
    public Upgrades(Integer maxLevel, Double costPerLevel, List<String> progress, List<String> maxedProgress) {
        if (maxLevel != null) { this.maxLevel = maxLevel; }
        if (costPerLevel != null) { this.costPerLevel = costPerLevel; }
        if (progress != null) { this.progress = progress; }
        if (maxedProgress != null) { this.maxedProgress = maxedProgress; }
    }
    public Upgrades() {}

    public int getMaxLevel() {
        return maxLevel;
    }
    public double getCostPerLevel() {
        return costPerLevel;
    }

    public List<String> getProgressMessage() {
        return progress;
    }

    public List<String> getMaxedProgressMessage() {
        return maxedProgress;
    }
}
