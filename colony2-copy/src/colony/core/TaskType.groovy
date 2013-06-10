package colony.core

class TaskType {

	private static List<TaskType> allTTypes = []

	String name
	Closure script

	private TaskType(String theName, Closure theScript) {
		name = theName
		script = theScript
		allTTypes.add(this)
	}

	public static final REPAIR_STRUCTURE = new TaskType(
	'Repair structure', {
		Task t, Member m, int actionPoints ->
		Colony col = t.col
		col.structure += actionPoints
		if (col.structure > Settings.colInitialStructure) {
			col.structure = Settings.colInitialStructure
		}
		col.log("${m} repaired the structure, now $col.structure")
		return (col.structure == Settings.colInitialStructure)
	})

	public static final FARMING = new TaskType(
	'Farm', {
		Task t, Member m, int actionPoints ->
		Colony col = t.col
		col.food += actionPoints
		col.log("${m} has farmed, available food is now $col.food")
		return false
	})

	public static final MINING = new TaskType(
	'Mine', {
		Task t, Member m, int actionPoints ->
		Colony col = t.col
		col.log("${m} has mined, available materials are now $col.food")
		col.materials += actionPoints
		return false
	})

	public static final RESTING = new TaskType(
	'Rest', {
		Task t, Member m, int actionPoints ->
		m.health += actionPoints
		if (m.health > Settings.memInitialHealth) {
			m.health = Settings.memInitialHealth
		}
		return false
	})
	
	public static final REPAIR_BUILDING = new TaskType(
	'Repair building', {
		Task t, Member m, int actionPoints ->		
		boolean done = false
		Building b = t.b
		Colony col = t.col
		if (b) {
			b.health += actionPoints
			if (b.health > Settings.bldInitialHealth) {
				b.health = Settings.bldInitialHealth
			}
			done = (b.health == Settings.bldInitialHealth)
			col.log("${m} has repaired ${b}, building health is now $b.health")
		}
		return done
	})

	/** get information about what is in the store */
	public static final CHECK_STORAGE = new TaskType(
	'Check storage', {
		Task t, Member m, int actionPoints ->
		// TODO - TaskType check storage
		return false
	})

	/** get information about the colony structure */
	public static final CHECK_STRUCTURE = new TaskType(
	'Check structure', {
		Task t, Member m, int actionPoints ->
		// TODO - TaskType check storage
		return false
	})

	public String toString() {
		name
	}
}