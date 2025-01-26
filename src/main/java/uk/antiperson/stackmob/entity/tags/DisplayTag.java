package uk.antiperson.stackmob.entity.tags;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

public class DisplayTag {

    private final StackEntity stackEntity;
    private TextDisplay display;
    private final StackMob sm;

    public DisplayTag(StackMob sm, StackEntity stackEntity) {
        this.stackEntity = stackEntity;
        this.sm = sm;
    }

    private StackEntity getStackEntity() {
        return stackEntity;
    }

    public void spawn() {
        sm.getLogger().info("spawning display");
        display = (TextDisplay) stackEntity.getWorld().spawnEntity(calculateLocation(stackEntity.getEntity().getLocation()), EntityType.TEXT_DISPLAY);
        display.setBillboard(Display.Billboard.CENTER);
        display.setTeleportDuration(1);
        if (getStackEntity().getEntityConfig().getTagMode() == StackEntity.TagMode.NEARBY) {
            float multiplier = stackEntity.getEntityConfig().getTagNearbyRadius() / readTrackingRange();
            display.setViewRange(multiplier);
        }
        updateName(stackEntity.getTag().getDisplayName());
    }

    private float readTrackingRange() {
        String path = sm.getServer().spigot().getSpigotConfig().isConfigurationSection("world-settings." + getStackEntity().getWorld().getName()) ?
                getStackEntity().getWorld().getName() : "default";
        String finalPath = "world-settings." + path + ".entity-tracking-range.display";
        return sm.getServer().spigot().getSpigotConfig().getInt(finalPath);
    }

    public void remove() {
        display.remove();
    }

    public boolean exists() {
        return display != null;
    }

    public void move(Location location) {
        display.teleport(calculateLocation(location));
    }

    private Location calculateLocation(Location location) {
        double adjustment = stackEntity.getEntity().customName() == null ? 0.3 : 0.5;
        if (stackEntity.getEntityConfig().getArmorstandOffset() > 0) {
            adjustment = stackEntity.getEntityConfig().getArmorstandOffset();
        }
        return location.add(0, stackEntity.getEntity().getHeight() + adjustment, 0);
    }

    public void updateName(Component name) {
        display.text(name);
    }
}
