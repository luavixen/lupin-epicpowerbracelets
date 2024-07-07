package dev.foxgirl.epicpowerbracelets;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("epicpowerbracelets")
public class EpicPowerBraceletsMod {

    public EpicPowerBraceletsMod() {
        EventBuses.registerModEventBus("epicpowerbracelets", FMLJavaModLoadingContext.get().getModEventBus());
        new EpicPowerBraceletsImpl();
    }

}
