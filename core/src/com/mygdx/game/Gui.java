package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


public class Gui implements Disposable {

    private Stage stage;
    private BitmapFont font;
    private Array<Label> labels;

    public Gui() {
        stage = new Stage();
        font = new BitmapFont(Gdx.files.internal("thai.fnt"));
        labels = new Array<Label>();
    }

    public void update() {
        stage.act();
    }

    public void draw() {
        stage.draw();
    }

    public void addMessage(String text, float lifetime) {
        final Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        final Label messageLabel = new Label(text, style);
        messageLabel.setPosition(15f, 0);
        messageLabel.addAction(delay(lifetime, sequence(fadeOut(1f), new Action() {
            @Override
            public boolean act(float delta) {
                labels.removeValue(messageLabel, true);
                messageLabel.remove();
                return true;
            }
        })));
        moveLabelUp();
        stage.addActor(messageLabel);
        labels.add(messageLabel);
    }

    private void moveLabelUp() {
        for (int i = 0; i < labels.size ;i++) {
            Label label = labels.get(i);
            label.setPosition(label.getX(), label.getY() + 20f);
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }
}
