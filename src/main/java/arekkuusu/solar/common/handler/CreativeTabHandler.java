/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
public final class CreativeTabHandler {

	public static final CreativeTab MISC_ITEMS = new MiscItems();
	public static final CreativeTab MISC_BLOCKS = new MiscBlocks();

	private static abstract class CreativeTab extends CreativeTabs {

		NonNullList<ItemStack> list;

		CreativeTab(String name) {
			super(LibMod.MOD_ID + "." + name);
		}

		@Override
		@SideOnly(Side.CLIENT)
		@Nonnull
		public ItemStack getTabIconItem() {
			return getIconItemStack();
		}

		@SideOnly(Side.CLIENT)
		void addItem(Item item) {
			item.getSubItems(this, list);
		}

		@SideOnly(Side.CLIENT)
		void addBlock(Block block) {
			block.getSubBlocks(this, list);
		}
	}

	private static class MiscItems extends CreativeTab {

		MiscItems() {
			super("misc_items");
			setBackgroundImageName("items.png");
		}

		@Override
		@Nonnull
		public ItemStack getIconItemStack() {
			return new ItemStack(ModItems.QUINGENTILLIARD);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
			this.list = list;
			addItem(ModItems.QUINGENTILLIARD);
			addItem(ModItems.QUANTUM_QUARTZ);
		}
	}

	private static class MiscBlocks extends CreativeTab {

		MiscBlocks() {
			super("misc_blocks");
			setBackgroundImageName("items.png");
		}

		@Override
		@Nonnull
		public ItemStack getIconItemStack() {
			return new ItemStack(ModBlocks.SCHRODINGER_GLYPH);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> list) {
			this.list = list;
			addBlock(ModBlocks.PRIMAL_STONE);
			addBlock(ModBlocks.PRIMAL_GLYPH);
			addBlock(ModBlocks.ASHEN);
			addBlock(ModBlocks.SCHRODINGER_GLYPH);
			addBlock(ModBlocks.PHENOMENA);
			addBlock(ModBlocks.Q_SQUARED);
			addBlock(ModBlocks.THEOREMA);
			addBlock(ModBlocks.singularity);
			addBlock(ModBlocks.PRISM_FLOWER);
			addBlock(ModBlocks.QUANTUM_MIRROR);
			addBlock(ModBlocks.GRAVITY_HOPPER);
			addBlock(ModBlocks.CRYSTAL_VOID);
			addBlock(ModBlocks.BLINKER);
			addBlock(ModBlocks.HYPER_CONDUCTOR);
			addBlock(ModBlocks.ELECTRON);
			addBlock(ModBlocks.GRAVITY_INHIBITOR);
		}
	}
}
