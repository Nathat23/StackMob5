package uk.antiperson.stackmob.listeners;

import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.StackEntity;
import uk.antiperson.stackmob.entity.EntityFood;
import uk.antiperson.stackmob.utils.EntityUtils;

@ListenerMetadata(config = "events.breed.enabled")
public class BreedInteractListener implements Listener {

    private final StackMob sm;
    public BreedInteractListener(StackMob sm) {
        this.sm = sm;
    }

    @EventHandler
    public void onBreedInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getRightClicked() instanceof Animals)) {
            return;
        }
        Animals animals = (Animals) event.getRightClicked();
        if (!animals.canBreed()) {
            return;
        }
        ItemStack foodItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!EntityFood.isCorrectFood(event.getRightClicked(), foodItem.getType())) {
            return;
        }
        StackEntity stackEntity = sm.getEntityManager().getStackEntity(animals);
        if (stackEntity == null || stackEntity.isSingle()) {
            return;
        }
        ListenerMode breed = sm.getMainConfig().getListenerMode(animals.getType(), "breed");
        if (breed == ListenerMode.SPLIT) {
            stackEntity.slice();
            return;
        }
        int itemAmount = event.getPlayer().getInventory().getItemInMainHand().getAmount();
        stackEntity.splitIfNotEnough(itemAmount);
        if (itemAmount == 1) {
            EntityUtils.removeHandItem(event.getPlayer(), 1);
            return;
        }
        int kidAmount = sm.getMainConfig().getEventMultiplyLimit(animals.getType(), "breed", stackEntity.getSize() / 2);
        int parentAmount = kidAmount * 2;
        if (stackEntity.getSize() > parentAmount) {
            stackEntity.slice(parentAmount);
        }
        EntityUtils.removeHandItem(event.getPlayer(), parentAmount);
        stackEntity.getDrops().dropExperience(event.getRightClicked().getLocation(),1,7, kidAmount);
        // Spawn the kid
        StackEntity kid = stackEntity.duplicate();
        kid.setSize(kidAmount);
        ((Animals) kid.getEntity()).setBaby();
        // Update the adult
        animals.setBreed(false);
        animals.setBreedCause(event.getPlayer().getUniqueId());
    }
}
