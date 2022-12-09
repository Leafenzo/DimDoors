package org.dimdev.dimdoors.world.feature;

import java.util.List;

import org.dimdev.dimdoors.DimensionalDoors;
import org.dimdev.dimdoors.block.ModBlocks;
import org.dimdev.dimdoors.world.feature.gateway.LimboGatewayFeature;
import org.dimdev.dimdoors.world.feature.gateway.schematic.EndGateway;
import org.dimdev.dimdoors.world.feature.gateway.schematic.SandstonePillarsGateway;
import org.dimdev.dimdoors.world.feature.gateway.schematic.SchematicGateway;
import org.dimdev.dimdoors.world.feature.gateway.schematic.SchematicGatewayFeature;
import org.dimdev.dimdoors.world.feature.gateway.schematic.SchematicGatewayFeatureConfig;
import org.dimdev.dimdoors.world.feature.gateway.schematic.TwoPillarsGateway;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;

import static net.minecraft.world.gen.feature.Feature.ORE;
import static net.minecraft.world.gen.feature.Feature.SPRING_FEATURE;
import static org.dimdev.dimdoors.DimensionalDoors.id;

public final class ModFeatures {
	public static final SchematicGateway SANDSTONE_PILLARS_GATEWAY = new SandstonePillarsGateway();
	public static final SchematicGateway TWO_PILLARS_GATEWAY = new TwoPillarsGateway();
	public static final SchematicGateway END_GATEWAY = new EndGateway();

	public static final Feature<SchematicGatewayFeatureConfig> SCHEMATIC_GATEWAY_FEATURE = Registry.register(Registries.FEATURE, id("schematic_gateway"), new SchematicGatewayFeature(SchematicGatewayFeatureConfig.CODEC));
	public static final Feature<DefaultFeatureConfig> LIMBO_GATEWAY_FEATURE = Registry.register(Registries.FEATURE, id("limbo_gateway"), new LimboGatewayFeature());

	public static void init() {
		SANDSTONE_PILLARS_GATEWAY.init();
		TWO_PILLARS_GATEWAY.init();
		END_GATEWAY.init();

		Configured.init();
		Placed.init();
	}

	public static final class Configured {
		public static final RegistryKey<ConfiguredFeature<?, ?>> SANDSTONE_PILLARS = of("sandstone_pillars");
		public static final RegistryKey<ConfiguredFeature<?, ?>> TWO_PILLARS = of("two_pillars");
		public static final RegistryKey<ConfiguredFeature<?, ?>> END_GATEWAY = of("end_gateway");
		public static final RegistryKey<ConfiguredFeature<?, ?>> LIMBO_GATEWAY = of("limbo_gateway");
		public static final RegistryKey<ConfiguredFeature<?, ?>> SOLID_STATIC_ORE = of("solid_static_ore");
		public static final RegistryKey<ConfiguredFeature<?, ?>> DECAYED_BLOCK_ORE = of("decayed_block_ore");
		public static final RegistryKey<ConfiguredFeature<?, ?>> ETERNAL_FLUID_SPRING = of("eternal_fluid_spring");

		public static void init() {}

		public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
			return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id(id));
		}
	}

	public static class Placed {
		public static final RegistryEntry<PlacedFeature> SANDSTONE_PILLARS_PLACED_FEATURE = PlacedFeatures.register("dimdoors:sandstone_pillars", Configured.SANDSTONE_PILLARS_CONFIGURED_FEATURE, List.of(RarityFilterPlacementModifier.of(DimensionalDoors.getConfig().getWorldConfig().gatewayGenChance), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of()));
		public static final RegistryEntry<PlacedFeature> TWO_PILLARS_PLACED_FEATURE = PlacedFeatures.register("dimdoors:two_pillars", Configured.TWO_PILLARS_CONFIGURED_FEATURE, List.of(RarityFilterPlacementModifier.of(DimensionalDoors.getConfig().getWorldConfig().gatewayGenChance), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of()));
		public static final RegistryEntry<PlacedFeature> END_GATEWAY_PLACED_FEATURE = PlacedFeatures.register("dimdoors:end_gateway", Configured.END_GATEWAY_CONFIGURED_FEATURE, List.of(RarityFilterPlacementModifier.of(DimensionalDoors.getConfig().getWorldConfig().gatewayGenChance), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of()));
		public static final RegistryEntry<PlacedFeature> LIMBO_GATEWAY_PLACED_FEATURE = PlacedFeatures.register("dimdoors:limbo_gateway", Configured.LIMBO_GATEWAY_CONFIGURED_FEATURE, List.of(RarityFilterPlacementModifier.of(DimensionalDoors.getConfig().getWorldConfig().gatewayGenChance), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of()));
		public static final RegistryEntry<PlacedFeature> SOLID_STATIC_ORE_PLACED_FEATURE = PlacedFeatures.register("dimdoors:solid_static_ore", Configured.SOLID_STATIC_ORE_CONFIGURED_FEATURE, List.of(CountPlacementModifier.of(3), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), SquarePlacementModifier.of(), BiomePlacementModifier.of()));
		public static final RegistryEntry<PlacedFeature> DECAYED_BLOCK_ORE_PLACED_FEATURE = PlacedFeatures.register("dimdoors:decayed_block_ore", Configured.DECAYED_BLOCK_ORE_CONFIGURED_FEATURE, List.of(CountPlacementModifier.of(4), HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(79)), SquarePlacementModifier.of(), BiomePlacementModifier.of()));
		public static final RegistryEntry<PlacedFeature> ETERNAL_FLUID_SPRING_PLACED_FEATURE = PlacedFeatures.register("dimdoors:eternal_fluid_spring", Configured.ETERNAL_FLUID_SPRING_CONFIGURED_FEATURE, List.of(CountPlacementModifier.of(25), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(192)), BiomePlacementModifier.of()));

		public static void init() {
			BiomeModifications.addFeature(ctx -> ctx.hasTag(ConventionalBiomeTags.IN_OVERWORLD) &&
					!ctx.hasTag(ConventionalBiomeTags.DESERT) &&
					!ctx.hasTag(ConventionalBiomeTags.OCEAN),
					GenerationStep.Feature.SURFACE_STRUCTURES,
					TWO_PILLARS_PLACED_FEATURE.getKey().get()
			);
			BiomeModifications.addFeature(
					ctx -> ctx.hasTag(ConventionalBiomeTags.DESERT),
					GenerationStep.Feature.SURFACE_STRUCTURES,
					SANDSTONE_PILLARS_PLACED_FEATURE.getKey().get()
			);

			BiomeModifications.addFeature(
					ctx -> ctx.getBiomeKey().equals(BiomeKeys.END_HIGHLANDS) || ctx.getBiomeKey().equals(BiomeKeys.END_MIDLANDS) || ctx.getBiomeKey().equals(BiomeKeys.SMALL_END_ISLANDS),
					GenerationStep.Feature.SURFACE_STRUCTURES,
					END_GATEWAY_PLACED_FEATURE.getKey().get()
			);
		}

		public static RegistryKey<PlacedFeature> of(String id) {
			return RegistryKey.of(RegistryKeys.PLACED_FEATURE, id(id));
		}
	}
}
