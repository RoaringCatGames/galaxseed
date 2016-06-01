package com.roaringcatgames.libgdxjam.components;

import com.badlogic.ashley.core.ComponentMapper;

/**
 * Created by barry on 5/31/16 @ 7:12 PM.
 */
public class Mappers {


    public static ComponentMapper<BulletComponent> bullet = ComponentMapper.getFor(BulletComponent.class);
    public static ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
    public static ComponentMapper<ExplosionComponent> explosion = ComponentMapper.getFor(ExplosionComponent.class);
    public static ComponentMapper<GunComponent> gun = ComponentMapper.getFor(GunComponent.class);
    public static ComponentMapper<HealthLeafComponent> healthLeaf = ComponentMapper.getFor(HealthLeafComponent.class);
    public static ComponentMapper<HealthPackComponent> healthPack = ComponentMapper.getFor(HealthPackComponent.class);
    public static ComponentMapper<MenuItemComponent> menuItem = ComponentMapper.getFor(MenuItemComponent.class);
    public static ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static ComponentMapper<PowerUpComponent> powerUp = ComponentMapper.getFor(PowerUpComponent.class);
    public static ComponentMapper<ProjectileComponent> projectile = ComponentMapper.getFor(ProjectileComponent.class);
    public static ComponentMapper<ScoreComponent> score = ComponentMapper.getFor(ScoreComponent.class);
    public static ComponentMapper<SpawnerComponent> spawner = ComponentMapper.getFor(SpawnerComponent.class);
    public static ComponentMapper<WeaponComponent> weapon = ComponentMapper.getFor(WeaponComponent.class);
    public static ComponentMapper<WhenOffScreenComponent> whenOffScreen = ComponentMapper.getFor(WhenOffScreenComponent.class);
}
