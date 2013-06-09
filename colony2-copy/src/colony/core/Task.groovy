package colony.core

import java.util.List;

class Task {
	
	Colony col
	TaskType type
	Building b
	
	List<Member> contributors = []
	boolean done = false
	
	public Task(Colony theColony, TaskType theType) {
		col = theColony
		type = theType
	}
	
	public Task(Colony theColony, String typeName) { // for deserialization
		this(theColony, TaskType.findByName(typeName))
	}
	
	public String toString() { type.toString() }
	
	public void work(Member m, int actionPoints) {
		if (contributors.contains(m)) {
			type.script.call(this, m, actionPoints)
		}
	}
	
	public boolean isAssignedTo(Member m) {
		return contributors?.contains(m)
	}
	
}
