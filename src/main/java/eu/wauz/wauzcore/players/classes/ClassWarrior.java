package eu.wauz.wauzcore.players.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.wauz.wauzcore.items.enums.ArmorCategory;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentHelper;
import eu.wauz.wauzcore.menu.heads.CharacterIconHeads;
import eu.wauz.wauzcore.players.classes.warrior.SubclassBerserker;
import eu.wauz.wauzcore.players.classes.warrior.SubclassBulwark;
import eu.wauz.wauzcore.players.classes.warrior.SubclassPaladin;
import eu.wauz.wauzcore.players.classes.warrior.SubclassTemplar;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.skillgems.SkillTheChariot;
import eu.wauz.wauzcore.system.annotations.CharacterClass;

/**
 * A class, that can be chosen by a player.
 * Master of defense and axe combat.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClassPool
 */
@CharacterClass
public class ClassWarrior extends BaseClass {
	
	/**
	 * Constructs a new instance of the class and initializes its subclasses.
	 * 
	 * @see BaseClass#registerSubclass(WauzPlayerSubclass)
	 */
	public ClassWarrior() {
		registerSubclass(new SubclassBerserker());
		registerSubclass(new SubclassBulwark());
		registerSubclass(new SubclassTemplar());
		registerSubclass(new SubclassPaladin());
	}

	/**
	 * @return The name of the class.
	 */
	@Override
	public String getClassName() {
		return "Warrior";
	}
	
	/**
	 * @return The description of the class.
	 */
	@Override
	public String getClassDescription() {
		return "For as long as war has raged, heroes from every race have aimed to master the art of battle. Warriors combine strength, leadership, and a vast knowledge of weapons to wreak havoc in glorious combat.";
	}

	/**
	 * @return The color associated with the class.
	 */
	@Override
	public ChatColor getClassColor() {
		return ChatColor.YELLOW;
	}

	/**
	 * @return The item stack representing the class.
	 */
	@Override
	public ItemStack getClassItemStack() {
		return CharacterIconHeads.getWarriorItem();
	}

	/**
	 * @return The highest weight armor category the class can wear.
	 */
	@Override
	public ArmorCategory getArmorCategory() {
		return ArmorCategory.HEAVY;
	}
	
	/**
	 * @return The starting stats and passive skills of the class.
	 */
	@Override
	public WauzPlayerClassStats getStartingStats() {
		WauzPlayerClassStats stats = new WauzPlayerClassStats();
		stats.setAxeSkill(135000);
		stats.setAxeSkillMax(250000);
		stats.setStaffSkill(80000);
		return stats;
	}

	@Override
	public ItemStack getStartingWeapon() {
		WauzPlayerSkill skill = new SkillTheChariot();
		return WauzEquipmentHelper.getSkillgemWeapon(skill, Material.DIAMOND_AXE, false);
	}

}
