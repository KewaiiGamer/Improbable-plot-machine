/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client;

import arekkuusu.implom.client.render.*;
import arekkuusu.implom.client.render.entity.EyeOfSchrodingerRenderer;
import arekkuusu.implom.client.render.entity.LumenRenderer;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.block.tile.*;
import arekkuusu.implom.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.implom.common.entity.EntityLumen;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 29/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public final class ModRenders {

	public static void preInit() {
		registerEntity(EntityEyeOfSchrodinger.class, EyeOfSchrodingerRenderer::new);
		registerEntity(EntityLumen.class, LumenRenderer::new);
		IPM.LOG.info("[YOU WAN SUM PIE!]");
	}

	public static void init() {
		registerTESR(TileQuantumMirror.class, new QuantumMirrorRenderer());
		registerTESR(TilePhenomena.class, new PhenomenaRenderer());
		registerTESR(TileQSquared.class, new QSquaredRenderer());
		registerTESR(TileHyperConductor.class, new HyperConductorRenderer());
		registerTESR(TileVacuumConveyor.class, new VacuumConveyorRenderer());
		registerTESR(TileMechanicalTranslocator.class, new MechanicalTranslocatorRenderer());
		registerTESR(TileQimranut.class, new QimranutRenderer());
		registerTESR(TileNeutronBattery.class, new NeutronBatteryRenderer());
		registerTESR(TilePholarizer.class, new PholarizerRenderer());
		registerTESR(TileFissionInducer.class, new FissionInducerRenderer());
		registerTESR(TileElectron.class, new ElectronRenderer());
		registerTESR(TileQuartzConsumer.class, new QuartzConsumerRenderer());
		registerTESR(TileDifferentiator.class, new DifferentiatorRenderer());
		registerTESR(TileDifferentiatorInterceptor.class, new DifferentiatorInterceptorRenderer());
		registerTESR(TileKondenzator.class, new KondenzatorRenderer());
		registerTESR(TileCrystallicSynthesizer.class, new CrystallicSynthesizerRenderer());

		registerTESRItemStack(ModBlocks.QUANTUM_MIRROR, TileQuantumMirror.class);
		registerTESRItemStack(ModBlocks.Q_SQUARED, TileQSquared.class);
		registerTESRItemStack(ModBlocks.HYPER_CONDUCTOR, TileHyperConductor.class);
		registerTESRItemStack(ModBlocks.VACUUM_CONVEYOR, TileVacuumConveyor.class);
		registerTESRItemStack(ModBlocks.MECHANICAL_TRANSLOCATOR, TileMechanicalTranslocator.class);
		registerTESRItemStack(ModBlocks.QIMRANUT, TileQimranut.class);
		registerTESRItemStack(ModBlocks.NEUTRON_BATTERY, TileNeutronBattery.class);
		registerTESRItemStack(ModBlocks.PHOLARIZER, TilePholarizer.class);
		registerTESRItemStack(ModBlocks.FISSION_INDUCER, TileFissionInducer.class);
		registerTESRItemStack(ModBlocks.ELECTRON, TileElectron.class);
		registerTESRItemStack(ModBlocks.DIFFERENTIATOR, TileDifferentiator.class);
		registerTESRItemStack(ModBlocks.DIFFERENTIATOR_INTERCEPTOR, TileDifferentiatorInterceptor.class);
		registerTESRItemStack(ModBlocks.KONDENZATOR, TileKondenzator.class);
		registerTESRItemStack(ModBlocks.CRYSTALLIC_SYNTHESIZER, TileCrystallicSynthesizer.class);
		IPM.LOG.info("[NOM PIE!]");
	}

	private static <T extends TileEntity> void registerTESR(Class<T> tile, TileEntitySpecialRenderer<T> render) {
		ClientRegistry.bindTileEntitySpecialRenderer(tile, render);
	}

	@SuppressWarnings("deprecation")
	private static <T extends TileEntity> void registerTESRItemStack(Item item, Class<T> tile) {
		ForgeHooksClient.registerTESRItemStack(item, 0, tile);
	}

	@SuppressWarnings("deprecation")
	private static <T extends TileEntity> void registerTESRItemStack(Block block, Class<T> tile) {
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block), 0, tile);
	}

	private static <T extends Entity> void registerEntity(Class<T> entity, IRenderFactory<? super T> render) {
		RenderingRegistry.registerEntityRenderingHandler(entity, render);
	}
}
