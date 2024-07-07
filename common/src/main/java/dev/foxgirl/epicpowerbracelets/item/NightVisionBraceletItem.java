package dev.foxgirl.epicpowerbracelets.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class NightVisionBraceletItem extends AbstractBraceletItem {

    @Override
    public Holder<MobEffect> getMobEffect() {
        return MobEffects.NIGHT_VISION;
    }

}
