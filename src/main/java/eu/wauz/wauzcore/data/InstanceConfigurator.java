package eu.wauz.wauzcore.data;

import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.GlobalConfigurationUtils;

/**
 * Configurator to fetch or modify data from the Instance.yml files.
 * 
 * @author Wauzmons
 */
public class InstanceConfigurator extends GlobalConfigurationUtils {
	
// Instance Files

	/**
	 * @return The list of instance shop names.
	 */
	public static List<String> getInstanceNameList() {
		return GlobalConfigurationUtils.getInstanceNameList();
	}

// General Parameters
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The name of the world template of the instance.
	 */
	public static String getWorldTemplateName(String instanceName) {
		return instanceConfigGetString(instanceName, "world");
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The maximum players of the instance.
	 */
	public static int getMaximumPlayers(String instanceName) {
		int maxPlayers = instanceConfigGetInt(instanceName, "maxplayers");
		return maxPlayers > 0 ? maxPlayers : 5;
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The maximum deaths per player of the instance.
	 */
	public static int getMaximumDeaths(String instanceName) {
		int maxDeaths = instanceConfigGetInt(instanceName, "maxdeaths");
		return maxDeaths > 0 ? maxDeaths : 3;
	}
	
// Type Specific
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The type of the instance.
	 */
	public static String getInstanceType(String instanceName) {
		String instanceType = instanceConfigGetString(instanceName, "type");
		return StringUtils.isNotBlank(instanceType) ? instanceType : "Unknown";
	}
	
	/**
	 * @param instanceName The name of the instance.
	 * 
	 * @return The list of key names of the instance.
	 */
	public static List<String> getKeyNameList(String instanceName) {
		return instanceConfigGetStringList(instanceName, "keys");
	}
	
// World Specific
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The name of the instance world.
	 */
	public static String getInstanceWorldName(World world) {
		return instanceWorldConfigGetString(world, "name");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param worldName The new name of the instance world.
	 */
	public static void setInstanceWorldName(World world, String worldName) {
		instanceWorldConfigSet(world, "name", worldName);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The type of the instance world.
	 */
	public static String getInstanceWorldType(World world) {
		return instanceWorldConfigGetString(world, "type");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param worldType The new type of the instance world.
	 */
	public static void setInstanceWorldType(World world, String worldType) {
		instanceWorldConfigSet(world, "type", worldType);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The maximum players of the instance world.
	 */
	public static int getInstanceWorldMaximumPlayers(World world) {
		return instanceWorldConfigGetInt(world, "maxplayers");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param maxPlayers The new maximum players of the instance world.
	 */
	public static void setInstanceWorldMaximumPlayers(World world, int maxPlayers) {
		instanceWorldConfigSet(world, "maxplayers", maxPlayers);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The maximum deaths per player of the instance world.
	 */
	public static int getInstanceWorldMaximumDeaths(World world) {
		return instanceWorldConfigGetInt(world, "maxdeaths");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param maxDeaths The new maximum deaths per player of the instance world.
	 */
	public static void setInstanceWorldMaximumDeaths(World world, int maxDeaths) {
		instanceWorldConfigSet(world, "maxdeaths", maxDeaths);
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param player The player that may have died.
	 * 
	 * @return The amount of times a player died in the instance world.
	 */
	public static int getInstanceWorldPlayerDeathCount(World world, Player player) {
		return instanceWorldConfigGetInt(world, "deaths." + player.getUniqueId().toString());
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param player The player that may have died.
	 * @param deathCount The new amount of times a player died in the instance world.
	 */
	public static void setInstanceWorldPlayerDeathCount(World world, Player player, int deathCount) {
		instanceWorldConfigSet(world, "deaths." + player.getUniqueId().toString(), deathCount);
	}
	
	/**
	 * @param world The world of the active instance.
	 * 
	 * @return The list of key names of the instance world.
	 */
	public static Set<String> getInstanceWorldKeyIds(World world) {
		return instanceWorldConfigGetKeys(world, "keys");
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param keyId The name of the key.
	 * 
	 * @return The status of the key.
	 */
	public static String getInstanceKeyStatus(World world, String keyId) {
		return instanceWorldConfigGetString(world, "keys." + keyId);
	}
	
	/**
	 * @param world The world of the active instance.
	 * @param keyId The name of the key.
	 * @param keyStatus The new status of the key.
	 */
	public static void setInstanceWorldKeyStatus(World world, String keyId, String keyStatus) {
		instanceWorldConfigSet(world, "keys." + keyId, keyStatus);
	}

}
