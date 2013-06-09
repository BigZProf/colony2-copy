package colony.core

class EventType {
	
	public static List all = [] 
	
	String name
	int	probaPct
	
	private EventType(String theName, int theProbaPct) {
		name = theName
		probaPct = theProbaPct
		all.add(this)
	}
	
	public static final CONTAGIOUS_DISEASE = new EventType('Contagious disease', 5)
	public static final THUNDER_STRIKE = new EventType('Thunder Strike', 5)
	public static final CROP_PEST = new EventType('Crop Pest', 5)
	
	public String toString() { name }
	
}