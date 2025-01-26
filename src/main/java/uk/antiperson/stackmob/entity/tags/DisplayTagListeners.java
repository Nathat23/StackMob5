package uk.antiperson.stackmob.entity.tags;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;

public class DisplayTagListeners implements Listener {

    private final StackMob sm;
    public DisplayTagListeners(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (!event.hasChangedPosition()) {
            return;
        }
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(event.getEntity());
        if (stackEntity == null) {
            return;
        }
        if (stackEntity.getDisplayTag().exists()) {
            stackEntity.getDisplayTag().move(event.getTo().clone());
            return;
        }
        stackEntity.getTag().update();
    }
}
