package net.firtreeman.bombvests.item.custom;

import net.firtreeman.bombvests.util.DYNAMITE_TYPES;
import net.minecraft.world.item.Item;

public class DynamiteItem extends Item {
    private final DYNAMITE_TYPES dynamiteType;

    public DynamiteItem(Properties pProperties, DYNAMITE_TYPES dynamiteType) {
        super(pProperties);
        this.dynamiteType = dynamiteType;
    }

    public DYNAMITE_TYPES getDynamiteType() {
        return dynamiteType;
    }
}
