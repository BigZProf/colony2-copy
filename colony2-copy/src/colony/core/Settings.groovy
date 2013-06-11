package colony.core

class Settings {
	
	// Colony settings
	static colRandomSeed = 1
	static colDefDir = 'test/data/colonies/def'
	static colStateDir = 'test/data/colonies/state'	
	static colLogDir = 'test/data/colonies/log'	
	static colInitialFood = 1000
	static colInitialMaterials= 500
	
	// Member settings
	static memInitialHealth = 100
	static memMaxActionPoints = 20
	
	// Building settings
	static bldInitialIntegrity = 100	
	static farmProdRate = 100
	static mineProdRate = 100
	static maxStorage = 100
	
	// Events settings
	static evtThunderMaxDamage = 100
	static evtCropPestDamagePct = 0.5f
	
	// Game settings
	static gameMaxDays = 3 
	
}