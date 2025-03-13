package net.mrmascot.seadogs.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.mrmascot.seadogs.AgeOfPirates;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mrmascot.seadogs.item.custom.MusketItem;

public class ModItems {
    public static final Item BANANA = registerItem("banana", new Item(new Item.Settings().food(ModFoodComponents.BANANA)));
    public static final Item COOKED_BANANA = registerItem("cooked_banana", new Item(new Item.Settings().food(ModFoodComponents.COOKED_BANANA)));

    public static final Item MUSKET = registerItem("musket", new MusketItem(new Item.Settings().maxDamage(500)));

    public static final Item BULLET = registerItem("bullet", new ArrowItem(new Item.Settings().maxDamage(3)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AgeOfPirates.MOD_ID, name), item);
        //test1 pvd
    }

    public static void registerModItems() {
        AgeOfPirates.LOGGER.info("Registering Mod Items for " + AgeOfPirates.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK ).register(entries -> {
            entries.add(BANANA);
            entries.add(COOKED_BANANA);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT ).register(entries -> {
            entries.add(MUSKET);
            entries.add(BULLET);
        });
    }
}