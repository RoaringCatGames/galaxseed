package com.roaringcatgames.galaxseed.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.roaringcatgames.galaxseed.App;
import com.roaringcatgames.galaxseed.Assets;
import com.roaringcatgames.galaxseed.components.WeaponType;
import com.roaringcatgames.galaxseed.values.Z;
import com.roaringcatgames.kitten2d.ashley.IActionResolver;
import com.roaringcatgames.kitten2d.ashley.K2EntityTweenAccessor;
import com.roaringcatgames.kitten2d.ashley.components.*;

/**
 * Handles Actions fired from the Space Screen
 */
public class SpaceScreenActionResolver implements IActionResolver {
    public static final String CLOSE_INFO = "CLOSE_INFO";
    public static final String BLASTER_INFO = "BLASTER_INFO";
    public static final String COPTER_INFO = "COPTER_INFO";
    public static final String POLLEN_INFO = "POLLEN_INFO";

    private Entity lastTargetEntity;

    @Override
    public void resolveAction(String eventName, Entity firingEntity, Engine containerEngine) {
        switch(eventName){
            case BLASTER_INFO:
                showWeaponInfo(WeaponType.GUN_SEEDS, containerEngine);
                break;

            case COPTER_INFO:
                showWeaponInfo(WeaponType.HELICOPTER_SEEDS, containerEngine);
                break;

            case POLLEN_INFO:
                showWeaponInfo(WeaponType.POLLEN_AURA, containerEngine);
                break;

            case CLOSE_INFO :
                containerEngine.removeEntity(firingEntity);
                lastTargetEntity = null;
                break;

            default:
                break;

        }
    }

    private void showWeaponInfo(WeaponType wt, Engine engine){
        if(lastTargetEntity != null){
            engine.removeEntity(lastTargetEntity);
        }
        Entity weaponInfo = engine.createEntity();
        TextureRegion region = wt == WeaponType.GUN_SEEDS ? Assets.getBerryBlasterInfo() :
                wt == WeaponType.HELICOPTER_SEEDS ? Assets.getCopterCannonInfo() :
                        Assets.getProtectAndPollenInfo();
        weaponInfo.add(TransformComponent.create(engine)
                .setPosition(App.W / 2f, App.H / 2f, Z.weaponInfo)
                .setScale(0.1f, 0.1f));

        weaponInfo.add(TweenComponent.create(engine)
                .addTween(Tween.to(weaponInfo, K2EntityTweenAccessor.SCALE, 0.075f)
                        .ease(TweenEquations.easeOutBounce)
                        .target(1f, 1f)));
        weaponInfo.add(TextureComponent.create(engine)
                .setRegion(region));

        weaponInfo.add(ClickableComponent.create(engine)
                .setEventName("CLOSE_INFO"));
        weaponInfo.add(BoundsComponent.create(engine)
                .setBounds(0f, 0f, 1.5f, 1.5f)
                .setOffset(8.5f, 5.5f));

        engine.addEntity(weaponInfo);
        lastTargetEntity = weaponInfo;

    }
}
