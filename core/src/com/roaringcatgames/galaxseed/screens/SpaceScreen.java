    package com.roaringcatgames.galaxseed.screens;

    import com.badlogic.ashley.core.EntitySystem;
    import com.badlogic.ashley.core.Family;
    import com.badlogic.ashley.core.PooledEngine;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputProcessor;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.math.Vector3;
    import com.badlogic.gdx.utils.Array;
    import com.roaringcatgames.galaxseed.IGameServiceController;
    import com.roaringcatgames.kitten2d.ashley.systems.*;
    import com.roaringcatgames.kitten2d.gdx.helpers.IGameProcessor;
    import com.roaringcatgames.kitten2d.gdx.screens.LazyInitScreen;
    import com.roaringcatgames.galaxseed.App;
    import com.roaringcatgames.galaxseed.components.*;
    import com.roaringcatgames.galaxseed.data.EnemySpawns;
    import com.roaringcatgames.galaxseed.systems.*;
    import com.roaringcatgames.galaxseed.values.GameState;
    import com.roaringcatgames.galaxseed.values.Z;

    /**
     * Created by barry on 12/22/15 @ 5:51 PM.
     */
    public class SpaceScreen extends LazyInitScreen implements InputProcessor {

        private IGameProcessor game;
        private IGameServiceController gameServiceController;
        private PooledEngine engine;

        private Array<EntitySystem> playingOnlySystems;

        private boolean hasStarted = false;
        private EntitySystem enemySpawnSystem;
        private BackgroundSystem bgSystem;

        public SpaceScreen(IGameProcessor game, IGameServiceController gameServices) {
            super();
            this.game = game;
            this.gameServiceController = gameServices;
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

            Vector2 minBounds = new Vector2(0f, 0f);
            Vector2 maxBounds = new Vector2(App.W, App.H);

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

            //Systems to control from screen
            FiringSystem firingSystem = new FiringSystem();
            enemySpawnSystem = new EnemySpawnSystem();
            enemySpawnSystem.setProcessing(false);
            EnemyFiringSystem enemyFiringSystem = new EnemyFiringSystem();
            EnemyDamageSystem enemyDmgSystem = new EnemyDamageSystem();
            PlayerDamageSystem playerDmgSystem = new PlayerDamageSystem();
            GameOverSystem gameOverSystem = new GameOverSystem(game);
            gameOverSystem.setProcessing(false);
            PathFollowSystem pathFollowSystem = new PathFollowSystem();
            bgSystem = new BackgroundSystem(minBounds, maxBounds, false, true);

            //Custom Systems

            engine.addSystem(new CleanUpSystem(maxBounds.cpy().scl(-0.25f), maxBounds.cpy().scl(1.25f)));
            engine.addSystem(new PlayerSystem(playerPosition, 0.5f, game, WeaponType.GUN_SEEDS));

            engine.addSystem(firingSystem);
            engine.addSystem(enemySpawnSystem);
            engine.addSystem(enemyFiringSystem);
            engine.addSystem(new RemainInBoundsSystem(minBounds, maxBounds));
            engine.addSystem(new ScreenWrapSystem(minBounds, maxBounds, App.PPM));
            engine.addSystem(bgSystem);
            engine.addSystem(new ParticleSystem());
            engine.addSystem(new BulletSystem());
            engine.addSystem(new ScoreSystem());
            engine.addSystem(new ShakeSystem());
            engine.addSystem(new OscillationSystem());
            engine.addSystem(new PowerUpSystem());
            engine.addSystem(new WeaponChangeSystem(game));
            engine.addSystem(new HelicopterSeedSystem());
            engine.addSystem(new StatusSystem());


            engine.addSystem(enemyDmgSystem);
            engine.addSystem(new PollenAuraSystem());
            engine.addSystem(playerDmgSystem);
            engine.addSystem(new ExplosionSystem());
            engine.addSystem(new FollowerSystem(Family.one(EnemyComponent.class,
                    HealthPackComponent.class,
                    PlayerComponent.class,
                    GunComponent.class,
                    WeaponDecorationComponent.class).get()));


            engine.addSystem(new DemoSystem());
            engine.addSystem(gameOverSystem);
            engine.addSystem(new FadingSystem());
            engine.addSystem(new HealthPackSystem());
            engine.addSystem(new WeaponDecorationSystem());
            engine.addSystem(new MessageSystem());

            //Extension Systems
            engine.addSystem(renderingSystem);
            engine.addSystem(textRenderingSystem);


            engine.addSystem(pathFollowSystem);
            //engine.addSystem(new DebugSystem(renderingSystem.getCamera(), Color.CYAN, Color.PINK, Input.Keys.TAB));
            //engine.addSystem(new FPSSystem(Assets.get48Font(), new Vector2(App.W - 3f, App.H - 3f), 10));

            App.game.multiplexer.addProcessor(this);

            playingOnlySystems.add(movementSystem);
            playingOnlySystems.add(firingSystem);
            playingOnlySystems.add(enemyFiringSystem);
            playingOnlySystems.add(enemySpawnSystem);
            playingOnlySystems.add(enemyDmgSystem);
            playingOnlySystems.add(playerDmgSystem);
            playingOnlySystems.add(pathFollowSystem);
        }

        @Override
        public void show() {
            super.show();

            App.setState(GameState.PLAYING);
            EnemySpawns.resetSpawns();
            App.resetWeapons();
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
            }
            engine.update(deltaToApply);

            if(App.getState() != lastState){
                GameState prevState = lastState;
                lastState = App.getState();
                if(lastState == GameState.GAME_OVER) {

                    if(gameServiceController != null){
                        int score = engine.getSystem(ScoreSystem.class).getScore();
                        Gdx.app.log("SPACE SCREEN", "Submitting Score: " + score);
                        gameServiceController.submitScore(score);
                    }

                    game.pauseBgMusic();
                    engine.getSystem(GameOverSystem.class).setProcessing(true);
                    for (EntitySystem es : playingOnlySystems) {
                        if(es.checkProcessing()) {
                            es.setProcessing(false);
                        }
                    }
                }else if(lastState == GameState.PLAYING && prevState != GameState.WEAPON_SELECT){
                    engine.getSystem(GameOverSystem.class).setProcessing(false);
                    if(hasStarted) {
                        for (EntitySystem es : playingOnlySystems) {
                            if (!es.checkProcessing()) {
                                es.setProcessing(true);
                            }
                        }
                    }
                }else if(prevState == GameState.WEAPON_SELECT && lastState == GameState.PLAYING){
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

            if(!hasStarted){
                bgSystem.placePlanets();
                enemySpawnSystem.setProcessing(true);
                game.playBgMusic("GAME");
                hasStarted = true;
            }

//            if(App.isDesktop()) {
//                Gdx.graphics.setCursor(App.getHiddenCursor());
//            }

            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {

//            if(App.isDesktop()) {
//                Gdx.graphics.setCursor(App.getActiveCursor());
//            }
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
            //game.getCamera().zoom += amount;
            return false;
        }
    }
