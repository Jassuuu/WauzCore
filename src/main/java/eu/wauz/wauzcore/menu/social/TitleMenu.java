package eu.wauz.wauzcore.menu.social;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.system.WauzTitle;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the social menu, that is used for selecting chat titles.
 * 
 * @author Wauzmons
 *
 * @see WauzTitle
 */
public class TitleMenu implements WauzInventory {

	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "titles";
	}

	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		TitleMenu.open(player);
	}
	
	/**
	 * Opens the menu for the given player.
	 * A list of all titles to choose from will be shown.
	 * The menu lets the player choose from all unlocked titles.
	 * Locked titles can be bought here aswell, if requirements are met.
	 * 
	 * @param player The player that should view the inventory.
	 */
	public static void open(Player player) {
		List<WauzTitle> titles = WauzTitle.getAllTitles();
		int inventorySize = MenuUtils.roundInventorySize(titles.size() + 18);
		WauzInventoryHolder holder = new WauzInventoryHolder(new TitleMenu());
		Inventory menu = Bukkit.createInventory(holder, inventorySize, ChatColor.BLACK + "" + ChatColor.BOLD + "Title Collection");
		
		List<String> unlockedTitles = PlayerConfigurator.getCharacterTitleList(player);
		String currentTitle = PlayerConfigurator.getCharacterTitle(player);
		
		int titleNumber = 9;
		for(WauzTitle title : titles) {
			if(titleNumber + 9 >= inventorySize) {
				break;
			}
			
			String titleName = title.getTitleName();
			boolean unlocked = unlockedTitles.contains(titleName);
			boolean selected = StringUtils.equals(titleName, currentTitle);
			
			ItemStack titleItemStack = unlocked ? HeadUtils.getTitlesItem() : HeadUtils.getDeclineItem();
			ItemMeta titleItemMeta = titleItemStack.getItemMeta();
			titleItemMeta.setDisplayName(unlocked ? ChatColor.GREEN + "Unlocked" : ChatColor.RED + "Locked");
			List<String> titleLores = new ArrayList<>();
			titleLores.add(ChatColor.YELLOW + "Title: " + title.getTitleDisplayName());
			titleLores.add(ChatColor.GRAY + "Title-ID: " + titleName);
			titleLores.add(ChatColor.GRAY + "Required Level: " + title.getTitleLevel());
			titleLores.add("");
			if(unlocked) {
				if(selected) {
					titleLores.add(ChatColor.GRAY + "Currently Selected");
					titleItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
					titleItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				else {
					titleLores.add(ChatColor.GRAY + "Click to Select");
				}
			}
			else {
				titleLores.add(ChatColor.GRAY + "Click to Buy for " + title.getTitleCost() + " Soulstones");
			}
			titleItemMeta.setLore(titleLores);
			titleItemStack.setItemMeta(titleItemMeta);
			menu.setItem(titleNumber, titleItemStack);
			titleNumber++;
		}
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}

	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * The default event will be automatically canceled.
	 * If an already owned title is clicked, it will be selected.
	 * If another title is clicked and requirements are met, it will be bought.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PlayerConfigurator#setCharacterTitle(Player, String)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !ItemUtils.hasLore(clicked)) {
			return;
		}
		
		String titleName = ItemUtils.getStringFromLore(clicked, "Title-ID", 1);
		WauzTitle title = WauzTitle.getTitle(titleName);
		if(title == null) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Unlocked")) {
			int level = PlayerConfigurator.getCharacterLevel(player);
			if(level < title.getTitleLevel()) {
				player.sendMessage(ChatColor.RED + "You don't meet the level requirement for this title!");
				player.closeInventory();
				return;
			}
			PlayerConfigurator.setCharacterTitle(player, titleName);
			String newTitle = WauzTitle.getTitle(player);
			player.sendMessage(ChatColor.GREEN + "Your chat title was changed to \"" + newTitle + "\"!");
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Locked")) {
			long souls = PlayerConfigurator.getCharacterSoulstones(player);
			if(souls < title.getTitleCost()) {
				player.sendMessage(ChatColor.RED + "You don't have enough soulstones to unlock this title!");
				player.closeInventory();
				return;
			}
		}
	}

}