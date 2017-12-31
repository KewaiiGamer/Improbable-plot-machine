/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
public class TileGravityHopper extends TileBase implements ITickable {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.create(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.create(0.5D, 0.25D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.create(0.5D, 0.5D, 0.25D))
			.put(EnumFacing.SOUTH, Vector3.create(0.5D, 0.5D, 0.75D))
			.put(EnumFacing.EAST, Vector3.create(0.75D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.create(0.25D, 0.5D, 0.5D))
			.build();
	private boolean powered;
	private boolean inverse;
	private int tick;

	@Override
	@SuppressWarnings("ConstantConditions")
	public void update() {
		if(!world.isRemote) {
			if(tick % 2 == 0) {
				traceBlock(getFacing()).ifPresent(out -> {
					traceBlock(getFacing().getOpposite()).ifPresent(in -> {
						ItemStack stack = transferOut(out, true);
						if(!stack.isEmpty() && transferIn(in, stack, true)) {
							transferIn(in, transferOut(out, false), false);
						}
					});
				});
			}
		} else {
			spawnParticles();
		}
		tick++;
	}

	private Optional<BlockPos> traceBlock(EnumFacing facing) { //Oh boy, I cant wait to use raytrace! ♪~ ᕕ(ᐛ)ᕗ
		for(int forward = 0; forward < 15; forward++) {
			BlockPos target = pos.offset(facing, forward + 1);
			IBlockState state = world.getBlockState(target);
			if(state.getBlock().hasTileEntity(state)) {
				TileEntity tile = world.getTileEntity(target);
				if(tile != null && (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())
						|| tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))) {
					return Optional.of(target);
				}
			}
		}
		return Optional.empty();
	}

	private Pair<ISidedInventory, IItemHandler> getInventory(BlockPos target, EnumFacing facing) {
		if(world.isBlockLoaded(target, false)) {
			TileEntity tile = world.getTileEntity(target);
			if(tile != null) {
				IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				if(handler == null) handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				return Pair.of(tile instanceof ISidedInventory ? (ISidedInventory) tile : null, handler);
			}
		}
		return Pair.of(null, null);
	}

	private ItemStack transferOut(BlockPos pos, boolean test) {
		EnumFacing facing = getFacing().getOpposite();
		Pair<ISidedInventory, IItemHandler> inv = getInventory(pos, facing);
		if(inv.getValue() != null) {
			IItemHandler handler = inv.getValue();
			ISidedInventory tile = inv.getKey();

			for(int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack in = handler.getStackInSlot(slot);
				if(!in.isEmpty() && (tile == null || tile.canExtractItem(slot, in, facing))) {
					return handler.extractItem(slot, Integer.MAX_VALUE, test);
				}
			}
		}
		return ItemStack.EMPTY;
	}

	private boolean transferIn(BlockPos pos, ItemStack inserted, boolean test) {
		EnumFacing facing = getFacing();
		Pair<ISidedInventory, IItemHandler> inv = getInventory(pos, facing);
		if(inv.getValue() != null) {
			IItemHandler handler = inv.getValue();
			ISidedInventory tile = inv.getKey();

			for(int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack inSlot = handler.getStackInSlot(slot);
				if(tile != null && !tile.canInsertItem(slot, inserted, facing)) return false;
				if(inSlot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(inSlot, inserted) && (inSlot.getCount() < inSlot.getMaxStackSize() && inSlot.getCount() < handler.getSlotLimit(slot)))) {
					return handler.insertItem(slot, inserted, test) != inserted;
				}
			}
		}
		return false;
	}

	private void spawnParticles() {
		if(tick % 180 == 0) {
			EnumFacing facing = getFacing();
			Vector3 back = getOffSet(facing);
			Vector3 vec = Vector3.create(facing).multiply(0.005D);
			ParticleUtil.spawnNeutronBlast(world, back, vec, 0xFF0303, 40, 0.25F, true);
		} else if(tick % 4 == 0 && world.rand.nextBoolean()) {
			EnumFacing facing = getFacing();
			Vector3 back = getOffSet(facing.getOpposite());
			double speed = world.rand.nextDouble() * -0.015D;
			Vector3 vec = Vector3.create(facing).multiply(speed);
			ParticleUtil.spawnLightParticle(world, back, vec, 0x49FFFF, 30, 2F);
		}
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(BlockDirectional.FACING, getFacing().getOpposite()));
		this.inverse = inverse;
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
	}

	private EnumFacing getFacing() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	private Vector3 getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).copy().add(pos);
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
	}
}
