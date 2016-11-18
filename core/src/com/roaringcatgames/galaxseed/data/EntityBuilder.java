package com.roaringcatgames.galaxseed.data;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.data.entitydefs.AnimationDefinition;
import com.roaringcatgames.galaxseed.data.entitydefs.AnimationSetDefinition;
import com.roaringcatgames.galaxseed.data.entitydefs.EntityDefinition;
import com.roaringcatgames.galaxseed.data.entitydefs.Transform;
import com.roaringcatgames.kitten2d.ashley.components.*;

/**
 * Utility to convert EntityDefinitions to Ashley Entities with Kitten2d/LitterBox Components
 */
public class EntityBuilder {

    public static Array<Entity> buildEntities(Engine engine, EntityList entityDefs){
        Array<Entity> entities = new Array<>();

        for(EntityDefinition def:entityDefs.entities){
            Entity e = engine.createEntity();
            boolean hasTexture = false;
            if(def.transform != null){
                e.add(buildTransformComponent(engine, def.transform));
            }

            if(def.spriteName != null && !"".equals(def.spriteName)){
                e.add(TextureComponent.create(engine)
                    .setRegion(Assets.getSpriteByName(def.spriteName)));
                hasTexture = true;
            }

            if(def.animationSet != null){
                if(!hasTexture){
                    e.add(TextureComponent.create(engine));
                }

                AnimationComponent ac = buildAnimationComponent(engine, def.animationSet);

                e.add(StateComponent.create(engine)
                        .set("DEFAULT")
                        .setLooping(def.animationSet.shouldLoop));
                e.add(ac);
            }

            if(def.bounds != null){
                if(def.bounds.radius != 0f){
                    e.add(buildCircleBoundsComponent(engine, def));
                }else if(def.bounds.width != 0f || def.bounds.height != 0f){
                    e.add(buildBoundsComponent(engine, def));
                }
            }

            entities.add(e);
        }

        return entities;
    }

    private static BoundsComponent buildBoundsComponent(Engine engine, EntityDefinition def) {
        return BoundsComponent.create(engine)
                .setBounds(def.bounds.x, def.bounds.y, def.bounds.width, def.bounds.height)
                .setOffset(def.bounds.offsetX, def.bounds.offsetY);
    }

    private static CircleBoundsComponent buildCircleBoundsComponent(Engine engine, EntityDefinition def) {
        return CircleBoundsComponent.create(engine)
            .setCircle(def.bounds.x, def.bounds.y, def.bounds.radius)
            .setOffset(def.bounds.offsetX, def.bounds.offsetY);
    }

    private static AnimationComponent buildAnimationComponent(Engine engine, AnimationSetDefinition def) {
        AnimationComponent ac = AnimationComponent.create(engine);
        for(AnimationDefinition aniDef:def.animations){
            ac.addAnimation(aniDef.stateName, new Animation(aniDef.frameDuration, Assets.getAnimationFramesByName(aniDef.animationName), aniDef.playMode));
        }

        ac.setShouldClearOnBlankState(def.shouldClearOnBlankState)
          .setPaused(def.isPaused);
        return ac;
    }

    private static TransformComponent buildTransformComponent(Engine engine, Transform transform){
        return TransformComponent.create(engine)
                .setPosition(transform.x, transform.y, transform.z)
                .setScale(transform.scaleX, transform.scaleY)
                .setRotation(transform.rotation)
                .setOpacity(transform.opacity)
                .setOriginOffset(transform.originOffsetX, transform.originOffsetY)
                .setTint(transform.tint)
                .setHidden(transform.isHidden);
    }
}
