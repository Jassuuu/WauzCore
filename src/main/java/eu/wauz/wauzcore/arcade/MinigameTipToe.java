package eu.wauz.wauzcore.arcade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import eu.wauz.upward.generation.PathGenerator;
import net.md_5.bungee.api.ChatColor;

/**
 * A race minigame, where you have to find a hidden path among fake tiles.
 * 
 * @author Wauzmons
 */
public class MinigameTipToe implements ArcadeMinigame {
	
	/**
	 * A map of fake tiles, indexed by their blocks.
	 */
	private Map<Block, List<Block>> blockFakeTileMap = new HashMap<>();
	
	/**
	 * The players who have crossed the finish line.
	 */
	private List<Player> finishedPlayers = new ArrayList<>();
	
	/**
	 * The amount of players who can win the game.
	 */
	private int maxWinningPlayers = 1;

	/**
	 * @return The display name of the minigame.
	 */
	@Override
	public String getName() {
		return "TipToe";
	}

	/**
	 * @return The scoreboard description of the minigame.
	 */
	@Override
	public List<String> getDescription() {
		List<String> description = new ArrayList<>();
		description.add(ChatColor.WHITE + "Avoid Fake Tiles and");
		description.add(ChatColor.WHITE + "find the Hidden Paths");
		description.add(ChatColor.WHITE + "to reach the Finish Line!");
		description.add("   ");
		description.add(ChatColor.BLUE + "Qualified Players: " + ChatColor.GOLD + finishedPlayers.size() + " / " + maxWinningPlayers);
		return description;
	}

	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	@Override
	public void startGame(List<Player> players) {
		int startX = 0;
		int startZ = 0;
		int width = 5;
		int length = 5;
		int y = 0;
		World world = ArcadeLobby.getWorld();
		PathGenerator generator = new PathGenerator(width, length);
		generator.run();
		int[][] grid = generator.getPathMatrix();
		for(int x = 0; x < width; x++) {
			for(int z = 0; z < length; z++) {
				
			}
		}
		Location spawnLocation = new Location(world, 500.5, 88, 479.5, 0, 0);
		ArcadeUtils.placeTeam(players, spawnLocation);
		ArcadeUtils.runStartTimer(10, 180);
	}

	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	@Override
	public List<Player> endGame() {
		List<Player> winners = new ArrayList<>(finishedPlayers);
		blockFakeTileMap.clear();
		finishedPlayers.clear();
		maxWinningPlayers = 1;
		return winners;
	}
	
	/**
	 * Handles the given move event, that occured in the minigame.
	 * 
	 * @param event The move event.
	 */
	@Override
	public void handleMoveEvent(PlayerMoveEvent event) {
		Block blockBelow = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
		List<Block> tileBlocks = blockFakeTileMap.get(blockBelow);
		if(tileBlocks != null) {
			makeTileFall(tileBlocks);
		}
	}
	
	public void createTile(Location cornerLocation, boolean isFake) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Makes the given tile fall down and removes it from the map.
	 * 
	 * @param tileBlocks The blocks forming the tile.
	 */
	public void makeTileFall(List<Block> tileBlocks) {
		for(Block block : tileBlocks) {
			blockFakeTileMap.remove(block);
			block.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
			block.setType(Material.AIR);
		}
	}

}
