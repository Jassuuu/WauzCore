package eu.wauz.wauzcore.arcade;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * A minigame playable in the arcade mode.
 * 
 * @author Wauzmons
 */
public interface ArcadeMinigame {
	
	/**
	 * @return The display name of the minigame.
	 */
	public String getName();
	
	/**
	 * @return The scoreboard description of the minigame.
	 */
	public List<String> getDescription();
	
	/**
	 * Starts a new game.
	 * 
	 * @param players The players who participate.
	 */
	public void startGame(List<Player> players);
	
	/**
	 * Ends the game and decides a winner.
	 * 
	 * @return The players wo won the game.
	 */
	public List<Player> endGame();
	
	/**
	 * Handles the start event, that gets fired when the start countdown ends.
	 */
	public default void handleStartEvent() {
		
	}
	
	/**
	 * Handles the given quit event, that occured in the minigame.
	 * 
	 * @param player The player who quit.
	 */
	public default void handleQuitEvent(Player player) {
		
	}
	
	/**
	 * Handles the given death event, that occured in the minigame.
	 * 
	 * @param event The death event.
	 */
	public default void handleDeathEvent(PlayerDeathEvent event) {
		
	}
	
	/**
	 * Handles the given damage event, that occured in the minigame.
	 * 
	 * @param event The damage event.
	 */
	public default void handleDamageEvent(EntityDamageEvent event) {
		
	}
	
	/**
	 * Handles the given projectile hit event, that occured in the minigame.
	 * 
	 * @param event The projectile hit event.
	 */
	public default void handleProjectileHitEvent(ProjectileHitEvent event) {
		
	}
	
	/**
	 * Handles the given interact event, that occured in the minigame.
	 * 
	 * @param event The interact event.
	 */
	public default void handleInteractEvent(PlayerInteractEvent event) {
		
	}
	
	/**
	 * Handles the given animation event, that occured in the minigame.
	 * 
	 * @param event The animation event.
	 */
	public default void handleAnimationEvent(PlayerAnimationEvent event) {
		
	}
	
	/**
	 * Handles the given move event, that occured in the minigame.
	 * 
	 * @param event The move event.
	 */
	public default void handleMoveEvent(PlayerMoveEvent event) {
		
	}
	
	/**
	 * A method that is called every second of the minigame.
	 */
	public default void handleTick() {
		
	}

}
