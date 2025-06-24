package rocker.cherry.sparks2.skint;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rocker.cherry.sparks2.Sparks2;

public enum SkintItem {
    SKINT_SHARD(Material.GREEN_CANDLE, 1, "Осколок скинта", "Вы ощущаете знакомую энергию."),
    SKINT_CRUMB(Material.LIME_CANDLE, 2, "Крупица скинта", "Слабое тепло покалывает ваши пальцы."),
    SKINT_CRYSTAL(Material.LIGHT_BLUE_CANDLE, 3, "Кристалл скинта", "Все еще теплый в ваших руках."),
    SKINT_FRAGMENT(Material.CYAN_CANDLE, 4, "Фрагмент скинта", "Мощная энергия заключена внутри."),
    SKINT_HONED(Material.PURPLE_CANDLE, 5, "Ограненный скинт", "Величайшая сила, отточенная мастером."),
    SKINTONITE(Material.BLUE_CANDLE, 12345, "Скинтонит", "Держа его, вы ощущаете слабость.");

    private final Material material;
    private final int customModelData;
    private final String displayName;
    private final String lore;
    private final NamespacedKey skintItemMarkerKey;

    SkintItem(Material material, int customModelData, String displayName, String lore) {
        this.material = material;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = lore;
        this.skintItemMarkerKey = new NamespacedKey(Sparks2.getPlugin(Sparks2.class), "skint_item_marker");
    }

    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(customModelData);
            meta.displayName(Component.text(displayName).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            List<Component> loreList = new ArrayList<>();
            if (this == SKINT_HONED) {
                loreList.add(Component.text(lore).color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false));
            } else {
                loreList.add(Component.text(lore).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(loreList);
            meta.getPersistentDataContainer().set(skintItemMarkerKey, PersistentDataType.BYTE, (byte) 1);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public ItemStack createItemStack(int amount) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(customModelData);
            meta.displayName(Component.text(displayName).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            List<Component> loreList = new ArrayList<>();
            if (this == SKINT_HONED) {
                loreList.add(Component.text(lore).color(NamedTextColor.DARK_PURPLE).decoration(TextDecoration.ITALIC, false));
            } else {
                loreList.add(Component.text(lore).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
            }
            meta.lore(loreList);
            meta.getPersistentDataContainer().set(skintItemMarkerKey, PersistentDataType.BYTE, (byte) 1);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore() {
        return lore;
    }

    public NamespacedKey getSkintItemMarkerKey() {
        return skintItemMarkerKey;
    }

    public boolean isSkintItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(skintItemMarkerKey, PersistentDataType.BYTE) &&
               meta.getCustomModelData() == customModelData &&
               item.getType() == material;
    }

    public static SkintItem fromItemStack(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.getPersistentDataContainer().has(new NamespacedKey(Sparks2.getPlugin(Sparks2.class), "skint_item_marker"), PersistentDataType.BYTE)) {
            return null;
        }

        for (SkintItem skintItem : SkintItem.values()) {
            if (skintItem.getMaterial() == item.getType() &&
                Objects.equals(skintItem.getCustomModelData(), meta.getCustomModelData())) {
                return skintItem;
            }
        }
        return null;
    }
}