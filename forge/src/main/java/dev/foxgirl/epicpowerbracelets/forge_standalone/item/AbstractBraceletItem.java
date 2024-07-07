package dev.foxgirl.epicpowerbracelets.forge_standalone.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public abstract class AbstractBraceletItem extends Item {

    public AbstractBraceletItem() {
        super((new Properties()).stacksTo(1).rarity(Rarity.COMMON));
    }

    public abstract Holder<MobEffect> getMobEffect();

}
