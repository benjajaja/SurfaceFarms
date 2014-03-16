package gipsyking.surfacefarms;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

public class PlantListener implements Listener {
	
	private ArrayList<Material> plants = new ArrayList<Material>(Arrays.asList(new Material[]{
			Material.CROPS, Material.POTATO, Material.CARROT, Material.NETHER_WARTS,
			Material.CACTUS, Material.SUGAR_CANE_BLOCK, Material.MELON_STEM, Material.PUMPKIN_STEM,
			Material.SAPLING, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM
	}));
	private ArrayList<Material> bonemealDisabledPlants = new ArrayList<Material>(Arrays.asList(new Material[]{
			Material.CROPS, Material.POTATO, Material.CARROT, Material.NETHER_WARTS,
			Material.CACTUS, Material.SUGAR_CANE_BLOCK, Material.MELON_STEM, Material.PUMPKIN_STEM,
			Material.SAPLING
	}));
	private ArrayList<Material> skyLightPlants = new ArrayList<Material>(Arrays.asList(new Material[]{
			Material.CROPS, Material.SAPLING
	}));
	private ArrayList<Material> glassHousePlants = new ArrayList<Material>(Arrays.asList(new Material[]{
			Material.POTATO, Material.CARROT,
			Material.CACTUS, Material.SUGAR_CANE_BLOCK,
			Material.MELON_STEM, Material.MELON_BLOCK,
			Material.PUMPKIN_STEM, Material.PUMPKIN
	}));
	private ArrayList<Material> transparentBlocks = new ArrayList<Material>(Arrays.asList(new Material[]{
		Material.AIR, Material.GLASS, Material.THIN_GLASS
	}));
	private World world;
	
	
	public PlantListener(World world) {
		this.world = world;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onGrow(BlockGrowEvent event) {
		if (canGrow(event.getNewState().getType(), event.getBlock().getLocation()) != null) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onTreeGrow(StructureGrowEvent event) {
		if (canGrow(Material.SAPLING, event.getLocation()) != null) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBoneMeal(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item != null && item.getType() == Material.INK_SACK && item.getDurability() == 15
				&& bonemealDisabledPlants.contains(event.getClickedBlock().getType())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("No puedes usar bonemeal con " + event.getClickedBlock().getType());
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getBlock().getWorld() != world) {
			return;
		}
		
		DenyReason reason = this.canGrow(event.getBlockPlaced().getType(), event.getBlock().getLocation());
		if (reason != null) {
			event.getPlayer().sendMessage(SurfaceFarms.i18n.reason(reason, event.getBlockPlaced().getType()));
			//event.setCancelled(true);
		}
	}
	
	

	private DenyReason canGrow(Material type, Location location) {
		if (plants.contains(type)) {
			if (location.getBlockY() > SurfaceFarms.MAX_HEIGHT) {
				return DenyReason.TOO_HIGH;
				
			} else if (location.getBlockY() < SurfaceFarms.MAX_DEPTH) {
				return DenyReason.TOO_DEEP;
				
			} else if (skyLightPlants.contains(type)) {
				if (SurfaceFarms.hasHigherBlockYAt(world, location.clone())) {
					return DenyReason.NO_DIRECT_SUNLIGHT;
				}
				
			} else if (glassHousePlants.contains(type)) {
				if (SurfaceFarms.hasHigherBlockYAt(world, location.clone(), transparentBlocks)) {
					return DenyReason.NO_GLASSHOUSE;
				}
			}
		}
		return null;
	}

	
}
