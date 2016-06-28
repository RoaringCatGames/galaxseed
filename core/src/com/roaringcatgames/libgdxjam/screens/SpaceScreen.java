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
    import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
    import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
    import com.roaringcatgames.libgdxjam.App;
    import com.roaringcatgames.libgdxjam.Assets;
    import com.roaringcatgames.libgdxjam.components.*;
    import com.roaringcatgames.libgdxjam.data.EnemySpawns;
    import com.roaringcatgames.libgdxjam.systems.*;
    import com.roaringcatgames.libgdxjam.values.GameState;
    import com.roaringcatgames.libgdxjam.values.Volume;
    import com.roaringcatgames.libgdxjam.values.Z;

    /**
     * Created by barry on 12/22/15 @ 5:51 PM.
     */
    public class SpaceScreen extends LazyInitScreen implements InputProcessor {

        private IGameProcessor game;
        private IScreenDispatcher dispatcher;
        private PooledEngine engine;
        private Music music;

        private Array<EntitySystem> playingOnlySystems;

        public SpaceScreen(IGameProcessor game, IScreenDispatcher dispatcher) {
            super();
            this.game = game;
            this.dispatcher = dispatcher;
            this.playingOnlySystems = new Array<>();
        }


        @Override
        protected void init() {
            engine = new PooledEngine();

            RenderingSystem renderingSystem = new RenderingSystem(game.getBatch(), game.getCamera(), App.PPM);

            TextRenderingSystem textRenderingSystem = new TextRenderingSystem(game.getBatch(), game.getGUICamera(), game.getCamera());

            Vector3 playerPosition = new Vector3(
                    App.playerLastPosition.x,
                    App.playerLastPosition.y,
                    Z.player);

            Gdx.app.log("Menu Screen", "Cam Pos: " + game.getCamera().position.x + " | " +
                    game.getCamera().position.y + " Cam W/H: " + game.getCamera().viewportWidth + "/" + game.getCamera().viewportHeight);


            //AshleyExtensions Systems
            MovementSystem movementSystem = new MovementSystem();
            engine.addSystem(movementSystem);
            engine.addSystem(new RotationSystem());
            engine.addSystem(new BoundsSystem());
            engine.addSystem(new MultiBoundsSystem());
            engine.addSystem(new AnimationSystem());
            engine.addSystem(new TweenSystem());

            //Custom Systems
            Vector2 minBounds = new Vector2(0f, 0f);
            Vector2 maxBounds = new Vector2(game.getCamera().viewportWidth, game.getCamera().viewportHeight);
            engine.addSystem(new CleanUpSystem(maxBounds.cpy().scl(-0.25f), maxBounds.cpy().scl(1.25f)));
            engine.addSystem(new PlayerSystem(playerPosition, 0.5f, game.getCamera(), WeaponType.POLLEN_AURA));
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
            engine.addSystem(new PowerUpSystem());
            engine.addSystem(new WeaponChangeSystem(game));
            engine.addSystem(new HelicopterSeedSystem());
            engine.addSystem(new StatusSystem());
            //engine.addSystem(new PlayerHealthSystem(cam));


            EnemyDamageSystem enemyDmgSystem = new EnemyDamageSystem();
            PlayerDamageSystem playerDmgSystem = new PlayerDamageSystem();
            engine.addSystem(enemyDmgSystem);
            engine.addSystem(new PollenAuraSystem());
            engine.addSystem(playerDmgSystem);
            engine.addSystem(new ExplosionSystem());
            engine.addSystem(new FollowerSystem(Family.one(EnemyComponent.class,
                    HealthPackComponent.class,
                    PlayerComponent.class,
                    GunComponent.class,
                    WeaponDecorationComponent.class).get()));

            GameOverSystem gameOverSystem = new GameOverSystem(game.getCamera(), dispatcher);
            gameOverSystem.setProcessing(false);
            engine.addSystem(gameOverSystem);
            engine.addSystem(new FadingSystem());
            engine.addSystem(new HealthPackSystem());
            engine.addSystem(new WeaponDecorationSystem());
            engine.addSystem(new MessageSystem());

            //Extension Systems
            engine.addSystem(renderingSystem);
            engine.addSystem(textRenderingSystem);

            PathFollowSystem pathFollowSystem = new PathFollowSystem();
            engine.addSystem(pathFollowSystem);
            engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));
            engine.addSystem(new FPSSystem(Assets.get48Font(), new Vector2(App.W - 3f, App.H - 3f), 10));
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
            App.resetWeapons();

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
                if(App.isSlowed()) {
                    deltaToApply *= App.SLOW_SCALE;
                }
                music.getPosition();

            }
            engine.update(deltaToApply);

            if(App.getState() != lastState){
                GameState prevState = lastState;
                lastState = App.getState();
                if(lastState == GameState.GAME_OVER) {
                    music.stop();
                    engine.getSystem(GameOverSystem.class).setProcessing(true);
                    for (EntitySystem es : playingOnlySystems) {
                        if(es.checkProcessing()) {
                            es.setProcessing(false);
                        }
                    }
                }else if(lastState == GameState.PLAYING && prevState != GameState.WEAPON_SELECT){
                    music.play();
                    engine.getSystem(GameOverSystem.class).setProcessing(false);
                    for (EntitySystem es : playingOnlySystems) {
                        if(!es.checkProcessing()) {
                            es.setProcessing(true);
                        }
                    }
                }else if(prevState == GameState.WEAPON_SELECT && lastState == GameState.PLAYING){
                    Gdx.app.log("SpaceScreen", "WEAPON_SELECT => PLAYING");
                    App.setSlowed(false);
                } if(lastState == GameState.WEAPON_SELECT){
                    App.setSlowed(true);
                    engine.getSystem(WeaponChangeSystem.class).showWeaponSelect();
                }
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
            game.getCamera().zoom += amount;
            return false;
        }
    }
