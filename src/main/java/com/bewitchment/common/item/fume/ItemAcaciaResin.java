package com.bewitchment.common.item.fume;

import com.bewitchment.Util;
import com.bewitchment.registry.ModObjects;
import net.minecraft.item.Item;

public class ItemAcaciaResin extends Item {
    public ItemAcaciaResin() {
        super();
        this.setContainerItem(ModObjects.empty_jar);
        Util.registerItem(this, "acacia_resin");
    }
}
