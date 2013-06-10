package colony.core;

public class Building {
	
	static int nextId = 1
	
	int id
	final BuildingType type;
	int health = Settings.bldInitialHealth
	
	/** for de-serialization */
	public Building(String theId, String btypeName) {
		type = BuildingType.get(btypeName)
		id = theId.toInteger()
	}
	
	public Building(BuildingType theType) {
		this(nextId++, theType)
	}
	
	public String toString() { type.toString() + id }

}