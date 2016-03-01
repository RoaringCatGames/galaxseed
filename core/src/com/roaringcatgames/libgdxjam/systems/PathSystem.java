package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.roaringcatgames.kitten2d.ashley.components.PathFollowComponent;
import com.roaringcatgames.libgdxjam.App;


/**
 * Created by barry on 2/13/16 @ 10:35 PM.
 */
public class PathSystem extends IteratingSystem implements InputProcessor {

    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    private Vector2 v1, v2, p0, p1, p2, tmp;


    Path<Vector2> path;

    public PathSystem(OrthographicCamera cam){
        super(Family.all(PathFollowComponent.class).get());
        this.cam = cam;
        this.shapeRenderer = new ShapeRenderer();
        this.v1 = new Vector2();
        this.v2 = new Vector2();
        this.p0 = new Vector2(0f, 0f);
        this.p1 = new Vector2(15f, 25f);
        this.p2 = new Vector2(20f, 30f);
        this.tmp = new Vector2();
        this.path = new Bezier<>(p0, p1, p2);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        switch(currentPointType){
            case P0:
                p0.set(mousePoint.x, mousePoint.y);
                break;
            case P1:
                p1.set(mousePoint.x, mousePoint.y);
                break;
            case P2:
                p2.set(mousePoint.x, mousePoint.y);
                break;
            default:
                break;
        }

        float k = 100f;
        Gdx.gl20.glLineWidth(3f);
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        for(float i = 0f; i < k-1f; ++i)
        {
            path.valueAt(v1, (i / k));
            path.valueAt(v2, ((i + 1f) / k));
            shapeRenderer.line(v1, v2);
        }
        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.circle(p0.x, p0.y, 1f);
        shapeRenderer.circle(p1.x, p1.y, 1f);
        shapeRenderer.circle(p2.x, p2.y, 1f);
        shapeRenderer.end();


    }

    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        App.game.multiplexer.addProcessor(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        App.game.multiplexer.removeProcessor(this);
    }

    private enum PointSetter { P0, P1, P2, NONE }

    private PointSetter currentPointType = PointSetter.NONE;

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.NUM_1){
            currentPointType = PointSetter.P0;
        }else if(keycode == Input.Keys.NUM_2){
            currentPointType = PointSetter.P1;
        }else if(keycode == Input.Keys.NUM_3){
            currentPointType = PointSetter.P2;
        }else if(keycode == Input.Keys.ESCAPE){
            currentPointType = PointSetter.NONE;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    Vector3 mousePoint = new Vector3();
    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        mousePoint.set(screenX, screenY, 0);
        cam.unproject(mousePoint);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
