package com.chatnoir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ChatNoir extends ApplicationAdapter {
	SpriteBatch batch;

	
	@Override
	public void create () {
		loadData();
		init();
	}
	private void init(){
		batch = new SpriteBatch();
	}

	private void loadData(){

	}

	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
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
