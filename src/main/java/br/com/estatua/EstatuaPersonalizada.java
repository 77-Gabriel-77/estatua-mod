package br.com.estatua;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

@Mod(EstatuaPersonalizada.MODID)
public class EstatuaPersonalizada {
    public static final String MODID = "estatuapersonalizada";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> ESTATUA = ITEMS.register("estatua_personalizada",
            () -> new StatueItem(new Item.Properties().tab(ItemGroup.TAB_DECORATIONS).stacksTo(16)));

    public EstatuaPersonalizada() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
    }
}
