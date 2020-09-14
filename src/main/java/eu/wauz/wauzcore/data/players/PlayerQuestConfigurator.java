package eu.wauz.wauzcore.data.players;

import org.bukkit.entity.Player;

import eu.wauz.wauzcore.data.api.PlayerQuestConfigurationUtils;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;

/**
 * Configurator to fetch or modify data from the PlayerQuest.yml files.
 * 
 * @author Wauzmons
 */
public class PlayerQuestConfigurator extends PlayerQuestConfigurationUtils {
	
// General Parameters
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * 
	 * @return The phase of the quest.
	 */
	public static int getQuestPhase(Player player, String quest) {
		return playerQuestConfigGetInt(player, quest, "phase");
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param phase The new phase of the quest.
	 */
	public static void setQuestPhase(Player player, String quest, int phase) {
		playerQuestConfigSet(player, quest, "phase", phase);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param phase The phase of the quest.
	 * @param requirement The requirement of the phase of the quest.
	 * 
	 * @return The stored value for the requirement.
	 */
	public static long getQuestPhaseRequirementValue(Player player, String quest, int phase, int requirement) {
		return playerQuestConfigGetLong(player, quest, "requirement." + phase + "-" + requirement);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * @param phase The phase of the quest.
	 * @param requirement The requirement of the phase of the quest.
	 * @param value The new stored value for the requirement.
	 */
	public static void setQuestPhaseRequirementValue(Player player, String quest, int phase, int requirement, long value) {
		playerQuestConfigSet(player, quest, "requirement." + phase + "-" + requirement, value);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * 
	 * @return The amount the quest was completed.
	 */
	public static int getQuestCompletions(Player player, String quest) {
		return playerQuestConfigGetInt(player, quest, "completions");
	}
	
	/**
	 * Increases the quest completions by 1.
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 */
	public static void addQuestCompletions(Player player, String quest) {
		playerQuestConfigSet(player, quest, "completions", getQuestCompletions(player, quest) + 1);
		AchievementTracker.addProgress(player, WauzAchievementType.COMPLETE_QUESTS, 1);
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * 
	 * @return If the quest has been completed at least once.
	 */
	public static boolean isQuestCompleted(Player player, String quest) {
		return getQuestCompletions(player, quest) > 0;
	}
	
	/**
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 * 
	 * @return The cooldown of the quest. (Last time it was completed)
	 */
	public static long getQuestCooldown(Player player, String quest) {
		return playerQuestConfigGetLong(player, quest, "cooldown");
	}
	
	/**
	 * Updates the cooldown of the quest to now. (Last time it was completed)
	 * 
	 * @param player The player that owns the config file.
	 * @param quest The quest that the config belongs to.
	 */
	public static void setQuestCooldown(Player player, String quest) {
		playerQuestConfigSet(player, quest, "cooldown", System.currentTimeMillis());
	}

}
