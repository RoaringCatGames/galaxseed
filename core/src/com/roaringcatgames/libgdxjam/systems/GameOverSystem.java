package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Music;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.PlayerComponent;
import com.roaringcatgames.libgdxjam.components.TextComponent;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 3/2/16 @ 8:43 PM.
 */
public class GameOverSystem extends IteratingSystem {

    private Entity gameOverText;
    private Entity restartButton;
    private Music endSong;

    private boolean hasInitialized = false;

    public GameOverSystem(){
        super(Family.all(PlayerComponent.class).get());
        endSong = Assets.getGameOverMusic();
    }

    @Override
    public void setProcessing(boolean processing) {
        super.setProcessing(processing);

        if(processing){
            hasInitialized = false;
            if(gameOverText == null) {
                gameOverText = ((PooledEngine) getEngine()).createEntity();
                gameOverText.add(TransformComponent.create()
                        .setPosition(App.W / 2f, App.H / 2f, Z.gameOver));
                gameOverText.add(TextComponent.create()
                        .setFont(Assets.get64Font())
                        .setText("GAME OVER"));

                getEngine().addEntity(gameOverText);
            }
            gameOverText.getComponent(TransformComponent.class).setHidden(false);
            endSong.play();
        }else{
            if(endSong.isPlaying()) {
                endSong.stop();
            }
            if(gameOverText != null) {
                gameOverText.getComponent(TransformComponent.class).setHidden(true);
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
