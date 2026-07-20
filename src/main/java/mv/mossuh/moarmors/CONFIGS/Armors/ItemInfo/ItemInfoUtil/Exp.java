package mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil;

import mv.mossuh.moarmors.ENUMS.ExpType;

import java.util.ArrayList;
import java.util.List;

public class Exp {

    ExpType type = ExpType.NONE;
    List<EntityExp> expEntities = new ArrayList<>();
    List<String> expEntitiesAsString = new ArrayList<>();

    public Exp(ExpType type, List<String> expEntitiesAsString, List<EntityExp> expEntities) {
        if (type != null) { this.type = type; }
        if (expEntitiesAsString != null) { this.expEntities = expEntities; }
        this.expEntitiesAsString = expEntitiesAsString;
    }

    public ExpType getExpType() {
        return type;
    }
    public List<EntityExp> getExpEntities() {
        return expEntities;
    }
    public List<String> getExpEntitiesAsString() { return expEntitiesAsString; }
}
