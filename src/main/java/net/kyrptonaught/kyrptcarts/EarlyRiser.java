package net.kyrptonaught.kyrptcarts;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {
    //Fabric-ASM
    @Override
    public void run() {
        MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();
        String cartType = remapper.mapClassName("intermediary", "net.minecraft.class_1688$class_1689");
        ClassTinkerers.enumBuilder(cartType).addEnum("KYRPT_CART").build();
    }
}
