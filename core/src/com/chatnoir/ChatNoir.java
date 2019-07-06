package com.chatnoir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class ChatNoir extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer sr;
	Grid grid;
	Cat cat;
	@Override
	public void create () {
		loadData();
		init();
	}
	private void init(){

		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		grid = new Grid();
	}

	private void loadData(){
		cat= new Cat(new Texture("cat.png"));
	}

	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(1, 1,1,  1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
				(Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

		grid.draw(sr);
		batch.begin();
		batch.draw(cat.getTexture(), grid.map[cat.posX][cat.posY].x -grid.WIDTH/2,
				grid.map[cat.posX][cat.posY].y - grid.HEIGHT/2, grid.WIDTH, grid.HEIGHT);
		batch.end();
	}

	private void update(){
		handleInput();

	}

	private void handleInput(){

		if(Gdx.input.isTouched()){
			 Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());

			 for(int i=0 ; i <grid.map.length; i++){
			 	for(int j=0; j<grid.map[i].length; j++){

			 		if(grid.map[i][j].contains(touch)){
			 			grid.map[i][j].open = false;
					}
				}
			 }
		}
	}

	@Override
	public void dispose () {
		cat.getTexture().dispose();
		sr.dispose();
		batch.dispose();
	}
}
