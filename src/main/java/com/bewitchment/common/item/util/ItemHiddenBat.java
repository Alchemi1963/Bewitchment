package com.bewitchment.common.item.util;

import com.bewitchment.ModConfig;
import com.bewitchment.Util;
import net.minecraft.item.Item;

/**
 * Created by Joseph on 3/3/2020.
 */
public class ItemHiddenBat extends Item {
	public ItemHiddenBat() {
		super();
		Util.registerItem(this, "hidden_bat");
		setMaxDamage(1);
		setMaxStackSize(1);
		this.isInCreativeTab(null);
	}
}
