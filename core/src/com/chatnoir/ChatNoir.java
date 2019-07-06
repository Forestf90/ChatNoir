package com.chatnoir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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

		batch.begin();
		grid.draw(sr, batch, cat);
		batch.end();
	}

	private void update(){
		handleInput();

	}

	private void handleInput(){

	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
