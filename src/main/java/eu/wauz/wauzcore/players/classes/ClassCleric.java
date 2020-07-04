package eu.wauz.wauzcore.players.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.skills.SkillTheHighPriestess;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkill;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * A class, that can be chosen by a player.
 * Master of healing and staff fighting.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
public class ClassCleric implements WauzPlayerClass {

	/**
	 * @return The name of the class.
	 */
	@Override
	public String getClassName() {
		return "Cleric";
	}
	
	/**
	 * @return The description of the class.
	 */
	@Override
	public String getClassDescription() {
		return "Clerics are devoted to the spiritual, and express their unwavering faith by serving the people. For millennia they have left behind the confines of their temples and shrines so they can support their allies.";
	}

	/**
	 * @return The color associated with the class.
	 */
	@Override
	public ChatColor getClassColor() {
		return ChatColor.BLUE;
	}

	/**
	 * @return The item stack representing the class.
	 */
	@Override
	public ItemStack getClassItemStack() {
		return HeadUtils.getClericItem();
	}

	/**
	 * @return The highest weight armor category the class can wear.
	 */
	@Override
	public ArmorCategory getArmorCategory() {
		return ArmorCategory.LIGHT;
	}

	/**
	 * @return The starting stats and passive skills of the class.
	 */
	@Override
	public WauzPlayerClassStats getStartingStats() {
		WauzPlayerClassStats stats = new WauzPlayerClassStats();
		stats.setStaffSkill(135000);
		stats.setStaffSkillMax(250000);
		stats.setSwordSkill(80000);
		return stats;
	}

	@Override
	public ItemStack getStartingWeapon() {
		WauzPlayerSkill skill = new SkillTheHighPriestess();
		return WauzDebugger.getSkillgemWeapon(skill, Material.DIAMOND_HOE, false);
	}

}
