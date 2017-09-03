package autonomous;

import interfaces.AutFunction;

public class MoveForwardInInches implements AutFunction {
	
	public boolean isDone;

	@Override
	public void update(long deltaTime) {
		
	}

	@Override
	public boolean isDone() {
		return isDone;
	}
	
}