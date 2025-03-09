package net.mrmascot.seadogs.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.mrmascot.seadogs.AgeOfPirates;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

public class MusketItem extends GunpowderWeaponItem {

    public static final int TICKS_PER_SECOND = 20;
    public static final int RANGE = 15;

    public static final Logger LOGGER = LoggerFactory.getLogger(AgeOfPirates.MOD_ID);

    public MusketItem(Settings settings) {
        super(settings);
    }

    public static ItemStack getHeldProjectile(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (predicate.test(entity.getStackInHand(Hand.OFF_HAND))) {
            return entity.getStackInHand(Hand.OFF_HAND);
        } else {
            return predicate.test(entity.getStackInHand(Hand.MAIN_HAND)) ? entity.getStackInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        LOGGER.info("onStoppedUsing");
        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float f = getPullProgress(i);
        if (f < 0.1) {
            isLoaded();
        }
    }

    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        LOGGER.info("shoot");
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed, divergence);
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }


    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        LOGGER.info("getUseAction");
        return UseAction.BOW;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !user.getProjectileType(itemStack).isEmpty();
        if (!user.isInCreativeMode() && !bl) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    public Predicate<ItemStack> getProjectiles() {
        LOGGER.info("getProjectiles");
        return BOW_PROJECTILES;
    }

    public int getRange() {
        LOGGER.info("getRange");
        return 15;
    }

}
