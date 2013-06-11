package test.util

import static colony.core.BuildingType.*
import static groovy.io.FileType.*
import colony.core.Building
import colony.core.BuildingType
import colony.core.Colony
import colony.core.Member
import colony.core.Settings

class TestUtils {
	
	static Colony sampleColony(String colonyName) {
		
		def memberNames = [
			'Gultar Ze Terrible', 'Cinderella', 'Yogolo', 'Pikimousse',
			'Simbad', 'Calan', 'Pfiouu', 'Aurag', 'Stania'
			]
		
		List<BuildingType> buildingTypes =
			[ STORE ] * 2 +
			[ FARM ] * 4 +
			[ MINE ] * 3 +
			[ BEDROOM ]
		
		Colony col1 = new Colony(colonyName)
		
		memberNames.each{ name ->
			Member m = new Member(name, col1)
			assert m.name == name
			assert m.colony == col1
			assert m.health == Settings.memInitialHealth
			assert m.isSick == false
			col1.members.add m
		}
		
		buildingTypes.each{ BuildingType btype ->
			Building b = new Building(btype, col1)
			assert b.type == btype
			assert b.colony == col1
			assert b.integrity == Settings.bldInitialIntegrity
			col1.buildings.add b
		}
		
		return col1
	}
	
	static void deleteDefinition(String colonyName) {
		def fileName = colonyName.replaceAll(/\W/, '')
		File file = new File("${Settings.colDefDir}/${fileName}.xml")
		if (file.exists()) {
			file.delete()
		}
	}

	static void deleteState(String colonyName) {
		def fileName = colonyName.replaceAll(/\W/, '')
		def colonyStateDir = new File(Settings.colStateDir)
		def filter = ~/${colonyName}_.*\.xml/
		colonyStateDir.traverse(types: FILES, nameFilter: filter) { file ->
			file.delete()
		}	
	}
	
	static void deleteLog(String colonyName) {
		def fileName = colonyName.replaceAll(/\W/, '')
		def colonyStateDir = new File(Settings.colLogDir)
		def filter = ~/${colonyName}.log/
		colonyStateDir.traverse(types: FILES, nameFilter: filter) { file ->
			file.delete()
		}
	}
	
	static void deleteAll(String colonyName) {
		deleteDefinition colonyName
		deleteLog colonyName
		deleteState colonyName
	}

}
