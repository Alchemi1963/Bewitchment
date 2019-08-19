package com.bewitchment.common.item.fume;

import com.bewitchment.Util;
import com.bewitchment.registry.ModObjects;
import net.minecraft.item.Item;

public class ItemDropletWisdom extends Item {
    public ItemDropletWisdom() {
        super();
        this.setContainerItem(ModObjects.empty_jar);
        Util.registerItem(this, "droplet_of_wisdom");
    }
}
