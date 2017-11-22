package com.mygdx.game.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderTest1 extends ApplicationAdapter {

    Texture grass;
    Texture dirt;
    Texture mask;

    SpriteBatch batch;
    ShaderProgram program;

    TextureRegion region;

    @Override
    public void create() {
        grass = new Texture("grass.png");
        grass.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        grass.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        region = new TextureRegion(grass);
        region.setRegion(0, 0, 1024, 576);

        batch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(region, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        grass.dispose();
    }
}
