package rocker.cherry.sparks2.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Cat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.inventory.ItemStack;
import rocker.cherry.sparks2.Sparks2;
import rocker.cherry.sparks2.skint.SkintItem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SkintLootListener implements Listener {

    private final Sparks2 plugin;
    private final Random random = new Random();

    public SkintLootListener(Sparks2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Player killer = null;
        if (entity instanceof LivingEntity) {
            killer = ((LivingEntity) entity).getKiller();
        }

        if (killer == null) {
            return;
        }


        if (entity.getType() == EntityType.WARDEN) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRYSTAL.createItemStack(), plugin.getConfig().getDouble("drop_chances.warden.skint_crystal"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.warden.skintonite"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_FRAGMENT.createItemStack(), plugin.getConfig().getDouble("drop_chances.warden.skint_fragment"));
        }


        if (entity.getType() == EntityType.BLAZE) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_SHARD.createItemStack(), plugin.getConfig().getDouble("drop_chances.blaze.skint_shard"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.blaze.skintonite"));
        }


        if (entity.getType() == EntityType.CAVE_SPIDER) {
            double baseChance = plugin.getConfig().getDouble("drop_chances.cave_spider.skint_crumb");
            double chance = baseChance + (killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING) * 0.01);
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), chance);
        }


        if (entity.getType() == EntityType.CREEPER) {
            double crumbBaseChance = plugin.getConfig().getDouble("drop_chances.creeper.skint_crumb");
            double crumbChance = crumbBaseChance + (killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING) * 0.01);
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), crumbChance);
            double skintoniteBaseChance = plugin.getConfig().getDouble("drop_chances.creeper.skintonite");
            double skintoniteChance = skintoniteBaseChance + (killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING) * 0.005);
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), skintoniteChance);
        }


        if (entity.getType() == EntityType.DROWNED) {
            double crumbBaseChance = plugin.getConfig().getDouble("drop_chances.drowned.skint_crumb");
            double crumbChance = crumbBaseChance + (killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING) * 0.01);
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), crumbChance);
            double skintoniteBaseChance = plugin.getConfig().getDouble("drop_chances.drowned.skintonite");
            double skintoniteChance = skintoniteBaseChance + (killer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING) * 0.001);
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), skintoniteChance);
        }


        if (entity.getType() == EntityType.ENDERMAN) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.enderman.skintonite"));
        }


        if (entity.getType() == EntityType.HUSK) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), plugin.getConfig().getDouble("drop_chances.husk.skint_crumb"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.husk.skintonite"));
        }


        if (entity.getType() == EntityType.MAGMA_CUBE) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_SHARD.createItemStack(), plugin.getConfig().getDouble("drop_chances.magma_cube.skint_shard"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.magma_cube.skintonite"));
        }


        if (entity.getType() == EntityType.PIGLIN_BRUTE) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_SHARD.createItemStack(), plugin.getConfig().getDouble("drop_chances.piglin_brute.skint_shard"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.piglin_brute.skintonite"));
        }


        if (entity.getType() == EntityType.PIGLIN) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_SHARD.createItemStack(), plugin.getConfig().getDouble("drop_chances.piglin.skint_shard"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.piglin.skintonite"));
        }


        if (entity.getType() == EntityType.SKELETON) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), plugin.getConfig().getDouble("drop_chances.skeleton.skint_crumb"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.skeleton.skintonite"));
        }


        if (entity.getType() == EntityType.SPIDER) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), plugin.getConfig().getDouble("drop_chances.spider.skint_crumb"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.spider.skintonite"));
        }


        if (entity.getType() == EntityType.WITHER_SKELETON) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_SHARD.createItemStack(), plugin.getConfig().getDouble("drop_chances.wither_skeleton.skint_shard"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.wither_skeleton.skintonite"));
        }


        if (entity.getType() == EntityType.WITHER) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_HONED.createItemStack(), plugin.getConfig().getDouble("drop_chances.wither.skint_honed"));
        }


        if (entity.getType() == EntityType.ZOMBIE_VILLAGER) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), plugin.getConfig().getDouble("drop_chances.zombie_villager.skint_crumb"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.zombie_villager.skintonite"));
        }


        if (entity.getType() == EntityType.ZOMBIE) {
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINT_CRUMB.createItemStack(), plugin.getConfig().getDouble("drop_chances.zombie.skint_crumb"));
            addSkintDropToItemStackList(event.getDrops(), SkintItem.SKINTONITE.createItemStack(), plugin.getConfig().getDouble("drop_chances.zombie.skintonite"));
        }


        if (entity.getType() == EntityType.FOX) {
            List<WeightedLootEntry> foxLootTable = Arrays.asList(
                    new WeightedLootEntry(SkintItem.SKINT_CRUMB.createItemStack(), 15),
                    new WeightedLootEntry(new ItemStack(Material.FEATHER), 8),
                    new WeightedLootEntry(new ItemStack(Material.RABBIT_FOOT), 8),
                    new WeightedLootEntry(new ItemStack(Material.RABBIT_HIDE), 8),
                    new WeightedLootEntry(new ItemStack(Material.EGG), 8),
                    new WeightedLootEntry(new ItemStack(Material.EMERALD), 5),
                    new WeightedLootEntry(new ItemStack(Material.CHICKEN), 8),
                    new WeightedLootEntry(new ItemStack(Material.SWEET_BERRIES), 10)
            );
            addWeightedLoot(event.getDrops(), foxLootTable);
        }
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getType().isAir() || !tool.hasItemMeta()) {
            return;
        }


        if (block.getType() == Material.DIAMOND_ORE) {
            if (tool.getType().name().endsWith("_PICKAXE") && !tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
                double baseChance = plugin.getConfig().getDouble("drop_chances.diamond_ore.skint_shard");
                double chance = baseChance + (tool.getEnchantmentLevel(Enchantment.FORTUNE) * 0.05);
                addSkintDrop(event.getItems(), SkintItem.SKINT_SHARD.createItemStack(), chance);
            }
        }


        if (block.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            if (tool.getType().name().endsWith("_PICKAXE") && !tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
                double baseChance = plugin.getConfig().getDouble("drop_chances.deepslate_diamond_ore.skint_shard");
                double chance = baseChance + (tool.getEnchantmentLevel(Enchantment.FORTUNE) * 0.05);
                addSkintDrop(event.getItems(), SkintItem.SKINT_SHARD.createItemStack(), chance);
            }
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();


        player.getNearbyEntities(8, 4, 8).stream()
                .filter(entity -> entity.getType() == EntityType.CAT)
                .map(entity -> (Cat) entity)
                .filter(cat -> cat.isTamed() && cat.getOwner() != null && cat.getOwner().equals(player))
                .findFirst()
                .ifPresent(cat -> {
                    List<WeightedLootEntry> catLootTable = Arrays.asList(
                            new WeightedLootEntry(SkintItem.SKINT_CRUMB.createItemStack(), 15),
                            new WeightedLootEntry(new ItemStack(Material.PHANTOM_MEMBRANE), 10),
                            new WeightedLootEntry(new ItemStack(Material.STRING), 10),
                            new WeightedLootEntry(new ItemStack(Material.RABBIT_FOOT), 10),
                            new WeightedLootEntry(new ItemStack(Material.CHICKEN), 10),
                            new WeightedLootEntry(new ItemStack(Material.FEATHER), 10),
                            new WeightedLootEntry(new ItemStack(Material.RABBIT_HIDE), 10),
                            new WeightedLootEntry(new ItemStack(Material.ROTTEN_FLESH), 10)
                    );
                    ItemStack droppedItem = getRandomWeightedLoot(catLootTable);
                    if (droppedItem != null) {
                        cat.getWorld().dropItem(cat.getLocation().add(0, 0.5, 0), droppedItem);
                    }
                });
    }

    private void addSkintDropToItemStackList(List<ItemStack> drops, ItemStack skintItem, double chance) {
        if (random.nextDouble() < chance) {
            drops.add(skintItem);
        }
    }

    private void addSkintDrop(List<org.bukkit.entity.Item> drops, ItemStack skintItem, double chance) {
        if (random.nextDouble() < chance) {
            if (!drops.isEmpty()) {
                drops.get(0).getWorld().dropItem(drops.get(0).getLocation(), skintItem);
            } else {
                plugin.getLogger().warning("Attempted to add skint drop to an empty item list in BlockDropItemEvent.");
            }
        }
    }

    private void addWeightedLoot(List<ItemStack> drops, List<WeightedLootEntry> lootTable) {
        ItemStack itemToDrop = getRandomWeightedLoot(lootTable);
        if (itemToDrop != null) {
            drops.add(itemToDrop);
        }
    }

    private ItemStack getRandomWeightedLoot(List<WeightedLootEntry> lootTable) {
        int totalWeight = lootTable.stream().mapToInt(entry -> entry.weight).sum();
        int randomIndex = ThreadLocalRandom.current().nextInt(totalWeight);

        for (WeightedLootEntry entry : lootTable) {
            randomIndex -= entry.weight;
            if (randomIndex < 0) {
                return entry.itemStack;
            }
        }
        return null;
    }

    private static class WeightedLootEntry {
        ItemStack itemStack;
        int weight;

        public WeightedLootEntry(ItemStack itemStack, int weight) {
            this.itemStack = itemStack;
            this.weight = weight;
        }
    }
}
