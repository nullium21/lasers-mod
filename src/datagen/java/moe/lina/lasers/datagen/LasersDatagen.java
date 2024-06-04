package moe.lina.lasers.datagen;

import java.util.List;
import java.util.Optional;

import moe.lina.lasers.base.HasIdentifier;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import static moe.lina.lasers.LasersMod.*;

public class LasersDatagen implements DataGeneratorEntrypoint {

    private static <T extends Block & HasIdentifier> FabricDataGenerator.Pack.Factory<FabricModelProvider> simpleBlock(T block) {
        return out -> new FabricModelProvider(out) {
            @Override
            public String getName() {
                return block.getClass().getSimpleName();
            }

            @Override
            public void generateBlockStateModels(BlockStateModelGenerator bsgen) {
                bsgen.registerSimpleState(block);
            }

            @Override
            public void generateItemModels(ItemModelGenerator imgen) {
                var model = new Model(Optional.of(block.getId().withPrefixedPath("block/")), Optional.empty());
                imgen.register(block.asItem(), model);
            }
        };
    }

    private static <T extends Block & HasIdentifier> FabricDataGenerator.Pack.Factory<FabricModelProvider> directionalBlock(T block) {
        return out -> new FabricModelProvider(out) {
            @Override
            public String getName() {
                return block.getClass().getSimpleName();
            }

            @Override
            public void generateBlockStateModels(BlockStateModelGenerator bsgen) {
                bsgen.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
                        .put(VariantSettings.MODEL, block.getId().withPrefixedPath("block/")))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates())
                );
            }

            @Override
            public void generateItemModels(ItemModelGenerator imgen) {
                var model = new Model(Optional.of(block.getId().withPrefixedPath("block/")), Optional.empty());
                imgen.register(block.asItem(), model);
            }
        };
    }

    private static <T> FabricDataGenerator.Pack.RegistryDependentFactory<FabricTagProvider<T>> tag(TagKey<T> key, RegistryKey<? extends Registry<T>> rkey, List<T> entries, List<TagKey<T>> tags) {
        return (out, registry) -> new FabricTagProvider<>(out, rkey, registry) {
            @Override
            protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
                var builder = getOrCreateTagBuilder(key);
                for (T entry : entries) builder.add(entry);
                for (TagKey<T> tag : tags) builder.addOptionalTag(tag);
            }
        };
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        var pack = gen.createPack();
        pack.addProvider(simpleBlock(LASER_BLOCK));
        pack.addProvider(directionalBlock(MIRROR_BLOCK));
        pack.addProvider(directionalBlock(LASER_RECEIVER_BLOCK));
        pack.addProvider(tag(
                LASER_TRANSPARENT, RegistryKeys.BLOCK,
                List.of(),
                List.of(ConventionalBlockTags.GLASS_BLOCKS, ConventionalBlockTags.GLASS_PANES)
        ));
    }
}
