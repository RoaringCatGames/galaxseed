package com.roaringcatgames.galaxseed.data;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.data.entitydefs.*;
import com.roaringcatgames.kitten2d.ashley.K2ComponentMappers;
import com.roaringcatgames.kitten2d.ashley.components.*;

import java.awt.*;

/**
 * Utility to convert EntityDefinitions to Ashley Entities with Kitten2d/LitterBox Components
 */
public class EntityBuilder {

    public static Array<Entity> buildEntities(Engine engine, Array<EntityDefinition> entityDefs){
        Array<Entity> entities = new Array<>();

        for(EntityDefinition def:entityDefs){
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

            if(def.clickable != null){
                e.add(ClickableComponent.create(engine)
                    .setEventName(def.clickable.eventName)
                    .setPointer(def.clickable.pointer));
            }

            entities.add(e);
        }

        return entities;
    }

    public static EntityList extractEntityList(Array<Entity> entities){
        EntityList entityDefs = new EntityList();

        for(Entity e:entities){
            EntityDefinition eDef = new EntityDefinition();

            if(K2ComponentMappers.transform.has(e)){
                TransformComponent tc = K2ComponentMappers.transform.get(e);
                eDef.transform = new Transform();
                eDef.transform.isHidden = tc.isHidden;
                eDef.transform.opacity = tc.tint.a;
                eDef.transform.originOffsetX = tc.originOffset.x;
                eDef.transform.originOffsetY = tc.originOffset.y;
                eDef.transform.scaleX = tc.scale.x;
                eDef.transform.scaleY = tc.scale.y;
                eDef.transform.tint = tc.tint;
//                eDef.transform.x = tc.position.x;
//                eDef.transform.y = tc.position.y;
                eDef.transform.z = tc.position.z;
                eDef.transform.rotation = tc.rotation;
            }

            if(K2ComponentMappers.animation.has(e)){
                AnimationComponent ac = K2ComponentMappers.animation.get(e);
                StateComponent sc = K2ComponentMappers.state.get(e);
                eDef.animationSet = new AnimationSetDefinition();
                eDef.animationSet.isPaused = ac.isPaused;
                eDef.animationSet.shouldLoop = sc.isLooping;
                eDef.animationSet.shouldClearOnBlankState = ac.shouldClearOnBlankState;
                eDef.animationSet.animations = new Array<>();
                for(ObjectMap.Entry<String, Animation> ani : ac.animations){
                    AnimationDefinition aniDef = new AnimationDefinition();
                    aniDef.stateName = ani.key;
                    aniDef.frameDuration = ani.value.getFrameDuration();
                    aniDef.playMode = ani.value.getPlayMode();
                    //aniDef.animationName = ????
                    eDef.animationSet.animations.add(aniDef);
                }
            }else if(K2ComponentMappers.texture.has(e)){
                TextureComponent txc = K2ComponentMappers.texture.get(e);
                //eDef.spriteName = txc.region.
            }

            if(K2ComponentMappers.circleBounds.has(e)){
                CircleBoundsComponent cbc = K2ComponentMappers.circleBounds.get(e);
                Bounds boundDef = new Bounds();
                boundDef.x = cbc.circle.x;
                boundDef.y = cbc.circle.y;
                boundDef.radius = cbc.circle.radius;
                boundDef.offsetX = cbc.offset.x;
                boundDef.offsetY = cbc.offset.y;
                eDef.bounds = boundDef;
            }

            if(K2ComponentMappers.bounds.has(e)){
                BoundsComponent bc = K2ComponentMappers.bounds.get(e);
                Bounds boundDef = new Bounds();
                boundDef.x = bc.bounds.x;
                boundDef.y = bc.bounds.y;
                boundDef.width = bc.bounds.width;
                boundDef.height = bc.bounds.height;
                boundDef.offsetX = bc.offset.x;
                boundDef.offsetY = bc.offset.y;
                eDef.bounds = boundDef;
            }

            entityDefs.entities.add(eDef);
        }
        return entityDefs;
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
        Vector2 quadPosition = transform.position.quadAdjusted(App.W, App.H);
        Gdx.app.log("TINT", transform.tint.r + " " + transform.tint.g + " " + transform.tint.b);
        return TransformComponent.create(engine)
                .setPosition(quadPosition.x, quadPosition.y, transform.z)
                .setScale(transform.scaleX, transform.scaleY)
                .setRotation(transform.rotation)
                .setTint(transform.tint)
                .setOpacity(transform.opacity)
                .setOriginOffset(transform.originOffsetX, transform.originOffsetY)
                .setHidden(transform.isHidden);
    }
}
