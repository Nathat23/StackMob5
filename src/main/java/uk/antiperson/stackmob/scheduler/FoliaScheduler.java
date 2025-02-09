package uk.antiperson.stackmob.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class FoliaScheduler implements Scheduler {

    private final Plugin plugin;

    public FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runGlobalTaskTimer(Runnable runnable, long delay, long period) {
        plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), (int) delay, (int) period);
    }

    @Override
    public void runTask(Location location, Runnable runnable) {
        plugin.getServer().getRegionScheduler().run(plugin, location, scheduledTask -> runnable.run());
    }

    @Override
    public void runTask(Entity entity, Runnable runnable) {
        entity.getScheduler().run(plugin, scheduledTask -> runnable.run(), () -> {});
    }

    @Override
    public void runTaskAsynchronously(Runnable runnable) {
        plugin.getServer().getAsyncScheduler().runNow(plugin, scheduledTask -> runnable.run());
    }

    @Override
    public void runTaskLater(Entity entity, Runnable runnable, long delay) {
        entity.getScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), () -> {}, (int) delay);
    }
}
