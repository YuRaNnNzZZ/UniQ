package re.domi.uniq.tweakers;

import net.minecraft.item.ItemStack;
import re.domi.uniq.ResourceUnifier;
import re.domi.uniq.tweaker.IGeneralTweaker;

import java.lang.reflect.Field;
import java.util.List;

public class RedPowerTweaker implements IGeneralTweaker {
    @Override
    public String getName() {
        return "RedPower Core";
    }

    @Override
    public String getModId() {
        return "RedPowerCore";
    }

    @Override
    public void run(ResourceUnifier unifier) throws Exception {
        Field recipes = Class.forName("com.eloraam.redpower.core.CraftLib").getDeclaredField("alloyRecipes");

        List<List<Object>> alloyRecipes = (List<List<Object>>) recipes.get(null);

        for (List<Object> recipe : alloyRecipes) {
            if (recipe.size() < 2 || (!(recipe.get(1) instanceof ItemStack))) continue;

            recipe.set(1, unifier.getPreferredStack((ItemStack) recipe.get(1)));
        }
    }
}
