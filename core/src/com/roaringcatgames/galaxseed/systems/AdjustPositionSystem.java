package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.roaringcatgames.galaxseed.components.AdjustablePositionComponent;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.VectorUtils;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;

/**
 * Created by barry on 11/15/16.
 */
public class AdjustPositionSystem extends IteratingSystem implements InputProcessor{

    private IGameProcessor game;
    private OrthographicCamera cam;
    private Viewport viewport;

    private ShapeRenderer shapeRenderer;

    private Entity currentAdjustable;

    private Vector3 adjustment = new Vector3(0f, 0f, 0f);

    public AdjustPositionSystem(OrthographicCamera camera, Viewport viewport, IGameProcessor game){
        super(Family.all(AdjustablePositionComponent.class, TransformComponent.class).get());
        this.cam = camera;
        this.viewport = viewport;
        this.game = game;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        game.addInputProcessor(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        game.removeInputProcessor(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(currentAdjustable != null){
            AdjustablePositionComponent ac = Mappers.adjust.get(currentAdjustable);
            TransformComponent tc = K2ComponentMappers.transform.get(currentAdjustable);
            Gdx.gl20.glLineWidth(1f);
            shapeRenderer.setProjectionMatrix(cam.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.CORAL);
            shapeRenderer.rect(tc.position.x - (ac.selectionWidth/2f),
                               tc.position.y - (ac.selectionHeight/2f),
                               ac.selectionWidth,
                               ac.selectionHeight);
            shapeRenderer.end();
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AdjustablePositionComponent ac = Mappers.adjust.get(entity);
        if(ac.isAdjusting){
            currentAdjustable = entity;
        }
    }

    /**
     * Input Processor implementation
     **/

    float x=0f, y=0f, z=0f;
    Vector3 touchPoint = new Vector3();
    Array<Entity> selectables = new Array<>();
    Rectangle target = new Rectangle();

     @Override
    public boolean keyDown(int keycode) {

         if(currentAdjustable != null) {
             AdjustablePositionComponent ac = Mappers.adjust.get(currentAdjustable);
             if(ac.isAdjusting) {
                 switch (keycode) {
                     case Input.Keys.UP:
                         y = ac.yAdjust;
                         break;
                     case Input.Keys.DOWN:
                         y = -ac.yAdjust;
                         break;
                     case Input.Keys.RIGHT:
                         x = ac.xAdjust;
                         break;
                     case Input.Keys.LEFT:
                         x = -ac.xAdjust;
                         break;
                     case Input.Keys.ESCAPE:
                         ac.isAdjusting = false;
                         currentAdjustable = null;
                         break;
                 }

                 if(currentAdjustable != null) {
                     TransformComponent tc = K2ComponentMappers.transform.get(currentAdjustable);
                     tc.position.add(x, y, z);
                 }
                 x = y = z = 0f;

                 return true;
             }
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
        touchPoint.set(screenX, screenY, 0f);
        this.viewport.unproject(touchPoint);

        selectables.clear();
        ImmutableArray<Entity> transformable = getEngine().getEntitiesFor(Family.all(TransformComponent.class).get());
        selectables.addAll(transformable.toArray());

        float w = 1f;
        float h = 1f;
        for(Entity e:selectables){
            TransformComponent tc = K2ComponentMappers.transform.get(e);
            AdjustablePositionComponent ac = Mappers.adjust.get(e);
            if(ac != null){
                w = ac.selectionWidth;
                h = ac.selectionHeight;
            }

            target.set(tc.position.x - (w/2f), tc.position.y - (h/2f), w, h);

            if(target.contains(touchPoint.x, touchPoint.y)){
                currentAdjustable = e;
                if(ac == null){
                    e.add(AdjustablePositionComponent.create(getEngine())
                        .setIsAjusting(true));
                }else{
                    ac.isAdjusting = true;
                }
                //break;
            }
        }

        return currentAdjustable != null;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
