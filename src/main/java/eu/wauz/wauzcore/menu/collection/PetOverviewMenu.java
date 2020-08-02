package eu.wauz.wauzcore.menu.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.data.players.PlayerConfigurator;
import eu.wauz.wauzcore.data.players.PlayerPetsConfigurator;
import eu.wauz.wauzcore.items.util.ItemUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.players.WauzPlayerDataPool;
import eu.wauz.wauzcore.players.calc.SpeedCalculator;
import eu.wauz.wauzcore.system.achievements.AchievementTracker;
import eu.wauz.wauzcore.system.achievements.WauzAchievementType;
import eu.wauz.wauzcore.system.util.Chance;
import eu.wauz.wauzcore.system.util.WauzMode;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * Sub menu of the collection menu, that is used to view and organize your pets.
 * 
 * @author Wauzmons
 *
 * @see PetOptionsMenu
 */
public class PetOverviewMenu implements WauzInventory {
	
	/**
	 * A map that contains all players with active pets, indexed by pet uuid.
	 */
	private static Map<String, Player> petOwnerMap = new HashMap<>();
	
	/**
	 * @return The id of the inventory.
	 */
	@Override
	public String getInventoryId() {
		return "pets";
	}
	
	/**
	 * Opens a new inventory of this type for the given player.
	 * 
	 * @param player The player that should view the inventory.
	 */
	@Override
	public void openInstance(Player player) {
		PetOverviewMenu.open(player, -1);
	}
	
// Pet Overview Menu
	
	/**
	 * Opens the menu for the given player.
	 * Shows all pets in the posession of the player, aswell as an option to unsummon the active pet.
	 * Also shows the breeding station, with a counter, that shows when the offspring will hatch.
	 * 
	 * @param player The player that should view the inventory.
	 * @param highlightedSlot The slot id to highlight a pet or -1 for none.
	 * 
	 * @see PlayerConfigurator#getCharacterPetType(Player, int)
	 * @see PlayerConfigurator#getCharacterPetBreedingHatchTime(Player)
	 */
	public static void open(Player player, int highlightedSlot) {
		WauzInventoryHolder holder = new WauzInventoryHolder(new PetOverviewMenu());
		Inventory menu = Bukkit.createInventory(holder, 9, ChatColor.BLACK + "" + ChatColor.BOLD + "Pet Overview");
		
		ItemStack emptySlotItemStack = new ItemStack(Material.BARRIER);
		ItemMeta emptySlotItemMeta = emptySlotItemStack.getItemMeta();
		emptySlotItemMeta.setDisplayName(ChatColor.RED + "Empty Pet Slot");
		emptySlotItemStack.setItemMeta(emptySlotItemMeta);
		
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String petType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
			if(!petType.equals("none")) {
				ItemStack petItemStack = new ItemStack(highlightedSlot == petSlot ? Material.PARROT_SPAWN_EGG : Material.CHICKEN_SPAWN_EGG);
				ItemMeta petItemMeta = petItemStack.getItemMeta();
				petItemMeta.setDisplayName(ChatColor.GREEN + petType);
				List<String> lores = new ArrayList<String>();
				lores.add(ChatColor.GRAY + "Click for Pet-Options");
				lores.add("");
				lores.add(ChatColor.DARK_GRAY + "Index: " + petSlot);
				petItemMeta.setLore(lores);
				petItemStack.setItemMeta(petItemMeta);
				menu.setItem(petSlot, petItemStack);
			}
			else {
				menu.setItem(petSlot, emptySlotItemStack);
			}
		}
		
		ItemStack unsummonItemStack = new ItemStack(Material.STRING);
		ItemMeta unsummonItemMeta = unsummonItemStack.getItemMeta();
		unsummonItemMeta.setDisplayName(ChatColor.RED + "Unsummon Active Pet");
		unsummonItemStack.setItemMeta(unsummonItemMeta);
		menu.setItem(5, unsummonItemStack);
		
		String parentAPetType = PlayerPetsConfigurator.getCharacterPetType(player, 6);
		boolean parentSlotAIsEmpty = parentAPetType.equals("none");
		
		ItemStack parentAItemStack = new ItemStack(parentSlotAIsEmpty ? Material.GHAST_SPAWN_EGG : Material.SHEEP_SPAWN_EGG);
		ItemMeta parentAItemMeta = parentAItemStack.getItemMeta();
		parentAItemMeta.setDisplayName(parentSlotAIsEmpty ? ChatColor.RED + "Free Breeding Slot" : ChatColor.GREEN + parentAPetType);
		List<String> parentALores = new ArrayList<String>();
		parentALores.add(ChatColor.GRAY + "Breeding Slot A");
		parentAItemMeta.setLore(parentALores);
		parentAItemStack.setItemMeta(parentAItemMeta);
		menu.setItem(6, parentAItemStack);
		
		String parentBPetType = PlayerPetsConfigurator.getCharacterPetType(player, 8);
		boolean parentSlotBIsEmpty = parentBPetType.equals("none");
		
		ItemStack parentBItemStack = new ItemStack(parentSlotBIsEmpty ? Material.GHAST_SPAWN_EGG : Material.SHEEP_SPAWN_EGG);
		ItemMeta parentBItemMeta = parentBItemStack.getItemMeta();
		parentBItemMeta.setDisplayName(parentSlotBIsEmpty ? ChatColor.RED + "Free Breeding Slot" : ChatColor.GREEN + parentBPetType);
		List<String> parentBLores = new ArrayList<String>();
		parentBLores.add(ChatColor.GRAY + "Breeding Slot B");
		parentBItemMeta.setLore(parentBLores);
		parentBItemStack.setItemMeta(parentBItemMeta);
		menu.setItem(8, parentBItemStack);
		
		ItemStack childItemStack = new ItemStack(Material.EGG);
		ItemMeta childItemMeta = childItemStack.getItemMeta();
		long hatchTime = PlayerPetsConfigurator.getCharacterPetBreedingHatchTime(player);
		if(hatchTime == 0) {
			childItemMeta.setDisplayName(ChatColor.RED + "No Egg in Breeding Station");
		}
		else if(hatchTime > System.currentTimeMillis()) {
			hatchTime += 1000 - System.currentTimeMillis();
			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(hatchTime),
		            TimeUnit.MILLISECONDS.toMinutes(hatchTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(hatchTime)),
		            TimeUnit.MILLISECONDS.toSeconds(hatchTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(hatchTime)));
			childItemMeta.setDisplayName(ChatColor.YELLOW + "Breeding... " + hms + " Hours Remain");
		}
		else {
			childItemMeta.setDisplayName(ChatColor.GREEN + "Click to hatch Pet");
		}
		childItemStack.setItemMeta(childItemMeta);
		menu.setItem(7, childItemStack);
		
		player.openInventory(menu);
	}
	
	/**
	 * Checks if an event in this inventory was triggered by a player click.
	 * Cancels the event and initiates the corresponding pet action.
	 * Clicking on a pet will open their options menu.
	 * Clicking on the string will retrieve / unsummon the pet.
	 * Clicking on the egg tries to hatch the offspring in the breeding station.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see PetOverviewMenu#getIndex(ItemStack)
	 * @see PetOptionsMenu#open(Player, Integer)
	 * @see PetOverviewMenu#unsummon(Player)
	 * @see PetOverviewMenu#hatch(Player, ItemStack)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		final Player player = (Player) event.getWhoClicked();
		
		if(clicked == null) {
			return;
		}
		else if(clicked.getType().equals(Material.CHICKEN_SPAWN_EGG) || clicked.getType().equals(Material.PARROT_SPAWN_EGG)) {
			PetOptionsMenu.open(player, getIndex(clicked));
		}
		else if(clicked.getType().equals(Material.STRING)) {
			unsummon(player);
		}
		else if(clicked.getType().equals(Material.EGG)) {
			hatch(player, clicked);
		}
	}

// Support Methods
	
	/**
	 * Gets the index of an item stack, based on its lore.
	 * 
	 * @param itemStack The item to get the index from.
	 * 
	 * @return The index of the item.
	 */
	private static Integer getIndex(ItemStack itemStack) {
		for(String string : itemStack.getItemMeta().getLore()) {
			if(string.contains("Index")) {
				String[] indexStringParts = string.split(" ");
				return Integer.parseInt(indexStringParts[1]);
			}
		}
		return null;
	}
	
	/**
	 * Gets the owner of a pet, from the pet owner map.
	 * 
	 * @param entity The pet to get the owner from.
	 * 
	 * @return The owner of the pet.
	 */
	public static Player getOwner(Entity entity) {
		return petOwnerMap.get(entity.getUniqueId().toString());
	}
	
	/**
	 * Adds a player to the owner map, to bind them to the pet.
	 * 
	 * @param petId The uuid of the pet, owned by the player.
	 * @param player The player to add to the owner map.
	 */
	public static void setOwner(String petId, Player player) {
		petOwnerMap.put(petId, player);
	}
	
	/**
	 * Removes a player from the owner map and resets their walk speed.
	 * 
	 * @param petId The uuid of the pet, owned by the player.
	 * @param player The player to remove from the owner map.
	 */
	public static void removeOwner(String petId, Player player) {
		petOwnerMap.remove(petId);
		SpeedCalculator.resetWalkSpeed(player);
	}
	
// Unsummoning
	
	/**
	 * Unsummons the currently active pet of the given player.
	 * Only works if an MMORPG character is selected and a valid pet exists.
	 * Sets the active pet slot of the player to -1.
	 * 
	 * @param player The player whose pet should be unsummoned.
	 * 
	 * @see WauzMode#isMMORPG(Entity)
	 * @see WauzPlayerDataPool#isCharacterSelected(Player)
	 * @see PlayerConfigurator#setCharacterActivePetSlot(Player, int)
	 * @see PetOverviewMenu#removeOwner(String, Player)
	 */
	public static void unsummon(Player player) {
		try {
			if(!WauzMode.isMMORPG(player)) {
				return;
			}
			if(!WauzPlayerDataPool.isCharacterSelected(player)) {
				return;
			}
							
			String petId = PlayerPetsConfigurator.getCharacterActivePetId(player);
			PlayerPetsConfigurator.setCharacterActivePetSlot(player, -1);
			
			if(!petId.contains("none")) {
				Entity entity = Bukkit.getServer().getEntity(UUID.fromString(petId));		
				if(entity != null) {
					for(Entity passenger : entity.getPassengers()) {
						passenger.remove();
					}
					entity.remove();
					removeOwner(petId, player);
					player.sendMessage(ChatColor.GREEN + "Your current Pet was unsommoned!");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
// Adding
	
	/**
	 * Adds a new pet to a player, who used a scroll of summoning.
	 * 
	 * @param event The interaction event with the scroll.
	 * 
	 * @see PetOverviewMenu#addPet(Player, ItemStack, String) Continued in this method.
	 */
	public static void addPet(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack scroll = event.getItem();
		
		if(!ItemUtils.hasDisplayName(scroll)) {
			return;
		}
		
		String type = scroll.getItemMeta().getLore().get(0);
		type = type.replaceAll("" + ChatColor.GREEN, "");	
		addPet(player, scroll, type);
	}
	
	/**
	 * Adds a new pet to the given player, that starts with standard max stats of level 3.
	 * 
	 * @param player The player that should receive the pet.
	 * @param scroll The scroll used to receive the pet or null if none.
	 * @param petType The type of the pet.
	 * 
	 * @see PetOverviewMenu#addPet(Player, ItemStack, String, int, int, int) Continued in this method.
	 */
	public static void addPet(Player player, ItemStack scroll, String petType) {
		addPet(player, scroll, petType, 3, 3, 3);
	}

	/**
	 * Adds a new pet to the given player.
	 * Only possible if the character has a free pet slot.
	 * Sets the initial and max level for the pet's stats, aswell as the exp needed to reach the next level.
	 * Opens the pet overview menu afterwards, in which the new pet will be highlighted.
	 * 
	 * @param player The player that should receive the pet.
	 * @param scroll The scroll used to receive the pet or null if none.
	 * @param petType The type of the pet.
	 * @param maxInt The maximum intelligence level of the pet.
	 * @param maxDex The maximum dexterity level of the pet.
	 * @param maxAbs The maximum absorption level of the pet.
	 * 
	 * @see PlayerConfigurator#getCharacterPetType(Player, int)
	 * @see PlayerConfigurator#setCharacterPetType(Player, int, String)
	 * @see PetOverviewMenu#getBaseExpToFeedingLevel(int)
	 * @see PlayerConfigurator#setCharacterPetIntelligenceMax(Player, int, int)
	 * @see PlayerConfigurator#setCharacterPetDexterityMax(Player, int, int)
	 * @see PlayerConfigurator#setCharacterPetAbsorptionMax(Player, int, int)
	 * @see AchievementTracker#addProgress(Player, WauzAchievementType, double)
	 * @see PetOverviewMenu#open(Player, int)
	 */
	public static void addPet(Player player, ItemStack scroll, String petType, int maxInt, int maxDex, int maxAbs) {
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String slotType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
			if(!slotType.equals("none")) {
				continue;
			}
			
			try {
				PlayerPetsConfigurator.setCharacterPetType(player, petSlot, petType);
				
				int baseExp = getBaseExpToFeedingLevel(1);
				
				PlayerPetsConfigurator.setCharacterPetIntelligence(player, petSlot, 0);
				PlayerPetsConfigurator.setCharacterPetIntelligenceMax(player, petSlot, maxInt);
				PlayerPetsConfigurator.setCharacterPetIntelligenceExpNeeded(player, petSlot, baseExp);
				
				PlayerPetsConfigurator.setCharacterPetDexterity(player, petSlot, 0);
				PlayerPetsConfigurator.setCharacterPetDexterityMax(player, petSlot, maxDex);
				PlayerPetsConfigurator.setCharacterPetDexterityExpNeeded(player, petSlot, baseExp);
				
				PlayerPetsConfigurator.setCharacterPetAbsorption(player, petSlot, 0);
				PlayerPetsConfigurator.setCharacterPetAbsorptionMax(player, petSlot, maxAbs);
				PlayerPetsConfigurator.setCharacterPetAbsorptionExpNeeded(player, petSlot, baseExp);
				
				player.sendMessage(ChatColor.GREEN + "You learned to summon " + petType + " from the Menu!");
				AchievementTracker.addProgress(player, WauzAchievementType.COLLECT_PETS, 1);
				
				if(scroll != null) {
					scroll.setAmount(scroll.getAmount() - 1);
				}
				open(player, petSlot);
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		}
		
		player.sendMessage(ChatColor.RED + "You have no free Pet-Slots!");
	}
	
// Leveling
	
	/**
	 * Gets the amount of exp needed to reach the next feeding level of the pet.
	 * 
	 * @param level The next level to be reached.
	 * 
	 * @return The needed exp to the level.
	 */
	public static int getBaseExpToFeedingLevel(int level) {
		switch (level) {
		case 1:
			return   100;
		case 2:
			return   250;
		case 3:
			return   500;
		case 4:
			return  1000;
		case 5:
			return  1800;
		case 6:
			return  3000;
		case 7:
			return  4500;
		case 8:
			return  7000;
		case 9:
			return 12000;
		case 10:
			return 20000;
		default:
			return 0;
		}
	}
	
// Hatching
	
	/**
	 * Tries to hatch the pet egg, to let the player receive the offspring from the breeding station.
	 * It will not be possible if it is still breeding or no free slot is available.
	 * The pet will receive the type of a random parent.
	 * The pet will receive the highest max stats of its parents, plus 1 point extra,
	 * for each parent, that had the stat leveled to its possible maximum, capped at 10 points.
	 * Resets the breeding hatch time and parent slots, before adding the new pet to the overview menu.
	 * 
	 * @param player The player who tries to hatch the pet egg.
	 * @param eggStack The pet egg item stack.
	 * 
	 * @see PlayerPetsConfigurator#getCharacterPetType(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetType(Player, int, String)
	 * @see PlayerPetsConfigurator#getCharacterPetIntelligence(Player, int)
	 * @see PlayerPetsConfigurator#getCharacterPetDexterity(Player, int)
	 * @see PlayerPetsConfigurator#getCharacterPetAbsorption(Player, int)
	 * @see PlayerPetsConfigurator#setCharacterPetBreedingHatchTime(Player, long)
	 * @see PetOverviewMenu#getNewStatAfterHatch(int, int, int, int)
	 * @see PetOverviewMenu#addPet(Player, ItemStack, String, int, int, int)
	 */
	public static void hatch(Player player, ItemStack eggStack) {
		String eggDisplayNamme = eggStack.getItemMeta().getDisplayName();
		if(eggDisplayNamme.contains("Breeding")) {
			if (eggDisplayNamme.contains("...")) {
				open(player, -1);
			}
			return;
		}
		
		boolean freeSlot = false;
		for(int petSlot = 0; petSlot < 5; petSlot++) {
			String slotType = PlayerPetsConfigurator.getCharacterPetType(player, petSlot);
			if(slotType.equals("none")) {
				freeSlot = true;
				break;
			}
		}
		if(!freeSlot) {
			player.sendMessage(ChatColor.RED + "You have no free Pet-Slots!");
			player.closeInventory();
			return;
		}
		
		String typeA = PlayerPetsConfigurator.getCharacterPetType(player, 6);
		int intA = PlayerPetsConfigurator.getCharacterPetIntelligence(player, 6);
		int intMaxA = PlayerPetsConfigurator.getCharacterPetIntelligenceMax(player, 6);
		int dexA = PlayerPetsConfigurator.getCharacterPetDexterity(player, 6);
		int dexMaxA = PlayerPetsConfigurator.getCharacterPetDexterityMax(player, 6);
		int absA = PlayerPetsConfigurator.getCharacterPetAbsorption(player, 6);
		int absMaxA = PlayerPetsConfigurator.getCharacterPetAbsorptionMax(player, 6);
		
		String typeB = PlayerPetsConfigurator.getCharacterPetType(player, 8);
		int intB = PlayerPetsConfigurator.getCharacterPetIntelligence(player, 8);
		int intMaxB = PlayerPetsConfigurator.getCharacterPetIntelligenceMax(player, 8);
		int dexB = PlayerPetsConfigurator.getCharacterPetDexterity(player, 8);
		int dexMaxB = PlayerPetsConfigurator.getCharacterPetDexterityMax(player, 8);
		int absB = PlayerPetsConfigurator.getCharacterPetAbsorption(player, 8);
		int absMaxB = PlayerPetsConfigurator.getCharacterPetAbsorptionMax(player, 8);
		
		String petType = Chance.percent(50) ? typeA : typeB;
		int newInt = getNewStatAfterHatch(intA, intB, intMaxA, intMaxB);
		int newDex = getNewStatAfterHatch(dexA, dexB, dexMaxA, dexMaxB);
		int newAbs = getNewStatAfterHatch(absA, absB, absMaxA, absMaxB);
		PlayerPetsConfigurator.setCharacterPetType(player, 6, "none");
		PlayerPetsConfigurator.setCharacterPetType(player, 8, "none");
		PlayerPetsConfigurator.setCharacterPetBreedingHatchTime(player, 0);
		addPet(player, null, petType, newInt, newDex, newAbs);
	}
	
	/**
	 * Gets the new stat of a freshly hatched pet, based off the parent's stats.
	 * 
	 * @param valA The current stat of parent A.
	 * @param valB The current stat of parent B.
	 * @param maxA The maximum stat of parent A.
	 * @param maxB The maximum stat of parent B.
	 * 
	 * @return The new stat.
	 * 
	 * @see PetOverviewMenu#hatch(Player, ItemStack)
	 */
	public static int getNewStatAfterHatch(int valA, int valB, int maxA, int maxB) {
		int stat = Math.max(maxA, maxB) + (valA >= maxA ? 1 : 0) + (valB >= maxB ? 1 : 0);
		return stat > 10 ? 10 : stat;
	}

}
