package com.roaringcatgames.libgdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.roaringcatgames.kitten2d.ashley.components.TransformComponent;
import com.roaringcatgames.libgdxjam.App;
import com.roaringcatgames.libgdxjam.Assets;
import com.roaringcatgames.libgdxjam.components.FPSComponent;
import com.roaringcatgames.libgdxjam.components.TextComponent;
import com.roaringcatgames.libgdxjam.values.Z;

/**
 * Created by barry on 3/22/16 @ 5:53 PM.
 */
public class FPSSystem extends IteratingSystem {


    private Entity fpsText;

    public FPSSystem(){
        super(Family.all(FPSComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        if(fpsText == null){
            fpsText = ((PooledEngine)engine).createEntity();
            fpsText.add(TextComponent.create()
                .setFont(Assets.get32Font())
                .setText("FPS: 0"));
            fpsText.add(TransformComponent.create()
                .setPosition(App.W - 2f, App.H - 1f, Z.debug));
            fpsText.add(FPSComponent.create(((PooledEngine)engine)));
            engine.addEntity(fpsText);
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        if(fpsText != null){
            engine.removeEntity(fpsText);
            fpsText = null;
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        int fps = (int)Math.floor(1f/deltaTime);
        fpsText.getComponent(TextComponent.class).setText("FPS: " + fps);
    }
}
