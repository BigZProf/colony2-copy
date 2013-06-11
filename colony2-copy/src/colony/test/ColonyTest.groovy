package colony.test;

import test.util.TestUtils
import colony.core.Colony
import colony.core.Settings

class ColonyTest extends GroovyTestCase {
	
	public void testNewColony() {
		
		TestUtils.deleteAll 'Test Colony New 1!'
		TestUtils.deleteAll 'Test Colony New 2!'
			
		Colony col1 = TestUtils.sampleColony('Test Colony New 1!')
		assert col1.name == 'Test Colony New 1!'
		assert col1.fileName == 'TestColonyNew1' 		
		assert col1.buildings.size() == 10	
		col1.saveDefinition()
		
		Colony col2 = Colony.load 'Test Colony New 1!'
		assert col1.name == col2.name
		assert col1.buildings.size() == col2.buildings.size()
		
		col2.rename 'Test Colony New 2!'
		assert col2.name == 'Test Colony New 2!'
		assert col2.fileName == 'TestColonyNew2'
		col2.saveDefinition()
		
		File f1 = new File("${Settings.colDefDir}/TestColonyNew1.xml")
		File f2 = new File("${Settings.colDefDir}/TestColonyNew2.xml")
		assert f1.text == f2.text.replaceAll('Test Colony New 2!', 'Test Colony New 1!')
		
	}

}
