package colony.core

class Member {
	
	final int id
	final String name
	final Colony colony
	int health = Settings.memInitialHealth
	boolean isSick = false
	int actionPoints = 0
	Building isIn
	Task assignedTo
	List<Information> infos = []

	// used by deserialization
	public Member(String theId, String theName, Colony theColony) {
		name = theName
		id = theId.toInteger()
		colony = theColony
	}	
	
	public Member(String theName, Colony theColony) {
		name = theName
		colony = theColony
		id = colony.nextId++
	}

	public String toString() {
		name
	}
	
	public void work(int aPoints) {
		assert assignedTo != null
		if (aPoints > actionPoints) {
			aPoints = actionPoints
		}
		if (aPoints > 0) {
			assignedTo.type.script.call(assignedTo, this, aPoints)
		}
		actionPoints -= aPoints
	}
	
	public void moveTo(building) {
		isIn = building
	}

}
