package dev.foxgirl.epicpowerbracelets.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class SlowFallBraceletItem extends AbstractBraceletItem {

    @Override
    public Holder<MobEffect> getMobEffect() {
        return MobEffects.SLOW_FALLING;
    }

}
