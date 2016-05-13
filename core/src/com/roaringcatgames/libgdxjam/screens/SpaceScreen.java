    package com.roaringcatgames.libgdxjam.screens;

    import com.badlogic.ashley.core.EntitySystem;
    import com.badlogic.ashley.core.Family;
    import com.badlogic.ashley.core.PooledEngine;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputProcessor;
    import com.badlogic.gdx.audio.Music;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.math.Vector3;
    import com.badlogic.gdx.utils.Array;
    import com.badlogic.gdx.utils.viewport.FitViewport;
    import com.badlogic.gdx.utils.viewport.Viewport;
    import com.roaringcatgames.kitten2d.ashley.systems.*;
    import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
    import com.roaringcatgames.libgdxjam.App;
    import com.roaringcatgames.libgdxjam.Assets;
    import com.roaringcatgames.libgdxjam.components.EnemyComponent;
    import com.roaringcatgames.libgdxjam.components.HealthPackComponent;
    import com.roaringcatgames.libgdxjam.data.EnemySpawns;
    import com.roaringcatgames.libgdxjam.systems.*;
    import com.roaringcatgames.libgdxjam.values.GameState;
    import com.roaringcatgames.libgdxjam.values.Volume;
    import com.roaringcatgames.libgdxjam.values.Z;

    /**
     * Created by barry on 12/22/15 @ 5:51 PM.
     */
    public class SpaceScreen extends LazyInitScreen implements InputProcessor {

        private IScreenDispatcher dispatcher;
        private SpriteBatch batch;
        private PooledEngine engine;
        private OrthographicCamera cam;
        private Viewport viewport;
        private Music music;

        private Array<EntitySystem> playingOnlySystems;

        public SpaceScreen(SpriteBatch batch, IScreenDispatcher dispatcher) {
            super();
            this.batch = batch;
            this.dispatcher = dispatcher;
            this.playingOnlySystems = new Array<>();
        }


        @Override
        protected void init() {
            engine = new PooledEngine();

            RenderingSystem renderingSystem = new RenderingSystem(batch, App.PPM);
            cam = renderingSystem.getCamera();
            viewport = new FitViewport(20f, 30f, cam);
            viewport.apply();
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.position.set(cam.viewportWidth/2f, cam.viewportHeight/2f, 0);

            //Normally we would use a different camera, but our main camera
            //  never moves, so we're safe to use a single one here
            OrthographicCamera guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            guiCam.position.set(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 0f);

            TextRenderingSystem textRenderingSystem = new TextRenderingSystem(batch, guiCam, cam);

            Vector3 playerPosition = new Vector3(
                    App.playerLastPosition.x,
                    App.playerLastPosition.y,
                    Z.player);

            Gdx.app.log("Menu Screen", "Cam Pos: " + cam.position.x + " | " +
                    cam.position.y + " Cam W/H: " + cam.viewportWidth + "/" + cam.viewportHeight);


            //AshleyExtensions Systems
            MovementSystem movementSystem = new MovementSystem();
            engine.addSystem(movementSystem);
            engine.addSystem(new RotationSystem());
            engine.addSystem(new BoundsSystem());
            engine.addSystem(new AnimationSystem());
            engine.addSystem(new TweenSystem());

            //Custom Systems
            Vector2 minBounds = new Vector2(0f, 0f);
            Vector2 maxBounds = new Vector2(cam.viewportWidth, cam.viewportHeight);
            engine.addSystem(new CleanUpSystem(maxBounds.cpy().scl(-0.25f), maxBounds.cpy().scl(1.25f)));
            engine.addSystem(new PlayerSystem(playerPosition, 0.5f, cam));
            FiringSystem firingSystem = new FiringSystem();
            engine.addSystem(firingSystem);
            EnemySpawnSystem enemySpawnSystem = new EnemySpawnSystem();
            engine.addSystem(enemySpawnSystem);
            EnemyFiringSystem enemyFiringSystem = new EnemyFiringSystem();
            engine.addSystem(enemyFiringSystem);
            engine.addSystem(new RemainInBoundsSystem(minBounds, maxBounds));
            engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
            engine.addSystem(new BackgroundSystem(minBounds, maxBounds, true, true));
            engine.addSystem(new ParticleSystem());
            engine.addSystem(new BulletSystem());
            engine.addSystem(new ScoreSystem());
            engine.addSystem(new ShakeSystem());
            engine.addSystem(new OscillationSystem());


            EnemyDamageSystem enemyDmgSystem = new EnemyDamageSystem();
            PlayerDamageSystem playerDmgSystem = new PlayerDamageSystem();
            engine.addSystem(enemyDmgSystem);
            engine.addSystem(playerDmgSystem);
            engine.addSystem(new ExplosionSystem());
            engine.addSystem(new FollowerSystem(Family.one(EnemyComponent.class, HealthPackComponent.class).get()));

            GameOverSystem gameOverSystem = new GameOverSystem(cam, dispatcher);
            gameOverSystem.setProcessing(false);
            engine.addSystem(gameOverSystem);
            engine.addSystem(new FadingSystem());
            engine.addSystem(new HealthPackSystem());

            //Extension Systems
            engine.addSystem(renderingSystem);
            engine.addSystem(textRenderingSystem);
            engine.addSystem(new PlayerHealthSystem(cam));
            PathFollowSystem pathFollowSystem = new PathFollowSystem();
            engine.addSystem(pathFollowSystem);
            engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));
            engine.addSystem(new FPSSystem(Assets.get48Font(), new Vector2(App.W - 3f, 3f), 10));
            App.game.multiplexer.addProcessor(this);

            playingOnlySystems.add(movementSystem);
            playingOnlySystems.add(firingSystem);
            playingOnlySystems.add(enemyFiringSystem);
            playingOnlySystems.add(enemySpawnSystem);
            playingOnlySystems.add(enemyDmgSystem);
            playingOnlySystems.add(playerDmgSystem);
            playingOnlySystems.add(pathFollowSystem);

            music = Assets.getBackgroundMusic();

        }

        @Override
        public void show() {
            super.show();

            App.setState(GameState.PLAYING);
            EnemySpawns.resetSpawns();

            //Start Music Playing
            music.setVolume(Volume.BG_MUSIC);
            music.setLooping(true);
            music.play();
        }

        /**************************
         * Screen Adapter Methods
         **************************/
        GameState lastState;
        @Override
        protected void update(float deltaChange) {
            float deltaToApply = Math.min(deltaChange, App.MAX_DELTA_TICK);
            if(App.isSlowed()){
                App.setTimeSpentSlow(App.getTimeSpentSlow() + deltaChange);
                if(App.getTimeSpentSlow() >= App.PAUSE_LENGTH){
                    App.setSlowed(false);
                }
                if(App.isSlowed()) {
                    deltaToApply *= App.SLOW_SCALE;
                }
            }
            engine.update(deltaToApply);

            if(App.getState() != lastState){
                lastState = App.getState();
                if(lastState == GameState.GAME_OVER) {
                    music.stop();
                    engine.getSystem(GameOverSystem.class).setProcessing(true);
                    for (EntitySystem es : playingOnlySystems) {
                        if(es.checkProcessing()) {
                            es.setProcessing(false);
                        }
                    }
                }else if(lastState == GameState.PLAYING){
                    music.play();
                    engine.getSystem(GameOverSystem.class).setProcessing(false);
                    for (EntitySystem es : playingOnlySystems) {
                        if(!es.checkProcessing()) {
                            es.setProcessing(true);
                        }
                    }
                }
            }
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

            if(keycode == Input.Keys.E){
                for(EntitySystem s:engine.getSystems()){
                    if(s instanceof EnemySpawnSystem) {
                        s.setProcessing(!s.checkProcessing());
                    }
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
            cam.zoom += amount;
            return false;
        }


    }
