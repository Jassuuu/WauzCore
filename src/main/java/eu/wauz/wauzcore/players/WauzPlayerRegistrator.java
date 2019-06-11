package eu.wauz.wauzcore.players;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.PlayerConfigurator;
import eu.wauz.wauzcore.system.nms.WauzNmsClient;
import eu.wauz.wauzcore.system.util.WauzDateUtils;
import net.md_5.bungee.api.ChatColor;

public class WauzPlayerRegistrator {

	private static WauzCore core = WauzCore.getInstance();

	public static void login(final Player player) throws Exception {
		PermissionAttachment attachment = player.addAttachment(core);
		File playerDirectory = new File(core.getDataFolder(), "PlayerData/" + player.getUniqueId() + "/");
		playerDirectory.mkdir();
		File playerDataFile = new File(playerDirectory, "global.yml");
		FileConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

// Create new Player-Config
		
		playerDataConfig.set("name", player.getName());
		playerDataConfig.set("lastplayed", System.currentTimeMillis());
		
		if(!playerDataFile.exists()) {
			playerDataConfig.set("rank", "Normal");
			if(player.hasPermission("wauz.system"))
				playerDataConfig.set("rank", "Admin");
			
			playerDataConfig.set("guild", "none");
			
			playerDataConfig.set("tokens", 0);
			playerDataConfig.set("tokenlimit.survival.date", WauzDateUtils.getDateLong());
			playerDataConfig.set("tokenlimit.survival.amount", 0);
			playerDataConfig.set("tokenlimit.mmorpg.date", WauzDateUtils.getDateLong());
			playerDataConfig.set("tokenlimit.mmprgp.amount", 0);
			playerDataConfig.set("score.survival", 0);
		}
		playerDataConfig.save(playerDataFile);

// Set up Inventory

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WauzCore.getInstance(), new Runnable() {
            public void run() {
        		player.setHealth(20);
        		player.setFoodLevel(20);
        		player.setSaturation(20);
        		player.getInventory().clear();
        		player.setExp(0);
        		player.setLevel(0);
        		
        		player.setBedSpawnLocation(WauzCore.getHubLocation(), true);
        		player.teleport(WauzCore.getHubLocation());
        		
        		player.sendMessage("Welcome to Wauzland! v" + core.getDescription().getVersion());
        		WauzPlayerDataPool.regPlayer(player);
        		WauzPlayerScoreboard.scheduleScoreboard(player);
            }
		}, 10);

// Load Permissions
		
		attachment.setPermission("dt.travel.*", true);
		attachment.setPermission("discordmc.chat", true);
	}
	
	public static void logout(Player player) {
		PlayerConfigurator.setLastPlayed(player);
		CharacterManager.logoutCharacter(player);
	}
	
	public static void respawn(final Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() { public void run() {
        	WauzNmsClient.nmsRepsawn(player);
        	player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "YOU DIED", "", 10, 70, 20);
        }});
		
		WauzPlayerData pd = WauzPlayerDataPool.getPlayer(player);
		if(pd == null) return;
		
		pd.setResistanceHeat((short) 0);
		pd.setResistanceCold((short) 0);
		pd.setResistancePvsP((short) 0);
		WauzPlayerActionBar.update(player);
	}

}