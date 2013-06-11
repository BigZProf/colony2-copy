package colony.core

import static colony.core.BuildingType.*
import groovy.xml.MarkupBuilder

class Colony {

	private String name
	private String fileName
	private int day = 0
	
	int nextId = 1

	int food = Settings.colInitialFood					// currently available
	int materials= Settings.colInitialMaterials			// currently available

	List<Member> members = []
	List<Building> buildings = []
	List<Task> tasks = []

	Random random = new Random(Settings.colRandomSeed)

	public Colony( String theName, boolean reset = true ) {
		name = theName
		fileName = theName.replaceAll(/\W/, '')
		log "====== Creation of colony '$theName' ======="
		if (reset) {
			deleteDefinition()
		}
	}
	
	public static Colony load(String colonyName) {
		Colony c = new Colony(colonyName, false)
		c.loadDefinition()
		return c
	}
	
	public void rename(newName) {
		log "Renaming colony from '$name' to $newName"
		name = newName
		fileName = newName.replaceAll(/\W/, '')
	}

	public String toString() {
		name
	}

	public List randomEvents() {
		EventType.all.grep { EventType et ->
			random.nextInt(100) < et.probaPct
		}
	}

	public void processEvents(List randomEvents) {
		randomEvents.each { EventType eventType ->
			log "$eventType:"
			switch(eventType) {
				case EventType.CONTAGIOUS_DISEASE:
				// selects randomly a colony member that gets sick
					if ( members != []) {
						Member m = members[random.nextInt(members.size())]
						m.isSick = true
						log "$m is sick"
					}
					break
				case EventType.THUNDER_STRIKE:
				// selects randomly a building that gets damaged
					if (buildings != []) {
						Building b = buildings[random.nextInt(buildings.size())]
						log "$b before thunder strike, health = $b.integrity"
						b.integrity -= (random.nextInt(Settings.evtThunderMaxDamage)+1)
						log "$b after thunder strike, health = $b.integrity"
					}
					break
				case EventType.CROP_PEST:
				// the current food available is reduced
					def food_stores = buildings.grep {it.type == STORE && it.food > 0}
					int store_num = random.nextInt(food_stores.size())
					Building store = food_stores[store_num]
					store.food *= Settings.evtCropPestDamagePct
					log "Crop pest attacks $store: $food food remaining"
					break
			}
		}
	}

	public String toXML() {
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.colony(name: name, day: day, next_id: nextId) {
			members() {
				for(Member m in members) {
					member(id: m.id, name: m.name, health: m.health, building_id: m.isIn?.id)
				}
			}
			buildings() {
				for(Building b in buildings) {
					building(id: b.id, type: b.type, integrity: b.integrity)
				}
			}
			tasks() {
				for(Task t in tasks) {
					task(type: t.type, building_id: t.building?.id) {
						contributors() {
							for(Member m in t.contributors) {
								member_id(m.id)
							}
						}
					}
				}
			}
		}
		writer.toString()
	}

	public void fromXML(String xml) {
		def colParser = new XmlParser().parseText(xml)
		name = colParser.attribute("name")
		day = colParser.attribute("day").toInteger()
		nextId = colParser.attribute("next_id").toInteger()
		members = []
		colParser.members.member.each { member ->
			String id = member.attribute("id")
			String name = member.attribute("name")
			Member m = new Member(id, name, this)
			m.health = member.attribute("health").toInteger()
			String building_id = member.attribute("building_id")
			if (building_id != '') {
				Building b = buildings.grep {it.id == building_id.toInteger()}.first()
				m.moveTo b
			}	
			members.add m
		}
		buildings = []
		colParser.buildings.building.each { building ->
			String id = building.attribute("id")
			String type = building.attribute("type")			
			Building b = new Building(id, type, this)
			b.integrity = building.attribute("integrity").toInteger()
			buildings.add b
		}
		tasks = []
		colParser.tasks.task.each { task ->
			Task t = new Task(this,TaskType.allBTypes[task.attribute("type")])
			if (task.attribute("building_id") != "null") {
				int buildingId = task.attribute("building_id").toInteger()
			}
			colParser.tasks.contributors.member_id.each { member_id ->
				t.contributors.add members.grep {it.id == member_id.toInteger()}
			}
		}
	}

	void loadDefinition() {
		File file = definitionFile()
		if (file.exists()) {
			log "Loading definition from ${Settings.colDefDir}/${fileName}.xml"
			fromXML file.text
		}
	}
	
	private void deleteDefinition() {
		File file = definitionFile()
		if (file.exists()) {
			log "Erasing definition from ${Settings.colDefDir}/${fileName}.xml"
			file.delete()
		}
	}
	
	File definitionFile() {
		new File("${Settings.colDefDir}/${fileName}.xml")
	}
	
	File logFile() {
		new File("${Settings.colLogDir}/${fileName}.log")
	}
	
	File stateFile(state_day) {
		new File("${Settings.colStateDir}/${fileName}_${String.format('%03d',state_day)}.xml")
	}
	void saveDefinition() {
		definitionFile().write toXML()
	}

	void loadState(int state_day) {
		day = state_day
		fromXML new File("${Settings.colStateDir}/${fileName}_${String.format('%03d',day)}.xml").text
	}

	void saveState() {
		stateFile(day).write toXML()
	}

	void log(message) {
		String timestamp = new Date().format('YYYY/MM/dd HH:mm:ss.S')
		logFile() << "${timestamp}: ${message}\n"
	}

}
