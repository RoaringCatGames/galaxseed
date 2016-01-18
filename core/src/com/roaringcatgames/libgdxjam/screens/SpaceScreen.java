    package com.roaringcatgames.libgdxjam.screens;

    import com.badlogic.ashley.core.Entity;
    import com.badlogic.ashley.core.EntitySystem;
    import com.badlogic.ashley.core.PooledEngine;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputProcessor;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.math.Vector3;
    import com.badlogic.gdx.utils.viewport.ExtendViewport;
    import com.badlogic.gdx.utils.viewport.FitViewport;
    import com.badlogic.gdx.utils.viewport.Viewport;
    import com.roaringcatgames.kitten2d.ashley.systems.*;
    import com.roaringcatgames.libgdxjam.App;
    import com.roaringcatgames.libgdxjam.Z;
    import com.roaringcatgames.libgdxjam.systems.*;

    /**
     * Created by barry on 12/22/15 @ 5:51 PM.
     */
    public class SpaceScreen extends LazyInitScreen implements InputProcessor {

        private IScreenDispatcher dispatcher;
        private SpriteBatch batch;
        private PooledEngine engine;
        private OrthographicCamera cam;
        private Vector3 touchPoint;
        private Viewport viewport;

        private Entity ball;

        public SpaceScreen(SpriteBatch batch, IScreenDispatcher dispatcher) {
            super();
            this.batch = batch;
            this.dispatcher = dispatcher;
            this.touchPoint = new Vector3();
        }


        @Override
        protected void init() {
            engine = new PooledEngine();

            RenderingSystem renderingSystem = new RenderingSystem(batch, App.PPM);
            cam = renderingSystem.getCamera();
            viewport = new FitViewport(20f, 30f, cam);// FitViewport(20f, 30f, cam);
            viewport.apply();
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.position.set(cam.viewportWidth/2f, cam.viewportHeight/2f, 0);

            Vector3 playerPosition = new Vector3(
                    cam.position.x,
                    5f,
                    Z.player);

            Gdx.app.log("Menu Screen", "Cam Pos: " + cam.position.x + " | " +
                    cam.position.y + " Cam W/H: " + cam.viewportWidth + "/" + cam.viewportHeight);


            //AshleyExtensions Systems
            engine.addSystem(new MovementSystem());
            engine.addSystem(new RotationSystem());
            engine.addSystem(new BoundsSystem());
            engine.addSystem(new AnimationSystem());

            //engine.addSystem(new CircleBoundsSystem());
            //Custom Systems
            Vector2 minBounds = new Vector2(0f, 0f);
            Vector2 maxBounds = new Vector2(cam.viewportWidth, cam.viewportHeight);
            engine.addSystem(new CleanUpSystem(maxBounds.cpy().scl(-1f), maxBounds.cpy().scl(2f)));
            engine.addSystem(new PlayerSystem(playerPosition, 1f, cam));
            engine.addSystem(new FiringSystem());
            engine.addSystem(new EnemySpawnSystem());
            engine.addSystem(new EnemyFiringSystem());
            engine.addSystem(new RemainInBoundsSystem(minBounds, maxBounds));
            engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
            engine.addSystem(new BackgroundSystem(minBounds, maxBounds, true));
            engine.addSystem(new BulletSystem());
            engine.addSystem(new EnemyDamageSystem());
            engine.addSystem(new PlayerDamageSystem());
            engine.addSystem(new FollowerSystem());


            //Extension Systems
            engine.addSystem(renderingSystem);
            engine.addSystem(new PlayerHealthSystem(cam));
            //engine.addSystem(new GravitySystem(new Vector2(0f, -9.8f)));
            //engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));
            App.game.multiplexer.addProcessor(this);
        }

        /**************************
         * Screen Adapter Methods
         **************************/
        @Override
        protected void update(float deltaChange) {
            engine.update(Math.min(deltaChange, App.MAX_DELTA_TICK));
        }

        @Override
        public void resize(int width, int height) {
            super.resize(width, height);
            if(viewport != null) {
                viewport.update(width, height);
                cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
            }
        }

        /**************************
         * Input Processor Methods
         **************************/

        @Override
        public boolean keyDown(int keycode) {

//            if(keycode == Input.Keys.ESCAPE){
//                for(EntitySystem s:engine.getSystems()){
//                    if(!(s instanceof RenderingSystem) &&
//                            !(s instanceof DebugSystem) &&
//                            !(s instanceof PlayerHealthSystem)) {
//                        s.setProcessing(!s.checkProcessing());
//                    }
//                }
//            }

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

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            //cam.zoom += amount * 0.5f;
            return false;
        }


    }
