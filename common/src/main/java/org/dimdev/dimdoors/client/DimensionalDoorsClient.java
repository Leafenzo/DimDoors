package org.dimdev.dimdoors.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.dimdev.dimdoors.DimensionalDoors;
import org.dimdev.dimdoors.ModConfig;
import org.dimdev.dimdoors.block.ModBlocks;
import org.dimdev.dimdoors.block.entity.DetachedRiftBlockEntity;
import org.dimdev.dimdoors.block.entity.ModBlockEntityTypes;
import org.dimdev.dimdoors.client.screen.TesselatingLoomScreen;
import org.dimdev.dimdoors.compat.iris.IrisCompat;
import org.dimdev.dimdoors.entity.MaskEntity;
import org.dimdev.dimdoors.entity.ModEntityTypes;
import org.dimdev.dimdoors.item.RaycastHelper;
import org.dimdev.dimdoors.network.client.ClientPacketHandler;
import org.dimdev.dimdoors.network.packet.c2s.NetworkHandlerInitializedC2SPacket;
import org.dimdev.dimdoors.particle.client.LimboAshParticle;
import org.dimdev.dimdoors.particle.client.MonolithParticle;
import org.dimdev.dimdoors.particle.client.RiftParticle;
import org.dimdev.dimdoors.screen.ModScreenHandlerTypes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dimdev.dimdoors.particle.ModParticleTypes.*;

@Environment(EnvType.CLIENT)
public class DimensionalDoorsClient {
	private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();
	public static final ResourceLocation childItem = DimensionalDoors.id("item/child_item");

	public static ShaderPackDetector detector = () -> false;

	public static void init() {
		ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((handler) -> ClientPacketHandler.sendPacket(new NetworkHandlerInitializedC2SPacket()));

		ClientGuiEvent.DEBUG_TEXT_LEFT.register(new ClientGuiEvent.DebugText() {
			@Override
			public void gatherText(List<String> strings) {
				assert Minecraft.getInstance().player != null;
				HitResult hit = RaycastHelper.findDetachRift(Minecraft.getInstance().player, RaycastHelper.DETACH);
				if(hit.getType() == HitResult.Type.BLOCK) {
					if(Minecraft.getInstance().level.getBlockEntity(((BlockHitResult) hit).getBlockPos()) instanceof DetachedRiftBlockEntity rift) {
						strings.add("Size: " + rift.size);
					}
				}
			}
		});

		registerCompats();

		MenuRegistry.registerScreenFactory(ModScreenHandlerTypes.TESSELATING_LOOM.get(), TesselatingLoomScreen::new);
//		ModFluids.initClient();
		initBlockEntitiesClient();
		ModBlocks.initClient();

		AutoConfig.getGuiRegistry(ModConfig.class).registerPredicateProvider((i18n, field, config, defaults, registry) -> Collections.singletonList(ENTRY_BUILDER
				.startStrList(Component.translatable(i18n), ((Set<String>) Utils.getUnsafely(field, config, defaults)).stream().toList())
				.setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue.stream().collect(Collectors.toSet()))).build()), isSetOfType(String.class));

//		DimensionRenderering.initClient();

		registerListeners();

		ClientPacketHandler.init();

//		ModRecipeBookGroups.init();
    }

	private static void registerCompats() {
		if(Platform.isModLoaded("iris") || Platform.isModLoaded("oculus")) detector = new IrisCompat();
    }

	@Environment(EnvType.CLIENT)
	public static void initEntitiesClient(BiConsumer<EntityType, EntityRendererProvider> consumer) {
		consumer.accept(ModEntityTypes.MONOLITH.get(), MonolithRenderer::new);
        consumer.accept(ModEntityTypes.MASK.get(), context -> new EntityRenderer<MaskEntity>(context) {
			@Override
			public ResourceLocation getTextureLocation(MaskEntity entity) {
				return new ResourceLocation("blep");
			} // hehehe :3
		});
		consumer.accept(ModEntityTypes.PATROL_NODE.get(), PatrolNodeRenderer::new);
	}

	@Environment(EnvType.CLIENT)
	public static void initBlockEntitiesClient() {
		BlockEntityRendererRegistry.register(ModBlockEntityTypes.ENTRANCE_RIFT.get(), context -> new EntranceRiftBlockEntityRenderer());
		BlockEntityRendererRegistry.register(ModBlockEntityTypes.DETACHED_RIFT.get(), ctx -> new DetachedRiftBlockEntityRenderer());
	}

	private static Predicate<Field> isListOfType(Type... types) {
		return field -> {
			if (List.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
				Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
				return args.length == 1 && Stream.of(types).anyMatch(type -> Objects.equals(args[0], type));
			} else {
				return false;
			}
		};
	}

	private static Predicate<Field> isSetOfType(Type... types) {
		return field -> {
			if (Set.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
				Type[] args = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
				return args.length == 1 && Stream.of(types).anyMatch(type -> Objects.equals(args[0], type));
			} else {
				return false;
			}
		};
	}

    private static void registerListeners() {
		ClientReloadShadersEvent.EVENT.register((provider, sink) -> {
			try {
				sink.registerShader(new ShaderInstance(provider, "dimensional_portal", DefaultVertexFormat.POSITION), ModShaders::setDimensionalPortal);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public static void initParticles(BiConsumer<ParticleType<? extends ParticleOptions>, ParticleProvider<?>> specialProvider, BiConsumer<ParticleType<?>, Function<SpriteSet, ? extends ParticleProvider<? extends ParticleOptions>>> spriteProivder) {
		specialProvider.accept(MONOLITH.get(), (particleOptions, clientLevel, x, y, z, g, h, i) -> new MonolithParticle(clientLevel, x, y, z));
		spriteProivder.accept(RIFT.get(), RiftParticle.Factory::new);
		spriteProivder.accept(LIMBO_ASH.get(), LimboAshParticle.Factory::new);
	}
}
