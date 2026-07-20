package mv.mossuh.moarmors.CONFIGS.Armors.ItemInfo.ItemInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class EntityExp {
    private String entity = "NONE";
    private Byte data = -1;
    private double exp = 0;

    public EntityExp(String entity, Byte data, Double exp) {
        if (entity != null) { this.entity = entity; }
        if (data != null) { this.data = data; }
        if (exp != null) { this.exp = exp; }
    }

    public String getEntity() {
        return entity;
    }
    public byte getEntityData() {
        return data;
    }
    public double getExp() {
        return exp;
    }



    public static List<EntityExp> stringToEntitiesExp(List<String> entities) {
        List<EntityExp> entityExps = new ArrayList<>();
        for (String e : entities) {
            String[] entitySplit = e.replaceAll(" ", "").split("->", 2);
            String entityWithData = entitySplit[0];
            double exp = 1;
            byte data = -1;
            String[] entitySplit2 = entityWithData.split(":", 2);
            String entity = entitySplit2[0];
            if (entitySplit.length == 2) {
                exp = Double.parseDouble(entitySplit[1]);
            }
            if (entitySplit2.length == 2) {
                data = Byte.parseByte(entitySplit2[1]);
            }
            EntityExp entityExp = new EntityExp(entity, data, exp);
            entityExps.add(entityExp);
        }
        return entityExps;
    }

    public static boolean containsEntity(List<EntityExp> entityExps, String entity, short data) {
        for (EntityExp entityExp : entityExps) {
            String entityEx = entityExp.getEntity();
            short entityData = entityExp.getEntityData();
            if (entityEx.equalsIgnoreCase("ALL")) {
                return true;
            } else if (entityEx.equalsIgnoreCase(entity)) {
                if (entityData != -1) {
                    if (entityData == data) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static double getExpFromList(List<EntityExp> entityExps, String material, short data) {
        for (EntityExp entityExp : entityExps) {
            String entity = entityExp.getEntity();
            short entityData = entityExp.getEntityData();
            if (entity.equalsIgnoreCase("ALL")) {
                return entityExp.getExp();
            } else if (entity.equalsIgnoreCase(material)) {
                if (entityData != -1) {
                    if (entityData == data) {
                        return entityExp.getExp();
                    }
                } else {
                    return entityExp.getExp();
                }
            }
        }
        return 0;
    }
}
