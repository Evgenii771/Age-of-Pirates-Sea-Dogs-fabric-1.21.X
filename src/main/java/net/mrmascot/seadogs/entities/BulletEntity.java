package net.mrmascot.seadogs.entities;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class BulletEntity extends PersistentProjectileEntity {

    private ItemStack stack;
    private @Nullable ItemStack weapon;
    private double damage;


    public BulletEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        this.damage = 6.0F; // Настроим урон пули
        this.stack = ItemStack.EMPTY;
        this.weapon = null;
    }

    public BulletEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        this(type, world);
        this.stack = stack.copy();
        this.setPosition(x, y, z);

        // Настроим параметры, если пуля была выпущена из оружия
        if (weapon != null && world instanceof ServerWorld serverWorld) {
            if (weapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing a bullet");
            }
            this.weapon = weapon.copy();
            int piercing = EnchantmentHelper.getProjectilePiercing(serverWorld, weapon, this.stack);
            if (piercing > 0) {
                this.setPierceLevelReflection(this, (byte) piercing);
            }
            EnchantmentHelper.onProjectileSpawned(serverWorld, weapon, this, (item) -> this.weapon = null);
        }

        Unit unit = stack.remove(DataComponentTypes.INTANGIBLE_PROJECTILE);
        if (unit != null) {
            this.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
    }


    private void setPierceLevelReflection(PersistentProjectileEntity projectile, byte level) {
        try {
            Field pierceLevelField = PersistentProjectileEntity.class.getDeclaredField("pierceLevel");
            pierceLevelField.setAccessible(true);
            pierceLevelField.set(projectile, level);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public BulletEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        this(type, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), world, stack, shotFrom);
        this.setOwner(owner);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.getWorld().isClient) {
            entityHitResult.getEntity().damage(this.getDamageSources().arrow(this, (LivingEntity) this.getOwner()), (float) this.damage);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_ANVIL_LAND; // Звук попадания пули
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        if (!this.stack.isEmpty()) {
            NbtElement bulletTag = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, this.stack)
                    .getOrThrow();
            tag.put("BulletStack", bulletTag);
        }

        if (this.weapon != null && !this.weapon.isEmpty()) {
            NbtElement weaponTag = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, this.weapon)
                    .getOrThrow();
            tag.put("Weapon", weaponTag);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        if (tag.contains("BulletStack", 10)) { // 10 = CompoundTag
            this.stack = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("BulletStack"))
                    .resultOrPartial(error -> {}).orElse(ItemStack.EMPTY);
        }

        if (tag.contains("Weapon", 10)) {
            this.weapon = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("Weapon"))
                    .resultOrPartial(error -> {}).orElse(ItemStack.EMPTY);
        }
    }

    protected ItemStack getDefaultItemStack() {
        return ItemStack.EMPTY;
    }

}
