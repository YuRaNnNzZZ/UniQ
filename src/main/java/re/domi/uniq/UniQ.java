package re.domi.uniq;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import re.domi.uniq.tweaker.IRecipeTweaker;
import re.domi.uniq.tweaker.IGeneralTweaker;
import re.domi.uniq.tweakers.*;
import re.domi.uniq.tweakers.recipe.BasicRecipeTweaker;
import re.domi.uniq.tweakers.recipe.ForestryRecipeTweaker;
import re.domi.uniq.tweakers.recipe.MinecraftRecipeTweaker;

import java.util.*;

@Mod(modid = "uniq", dependencies = "after:*")
@SuppressWarnings({"unused", "WeakerAccess"})
public class UniQ
{
    @Instance("uniq")
    public static UniQ instance;

    public Logger logger;
    public Config config;

    public RecipeProcessor recipeProcessor = new RecipeProcessor();

    public ArrayList<IGeneralTweaker> tweakers = new ArrayList<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        this.logger = event.getModLog();
        this.config = new Config(event.getSuggestedConfigurationFile()).load();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        this.registerRecipeTweaker(new MinecraftRecipeTweaker("field_77575_e"), "net.minecraft.item.crafting.ShapedRecipes");
        this.registerRecipeTweaker(new MinecraftRecipeTweaker("field_77580_a"), "net.minecraft.item.crafting.ShapelessRecipes");
        this.registerRecipeTweaker(new BasicRecipeTweaker(), "net.minecraftforge.oredict.ShapedOreRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker(), "net.minecraftforge.oredict.ShapelessOreRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("IC2"), "ic2.core.AdvRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("IC2"), "ic2.core.AdvShapelessRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("Mekanism"), "mekanism.common.recipe.ShapedMekanismRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("Mekanism"), "mekanism.common.recipe.ShapelessMekanismRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("appliedenergistics2"), "appeng.recipes.game.ShapedRecipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("appliedenergistics2"), "appeng.recipes.game.ShapelessRecipe");
        this.registerRecipeTweaker(new ForestryRecipeTweaker(), "forestry.core.recipes.ShapedRecipeCustom");
        this.registerRecipeTweaker(new BasicRecipeTweaker("gregtech", "output", "net.minecraftforge.oredict.ShapedOreRecipe"), "gregtech.api.util.GT_Shaped_Recipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("gregtech", "output", "net.minecraftforge.oredict.ShapelessOreRecipe"), "gregtech.api.util.GT_Shapeless_Recipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("gregtech_addon", "output", "net.minecraftforge.oredict.ShapedOreRecipe"), "gregtechmod.api.util.GT_Shaped_Recipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("gregtech_addon", "output", "net.minecraftforge.oredict.ShapelessOreRecipe"), "gregtechmod.api.util.GT_Shapeless_Recipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("gregtech_addon", "output", "net.minecraftforge.oredict.ShapedOreRecipe"), "gregtechmod.api.util.GT_Shaped_NBT_Keeping_Recipe");
        this.registerRecipeTweaker(new BasicRecipeTweaker("gregtech_addon", "output", "net.minecraftforge.oredict.ShapelessOreRecipe"), "gregtechmod.api.util.GT_Shapeless_NBT_Keeping_Recipe");

        this.registerTweaker(new MinecraftTweaker());
        this.registerTweaker(new ThermalExpansionTweaker());
        this.registerTweaker(new IndustrialCraftTweaker());
        this.registerTweaker(new ImmersiveEngineeringTweaker());
        this.registerTweaker(new MekanismTweaker());
        this.registerTweaker(new NuclearCraftTweaker());
        this.registerTweaker(new EnderIOTweaker());
        this.registerTweaker(new ForestryTweaker());
        this.registerTweaker(new RailcraftTweaker());
        this.registerTweaker(new AppliedEnergisticsTweaker());
        this.registerTweaker(new TinkersConstructTweaker());
        this.registerTweaker(new MineFactoryReloadedTweaker());
        this.registerTweaker(new ExtraUtilitiesTweaker());
        this.registerTweaker(new AdvancedSolarPanelsTweaker());
        this.registerTweaker(new ThaumcraftTweaker());
        this.registerTweaker(new GregTechTweaker());
        this.registerTweaker(new RedPowerTweaker());
        this.registerTweaker(new GregTechAddonTweaker());
        this.registerTweaker(new EtFuturumTweaker());
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        this.logger.info("Registering custom OreDict entries...");

        ResourceUnifier unifier = new ResourceUnifier(this.logger).addOredictNames(this.config.customOreDictNames);

        this.logger.info("Building target list...");

        unifier.build(this.config);

        this.logger.info("Setting up recipe tweakers...");

        this.recipeProcessor.setup();

        this.logger.info("Running tweakers...");

        for (IGeneralTweaker tweaker : this.tweakers)
        {
            try
            {
                tweaker.run(unifier);
            }
            catch (Exception ex)
            {
                this.logger.error("Tweaker " + tweaker.getName() + " threw an exception while running:", ex);
            }
        }

        this.logger.info("Done.");
    }

    public void registerTweaker(IGeneralTweaker tweaker)
    {
        String modId = tweaker.getModId();

        if (modId.equals("") || Loader.isModLoaded(modId))
        {
            this.tweakers.add(tweaker);
        }
    }

    public void registerRecipeTweaker(IRecipeTweaker craftingTweaker, String recipeClassName)
    {
        this.recipeProcessor.registerRecipeTweaker(craftingTweaker, recipeClassName);
    }
}
