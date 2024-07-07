package dev.foxgirl.epicpowerbracelets;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod("epicpowerbracelets")
public class EpicPowerBraceletsMod {

    public EpicPowerBraceletsMod(IEventBus modEventBus) {
        new EpicPowerBraceletsImpl();
    }

}
