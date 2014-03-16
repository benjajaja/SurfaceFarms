package gipsyking.surfacefarms;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MobListener implements Listener {

	private World world;
	
	// "informable" meaning a nearby player is definitely near and can be messaged. Eggs may come froom dispensers.
	private static ArrayList<SpawnReason> informableReasons = new ArrayList<SpawnReason>(Arrays.asList(new SpawnReason[]{
			SpawnReason.BREEDING, SpawnReason.BUILD_IRONGOLEM, SpawnReason.BUILD_SNOWMAN, SpawnReason.SPAWNER_EGG
	}));
	
	private static ArrayList<EntityType> heightRestricted = new ArrayList<EntityType>(Arrays.asList(new EntityType[]{
			EntityType.CHICKEN, EntityType.COW, EntityType.EGG, EntityType.HORSE, EntityType.IRON_GOLEM,
			EntityType.OCELOT, EntityType.PIG, EntityType.SHEEP, EntityType.SNOWMAN, EntityType.WOLF
	}));
	private static ArrayList<EntityType> lightingRestricted = new ArrayList<EntityType>(Arrays.asList(new EntityType[]{
			EntityType.COW, EntityType.HORSE, EntityType.PIG, EntityType.SHEEP
	}));

	public MobListener(World world) {
		this.world = world;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Location location = event.getLocation();
		
		DenyReason reason = canSpawn(location, event.getEntityType());
		if (reason != null) {
			event.setCancelled(true);
			if (informableReasons.contains(event.getSpawnReason())) {
				Player closestPlayer = null;
				double distance = Double.MAX_VALUE;
				
				for (Player player: world.getPlayers()) {
					double pDist = player.getLocation().distance(location);
					
					if (pDist < distance) {
						closestPlayer = player;
					}
				}
				
				if (closestPlayer != null) {
					closestPlayer.sendMessage(SurfaceFarms.i18n.spawnReason(reason, event.getEntityType()));
				}
			}
		}
	}

	private DenyReason canSpawn(Location location, EntityType type) {
		if (heightRestricted.contains(type) && location.getBlockY() > SurfaceFarms.MAX_HEIGHT) {
			return DenyReason.TOO_HIGH;
			
		} else if (heightRestricted.contains(type) && location.getBlockY() < SurfaceFarms.MAX_DEPTH) {
			return DenyReason.TOO_DEEP;
			
		} else if (lightingRestricted.contains(type) && SurfaceFarms.hasHigherBlockYAt(world, location)) {
			return DenyReason.NO_DIRECT_SUNLIGHT;
			
		}
		return null;
	}

}
