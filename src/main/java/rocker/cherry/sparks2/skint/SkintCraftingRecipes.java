package rocker.cherry.sparks2.skint;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import rocker.cherry.sparks2.Sparks2;

public class SkintCraftingRecipes {

    public static void registerRecipes(Sparks2 plugin) {
        NamespacedKey shardKey = new NamespacedKey(plugin, "skint_shard_from_crumb");
        ShapedRecipe shardRecipe = new ShapedRecipe(shardKey, SkintItem.SKINT_SHARD.createItemStack());
        shardRecipe.shape("CCC", "CCC", "CCC");
        shardRecipe.setIngredient('C', SkintItem.SKINT_CRUMB.getMaterial());
        Bukkit.addRecipe(shardRecipe);
        NamespacedKey crumbKey = new NamespacedKey(plugin, "skint_crumb_from_shard");
        ShapelessRecipe crumbRecipe = new ShapelessRecipe(crumbKey, SkintItem.SKINT_CRUMB.createItemStack(9));
        crumbRecipe.addIngredient(SkintItem.SKINT_SHARD.getMaterial());
        Bukkit.addRecipe(crumbRecipe);
        NamespacedKey crystalKey = new NamespacedKey(plugin, "skint_crystal_from_shard");
        ShapedRecipe crystalRecipe = new ShapedRecipe(crystalKey, SkintItem.SKINT_CRYSTAL.createItemStack());
        crystalRecipe.shape("SSS", "SSS", "SSS");
        crystalRecipe.setIngredient('S', SkintItem.SKINT_SHARD.getMaterial());
        Bukkit.addRecipe(crystalRecipe);
        NamespacedKey shardFromCrystalKey = new NamespacedKey(plugin, "skint_shard_from_crystal");
        ShapelessRecipe shardFromCrystalRecipe = new ShapelessRecipe(shardFromCrystalKey, SkintItem.SKINT_SHARD.createItemStack(9));
        shardFromCrystalRecipe.addIngredient(SkintItem.SKINT_CRYSTAL.getMaterial());
        Bukkit.addRecipe(shardFromCrystalRecipe);
        NamespacedKey fragmentKey = new NamespacedKey(plugin, "skint_fragment_from_crystal");
        ShapedRecipe fragmentRecipe = new ShapedRecipe(fragmentKey, SkintItem.SKINT_FRAGMENT.createItemStack());
        fragmentRecipe.shape("CCC", "CCC", "CCC");
        fragmentRecipe.setIngredient('C', SkintItem.SKINT_CRYSTAL.getMaterial());
        Bukkit.addRecipe(fragmentRecipe);
        NamespacedKey crystalFromFragmentKey = new NamespacedKey(plugin, "skint_crystal_from_fragment");
        ShapelessRecipe crystalFromFragmentRecipe = new ShapelessRecipe(crystalFromFragmentKey, SkintItem.SKINT_CRYSTAL.createItemStack(9));
        crystalFromFragmentRecipe.addIngredient(SkintItem.SKINT_FRAGMENT.getMaterial());
        Bukkit.addRecipe(crystalFromFragmentRecipe);
    }
}