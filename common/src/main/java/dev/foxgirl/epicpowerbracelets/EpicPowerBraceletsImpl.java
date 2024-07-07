package dev.foxgirl.epicpowerbracelets;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.foxgirl.epicpowerbracelets.item.*;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public final class EpicPowerBraceletsImpl {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create("epicpowerbracelets", Registries.ITEM);

    public static final RegistrySupplier<AbstractBraceletItem> INVISIBILITY_BRACELET = ITEMS.register("invisibility_bracelet", InvisibilityBraceletItem::new);
    public static final RegistrySupplier<AbstractBraceletItem> NIGHT_VISION_BRACELET = ITEMS.register("night_vision_bracelet", NightVisionBraceletItem::new);
    public static final RegistrySupplier<AbstractBraceletItem> SLOW_FALL_BRACELET = ITEMS.register("slow_fall_bracelet", SlowFallBraceletItem::new);
    public static final RegistrySupplier<AbstractBraceletItem> SPEED_BRACELET = ITEMS.register("speed_bracelet", SpeedBraceletItem::new);
    public static final RegistrySupplier<AbstractBraceletItem> STRENGTH_BRACELET = ITEMS.register("strength_bracelet", StrengthBraceletItem::new);

    public static final Supplier<Set<AbstractBraceletItem>> BRACELETS = Suppliers.memoize(() -> ImmutableSet.copyOf(new AbstractBraceletItem[]{
        INVISIBILITY_BRACELET.get(),
        NIGHT_VISION_BRACELET.get(),
        SLOW_FALL_BRACELET.get(),
        SPEED_BRACELET.get(),
        STRENGTH_BRACELET.get(),
    }));

    public EpicPowerBraceletsImpl() {
        ITEMS.register();
        TickEvent.PLAYER_POST.register(this::onPlayerTick);
        TickEvent.SERVER_LEVEL_POST.register(this::onLevelTick);
    }

    private final Map<UUID, Set<MobEffect>> activeEffectsMapping = new WeakHashMap<>();

    private Set<MobEffect> getActiveEffects(Player player) {
        return activeEffectsMapping.computeIfAbsent(player.getUUID(), __ -> new ReferenceArraySet<>());
    }

    private void onPlayerTick(Player player) {
        if (player.level().isClientSide()) return;
        var activeEffects = getActiveEffects(player);
        for (AbstractBraceletItem bracelet : BRACELETS.get()) {
            var effect = bracelet.getMobEffect();
            var effectInstance = player.getEffect(effect);
            if (
                player.getMainHandItem().getItem() == bracelet ||
                player.getOffhandItem().getItem() == bracelet
            ) {
                if (
                    effectInstance == null ||
                    effectInstance.getAmplifier() == 0
                ) {
                    player.addEffect(new MobEffectInstance(effect, 210), player);
                    activeEffects.add(effect);
                }
            } else if (activeEffects.remove(effect)) {
                if (
                    effectInstance != null &&
                    effectInstance.getDuration() <= 210 &&
                    effectInstance.getAmplifier() == 0
                ) {
                    player.removeEffect(effect);
                }
            }
        }
    }

    private void applyBraceletToMob(Mob mob, AbstractBraceletItem bracelet) {
        var effect = bracelet.getMobEffect();
        var effectInstance = mob.getEffect(effect);
        if (effectInstance == null || effectInstance.getAmplifier() == 0) {
            mob.addEffect(new MobEffectInstance(effect, 70), mob);
        }
    }

    private void onLevelTick(ServerLevel level) {
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof Mob) {
                for (ItemStack stack : entity.getHandSlots()) {
                    var item = stack.getItem();
                    if (item instanceof AbstractBraceletItem) {
                        applyBraceletToMob((Mob) entity, (AbstractBraceletItem) item);
                    }
                }
            }
        }
    }

}
