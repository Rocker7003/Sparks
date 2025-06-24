package rocker.cherry.sparks2.listeners;

import rocker.cherry.sparks2.skint.SkintItem;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SkintAnvilListener implements Listener {

    private final NamespacedKey skintUpgradeLevelKey;
    private final NamespacedKey skintUpgradedDamageKey;
    private final NamespacedKey skintUpgradedAttackSpeedKey;
    private final NamespacedKey skintHonedEffectKey;
    private final NamespacedKey isSkintUpgradeKey;
    private final NamespacedKey isSkintResetKey;
    private final NamespacedKey skintUpgradeCostKey;

    private final Map<Material, Double> baseAttackDamages;
    private final Map<Material, Double> baseAttackSpeeds;

    private static final String SKINT_DAMAGE_MODIFIER_ID = "skint.total_damage";
    private static final String SKINT_SPEED_MODIFIER_ID = "skint.total_speed";

    public SkintAnvilListener(JavaPlugin plugin) {
        this.skintUpgradeLevelKey = new NamespacedKey(plugin, "skint_upgrade_level");
        this.skintUpgradedDamageKey = new NamespacedKey(plugin, "skint_upgraded_damage");
        this.skintUpgradedAttackSpeedKey = new NamespacedKey(plugin, "skint_upgraded_attack_speed");
        this.skintHonedEffectKey = new NamespacedKey(plugin, "skint_honed_weapon");
        this.isSkintUpgradeKey = new NamespacedKey(plugin, "is_skint_upgrade");
        this.isSkintResetKey = new NamespacedKey(plugin, "is_skint_reset");
        this.skintUpgradeCostKey = new NamespacedKey(plugin, "skint_upgrade_cost");

        this.baseAttackDamages = new HashMap<>();
        this.baseAttackSpeeds = new HashMap<>();
        
        baseAttackDamages.put(Material.WOODEN_SWORD, 4.0);
        baseAttackDamages.put(Material.STONE_SWORD, 5.0);
        baseAttackDamages.put(Material.IRON_SWORD, 6.0);
        baseAttackDamages.put(Material.GOLDEN_SWORD, 4.0);
        baseAttackDamages.put(Material.DIAMOND_SWORD, 7.0);
        baseAttackDamages.put(Material.NETHERITE_SWORD, 8.0);
        baseAttackDamages.put(Material.WOODEN_AXE, 7.0);
        baseAttackDamages.put(Material.STONE_AXE, 9.0);
        baseAttackDamages.put(Material.IRON_AXE, 9.0);
        baseAttackDamages.put(Material.GOLDEN_AXE, 7.0);
        baseAttackDamages.put(Material.DIAMOND_AXE, 9.0);
        baseAttackDamages.put(Material.NETHERITE_AXE, 10.0);
        baseAttackDamages.put(Material.TRIDENT, 9.0);
        baseAttackDamages.put(Material.MACE, 6.0);
        baseAttackDamages.put(Material.WOODEN_PICKAXE, 2.0);
        baseAttackDamages.put(Material.STONE_PICKAXE, 3.0);
        baseAttackDamages.put(Material.IRON_PICKAXE, 4.0);
        baseAttackDamages.put(Material.GOLDEN_PICKAXE, 2.0);
        baseAttackDamages.put(Material.DIAMOND_PICKAXE, 5.0);
        baseAttackDamages.put(Material.NETHERITE_PICKAXE, 6.0);
        baseAttackDamages.put(Material.WOODEN_SHOVEL, 2.5);
        baseAttackDamages.put(Material.STONE_SHOVEL, 3.5);
        baseAttackDamages.put(Material.IRON_SHOVEL, 4.5);
        baseAttackDamages.put(Material.GOLDEN_SHOVEL, 2.5);
        baseAttackDamages.put(Material.DIAMOND_SHOVEL, 5.5);
        baseAttackDamages.put(Material.NETHERITE_SHOVEL, 6.5);
        baseAttackDamages.put(Material.WOODEN_HOE, 1.0);
        baseAttackDamages.put(Material.STONE_HOE, 1.0);
        baseAttackDamages.put(Material.IRON_HOE, 1.0);
        baseAttackDamages.put(Material.GOLDEN_HOE, 1.0);
        baseAttackDamages.put(Material.DIAMOND_HOE, 1.0);
        baseAttackDamages.put(Material.NETHERITE_HOE, 1.0);

        baseAttackSpeeds.put(Material.WOODEN_SWORD, 1.6);
        baseAttackSpeeds.put(Material.STONE_SWORD, 1.6);
        baseAttackSpeeds.put(Material.IRON_SWORD, 1.6);
        baseAttackSpeeds.put(Material.GOLDEN_SWORD, 1.6);
        baseAttackSpeeds.put(Material.DIAMOND_SWORD, 1.6);
        baseAttackSpeeds.put(Material.NETHERITE_SWORD, 1.6);
        baseAttackSpeeds.put(Material.WOODEN_AXE, 0.8);
        baseAttackSpeeds.put(Material.STONE_AXE, 0.8);
        baseAttackSpeeds.put(Material.IRON_AXE, 0.9);
        baseAttackSpeeds.put(Material.GOLDEN_AXE, 1.0);
        baseAttackSpeeds.put(Material.DIAMOND_AXE, 1.0);
        baseAttackSpeeds.put(Material.NETHERITE_AXE, 1.0);
        baseAttackSpeeds.put(Material.TRIDENT, 1.1);
        baseAttackSpeeds.put(Material.MACE, 1.6);
        baseAttackSpeeds.put(Material.WOODEN_PICKAXE, 1.2);
        baseAttackSpeeds.put(Material.STONE_PICKAXE, 1.2);
        baseAttackSpeeds.put(Material.IRON_PICKAXE, 1.2);
        baseAttackSpeeds.put(Material.GOLDEN_PICKAXE, 1.2);
        baseAttackSpeeds.put(Material.DIAMOND_PICKAXE, 1.2);
        baseAttackSpeeds.put(Material.NETHERITE_PICKAXE, 1.2);
        baseAttackSpeeds.put(Material.WOODEN_SHOVEL, 1.0);
        baseAttackSpeeds.put(Material.STONE_SHOVEL, 1.0);
        baseAttackSpeeds.put(Material.IRON_SHOVEL, 1.0);
        baseAttackSpeeds.put(Material.GOLDEN_SHOVEL, 1.0);
        baseAttackSpeeds.put(Material.DIAMOND_SHOVEL, 1.0);
        baseAttackSpeeds.put(Material.NETHERITE_SHOVEL, 1.0);
        baseAttackSpeeds.put(Material.WOODEN_HOE, 1.0);
        baseAttackSpeeds.put(Material.STONE_HOE, 2.0);
        baseAttackSpeeds.put(Material.IRON_HOE, 3.0);
        baseAttackSpeeds.put(Material.GOLDEN_HOE, 1.0);
        baseAttackSpeeds.put(Material.DIAMOND_HOE, 4.0);
        baseAttackSpeeds.put(Material.NETHERITE_HOE, 4.0);
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack firstItem = inventory.getItem(0);
        ItemStack secondItem = inventory.getItem(1);
        if (firstItem == null || secondItem == null || firstItem.getType().isAir()) return;

        SkintItem skintSecondItem = SkintItem.fromItemStack(secondItem);
        if (skintSecondItem == null) return;

        ItemMeta firstMeta = firstItem.getItemMeta();
        if (firstMeta == null) return;

        if (!isUpgradableWeapon(firstItem)) return;

        ItemStack resultItem = firstItem.clone();
        ItemMeta resultMeta = resultItem.getItemMeta();
        if (resultMeta == null) return;


        if (skintSecondItem == SkintItem.SKINTONITE) {
            if (firstMeta.getPersistentDataContainer().has(skintUpgradeLevelKey, PersistentDataType.INTEGER)) {
                ItemStack cleanItem = new ItemStack(firstItem.getType());
                ItemMeta cleanMeta = cleanItem.getItemMeta();
                if (cleanMeta instanceof Repairable) {
                    ((Repairable) cleanMeta).setRepairCost(10);
                }
                cleanMeta.getPersistentDataContainer().set(isSkintResetKey, PersistentDataType.BYTE, (byte) 1);
                cleanItem.setItemMeta(cleanMeta);
                event.setResult(cleanItem);
            }
            return;
        }


        if (skintSecondItem == SkintItem.SKINT_HONED) {
            if (resultMeta.getPersistentDataContainer().has(skintHonedEffectKey, PersistentDataType.BYTE)) {
                event.setResult(null);
                return;
            }
            resultMeta.getPersistentDataContainer().set(skintHonedEffectKey, PersistentDataType.BYTE, (byte) 1);
            List<Component> lore = resultMeta.hasLore() ? new ArrayList<>(resultMeta.lore()) : new ArrayList<>();
            lore.add(Component.text(""));
            lore.add(Component.text("Эффект: Регенерация I").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            resultMeta.lore(lore);
            if (resultMeta instanceof Repairable) {
                int currentCost = ((Repairable) resultMeta).getRepairCost();
                ((Repairable) resultMeta).setRepairCost(currentCost + 5);
            }
            resultMeta.getPersistentDataContainer().set(isSkintUpgradeKey, PersistentDataType.BYTE, (byte) 1);
            resultItem.setItemMeta(resultMeta);
            event.setResult(resultItem);
            return;
        }


        int currentUpgradeLevel = resultMeta.getPersistentDataContainer().getOrDefault(skintUpgradeLevelKey, PersistentDataType.INTEGER, 0);
        
        SkintItem requiredItem;
        int requiredAmount;

        if (currentUpgradeLevel < 2) {
            requiredItem = SkintItem.SKINT_FRAGMENT;
            requiredAmount = 2;
        } else if (currentUpgradeLevel < 7) {
            requiredItem = SkintItem.SKINT_FRAGMENT;
            requiredAmount = (currentUpgradeLevel - 1) * 2 + 4;
        } else {
            requiredItem = SkintItem.SKINT_CRYSTAL;
            requiredAmount = (currentUpgradeLevel - 7) * 2 + 16;
        }

        if (skintSecondItem != requiredItem || secondItem.getAmount() < requiredAmount) {
            return;
        }

        double addedDamage = 0.6;
        double addedAttackSpeed = -0.09;

        double currentBonusDamage = resultMeta.getPersistentDataContainer().getOrDefault(skintUpgradedDamageKey, PersistentDataType.DOUBLE, 0.0);
        double currentBonusAttackSpeed = resultMeta.getPersistentDataContainer().getOrDefault(skintUpgradedAttackSpeedKey, PersistentDataType.DOUBLE, 0.0);

        double newBonusDamage = currentBonusDamage + addedDamage;
        double newBonusAttackSpeed = currentBonusAttackSpeed + addedAttackSpeed;
        
        double totalWeaponDamage = getBaseAttackDamage(firstItem.getType()) + newBonusDamage;
        double finalAttackSpeedModifier = getBaseAttackSpeed(firstItem.getType()) + newBonusAttackSpeed;

        if ((4.0 + finalAttackSpeedModifier) <= 0.1) {
            event.setResult(null); 
            return;
        }

        resultMeta.getPersistentDataContainer().set(skintUpgradeLevelKey, PersistentDataType.INTEGER, currentUpgradeLevel + 1);
        resultMeta.getPersistentDataContainer().set(skintUpgradedDamageKey, PersistentDataType.DOUBLE, newBonusDamage);
        resultMeta.getPersistentDataContainer().set(skintUpgradedAttackSpeedKey, PersistentDataType.DOUBLE, newBonusAttackSpeed);
        resultMeta.getPersistentDataContainer().set(skintUpgradeCostKey, PersistentDataType.INTEGER, requiredAmount);

        Multimap<Attribute, AttributeModifier> newModifiers = HashMultimap.create();
        AttributeModifier totalDamageModifier = new AttributeModifier(UUID.randomUUID(), SKINT_DAMAGE_MODIFIER_ID, totalWeaponDamage, AttributeModifier.Operation.ADD_NUMBER);
        AttributeModifier totalSpeedModifier = new AttributeModifier(UUID.randomUUID(), SKINT_SPEED_MODIFIER_ID, finalAttackSpeedModifier, AttributeModifier.Operation.ADD_NUMBER);
        newModifiers.put(Attribute.ATTACK_DAMAGE, totalDamageModifier);
        newModifiers.put(Attribute.ATTACK_SPEED, totalSpeedModifier);
        resultMeta.setAttributeModifiers(newModifiers);

        Component baseName = Component.translatable(firstItem.getType().translationKey());
        Component newDisplayName = baseName.append(Component.text(" +" + (currentUpgradeLevel + 1))).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
        resultMeta.displayName(newDisplayName);
        resultMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        
        List<Component> lore = new ArrayList<>();
        double finalDamageStat = totalWeaponDamage;
        double finalSpeedStat = 4.0 + finalAttackSpeedModifier;

        lore.add(Component.text(""));
        lore.add(Component.text("Когда в ведущей руке:").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(String.format(" %.2f Урон", finalDamageStat)).color(NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(String.format(" %.2f Скорость атаки", finalSpeedStat)).color(NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false));
        
        if (resultMeta.getPersistentDataContainer().has(skintHonedEffectKey, PersistentDataType.BYTE)) {
            lore.add(Component.text(""));
            lore.add(Component.text("Эффект: Регенерация I").color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        }
        resultMeta.lore(lore);

        if (resultMeta instanceof Repairable) {
            ((Repairable) resultMeta).setRepairCost(currentUpgradeLevel + 5);
        }
        
        resultMeta.getPersistentDataContainer().set(isSkintUpgradeKey, PersistentDataType.BYTE, (byte) 1);
        resultItem.setItemMeta(resultMeta);
        event.setResult(resultItem);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory)) return;
        if (event.getSlotType() != org.bukkit.event.inventory.InventoryType.SlotType.RESULT) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        ItemStack resultItem = event.getCurrentItem();
        if (resultItem == null || !resultItem.hasItemMeta()) return;

        ItemMeta meta = resultItem.getItemMeta();
        
        boolean isUpgrade = meta.getPersistentDataContainer().has(isSkintUpgradeKey, PersistentDataType.BYTE);
        boolean isReset = meta.getPersistentDataContainer().has(isSkintResetKey, PersistentDataType.BYTE);
        if (!isUpgrade && !isReset) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        AnvilInventory anvil = (AnvilInventory) event.getInventory();
        int cost = (meta instanceof Repairable) ? ((Repairable) meta).getRepairCost() : 1;

        if (player.getGameMode() != GameMode.CREATIVE && player.getLevel() < cost) {
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            player.setLevel(player.getLevel() - cost);
        }

        anvil.getItem(0).setAmount(0);
        ItemStack ingredient = anvil.getItem(1);
        if (ingredient != null) {
            int requiredAmount = 1;
            if (isUpgrade) {
                requiredAmount = meta.getPersistentDataContainer().getOrDefault(skintUpgradeCostKey, PersistentDataType.INTEGER, 1);
            }
            ingredient.setAmount(ingredient.getAmount() - requiredAmount);
        }
        
        ItemStack cleanResult = resultItem.clone();
        ItemMeta cleanMeta = cleanResult.getItemMeta();
        if(cleanMeta != null) {
            cleanMeta.getPersistentDataContainer().remove(isSkintUpgradeKey);
            cleanMeta.getPersistentDataContainer().remove(isSkintResetKey);
            cleanMeta.getPersistentDataContainer().remove(skintUpgradeCostKey);
            cleanResult.setItemMeta(cleanMeta);
        }
        
        event.setCurrentItem(null);
        player.setItemOnCursor(cleanResult);

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
    }
    
    private boolean isUpgradableWeapon(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        return baseAttackDamages.containsKey(item.getType());
    }

    private double getBaseAttackDamage(Material material) {
        return baseAttackDamages.getOrDefault(material, 0.0);
    }

    private double getBaseAttackSpeed(Material material) {
        return baseAttackSpeeds.getOrDefault(material, 4.0) - 4.0;
    }
}