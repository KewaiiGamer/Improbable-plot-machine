/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
@ObjectHolder(LibMod.MOD_ID)
public class ModItems {

	private static final Item PLACE_HOLDER = new Item();
	//--------------------------------Items--------------------------------//
	public static final Item quingentilliard = PLACE_HOLDER;

	public static void register(IForgeRegistry<Item> registry) {
		registry.register(itemBlock(ModBlocks.primal_stone));
		registry.register(new ItemPrimalGlyph());
		registry.register(itemBlock(ModBlocks.black_hole));
		registry.register(itemBlock(ModBlocks.singularity));
		registry.register(itemBlock(ModBlocks.prism_flower));
		registry.register(new ItemQuantumMirror());
		registry.register(itemBlock(ModBlocks.light_particle));
		registry.register(new ItemGravityHopper());
		registry.register(new ItemQuingentilliard());
		registry.register(new ItemSchrodingerGlyph());
		registry.register(itemBlock(ModBlocks.crystal_void));
	}

	@SuppressWarnings("ConstantConditions")
	private static Item itemBlock(Block block) {
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}
}
