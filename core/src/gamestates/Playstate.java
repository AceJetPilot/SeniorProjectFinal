package gamestates;

import java.util.ArrayList;

import managers.GameKeys;
import managers.GameStateManager;

import com.SpaceScroller.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import entities.Bullet;
import entities.EnemyBullet;
import entities.FlyingSaucer;
import entities.Particle;
import entities.Player;

public class Playstate extends GameState{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private BitmapFont font;
	private Player hudPlayer;
	
	private Player player;
	
	private ArrayList<Bullet> bullets;
	private ArrayList<Particle> particles;
	private ArrayList<FlyingSaucer> saucers;
	private ArrayList<EnemyBullet> eBullets;
	
	private final String levelNumber = "Level:";
	
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	private int totalSaucers;
	private int numSaucersLeft;
	private int enumToSpawn;
	private int totalWave;
	private int wave;
	private float spawnTimer;
	private float spawnTime;
	
	private float maxDelay;
	private float minDelay;
	private float currentDelay;
	private float bgTimer;
	private boolean playLowPulse;
	
	public Playstate(GameStateManager gsm){
		super(gsm);
	}

	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		//setfont
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		font = gen.generateFont(20);
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets); 
		
		saucers = new ArrayList<FlyingSaucer>();
		eBullets = new ArrayList<EnemyBullet>();
		
		particles = new ArrayList<Particle>();
		
		hudPlayer = new Player(null);
		
		//set up bg music
		maxDelay = 1;
		minDelay = 0.25f;
		currentDelay = maxDelay;
		bgTimer = maxDelay;
		playLowPulse = true;
		
				
		wave = 1;
		spawnTimer = 0;
		spawnTime = 3;
		level = 1;
		enumToSpawn = 4;
		totalWave = 10;
		spawnSaucers();
		
	}
	
	private void createParticles(float x, float y){
		for(int i = 0; i < 6; i++){
			particles.add(new Particle(x, y));
		}
	}
	
	
	
	private void spawnSaucers(){
		//saucers.clear();
		System.out.println("level:" + level);
		System.out.println("wave:" + wave);
		
		double typePicker;
		if(wave != totalWave){
			enumToSpawn = 4;
		}
		else{
			wave = 0;
			level++;
			totalWave += 5; 
		}
		if(level == 4){
			gsm.setState(GameStateManager.MENU);
		}
		//int numToSpawn = 4 + level - 1;
		//int enumToSpawn = 4;
		System.out.println(enumToSpawn);
		totalSaucers = enumToSpawn;
		numSaucersLeft = totalSaucers;
		
		for(int i = 0; i < totalSaucers; i++){
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);
			
			float dx = x * player.getx();
			float dy = y - player.gety();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			
			while(dist < 100){
				x = MathUtils.random(Game.WIDTH);
				y = MathUtils.random(Game.HEIGHT);
				dx = x * player.getx();
				dy = y - player.gety();
				dist = (float) Math.sqrt(dx * dx + dy * dy);
			}
			typePicker = Math.random();
			if(typePicker <= 0.7){
				saucers.add(new FlyingSaucer(0, 0, player, eBullets));
			}
			else if(typePicker > 0.7){
				saucers.add(new FlyingSaucer(1, 0, player, eBullets));
			}
			
			//saucers.add(new FlyingSaucer(1, 0, player, eBullets));
		}
	}
	
	public long getLevel(){ return level;}

	public void update(float dt) {
		//System.out.println("play state updating");
		handleInput();
		
		//next level
		spawnTimer += dt;
		//System.out.println(spawnTimer);
		if(spawnTimer > spawnTime){
			wave++;
			spawnSaucers();
			spawnTimer = 0;
		}
		//resets player
		player.update(dt);
		if(player.isDead()){
			if(player.getLives() == 0){
				gsm.setState(GameStateManager.MENU);
			}
			player.reset();
			player.loseLife();
			return;
		}
		//updates bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()){
				bullets.remove(i);
				i--;
			}
		}
		
		//updates particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).update(dt);
			if(particles.get(i).shouldRemove()){
				particles.remove(i);
				i--;
			}	
		}
		
		//updates Flying Saucers
		for(int i = 0; i < saucers.size(); i++){
			saucers.get(i).update(dt);
			if(saucers.get(i).shouldRemove()){
				saucers.remove(i);
				i--;
			}	
		}	
		//updates enemy bullets
		for(int i = 0; i < eBullets.size(); i++){
			eBullets.get(i).update(dt);
			if(eBullets.get(i).shouldRemove()){
				eBullets.remove(i);
				i--;
			}
		}
		
		checkCollisions();	
		
	}
	
	private void checkCollisions(){
		//player-asteroid collision
		/*if(!player.isHit()){
			for(int i = 0; i < asteroids.size(); i++){
				Asteroid a = asteroids.get(i);
				if(a.intersects(player)){
					player.hit();
					asteroids.remove(i);
					i--;
					splitAsteroids(a);
					Jukebox.play("explode");
					break;
				}
			}
		}*/
		//bullet-asteroid collision
		/*for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			for(int j = 0; j < asteroids.size(); j++){
				Asteroid a = asteroids.get(j);
				//if a contains b
				if(a.contains(b.getx(), b.gety())){
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroids(a);
					//increment score
					player.incrementScore(a.getScore());
					Jukebox.play("explode");
					break;
				}
			}
		}*/
		
		//player-flying saucer collision
		/*if(!player.isHit()){
			for(int i = 0; i < saucers.size(); i++){
				FlyingSaucer a = saucers.get(i);
				if(a.intersects(player)){
					player.hit();
					saucers.remove(i);
					numSaucersLeft--;
					i--;
					createParticles(player.getx(), player.gety());
					Jukebox.play("explode");
					break;
				}
			}
		}*/
		
		//bullet-flying saucer collision
		/*for(int i = 0; i < bullets.size(); i++){
			Bullet b = bullets.get(i);
			for(int j = 0; j < saucers.size(); j++){
				FlyingSaucer a = saucers.get(j);
				//if a contains b
				if(a.contains(b.getx(), b.gety())){
					bullets.remove(i);
					i--;
					saucers.remove(j);
					j--;
					//increment score
					player.incrementScore(a.getScore());
					Jukebox.play("explode");
					break;
				}
			}
		}*/
		
		//player-enemy bullet collsion
		/*if(!player.isHit()){
			for(int i = 0; i < eBullets.size(); i++){
				EnemyBullet b = eBullets.get(i);
				if(player.contains(b.getx(), b.gety())){
					player.hit();
					eBullets.remove(i);
					i--;
					Jukebox.play("explode");
				}
			}
		}*/
	}

	public void draw() {
		//System.out.println("play state drawing");
		player.draw(sr);
		
		//draws bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(sr);
		}
		
		//draw particles
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).draw(sr);
		}
		
		//draw saucers
		for(int i = 0; i < saucers.size(); i++){
			saucers.get(i).draw(sr);
		}
		
		//draw enemy bullets
		for(int i = 0; i < eBullets.size(); i++){
			eBullets.get(i).draw(sr);
		}
		
		//draw score
		sb.setColor(1, 1, 1, 1);
		sb.begin();
		font.draw(sb, Long.toString(player.getScore()), 40, 390 );
		font.draw(sb, levelNumber, 390, 390);
		//font.draw(sb, level, 410, 390);
		sb.end();
		
		//draw lives
		for(int i = 0; i < player.getLives(); i++){
			hudPlayer.setPosition(40 + i * 10, 360);
			hudPlayer.draw(sr);
		}
		
		
	}

	public void handleInput() {
		player.setLeft(GameKeys.isDown(GameKeys.LEFT));
		player.setRight(GameKeys.isDown(GameKeys.RIGHT));
		player.setUp(GameKeys.isDown(GameKeys.UP));
		player.setDown(GameKeys.isDown(GameKeys.DOWN));
		if(GameKeys.isPressed(GameKeys.SPACE)){
			player.shoot();
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
