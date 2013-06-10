package game

import colony.core.Colony
import colony.core.Member
import colony.core.Settings
import colony.core.Task
import colony.core.TaskType

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
		 * - if some task is already started in the building, work on it
		 * 		else create a task suitable with for the building type
		 * - inspect the structure or the building (gain information)
		 * - if another member is here, exchange information
		 */
		col.members.each { Member m ->
			
			// complete unfinished task
			if (m.actionPoints > 0 && m.assignedTo) {
				m.work 1 // TODO compute action points to spend for task
			}
			
			// move randomly to a building
			def buildingNum = col.random.nextInt col.buildings.size()
			def building = col.buildings[buildingNum]
			m.moveTo building
			col.log "${m} moved to ${building}"

			// help here is a task is already started
			Task t
			def possibleTasks = col.tasks.grep({ it.isDoneIn(building) && !it.done})
			if (possibleTasks != []) {
				def taskNum = col.random.nextInt(possibleTasks.size())
				t = possibleTasks[taskNum]
			}
			else { // create a task
				//TODO - find a better method than random to pick a task
				def possibleTTypes = col.buildings[buildingNum].type.taskTypes
				def ttypeNum = col.random.nextInt possibleTTypes.size()
				t = new Task(col, possibleTTypes[ttypeNum])
				col.tasks.add t
			}
			m.assignedTo = t
			col.log "${m} assigned ${t} task"
		}

		// TODO - complete playDay

	}

	void endDay() {
		// TODO - col.consume()
		// TODO - col.storeSurpluses()
		// TODO - col.updateStats()
		col.saveState(day)
		if (day > Settings.gameMaxDays) {
			over = true
		}
	}


}

