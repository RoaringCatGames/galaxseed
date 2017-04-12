package com.roaringcatgames.galaxseed.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.IGameServiceController;
import com.roaringcatgames.galaxseed.components.Mappers;
import com.roaringcatgames.galaxseed.components.WhenOnScreenComponent;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.BoundsComponent;

/**
 * Created by barry on 4/11/17.
 */
public class OnScreenSystem extends IteratingSystem {

    private IGameServiceController gameServiceController;
    private Rectangle screen = new Rectangle(0f, 0f, App.W, App.H);

    public OnScreenSystem(IGameServiceController gameServiceController){
        super(Family.all(WhenOnScreenComponent.class, BoundsComponent.class).get());
        this.gameServiceController = gameServiceController;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        screen.set(0f, 0f, App.W, App.H);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BoundsComponent bc = K2ComponentMappers.bounds.get(entity);
        WhenOnScreenComponent wc = Mappers.whenOnScreen.get(entity);
        if(gameServiceController != null && bc.bounds.overlaps(screen)){
            gameServiceController.unlockAchievement(wc.achievementName);
            entity.remove(WhenOnScreenComponent.class);
        }
    }
}
