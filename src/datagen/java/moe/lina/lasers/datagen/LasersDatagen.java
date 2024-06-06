package moe.lina.lasers.datagen;

import java.util.List;
import java.util.Optional;

import moe.lina.lasers.base.HasIdentifier;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;

import static moe.lina.lasers.LasersMod.*;

//? if >=1.20.6 {
/*import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.server.recipe.RecipeExporter;
*///?} else {
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import java.util.function.Consumer;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
//?}

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

        pack.addProvider((/*? if =1.20.1 >>*/FabricDataOutput out /*? if >=1.20.6 >>*//*,registry*/) -> new FabricRecipeProvider(out /*? if >=1.20.6 >>*//*,registry*/) {

            @Override
            //? if >=1.20.6 {
            /*public void generate(RecipeExporter exporter) {
            *///?} else {
            public void generate(Consumer<RecipeJsonProvider> exporter) {
            //?}
                ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, LASER_BLOCK_ITEM)
                        .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                        .input('d', ConventionalItemTags.REDSTONE_DUSTS)
                        //? if >=1.20.6 {
                        /*.input('c', ConventionalItemTags.COBBLESTONES)
                        .input('q', ConventionalItemTags.QUARTZ_GEMS)
                        *///?} else {
                        .input('c', Items.COBBLESTONE)
                        .input('q', Items.QUARTZ)
                        //?}
                        .input('i', ConventionalItemTags.IRON_INGOTS)
                        .input('l', Items.REDSTONE_LAMP)
                        .pattern("iqi")
                        .pattern("dld")
                        .pattern("ccc")
                        .offerTo(exporter);

                ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, LASER_RECEIVER_BLOCK)
                        .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                        //? if >=1.20.6 {
                        /*.input('c', ConventionalItemTags.COBBLESTONES)
                        .input('q', ConventionalItemTags.QUARTZ_GEMS)
                        *///?} else {
                        .input('c', Items.COBBLESTONE)
                        .input('q', Items.QUARTZ)
                        //?}
                        .input('d', ConventionalItemTags.REDSTONE_DUSTS)
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
