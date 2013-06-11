package colony.core

import static colony.core.TaskType.*
import java.util.List;

class BuildingType {

	private static Map<String, BuildingType> allBTypes = [:]
	
	final String name
	final List<TaskType> taskTypes // what you can do in the building (will get more complex)

	private BuildingType(String theName, List<TaskType> theTaskType) {
		name = theName
		taskTypes = theTaskType
		allBTypes.put(theName, this)
	}
	
	public static BuildingType get(String btypeName) {
		return allBTypes.get(btypeName)
	}

	public static final FARM = new BuildingType( 
		'Farm', [ FARMING, CHECK_STRUCTURE ] )
	public static final MINE = new BuildingType( 
		'Mine', [ MINING, CHECK_STRUCTURE ] )
	public static final STORE = new BuildingType( 
		'Store', [ CHECK_STORE, CHECK_STRUCTURE ] )
	public static final BEDROOM = new BuildingType( 
		'Bedroom', [ RESTING, CHECK_STRUCTURE ] )
	
	public String toString() {
		name
	}
}