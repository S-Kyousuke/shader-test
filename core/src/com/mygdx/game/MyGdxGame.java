package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class MyGdxGame extends ApplicationAdapter {
    private static final float SCENE_WIDTH = 1024f;
    private static final float SCENE_HEIGHT = 576f;

    private SpriteBatch batch;
    private Texture image;
    private ShaderProgram shader;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float[] resolution = new float[2];

    private boolean shaderEnable;
    private Vector3 touchPos = new Vector3();

    private float radius = 0.25f;
    private float softness = 0.2f;
    private float darkness = 0.95f;

    @Override
    public void create() {
        image = new Texture("image.png");
        batch = new SpriteBatch();
        shader = new ShaderProgram(
                Gdx.files.internal("shaders/myshader4.vert"),
                Gdx.files.internal("shaders/myshader4.frag"));
        if (!shader.isCompiled()) {
            Gdx.app.error("Couldn't load shader: ", shader.getLog());
        }

        shader.begin();
        shader.setUniformf("sceneRatio", SCENE_WIDTH / SCENE_HEIGHT);
        shader.setUniformf("radius", radius);
        shader.setUniformf("softness", softness);
        shader.setUniformf("darkness", darkness);
        shader.end();

        camera = new OrthographicCamera();
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);

        camera.position.x = image.getWidth() * 0.5f;
        camera.position.y = image.getHeight() * 0.5f;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();

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
    }

    private void handleInput() {
        final float change = Gdx.graphics.getDeltaTime() * 0.25f;

        if (Gdx.input.isTouched()) {
            radius += change;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            darkness += change;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            darkness -= change;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            softness += change;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            softness -= change;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            shaderEnable = !shaderEnable;
            if (shaderEnable) {
                batch.setShader(shader);
            } else {
                batch.setShader(null);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
            radius = Math.max(0.20f, radius % 1f);
            darkness = MathUtils.clamp(darkness, 0.5f, 1f);
            softness = MathUtils.clamp(softness, 0.01f, 1f);

            shader.begin();
            shader.setUniformf("radius", radius);
            shader.setUniformf("softness", softness);
            shader.setUniformf("darkness", darkness);
            shader.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        resolution[0] = width;
        resolution[1] = height;

        shader.begin();
        shader.setUniformf("resolution", width, height);
        shader.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shader.dispose();
    }

}

