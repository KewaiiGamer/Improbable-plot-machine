/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.handler.recipe.RecipeHandler;
import arekkuusu.solar.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
@Mod.EventBusSubscriber
public final class CommonEvents {

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModItems.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		RecipeHandler.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void worldLoad(WorldEvent.Load event) {
		WorldQuantumData.get(event.getWorld());
	}
}
