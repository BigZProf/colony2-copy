package game

import colony.core.Colony
import colony.core.Member
import colony.core.Settings


class Game {

	boolean over = false
	final Colony col
	int day

	public Game(Colony theColony) {
		col = theColony
		col.log "Create game with $theColony"
	}

	public void start(int day=0) {
		col.log "Start game at day $day"
		File f = new File("${Settings.colStateDir}/${col.fileName}_${day}.xml")
		if (f.exists()) {
			col.log "Restoring colony state"
			col.fromXML f.text
		}
	}

	public void loop(int days = Settings.gameMaxDays) {
		int untilDay = day + days
		while (!over && day < untilDay) {
			day++
			col.log "New day $day --------------"
			startDay()
			playDay()
			endDay()
		}
	}

	void startDay() {
		col.members.each { Member m -> m.actionPoints = Settings.memMaxActionPoints }
		col.processEvents(col.randomEvents())
	}

	void playDay() {
		/**
		 * for each member,
		 * while the member has action points
		 * - complete unfinished task if any
		 * - move randomly inside the colony (go to a building)
		 * - use the building (create a task according to the building type)
		 * - inspect the structure or the building (gain information)
		 * - if another member is here, exchange information
		 */
		col.members.each { Member m ->
			if (m.actionPoints > 0) {
				col.tasks.grep({ it.done == false && it.isAssignedTo(m) }).each {
					m.workOn it, 1
					return
				}
			}
			int buildingNum = col.r.nextInt col.buildings.size()
			m.moveTo col.buildings[buildingNum]
			col.log "${m} moved to ${m.isIn}"
		}
	}

	void endDay() {
		//col.consume()
		//col.storeSurpluses()
		//col.updateStats()
		col.saveState(day)
		if (day > Settings.gameMaxDays) {
			over = true
		}
	}

}
