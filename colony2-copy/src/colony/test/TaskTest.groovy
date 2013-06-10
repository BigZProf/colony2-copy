package colony.test;

import game.Game
import test.util.TestUtils
import colony.core.Colony
import colony.core.Task
import colony.core.TaskType

class TaskTest extends GroovyTestCase {

	public void testTaskFarm() {
		
		TestUtils.resetAll 'testTaskFarm'
		Colony c = TestUtils.sampleColony 'testTaskFarm'
		
		Game game = new Game(c)
		game.start()
		
		Task farm = new Task(c, TaskType.FARMING)
		assert farm.col == c
		assert farm.type == TaskType.FARMING	
		farm.contributors.add c.members[0]	
		c.tasks.add farm
		
		Task mine = new Task(c, TaskType.MINING)
		mine.contributors.addAll c.members
		c.tasks.add mine
			
		Task rp = new Task(c, TaskType.REPAIR_BUILDING)
		rp.b = c.buildings[0]
		rp.contributors.add c.members[1]	
		c.tasks.add rp
		
		c.fileName = 'TestTaskFarm'
		c.saveDefinition()

		game.loop()		
	}
}
