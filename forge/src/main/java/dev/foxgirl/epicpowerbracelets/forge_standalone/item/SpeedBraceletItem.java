package dev.foxgirl.epicpowerbracelets.forge_standalone.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class SpeedBraceletItem extends AbstractBraceletItem {

    @Override
    public Holder<MobEffect> getMobEffect() {
        return MobEffects.MOVEMENT_SPEED;
    }

}