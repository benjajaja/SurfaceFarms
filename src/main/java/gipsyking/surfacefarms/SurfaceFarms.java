package gipsyking.surfacefarms;


import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SurfaceFarms extends JavaPlugin {

	public static final int MAX_HEIGHT = 128;
	public static final int MAX_DEPTH = 48;
	public static SFi18n i18n = new SFi18n();
	
	private static SurfaceFarms _singleton_for_debug_only;

	@Override
	public void onEnable() {
		_singleton_for_debug_only = this;
		
		getServer().getPluginManager().registerEvents(new PlantListener(getServer().getWorld("world")), this);
		getServer().getPluginManager().registerEvents(new MobListener(getServer().getWorld("world")), this);
	}
	
	public static void debug(String string) {
		_singleton_for_debug_only.getLogger().info(string);
		for (Player player: _singleton_for_debug_only.getServer().getWorld("world").getPlayers()) {
			player.sendMessage(string);
		}
	}
	
	public static boolean hasHigherBlockYAt(World world, Location location) {
		int y = location.getBlockY();
		while(y < world.getMaxHeight() - 1) {
			y += 1;
			location.setY(y);
			
			if (world.getBlockAt(location).getType() != Material.AIR) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasHigherBlockYAt(World world, Location location, ArrayList<Material> allowed) {
		int y = location.getBlockY();
		while(y < world.getMaxHeight() - 1) {
			y += 1;
			location.setY(y);
			
			if (!allowed.contains(world.getBlockAt(location).getType())) {
				return true;
			}
		}
		return false;
	}
}
