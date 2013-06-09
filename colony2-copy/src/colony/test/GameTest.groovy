package colony.test;

import game.Game
import test.util.TestUtils
import colony.core.Colony

class GameTest extends GroovyTestCase {

	public void testGameInit() {
		
		TestUtils.resetAll 'testGameInit'
		
		Colony c = TestUtils.sampleColony('testGameInit')		
		Game game = new Game(c)
		
		assert game.over == false
		assert game.col != null		
		
		c.saveDefinition()
	}
	
	public void testGameStartDay0() {
		TestUtils.resetAll 'testGameStartDay0'
		
		Colony c = TestUtils.sampleColony('testGameStartDay0')
		Game game = new Game(c)
		
		game.start()
		assert game.day == 0

		c.saveDefinition()
	}
	
	public void testGamePlay() {
		TestUtils.resetAll 'testGamePlay'
		
		Colony c = TestUtils.sampleColony('testGamePlay')
		Game game = new Game(c)
		
		game.start()
		
		game.loop(1)
		assert game.day == 1

		game.loop(2)
		assert game.day == 3
		
		c.saveDefinition()
	}
}
