package re.domi.uniq.tweakers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.UniQ;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unchecked")
public class GregTechAddonTweaker implements IGeneralTweaker {
    public GregTechAddonTweaker() {
        if (Loader.isModLoaded(getModId()))
        {
            Item metaItem = GameRegistry.findItem(getModId(), "metaitem_1");

            OreDictionary.registerOre("ingotAluminum", new ItemStack(metaItem, 1, 11019));
            OreDictionary.registerOre("dustAluminum", new ItemStack(metaItem, 1, 2019));
            OreDictionary.registerOre("nuggetAluminum", new ItemStack(metaItem, 1, 9019));
            OreDictionary.registerOre("plateAluminum", new ItemStack(metaItem, 1, 17019));
            OreDictionary.registerOre("stickAluminum", new ItemStack(metaItem, 1, 23019));

            try
            {
                Class cMod = Class.forName("gregtechmod.GT_Mod");

                cMod.getDeclaredField("sCraftingUnification").set(null, false);
                cMod.getDeclaredField("sInventoryUnification").set(null, false);
            }
            catch (Exception ex)
            {
                UniQ.instance.logger.error("Tweaker GregTech threw an exception while initializing:", ex);
            }
        }
    }

    @Override
    public String getName() {
        return "GregTech-Addon";
    }

    @Override
    public String getModId() {
        return "gregtech_addon";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception {
        Class cGregTechAPI = Class.forName("gregtechmod.api.GregTech_API");

        Field fRecipeAdder = cGregTechAPI.getDeclaredField("sRecipeAdder");
        Object recipeAdderInstance = fRecipeAdder.get(null);

        Class cRecipeAdder = Class.forName("gregtechmod.api.interfaces.IGT_RecipeAdder");
        Class cRecipeMap = Class.forName("gregtechmod.api.recipe.RecipeMap");
        Class cRecipeFactory = Class.forName("gregtechmod.api.recipe.RecipeFactory");

        Field fRecipeList = cRecipeMap.getDeclaredField("recipeList");
        fRecipeList.setAccessible(true);

        for (String recipeMapName : new String[]{"fusionRecipes", "centrifugeRecipes", "electrolyzerRecipes", "chemicalRecipes", "blastRecipes", "canningRecipes", "alloySmelterRecipes", "circuitAssemblerRecipes", "hammerRecipes", "wiremillRecipes", "bendingRecipes", "extruderRecipes", "implosionCompressorRecipes", "grinderRecipes", "distillationrRecipes", "latheRecipes", "cutterRecipes", "freezerRecipes", "sawmillRecipes"}) {
            processRecipeMap(unifier, recipeMapName, recipeAdderInstance, cRecipeAdder, fRecipeList, cRecipeFactory);
        }
    }

    private void processRecipeMap(ResourceUnifier unifier, String mapName, Object recipeAdder, Class cRecipeAdder, Field fRecipeList, Class cRecipeFactory) throws Exception {
        Object recipeMapInstance = cRecipeAdder.getDeclaredMethod(mapName).invoke(recipeAdder);

        List recipeList = (List) fRecipeList.get(recipeMapInstance);

        for (Object recipe : recipeList) {
            Field fItemOutputs = recipe.getClass().getDeclaredField("itemOutputs");
            fItemOutputs.setAccessible(true);

            List<ItemStack> itemOutputs = (List<ItemStack>) fItemOutputs.get(recipe);

            for (int i = 0; i < itemOutputs.size(); i++) {
                itemOutputs.set(i, unifier.getPreferredStack(itemOutputs.get(i)));
            }
        }
    }
}
