package colony.core

import groovy.xml.MarkupBuilder

class Colony {

	String name
	String fileName

	int food = Settings.colInitialFood
	int structure = Settings.colInitialStructure
	int energy = Settings.colInitialEnergy
	int materials= Settings.colInitialMaterials

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
						log "$b before thunder strike, health = $b.health"
						b.health -= (random.nextInt(Settings.evtThunderMaxDamage)+1)
						log "$b after thunder strike, health = $b.health"
					}
					break
				case EventType.CROP_PEST:
				// the current food available is reduced
					food -= (food * random.nextInt(Settings.evtCropPestMaxDamagePct) / 100)
					log "Food after crop pest: $food"
					break
			}
		}
	}

	public String toXML() {
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.colony(name: name, food: food, structure: structure, energy: energy, materials: materials) {
			members() {
				for(Member m in members) {
					member(id: m.id, name: m.name, health: m.health, building_id: m.isIn?.id)
				}
			}
			buildings() {
				for(Building b in buildings) {
					building(id: b.id, type: b.type, health: b.health)
				}
			}
			tasks() {
				for(Task t in tasks) {
					task(type: t.type, building_id: t.b?.id) {
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
		food = colParser.attribute("food").toInteger()
		structure = colParser.attribute("structure").toInteger()
		energy = colParser.attribute("energy").toInteger()
		materials = colParser.attribute("materials").toInteger()
		members = []
		colParser.members.member.each { member ->
			int id = member.attribute("id").toInteger()
			String name = member.attribute("name")
			Member m = new Member(id, name)
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
			Building b = new Building(id, type)
			b.health = building.attribute("health").toInteger()
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
		File file = new File("${Settings.colDefDir}/${fileName}.xml")
		if (file.exists()) {
			log "Loading definition from ${Settings.colDefDir}/${fileName}.xml"
			fromXML file.text
		}
	}
	
	private void deleteDefinition() {
		File file = new File("${Settings.colDefDir}/${fileName}.xml")
		if (file.exists()) {
			log "Erasing definition from ${Settings.colDefDir}/${fileName}.xml"
			file.delete()
		}
	}
	
	void saveDefinition() {
		new File("${Settings.colDefDir}/${fileName}.xml").write toXML()
	}

	void loadState(int day) {
		fromXML new File("${Settings.colStateDir}/${fileName}_${String.format('%03d',day)}.xml").text
	}

	void saveState(int day) {
		new File("${Settings.colStateDir}/${fileName}_${String.format('%03d',day)}.xml").write toXML()
	}

	void log(message) {
		String timestamp = new Date().format('YYYY/MM/dd HH:mm:ss.S')
		new File("${Settings.colLogDir}/${fileName}.log") << "${timestamp}: ${message}\n"
	}

}
