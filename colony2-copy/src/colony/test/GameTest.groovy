package colony.test;

import game.Game
import test.util.TestUtils
import colony.core.Colony

class GameTest extends GroovyTestCase {

	public void testGameInit() {
		
		TestUtils.deleteAll 'testGameInit'
		
		Colony c = TestUtils.sampleColony('testGameInit')		
		Game game = new Game(c)
		
		assert game.over == false
		assert game.col != null		
		
		c.saveDefinition()
	}
	
	public void testGameStartDay0() {
		TestUtils.deleteAll 'testGameStartDay0'
		
		Colony c = TestUtils.sampleColony('testGameStartDay0')
		Game game = new Game(c)
		
		game.start()

		c.saveDefinition()
	}
	
	public void testGamePlay() {
		TestUtils.deleteAll 'testGamePlay'
		
		Colony c = TestUtils.sampleColony('testGamePlay')
		Game game = new Game(c)
		
		game.start()
		
		game.loop(1)
		assert c.day == 1

		game.loop(2)
		assert c.day == 3
		
		c.saveDefinition()
	}
}
