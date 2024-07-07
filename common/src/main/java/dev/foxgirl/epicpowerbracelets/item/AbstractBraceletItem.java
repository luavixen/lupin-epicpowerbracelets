package dev.foxgirl.epicpowerbracelets.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public abstract class AbstractBraceletItem extends Item {

    public AbstractBraceletItem() {
        super((new Item.Properties()).stacksTo(1).rarity(Rarity.COMMON).arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES));
    }

    public abstract MobEffect getMobEffect();

}
