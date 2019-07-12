package com.chatnoir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ChatNoir extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer sr;
	Grid grid;
	Cat cat;
	boolean drawAnimation =false;
	boolean moveCat =false;
	boolean block = true;
	boolean gameRun = true;
	private Stage stage;

	TextButton animButton;
	TextButton restartButton;
    Label statusLabel;
	Skin animSkin;
	@Override
	public void create () {
		loadData();
		init();
	}
	private void init(){

		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		grid = new Grid();

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		int buttonWidth = (Gdx.graphics.getWidth()*2) /7;

		animButton = new TextButton("Animations", animSkin, "toggle");
        animButton.setSize(buttonWidth,100);
        animButton.setPosition(Gdx.graphics.getWidth()-buttonWidth-20,50);
        animButton.getLabel().setFontScale(1.5f);
        animButton.addListener(new InputListener(){
//            @Override
//            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//				drawAnimation ^= true;
//            }
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			    if(moveCat) return true;
				drawAnimation ^= true;
				return true;
			}
        });

		restartButton = new TextButton("Restart", animSkin);
		restartButton.setSize(buttonWidth,100);
		restartButton.setPosition(20,50);
		restartButton.getLabel().setFontScale(1.5f);
		restartButton.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(moveCat) return true;
				resetGame();
				return true;
			}
		});

		statusLabel = new Label("",animSkin);
		statusLabel.setSize(Gdx.graphics.getWidth(),100);
		statusLabel.setPosition(0,220);
		statusLabel.setAlignment(Align.center);
		statusLabel.setFontScale(2.0f);


        stage.addActor(animButton);
        stage.addActor(statusLabel);
        stage.addActor(restartButton);
	}

	private void loadData(){
		cat= new Cat(new Texture("cat.png"));
        animSkin =new Skin(Gdx.files.internal("skin/uiskin.json"));
	}

	@Override
	public void render () {
		if(gameRun)update();
		Gdx.gl.glClearColor(1, 1,1,  1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
				(Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		grid.draw(sr);
		if(drawAnimation){ grid.drawAnimation(sr, cat.path, cat.visited);
		}
		batch.begin();
		batch.draw(cat.getTexture(), grid.map[cat.posX][cat.posY].x -grid.WIDTH/2,
				grid.map[cat.posX][cat.posY].y - grid.HEIGHT/2, grid.WIDTH, grid.HEIGHT);
		batch.end();

		stage.act();
		stage.draw();
	}

	private void update(){
		handleInput();
		if(!grid.animation && moveCat){
			moveCat();
			block =true;
			moveCat=false;
			//zrobic to tylko za pomocą block
		}

		if(cat.runAway(grid.map)){
			gameRun =false;
			statusLabel.setText("Cat esceped ! Click restart");
		}

	}

	private void handleInput(){
		if(!block) return;
		if(Gdx.input.isTouched()){
			 //Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY()-1-Gdx.input.getY());
			 int x= Gdx.input.getX();
			 int y= Gdx.graphics.getHeight() -Gdx.input.getY();

			 for(int i=0 ; i <grid.map.length; i++){
			 	for(int j=0; j<grid.map[i].length; j++){

			 		if(grid.map[i][j].contains(x, y)){
			 			if(!grid.map[i][j].open || cat.posX==i && cat.posY==j){
			 				return;
						}
			 			grid.map[i][j].open = false;
						cat.findPath(grid.map);
						if(drawAnimation){
							grid.animation = true;
							block = false;
							moveCat =true;
						}else moveCat();

					}
				}
			 }
		}
	}

	private void moveCat(){
		if(cat.path==null){
			gameWin();
		}else cat.makeMove();
	}
    private void resetGame(){
        grid = new Grid();
        cat.posX =5;
        cat.posY =5;
        cat.open.clear();
        cat.visited.clear();
        cat.path =null;
        gameRun=true;
        statusLabel.setText("");
    }

    private void gameWin(){
		gameRun =false;
		statusLabel.setText("Cat trapped ! Click restart");
	}
	@Override
	public void dispose () {
		cat.getTexture().dispose();
		sr.dispose();
		batch.dispose();
	}
}
