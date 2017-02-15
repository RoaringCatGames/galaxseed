package com.roaringcatgames.galaxseed.components;

import com.badlogic.ashley.core.ComponentMapper;

/**
 * Keep a set of ComponentMappers
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
    public static ComponentMapper<WhenOffScreenComponent> whenOffScreen = ComponentMapper.getFor(WhenOffScreenComponent.class);
    public static ComponentMapper<HelicopterSeedComponent> helicopterSeed = ComponentMapper.getFor(HelicopterSeedComponent.class);
    public static ComponentMapper<StatusComponent> status = ComponentMapper.getFor(StatusComponent.class);
    public static ComponentMapper<WeaponSelectComponent> weaponSelect = ComponentMapper.getFor(WeaponSelectComponent.class);
    public static ComponentMapper<ShieldComponent> shield = ComponentMapper.getFor(ShieldComponent.class);
    public static ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static ComponentMapper<AdjustablePositionComponent> adjust = ComponentMapper.getFor(AdjustablePositionComponent.class);
    public static ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);
}
