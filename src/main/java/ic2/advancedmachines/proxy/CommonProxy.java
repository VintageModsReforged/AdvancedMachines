package ic2.advancedmachines.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ic2.advancedmachines.AdvancedMachinesConfig;
import ic2.advancedmachines.AdvancedMachinesRecipes;
import ic2.advancedmachines.BlocksItems;
import ic2.advancedmachines.utils.LangHelper;
import ic2.api.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        // config, langs, items
        AdvancedMachinesConfig.init();
        LangHelper.init();
        BlocksItems.init();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
        // recipes
        OreDictionary.registerOre("lava", Item.bucketLava);
        OreDictionary.registerOre("lava", Items.getItem("lavaCell"));
        OreDictionary.registerOre("water", Item.bucketWater);
        OreDictionary.registerOre("water", Items.getItem("waterCell"));
        OreDictionary.registerOre("blockGlass", Block.glass);
        OreDictionary.registerOre("blockGlass", Items.getItem("reinforcedGlass"));
        AdvancedMachinesRecipes.init();
    }
}
