package net.mrmascot.seadogs.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.mrmascot.seadogs.AgeOfPirates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import javax.crypto.SealedObject;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(AgeOfPirates.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> GUNPOWDER_PROJECTILES = createTag("gunpowder_projectiles");
        public static final TagKey<Item> BULLET = createTag("Bullet");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(AgeOfPirates.MOD_ID, name));
        }
    }
}
