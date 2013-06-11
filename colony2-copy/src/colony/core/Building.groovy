package colony.core;

//import static colony.core.InformationType.*
public class Building {
	
	final int id
	final BuildingType type
	final Colony colony
	
	int integrity = Settings.bldInitialIntegrity
	int materials = 0
	int food = 0

	public Building(BuildingType theType, Colony theColony) {
		type = theType
		id = theColony.nextId++
		colony = theColony
	}

	/** for de-serialization */
	public Building(String theId, String btypeName, Colony theColony) {
		type = BuildingType.get(btypeName)
		id = theId.toInteger()
		colony = theColony
	}
	
	public List<Information> inspect() {
		List<Information> result = []
		result.add ( new Information( this.toString()+".integrity", integrity ))
		result.add ( new Information( this.toString()+".food", food ))
		result.add ( new Information( this.toString()+".materials", materials ))
		return result
	}
	
	void addMaterials(int theMaterials) {
		assert type == BuildingType.STORE || type == BuildingType.MINE
		materials += theMaterials
		if (materials + food > Settings.maxStorage) {
			materials = Settings.maxStorage - food // lost
		}
	}
	
	void addFood(int theFood) {
		assert type == BuildingType.STORE || type == BuildingType.FARM
		food += theFood
		if (materials + food > Settings.maxStorage) {
			food = Settings.maxStorage - materials // lost
		}
	}
	
	public String toString() { type.toString() + id }

}