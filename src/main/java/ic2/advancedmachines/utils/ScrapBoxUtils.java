package ic2.advancedmachines.utils;

import ic2.core.IC2;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class ScrapBoxUtils {

    public static List<Drop> dropList = new Vector<Drop>();
    public static Random random = new Random();

    public static void init() {
        if (IC2.suddenlyHoes) {
            addDrop(Item.hoeWood, 9001.0F);
        } else {
            addDrop(Item.hoeWood, 5.01F);
        }

        addDrop(Block.dirt, 5.0F);
        addDrop(Item.stick, 4.0F);
        addDrop(Block.grass, 3.0F);
        addDrop(Block.gravel, 3.0F);
        addDrop(Block.netherrack, 2.0F);
        addDrop(Item.rottenFlesh, 2.0F);
        addDrop(Item.appleRed, 1.5F);
        addDrop(Item.bread, 1.5F);
        addDrop(Ic2Items.filledTinCan.getItem(), 1.5F);
        addDrop(Item.swordWood);
        addDrop(Item.shovelWood);
        addDrop(Item.pickaxeWood);
        addDrop(Block.slowSand);
        addDrop(Item.sign);
        addDrop(Item.leather);
        addDrop(Item.feather);
        addDrop(Item.bone);
        addDrop(Item.porkCooked, 0.9F);
        addDrop(Item.beefCooked, 0.9F);
        addDrop(Block.pumpkin, 0.9F);
        addDrop(Item.chickenCooked, 0.9F);
        addDrop(Item.minecartEmpty, 0.9F);
        addDrop(Item.redstone, 0.9F);
        addDrop(Ic2Items.rubber.getItem(), 0.8F);
        addDrop(Item.lightStoneDust, 0.8F);
        addDrop(Ic2Items.coalDust.getItem(), 0.8F);
        addDrop(Ic2Items.copperDust.getItem(), 0.8F);
        addDrop(Ic2Items.tinDust.getItem(), 0.8F);
        addDrop(Ic2Items.plantBall.getItem(), 0.7F);
        addDrop(Ic2Items.suBattery.getItem(), 0.7F);
        addDrop(Ic2Items.ironDust.getItem(), 0.7F);
        addDrop(Ic2Items.goldDust.getItem(), 0.7F);
        addDrop(Item.slimeBall, 0.6F);
        addDrop(Block.oreIron, 0.5F);
        addDrop(Item.helmetGold, 0.5F);
        addDrop(Block.oreGold, 0.5F);
        addDrop(Item.cake, 0.5F);
        addDrop(Item.diamond, 0.1F);
        addDrop(Item.emerald, 0.05F);
        ArrayList<ItemStack> ores;
        if (Ic2Items.copperOre != null) {
            addDrop(Ic2Items.copperOre.getItem(), 0.7F);
        } else {
            ores = OreDictionary.getOres("oreCopper");
            if (!ores.isEmpty()) {
                addDrop(ores.get(0).copy(), 0.7F);
            }
        }

        if (Ic2Items.tinOre != null) {
            addDrop(Ic2Items.tinOre.getItem(), 0.7F);
        } else {
            ores = OreDictionary.getOres("oreTin");
            if (!ores.isEmpty()) {
                addDrop(ores.get(0).copy(), 0.7F);
            }
        }
    }

    public static ItemStack getDrop() {
        if (!dropList.isEmpty()) {
            float dropChance = random.nextFloat() * dropList.get(dropList.size() - 1).upperChanceBound;
            Iterator<Drop> it = dropList.iterator();

            while(it.hasNext()) {
                Drop drop = it.next();
                if (drop.upperChanceBound >= dropChance) {
                    return drop.itemStack.copy();
                }
            }
        }

        return null;
    }

    public static void addDrop(Item item) {
        addDrop(new ItemStack(item), 1.0F);
    }

    public static void addDrop(Item item, float chance) {
        addDrop(new ItemStack(item), chance);
    }

    public static void addDrop(Block block) {
        addDrop(new ItemStack(block), 1.0F);
    }

    public static void addDrop(Block block, float chance) {
        addDrop(new ItemStack(block), chance);
    }

    public static void addDrop(ItemStack item) {
        addDrop(item, 1.0F);
    }

    public static void addDrop(ItemStack item, float chance) {
        dropList.add(new Drop(item, chance));
    }

    public static class Drop {
        ItemStack itemStack;
        float upperChanceBound;

        public Drop(ItemStack itemStack, float chance) {
            this.itemStack = itemStack;
            if (ScrapBoxUtils.dropList.isEmpty()) {
                this.upperChanceBound = chance;
            } else {
                this.upperChanceBound = dropList.get(dropList.size() - 1).upperChanceBound + chance;
            }
        }
    }
}
