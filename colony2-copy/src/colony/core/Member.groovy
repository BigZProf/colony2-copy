package colony.core

class Member {
	
	static nextId = 1

	int id
	String name
	int health = Settings.memInitialHealth
	boolean isSick = false
	int actionPoints = 0
	Building isIn
	Task assignedTo

	// used by deserialization
	public Member(int theId, String theName) {
		name = theName
		id = theId
		nextId = theId+1
	}	
	
	public Member(theName) {
		name = theName
		id = nextId++
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
	}
	
	public void moveTo(building) {
		isIn = building
	}

}
