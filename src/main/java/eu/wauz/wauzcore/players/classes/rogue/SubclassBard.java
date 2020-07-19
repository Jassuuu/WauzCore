package eu.wauz.wauzcore.players.classes.rogue;

import eu.wauz.wauzcore.players.classes.ClassRogue;
import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerSubclass;

/**
 * A subclass / mastery, that belongs to a player class.
 * Specialization: Support, Buffing and Debuffing
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 * @see ClassRogue
 */
public class SubclassBard implements WauzPlayerSubclass {
	
	/**
	 * The static name of the subclass.
	 */
	public static String CLASS_NAME = "Bard";
	
	/**
	 * @return The name of the subclass.
	 */
	@Override
	public String getSubclassName() {
		return CLASS_NAME;
	}

}