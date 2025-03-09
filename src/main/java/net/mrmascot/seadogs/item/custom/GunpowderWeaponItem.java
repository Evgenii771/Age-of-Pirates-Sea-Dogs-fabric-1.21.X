package net.mrmascot.seadogs.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import net.minecraft.world.World;
import net.mrmascot.seadogs.AgeOfPirates;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class GunpowderWeaponItem extends Item {

    public static final Logger LOGGER = LoggerFactory.getLogger(AgeOfPirates.MOD_ID);

    public static final Predicate<ItemStack> BOW_PROJECTILES = (stack) -> stack.isIn(ItemTags.ARROWS);
    public static final Predicate<ItemStack> CROSSBOW_HELD_PROJECTILES;

    //protected abstract void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target);

    public abstract void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks);

    public GunpowderWeaponItem(Settings settings) {
        super(settings);
    }

    public int getEnchantability() {
        return 1;
    }

    public abstract int getRange();

    protected void isLoaded(){
        LOGGER.info("isLoaded");
    }

    protected void shootAll() {
        LOGGER.info("shootAll");
    }

    protected int getWeaponStackDamage(ItemStack projectile) {
        return 1;
    }

    protected abstract void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target);

    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        Item var8 = projectileStack.getItem();
        ArrowItem var10000;
        if (var8 instanceof ArrowItem arrowItem) {
            var10000 = arrowItem;
        } else {
            var10000 = (ArrowItem)Items.ARROW;
        }

        ArrowItem arrowItem2 = var10000;
        PersistentProjectileEntity persistentProjectileEntity = arrowItem2.createArrow(world, projectileStack, shooter, weaponStack);
        if (critical) {
            persistentProjectileEntity.setCritical(true);
        }

        return persistentProjectileEntity;
    }

    protected static List<ItemStack> load(ItemStack stack, ItemStack projectileStack, LivingEntity shooter) {
        if (projectileStack.isEmpty()) {
            return List.of();
        } else {
            World itemStack = shooter.getWorld();
            int var10000;
            if (itemStack instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)itemStack;
                var10000 = EnchantmentHelper.getProjectileCount(serverWorld, stack, shooter, 1);
            } else {
                var10000 = 1;
            }

            int i = var10000;
            List<ItemStack> list = new ArrayList(i);
            ItemStack itemStack3 = projectileStack.copy();

            for(int j = 0; j < i; ++j) {
                ItemStack itemStack2 = getProjectile(stack, j == 0 ? projectileStack : itemStack3, shooter, j > 0);
                if (!itemStack2.isEmpty()) {
                    list.add(itemStack2);
                }
            }

            return list;
        }
    }

    protected static ItemStack getProjectile(ItemStack stack, ItemStack projectileStack, LivingEntity shooter, boolean multishot) {
        int var10000;
        label28: {
            if (!multishot && !shooter.isInCreativeMode()) {
                World playerEntity = shooter.getWorld();
                if (playerEntity instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld)playerEntity;
                    var10000 = EnchantmentHelper.getAmmoUse(serverWorld, stack, projectileStack, 1);
                    break label28;
                }
            }

            var10000 = 0;
        }

        int i = var10000;
        if (i > projectileStack.getCount()) {
            return ItemStack.EMPTY;
        } else if (i == 0) {
            ItemStack itemStack = projectileStack.copyWithCount(1);
            itemStack.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            return itemStack;
        } else {
            ItemStack itemStack = projectileStack.split(i);
            if (projectileStack.isEmpty() && shooter instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)shooter;
                playerEntity.getInventory().removeOne(projectileStack);
            }

            return itemStack;
        }
    }

    static {
        CROSSBOW_HELD_PROJECTILES = BOW_PROJECTILES;
    }
}
