package dev.foxgirl.epicpowerbracelets.forge_standalone;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import dev.foxgirl.epicpowerbracelets.forge_standalone.item.*;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Supplier;

@Mod("epicpowerbracelets")
public class EpicPowerBraceletsMod {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "epicpowerbracelets");

    public static final RegistryObject<AbstractBraceletItem> INVISIBILITY_BRACELET = ITEMS.register("invisibility_bracelet", InvisibilityBraceletItem::new);
    public static final RegistryObject<AbstractBraceletItem> NIGHT_VISION_BRACELET = ITEMS.register("night_vision_bracelet", NightVisionBraceletItem::new);
    public static final RegistryObject<AbstractBraceletItem> SLOW_FALL_BRACELET = ITEMS.register("slow_fall_bracelet", SlowFallBraceletItem::new);
    public static final RegistryObject<AbstractBraceletItem> SPEED_BRACELET = ITEMS.register("speed_bracelet", SpeedBraceletItem::new);
    public static final RegistryObject<AbstractBraceletItem> STRENGTH_BRACELET = ITEMS.register("strength_bracelet", StrengthBraceletItem::new);

    public static final Supplier<Set<AbstractBraceletItem>> BRACELETS = Suppliers.memoize(() -> ImmutableSet.copyOf(new AbstractBraceletItem[]{
        INVISIBILITY_BRACELET.get(),
        NIGHT_VISION_BRACELET.get(),
        SLOW_FALL_BRACELET.get(),
        SPEED_BRACELET.get(),
        STRENGTH_BRACELET.get(),
    }));

    public EpicPowerBraceletsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::onBuildCreativeModeTabContents);
        forgeEventBus.addListener(this::onPlayerTick);
        forgeEventBus.addListener(this::onLevelTick);
    }

    public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            BRACELETS.get().forEach(event::accept);
        }
    }

    private final Map<UUID, Set<MobEffect>> activeEffectsMapping = new WeakHashMap<>();

    private Set<MobEffect> getActiveEffects(Player player) {
        return activeEffectsMapping.computeIfAbsent(player.getUUID(), __ -> new ReferenceArraySet<>());
    }

    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var player = event.player;
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
                    activeEffects.add(effect.value());
                }
            } else if (activeEffects.remove(effect.value())) {
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

    private void onLevelTick(TickEvent.LevelTickEvent event) {
        var level = event.level instanceof ServerLevel ? (ServerLevel) event.level : null;
        if (level == null) return;
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof Mob) {
                var mob = (Mob) entity;
                for (ItemStack stack : mob.getHandSlots()) {
                    var item = stack.getItem();
                    if (item instanceof AbstractBraceletItem) {
                        applyBraceletToMob(mob, (AbstractBraceletItem) item);
                    }
                }
            }
        }
    }

}
