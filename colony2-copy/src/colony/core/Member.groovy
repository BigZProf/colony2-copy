package colony.core

class Member {
	
	static nextId = 1

	int id
	String name
	int health = Settings.memInitialHealth
	boolean isSick = false
	int actionPoints = 0
	Building isIn

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
	
	public void workOn(Task t, int aPoints) {
		if (aPoints > actionPoints) {
			aPoints = actionPoints
		}
		if (aPoints > 0) {
			t.type.script.call(t, this, aPoints)
		}
	}
	
	public void moveTo(building) {
		isIn = building
	}

}
