package uk.antiperson.stackmob.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public interface Scheduler {
    void runGlobalTaskTimer(Runnable runnable, long delay, long period);
    void runTask(Location location, Runnable runnable);
    void runTask(Entity entity, Runnable runnable);
    void runTaskAsynchronously(Runnable runnable);
    void runTaskLater(Entity entity, Runnable runnable, long delay);
}
