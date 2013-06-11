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

	public static final FARMING = new TaskType(
	'Farming task', {
		Task t, Member m, int actionPoints ->
		Colony col = t.col
		Building farm = t.building
		farm.addFood(actionPoints * Settings.farmProdRate)
		col.log "${m} has farmed, available food is now ${farm.food}"
		return false
	})

	public static final MINING = new TaskType(
	'Mining task', {
		Task t, Member m, int actionPoints ->
		Colony col = t.col
		Building mine = t.building
		mine.addMaterials(actionPoints * Settings.mineProdRate)
		col.log "${m} has mined, available materials are now ${mine.materials}"
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
	'Repairing a building', {
		Task t, Member m, int actionPoints ->		
		boolean done = false
		Building b = t.building
		Colony col = t.col
		if (b) {
			b.integrity += actionPoints
			if (b.integrity > Settings.bldInitialIntegrity) {
				b.integrity = Settings.bldInitialIntegrity
			}
			done = (b.integrity == Settings.bldInitialIntegrity)
			col.log("${m} has repaired ${b}, building health is now ${b.integrity}%")
		}
		return done
	})

	/** get information about what is in the store */
	public static final CHECK_STORE = new TaskType(
	'Checking storage', {
		Task t, Member m, int actionPoints ->
		m.infos.addAll(t.building.inspect())
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