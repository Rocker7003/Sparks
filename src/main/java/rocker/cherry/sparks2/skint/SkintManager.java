package rocker.cherry.sparks2.skint;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import rocker.cherry.sparks2.Sparks2;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;
import java.util.Arrays;

public class SkintManager {

    private final Sparks2 plugin;
    private final Scoreboard scoreboard;
    private final Objective skintHasItemObjective;
    private final Objective skintDeathObjective;
    private final Objective skintTimerObjective;
    private final Objective skintNoSkintsObjective;
    private final Objective skintMessageObjective;
    private final FileConfiguration config;

    public SkintManager(Sparks2 plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.skintHasItemObjective = Objects.requireNonNull(scoreboard.getObjective("skint_has_item"));
        this.skintDeathObjective = Objects.requireNonNull(scoreboard.getObjective("skint_death"));
        this.skintTimerObjective = Objects.requireNonNull(scoreboard.getObjective("skint_timer"));
        this.skintNoSkintsObjective = Objects.requireNonNull(scoreboard.getObjective("skint_no_skints"));
        this.skintMessageObjective = Objects.requireNonNull(scoreboard.getObjective("skint_message"));
    }

    public void startSkintTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkSkintonite(player);
                    checkItemEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        long skintoniteTimerInterval = plugin.getConfig().getLong("skintonite_timer_interval", 600L);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Score skintTimerScore = skintTimerObjective.getScore(player.getName());
                    Score skintHasItemScore = skintHasItemObjective.getScore(player.getName());

                    if (skintHasItemScore.getScore() == 1) {
                        if (skintTimerScore.getScore() < skintoniteTimerInterval) {
                            skintTimerScore.setScore(skintTimerScore.getScore() + 1);
                        } else {
                            consumeSkint(player);
                            skintTimerScore.setScore(0);
                        }
                    } else {
                        skintTimerScore.setScore(0);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void checkItemEffects(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();


        if (SkintItem.SKINT_HONED.isSkintItem(mainHand)) {
            if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.REGENERATION, 100, 1, true, false));
            }
        }

        else if (mainHand.hasItemMeta() && mainHand.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "skint_honed_weapon"), PersistentDataType.BYTE)) {
            if (!player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.REGENERATION, 100, 0, true, false));
            }
        }



        if (SkintItem.SKINTONITE.isSkintItem(mainHand)) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.WEAKNESS, 100, 1, true, false));
        }
    }


    private void checkSkintonite(Player player) {
        boolean hasSkintonite = player.getInventory().contains(SkintItem.SKINTONITE.getMaterial(), 1);
        if (hasSkintonite) {
            hasSkintonite = Arrays.stream(player.getInventory().getContents())
                    .filter(Objects::nonNull)
                    .anyMatch(item -> SkintItem.SKINTONITE.isSkintItem(item));
        }

        Score skintHasItemScore = skintHasItemObjective.getScore(player.getName());
        if (hasSkintonite) {
            skintHasItemScore.setScore(1);
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WEAKNESS, 40, 0, true, false));
        } else {
            skintHasItemScore.setScore(0);
        }

        if (skintHasItemScore.getScore() == 0) {
            skintNoSkintsObjective.getScore(player.getName()).setScore(0);
            skintMessageObjective.getScore(player.getName()).setScore(0);
        }


        Score skintDeathScore = skintDeathObjective.getScore(player.getName());
        Score skintNoSkintsScore = skintNoSkintsObjective.getScore(player.getName());
        if (player.isDead() && skintDeathScore.getScore() >= 1 && skintNoSkintsScore.getScore() == 1) {
            lostLight(player);
            skintDeathScore.setScore(0);
        }
    }

    private void consumeSkint(Player player) {
        List<SkintItem> orderedSkints = Arrays.asList(
                SkintItem.SKINT_HONED,
                SkintItem.SKINT_FRAGMENT,
                SkintItem.SKINT_CRYSTAL,
                SkintItem.SKINT_SHARD,
                SkintItem.SKINT_CRUMB
        );

        boolean consumed = false;
        for (SkintItem skint : orderedSkints) {
            if (player.getInventory().containsAtLeast(skint.createItemStack(), 1)) {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && skint.isSkintItem(item)) {
                        item.setAmount(item.getAmount() - 1);
                        player.sendMessage(Component.text(config.getString("messages.pockets_lighter", "Вы чувствуете, как ваши карманы стали легче...")).color(NamedTextColor.DARK_RED));
                        consumed = true;
                        break;
                    }
                }
            }
            if (consumed) break;
        }

        if (!consumed) {
            skintNoSkintsObjective.getScore(player.getName()).setScore(1);

            Score skintMessageScore = skintMessageObjective.getScore(player.getName());
            if (skintMessageScore.getScore() == 0) {
                player.sendMessage(Component.text(config.getString("messages.consuming_life_force", "Скинтонит начинает поглощать вашу жизненную силу..."))
                        .color(NamedTextColor.DARK_RED)
                        .decorate(TextDecoration.BOLD));
                skintMessageScore.setScore(1);
            }

            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WITHER, 60, 1, true, false));
            player.damage(1.0, plugin.getServer().getEntity(player.getUniqueId()));
        }
    }

    private void lostLight(Player player) {
        String message = config.getString("messages.lost_light", "{player} потерял свой свет").replace("{player}", player.getName());
        Bukkit.getServer().broadcast(Component.text(message).color(NamedTextColor.WHITE));
        skintNoSkintsObjective.getScore(player.getName()).setScore(0);
    }
}