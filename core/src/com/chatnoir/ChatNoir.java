package com.chatnoir;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ChatNoir extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private Grid grid;
    private Cat cat;
    private boolean drawAnimation = false;
    private boolean moveCat = false;
    private boolean block = true;
    private boolean gameRun = true;
    private Stage stage;

    private TextButton animButton;
    private TextButton restartButton;
    private Label statusLabel;
    private Label titleLabel;
    private Skin animSkin;
    private BitmapFont font;

    @Override
    public void create() {
        loadData();
        init();
    }

    private void init() {

        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        grid = new Grid();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        int buttonWidth = (Gdx.graphics.getWidth() * 2) / 7;
        int buttonHeight = Gdx.graphics.getHeight() / 10;
//        animSkin.remove("sub-title", BitmapFont.class);
//        animSkin.remove("font", BitmapFont.class);
//        animSkin.remove("title", BitmapFont.class);
//        animSkin.add("sub-title", font, BitmapFont.class);
        animSkin.get(Label.LabelStyle.class).font = font;
        animSkin.get(TextButton.TextButtonStyle.class).font =font;


        //animSkin.get(Button.ButtonStyle.class).
        animButton = new TextButton("Animations", animSkin, "toggle");
        animButton.setLabel(new Label("Animations", animSkin));
        animButton.getLabel().setAlignment(Align.center);
        animButton.setSize(buttonWidth, buttonHeight);
        animButton.setPosition(Gdx.graphics.getWidth() - buttonWidth - 20, 50);
        //animButton.getLabel().setFontScale(1.5f);
        animButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (moveCat) return true;
                drawAnimation ^= true;
                return true;
            }
        });

        restartButton = new TextButton("Restart", animSkin, "oval3");
        restartButton.setLabel(new Label("Restart", animSkin));
        restartButton.getLabel().setAlignment(Align.center);
        restartButton.setSize(buttonWidth, buttonHeight);
        restartButton.setPosition(20, 50);
       // restartButton.getLabel().setFontScale(1.5f);
        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (moveCat) return true;
                resetGame();
                return true;
            }
        });

        statusLabel = new Label("", animSkin);
        statusLabel.setSize(Gdx.graphics.getWidth(), buttonHeight + 20);
        statusLabel.setPosition(0, Gdx.graphics.getHeight() / 2);
        statusLabel.setAlignment(Align.center);
        //statusLabel.setFontScale(2.0f);

        titleLabel = new Label("Chat Noir", animSkin);
        titleLabel.setWidth(Gdx.graphics.getWidth());
        titleLabel.setPosition(0,Gdx.graphics.getHeight()*0.9f);
        titleLabel.setAlignment(Align.center);

        stage.addActor(animButton);
        stage.addActor(statusLabel);
        stage.addActor(restartButton);
        stage.addActor(titleLabel);
    }

    private void loadData() {
        cat = new Cat(new Texture("cat.png"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/GreatVibes-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);
        generator.dispose();
        animSkin = new Skin(Gdx.files.internal("skin/lgdxs/lgdxs-ui.json"));
        //"skin/default/uiskin.json"
    }

    @Override
    public void render() {
        if (gameRun) update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT |
                (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        grid.draw(sr);
        if (drawAnimation) {
            grid.drawAnimation(sr, cat.path, cat.visited);
        }
        batch.begin();
        batch.draw(cat.getTexture(), grid.map[cat.posX][cat.posY].x - grid.WIDTH / 2,
                grid.map[cat.posX][cat.posY].y - grid.HEIGHT / 2, grid.WIDTH, grid.HEIGHT);
        batch.end();

        stage.act();
        stage.draw();
    }

    private void update() {
        handleInput();
        if (!grid.animation && moveCat) {
            moveCat();
            block = true;
            moveCat = false;
            //zrobic to tylko za pomocą block
        }

        if (cat.runAway(grid.map)) {
            gameRun = false;
            statusLabel.setText("Cat esceped ! Click restart");
        }

    }

    private void handleInput() {
        if (!block) return;
        if (Gdx.input.isTouched()) {
            //Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY()-1-Gdx.input.getY());
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (int i = 0; i < grid.map.length; i++) {
                for (int j = 0; j < grid.map[i].length; j++) {

                    if (grid.map[i][j].contains(x, y)) {
                        if (!grid.map[i][j].open || cat.posX == i && cat.posY == j) {
                            return;
                        }
                        grid.map[i][j].open = false;
                        cat.findPath(grid.map);
                        if (drawAnimation) {
                            grid.animation = true;
                            block = false;
                            moveCat = true;
                        } else moveCat();

                    }
                }
            }
        }
    }

    private void moveCat() {
        if (cat.path == null) {
            gameWin();
        } else cat.makeMove();
    }

    private void resetGame() {
        grid = new Grid();
        cat.posX = 5;
        cat.posY = 5;
        cat.open.clear();
        cat.visited.clear();
        cat.path = null;
        gameRun = true;
        statusLabel.setText("");
    }

    private void gameWin() {
        gameRun = false;
        statusLabel.setText("Cat trapped ! Click restart");
    }

    @Override
    public void dispose() {
        cat.getTexture().dispose();
        sr.dispose();
        batch.dispose();
    }
}
