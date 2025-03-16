package net.mrmascot.seadogs.Entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

//public class BulletEntity extends PersistentProjectileEntity {
//
//    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
//        super(entityType, world);
//        this.setNoGravity(true); // Отключаем гравитацию, пуля летит прямо
//    }
//
//    public BulletEntity(World world, LivingEntity shooter) {
//        super(EntityType.ARROW, shooter, world);
//        this.setNoGravity(true);
//    }
//
//    @Override
//    protected void onEntityHit(EntityHitResult entityHitResult) {
//        entityHitResult.getEntity().damage(null, 8.0F); // Наносим урон
//        this.remove(RemovalReason.DISCARDED); // Удаляем пулю после попадания
//    }
//
//    @Override
//    protected void onCollision(HitResult hitResult) {
//        super.onCollision(hitResult);
//        if (!this.getWorld().isClient) {
//            this.remove(RemovalReason.DISCARDED);
//        }
//    }
//
//    @Override
//    public void writeCustomDataToNbt(NbtCompound tag) {}
//
//    @Override
//    public void readCustomDataFromNbt(NbtCompound tag) {}
//}
