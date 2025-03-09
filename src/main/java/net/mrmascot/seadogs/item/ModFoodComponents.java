package net.mrmascot.seadogs.item;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class ModFoodComponents {
    public static  final FoodComponent BANANA = new FoodComponent.Builder().nutrition(2).saturationModifier(0.25f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 260), 0.6f).build();
    public static  final FoodComponent COOKED_BANANA = new FoodComponent.Builder().nutrition(3).saturationModifier(0.25f).build();
}
