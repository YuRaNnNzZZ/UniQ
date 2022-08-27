package re.domi.uniq.tweakers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EtFuturumTweaker implements IGeneralTweaker {
    @Override
    public String getName() {
        return "Et Futurum Requiem";
    }

    @Override
    public String getModId() {
        return "etfuturum";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception {
        this.processBlastFurnaceRecipes(unifier);
        this.processSmokerRecipes(unifier);
    }

    private void processBlastFurnaceRecipes(ResourceUnifier unifier) throws Exception {
        Class cRecipes = Class.forName("ganymedes01.etfuturum.recipes.BlastFurnaceRecipes");

        Field fSmeltingBase = cRecipes.getDeclaredField("smeltingBase");
        fSmeltingBase.setAccessible(true);
        Object smeltingBase = fSmeltingBase.get(null);

        Map<ItemStack, ItemStack> smeltingList = (Map<ItemStack, ItemStack>) cRecipes.getDeclaredField("smeltingList").get(smeltingBase);

        for (Map.Entry<ItemStack, ItemStack> entry : smeltingList.entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }
    }

    private void processSmokerRecipes(ResourceUnifier unifier) throws Exception {
        Class cRecipes = Class.forName("ganymedes01.etfuturum.recipes.SmokerRecipes");

        Field fSmeltingBase = cRecipes.getDeclaredField("smeltingBase");
        fSmeltingBase.setAccessible(true);
        Object smeltingBase = fSmeltingBase.get(null);

        Map<ItemStack, ItemStack> smeltingList = (Map<ItemStack, ItemStack>) cRecipes.getDeclaredField("smeltingList").get(smeltingBase);

        for (Map.Entry<ItemStack, ItemStack> entry : smeltingList.entrySet())
        {
            entry.setValue(unifier.getPreferredStack(entry.getValue()));
        }
    }
}
