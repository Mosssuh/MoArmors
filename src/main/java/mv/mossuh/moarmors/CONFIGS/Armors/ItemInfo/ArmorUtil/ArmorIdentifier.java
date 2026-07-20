package mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ArmorUtil;


import mv.mossuh.moboosters.CONFIGS.Config.Config;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArmorIdentifier {
    private String code = "invalid";
    private List<String> tags = new ArrayList<>();
    private String tagsAsString = "invalid";
    private List<VariableArg> defaultVariables = new ArrayList<>();
    private String boosterIdentifier = null;
    public ArmorIdentifier(String code, List<String> tags, List<VariableArg> defaultVariables, String boosterIdentifier) {
        if (code != null) {this.code = code; }
        if (tags != null) {
            this.tags = tags;
            this.tagsAsString = String.join(", ", tags);
        }
        if (defaultVariables != null) { this.defaultVariables = defaultVariables; }
        if (boosterIdentifier != null) { this.boosterIdentifier = boosterIdentifier; }
    }
    public ArmorIdentifier() {}

    public String getCode() { return code; }
    public List<String> getTags() { return tags; }
    public String getTagsAsString() { return tagsAsString; }
    public String getBoosterIdentifier() { return boosterIdentifier; }
    public boolean isBoosterIdentifier() {
        return Config.IDENTIFIERS.hasIdentifier(boosterIdentifier);
    }

    public boolean hasCode() {
        return !Objects.equals(code, "invalid");
    }
    public boolean hasTags() {
        return !tags.isEmpty() && !Objects.equals(tagsAsString, "invalid");
    }

    public boolean hasTag(String tag) {
        for (String savedTag : tags) {
            if (savedTag.equalsIgnoreCase(tag)) {
                return true;
            }
        }
        return false;
    }

    public List<VariableArg> getDefaultVariables() { return defaultVariables; }
    public boolean hasDefaultVariables() { return !defaultVariables.isEmpty(); }
}
