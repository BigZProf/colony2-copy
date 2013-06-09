package colony.core;

public class Building {
	
	static int nextId = 1
	
	static List types = [
		'FARM', 'MINE', 'BEDROOM', 'STORE'
	]

	int id
	final type;
	int health = Settings.bldInitialHealth
	
	/** for de-serialization */
	public Building(int theId, String theType) {
		if (! types.contains(theType)) {
			throw new Exception("Illegal building type: $theType")
		}
		type = theType
		id = theId.toInteger()
	}
	
	public Building(String theType) {
		this(nextId++, theType)
	}
	
	public String toString() { type + id }

}