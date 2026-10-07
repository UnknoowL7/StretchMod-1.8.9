package com.dabaduh.stretchmod;

import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = "stretchmod",
        name = "Stretch Mod",
        version = "1.0",
        clientSideOnly = true
)
public class StretchMod {

    @Mod.EventHandler
    public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
        System.out.println("[StretchMod] Loaded!");
    }
}
