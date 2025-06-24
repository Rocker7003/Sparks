package rocker.cherry.sparks2;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import rocker.cherry.sparks2.listeners.SkintLootListener;
import rocker.cherry.sparks2.listeners.SkintAnvilListener;
import rocker.cherry.sparks2.commands.SkintCommandExecutor;
import rocker.cherry.sparks2.skint.SkintManager;
import rocker.cherry.sparks2.skint.SkintCraftingRecipes;
import net.kyori.adventure.text.Component;


public final class Sparks2 extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Sparks2 plugin has been enabled!");

        registerListeners();

        getCommand("skint").setExecutor(new SkintCommandExecutor());

        initScoreboards();

        SkintManager skintManager = new SkintManager(this);
        skintManager.startSkintTimer();

        SkintCraftingRecipes.registerRecipes(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Sparks2 plugin has been disabled.");
    }


    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new SkintLootListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SkintAnvilListener(this), this);
    }


    private void initScoreboards() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) {
            getLogger().severe("ScoreboardManager is null! Cannot initialize scoreboards.");
            return;
        }
        Scoreboard scoreboard = manager.getMainScoreboard();


        getOrCreateObjective(scoreboard, "skint_has_item", "dummy");
        getOrCreateObjective(scoreboard, "skint_death", "deathCount");
        getOrCreateObjective(scoreboard, "skint_timer", "dummy");
        getOrCreateObjective(scoreboard, "skint_no_skints", "dummy");
        getOrCreateObjective(scoreboard, "skint_message", "dummy");
    }


    private Objective getOrCreateObjective(Scoreboard scoreboard, String name, String criteriaName) {
        Objective objective = scoreboard.getObjective(name);
        if (objective == null) {
            Criteria criteria = "deathCount".equals(criteriaName) ? Criteria.DEATH_COUNT : Criteria.DUMMY;
            objective = scoreboard.registerNewObjective(name, criteria, Component.text(name));
        }
        return objective;
    }
}
