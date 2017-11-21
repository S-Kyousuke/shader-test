package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.utils.Formatter;

public class MyGdxGame extends InputAdapter implements ApplicationListener {
    private static final float SCENE_WIDTH = 1024f;
    private static final float SCENE_HEIGHT = 576f;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Gui gui;

    private Texture image;
    private ShaderProgram shader;

    private boolean shaderEnable;
    private Vector3 touchPos = new Vector3();

    private float[] resolution = new float[2];
    private float radius;
    private float softness;
    private float darkness;

    @Override
    public void create() {
        image = new Texture("image.png");
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);

        camera.position.x = image.getWidth() * 0.5f;
        camera.position.y = image.getHeight() * 0.5f;

        Gdx.input.setInputProcessor(this);

        gui = new Gui();

        shader = new ShaderProgram(
                Gdx.files.internal("shaders/myshader.vert"),
                Gdx.files.internal("shaders/myshader.frag"));
        if (!shader.isCompiled()) {
            Gdx.app.error("Couldn't load shader: ", shader.getLog());
        }

        initUniform();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gui.update();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (shaderEnable) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            camera.project(touchPos);
            float u = touchPos.x / resolution[0];
            float v = touchPos.y / resolution[1];
            shader.setUniformf("position", new Vector2(u, v));
        }
        batch.draw(image, 0, 0);
        batch.end();

        gui.draw();
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount > 0) {
            addRadius(0.05f);
        } else {
            addRadius(-0.05f);
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                shaderEnable = !shaderEnable;
                if (shaderEnable) {
                    batch.setShader(shader);
                    gui.addMessage("เปิด shader", 2f);
                } else {
                    batch.setShader(null);
                    gui.addMessage("ปิด shader", 2f);
                }
                break;
            case Input.Keys.W:
                addDarkness(0.02f);
                break;
            case Input.Keys.S:
                addDarkness(-0.02f);
                break;
            case Input.Keys.D:
                addSoftness(0.05f);
                break;
            case Input.Keys.A:
                addSoftness(-0.05f);
                break;
            case Input.Keys.ENTER:
                initUniform();
                gui.addMessage("รีเซตเป็นค่าเริ่มต้น", 2f);
                break;
            default:
        }
        return true;
    }

    private void addRadius(float value) {
        radius = MathUtils.clamp(radius + value, 0.20f, 1f);
        updateUniform("radius", radius);
        gui.addMessage("รัศมี: " + Formatter.decimal(radius, 2), 2f);
    }

    private void addSoftness(float value) {
        softness = MathUtils.clamp(softness + value, 0.05f, 1f);
        updateUniform("softness", softness);
        gui.addMessage("ความนุ่ม: " + Formatter.decimal(softness, 2), 2f);
    }

    private void addDarkness(float value) {
        darkness = MathUtils.clamp(darkness + value, 0.80f, 1f);
        updateUniform("darkness", darkness);
        gui.addMessage("ความมืด: " + Formatter.decimal(darkness, 2), 2f);
    }

    private void updateUniform(String name, float value) {
        shader.begin();
        shader.setUniformf(name, value);
        shader.end();
    }

    private void initUniform() {
        radius = 0.25f;
        softness = 0.20f;
        darkness = 0.94f;

        shader.begin();
        shader.setUniformf("sceneRatio", SCENE_WIDTH / SCENE_HEIGHT);
        shader.setUniformf("radius", radius);
        shader.setUniformf("softness", softness);
        shader.setUniformf("darkness", darkness);
        shader.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        resolution[0] = width;
        resolution[1] = height;

        shader.begin();
        shader.setUniformf("resolution", width, height);
        shader.end();

        gui.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shader.dispose();
        gui.dispose();
    }

    @Override
    public void pause() {
        // no need to do anything
    }

    @Override
    public void resume() {
        // no need to do anything
    }
}

