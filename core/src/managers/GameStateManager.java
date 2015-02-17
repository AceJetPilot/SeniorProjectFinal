package managers;

import gamestates.GameState;
import gamestates.MenuState;
import gamestates.Playstate;

public class GameStateManager {
	
	//current gamestate
	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 420;
	
	public GameStateManager(){
		setState(MENU);
	}
	
	public void setState(int state){
		if(gameState != null){
			gameState.dispose();
		}
		if(state == MENU){
			gameState = new MenuState(this);
		}
		if(state == PLAY){
			gameState = new Playstate(this);
		}
	}
	
	public void update(float dt){
		gameState.update(dt);
	}
	
	public void draw(){
		gameState.draw();
	}
	
}
