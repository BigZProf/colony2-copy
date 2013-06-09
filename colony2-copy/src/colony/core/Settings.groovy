package colony.core

class Settings {
	
	// Colony settings
	static colRandomSeed = 1
	static colDefDir = 'test/data/colonies/def'
	static colStateDir = 'test/data/colonies/state'	
	static colLogDir = 'test/data/colonies/log'	
	static colInitialFood = 1000
	static colInitialStructure = 100
	static colInitialEnergy = 1000
	static colInitialMaterials= 500
	
	// Member settings
	static memInitialHealth=100
	static memMaxActionPoints=20
	
	// Building settings
	static bldInitialHealth = 100	
	static farmProdRate = 100
	static solarPlantProdRate = 100
	static mineProdRate = 100
	
	// Events settings
	static evtThunderMaxDamage = 100
	static evtCropPestMaxDamagePct = 50
	
	// Game settings
	static gameMaxDays = 3 
	
}