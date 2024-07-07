package dev.foxgirl.epicpowerbracelets;

import net.fabricmc.api.ModInitializer;

public class EpicPowerBraceletsMod implements ModInitializer {

    @Override
    public void onInitialize() {
        new EpicPowerBraceletsImpl();
    }

}
