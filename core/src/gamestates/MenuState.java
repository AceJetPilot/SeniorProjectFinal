package gamestates;

import com.SpaceScroller.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import managers.GameKeys;
import managers.GameStateManager;

public class MenuState extends GameState{

	private SpriteBatch sb;
	private BitmapFont titleFont;
	private BitmapFont font;
	
	private final String title = "Space Scroller";
	
	private int currentItem;
	private String[] menuItems;
	
	public MenuState(GameStateManager gsm){
		super(gsm);
	}

	public void init() {
		sb = new SpriteBatch();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		
		titleFont = gen.generateFont(56);
		titleFont.setColor(Color.WHITE);
		
		font = gen.generateFont(20);
		
		menuItems = new String[]{"Play", "Highscores", "Quit"};
	}

	public void update(float dt) {

		handleInput();
		
	}

	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		
		//draw title
		sb.begin();
		float width = titleFont.getBounds(title).width;
		titleFont.draw(sb, title, (Game.WIDTH - width) / 2, 300);
		
		//draw menu
		for(int i = 0; i < menuItems.length; i++){
			width = font.getBounds(menuItems[i]).width;
			if(currentItem == i) font.setColor(Color.RED);
			else font.setColor(Color.WHITE);
			font.draw(sb, menuItems[i], (Game.WIDTH - width) / 2, 180 - 35 * i);
		}
		sb.end();
	}

	public void handleInput() {
		if(GameKeys.isPressed(GameKeys.UP)){
			if(currentItem > 0){
				currentItem--;
			}
		}
		if(GameKeys.isPressed(GameKeys.DOWN)){
			if(currentItem < menuItems.length - 1){
				currentItem++;
			}
		}
		if(GameKeys.isPressed(GameKeys.ENTER)){
			select();
		}
		
	}
	private void select(){
		//play
		if(currentItem == 0){
			gsm.setState(GameStateManager.PLAY);
		}
		else if(currentItem == 1){
			//gsm.setSTate(GameStateManager.HIGHSCORES);
		}
		else if(currentItem == 2){
			Gdx.app.exit();
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
