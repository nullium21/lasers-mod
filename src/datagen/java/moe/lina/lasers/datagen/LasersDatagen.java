package moe.lina.lasers.datagen;

import java.util.List;
import java.util.Optional;

import moe.lina.lasers.base.HasIdentifier;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

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

        pack.addProvider((out, registry) -> new FabricRecipeProvider(out, registry) {

            @Override
            public void generate(RecipeExporter exporter) {
                ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, LASER_BLOCK_ITEM)
                        .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                        .input('d', ConventionalItemTags.REDSTONE_DUSTS)
                        .input('c', ConventionalItemTags.COBBLESTONES)
                        .input('i', ConventionalItemTags.IRON_INGOTS)
                        .input('l', Items.REDSTONE_LAMP)
                        .input('q', ConventionalItemTags.QUARTZ_GEMS)
                        .pattern("iqi")
                        .pattern("dld")
                        .pattern("ccc")
                        .offerTo(exporter);

                ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, LASER_RECEIVER_BLOCK)
                        .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                        .input('c', ConventionalItemTags.COBBLESTONES)
                        .input('d', ConventionalItemTags.REDSTONE_DUSTS)
                        .input('q', ConventionalItemTags.QUARTZ_GEMS)
                        .pattern("cqc")
                        .pattern("dqd")
                        .pattern("ccc")
                        .offerTo(exporter);

                ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, MIRROR_BLOCK, 2)
                        .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                        .input(ConventionalItemTags.GLASS_PANES)
                        .input(Items.IRON_NUGGET)
                        .input(ConventionalItemTags.WHITE_DYES)
                        .offerTo(exporter);
            }
        });
    }

    @Override
    public String getEffectiveModId() {
        return "lasers";
    }
}
