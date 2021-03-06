/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.entity.EntityTemporalItem;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by <Arekkuusu> on 06/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileVacuumConveyor extends TileBase implements ITickable {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.apply(0.5D, 0.1D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.apply(0.5D, 0.9D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.apply(0.5D, 0.5D, 0.9D))
			.put(EnumFacing.SOUTH, Vector3.apply(0.5D, 0.5D, 0.1D))
			.put(EnumFacing.EAST, Vector3.apply(0.1D, 0.5D, 0.5D))
			.put(EnumFacing.WEST, Vector3.apply(0.9D, 0.5D, 0.5D))
			.build();
	private Pair<IItemHandler, ISidedInventory> to, from;
	private ItemStack lookup = ItemStack.EMPTY;
	private AxisAlignedBB attract, repulse;
	private AxisAlignedBB attractInverse, repulseInverse;
	private AxisAlignedBB absorptionRange;
	private int cooldown;

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			this.absorptionRange = new AxisAlignedBB(getPos()).grow(0.5D);
			EnumFacing facing = getFacingLazy();
			this.attract = new AxisAlignedBB(getPos().offset(facing, 2)).grow(10D);
			this.repulse = new AxisAlignedBB(getPos().offset(facing)).grow(1D);
			this.attractInverse = new AxisAlignedBB(getPos().offset(facing.getOpposite(), 4)).grow(5);
			this.repulseInverse = new AxisAlignedBB(getPos().offset(facing, 4)).grow(5);
			updateInventoryAccess();
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState != newState;
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			updateInventoryAccess();
			boolean isAir = true;
			if(to.getKey() != null && (isAir = isAir(getFacingLazy().getOpposite()))) {
				collectItems();
			} else if(from.getKey() != null && (isAir = isAir(getFacingLazy()))) {
				dropItems();
			} else if(isAir) transposeItems();
		} else spawnParticles();
	}

	private void spawnParticles() {
		if(world.getTotalWorldTime() % 2 == 0) {
			EnumFacing facing = getFacingLazy();
			if(isAir(facing.getOpposite())) {
				spawnLightParticles(facing, false);
			}
			if(isAir(facing)) {
				spawnLightParticles(facing.getOpposite(), true);
			}
		}
	}

	private boolean isAir(EnumFacing facing) {
		return world.isAirBlock(pos.offset(facing));
	}

	private void spawnLightParticles(EnumFacing facing, boolean inverse) {
		Vector3 back = getOffSet(facing);
		facing = facing.getOpposite();

		Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 45);
		Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 45);
		double speed = world.rand.nextDouble() * 0.015D;
		Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
				.asImmutable()
				.rotate(x.multiply(z))
				.multiply(speed);
		IPM.getProxy().spawnSpeck(world, back, speedVec, 75, 1.75F, inverse ? 0xFFFFFF : 0x000000, GlowTexture.GLINT);
	}

	private void collectItems() {
		applyGravity(attract, 4D);
		IItemHandler handler = to.getKey();
		ISidedInventory sidedInv = to.getValue();
		getItemsFiltered(absorptionRange).forEach(e -> {
			e.setNoGravity(true);
			for(int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack inSlot = handler.getStackInSlot(slot);
				ItemStack in = e.getItem();
				if(sidedInv == null || sidedInv.canInsertItem(slot, in, getFacingLazy())) {
					if(inSlot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(inSlot, in)
							&& (inSlot.getCount() < inSlot.getMaxStackSize()
							&& inSlot.getCount() < handler.getSlotLimit(slot)))) {
						ItemStack out = handler.insertItem(slot, in, false);
						if(out != in) {
							e.setItem(out);
							break;
						}
					}
				}
			}
		});
	}

	private void dropItems() {
		if(cooldown <= 0) {
			IItemHandler handler = from.getKey();
			ISidedInventory sidedInv = from.getValue();
			for(int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack inSlot = handler.getStackInSlot(slot);
				if(!inSlot.isEmpty()
						&& (lookup.isEmpty() || ItemHandlerHelper.canItemStacksStack(lookup, inSlot))
						&& (sidedInv == null || sidedInv.canExtractItem(slot, inSlot, getFacingLazy()))) {
					BlockPos offset = pos.offset(getFacingLazy());
					Vector3 spawn = Vector3.apply(offset.getX(), offset.getY(), offset.getZ()).add(0.5D);
					ItemStack out = handler.extractItem(slot, Integer.MAX_VALUE, false);
					EntityTemporalItem entity = new EntityTemporalItem(world, spawn.x(), spawn.y(), spawn.z(), out);
					impulseEntityItem(spawn, entity);
					world.spawnEntity(entity);
					break;
				}
			}
			cooldown = 5;
		} else cooldown--;
		applyGravity(repulse, -1.25D);
	}

	private void transposeItems() {
		boolean impulse = false;
		if(world.isAirBlock(pos.offset(getFacingLazy().getOpposite()))) {
			applyGravity(attractInverse, 4D);
			impulse = true;
		}
		if(world.isAirBlock(pos.offset(getFacingLazy()))) {
			if(impulse) {
				BlockPos offset = getPos().offset(getFacingLazy());
				Vector3 spawn = Vector3.apply(offset.getX(), offset.getY(), offset.getZ()).add(0.5D);
				getItemsFiltered(new AxisAlignedBB(getPos()).grow(0.5D)).forEach(entity -> {
					impulseEntityItem(spawn, entity);
				});
			}
			applyGravity(repulseInverse, -4D);
		}
	}

	private void impulseEntityItem(Vector3 pos, EntityTemporalItem item) {
		Vector3 fuzzy = pos.add(Vector3.rotateRandom().multiply(0.1D));
		item.setPositionAndUpdate(fuzzy.x(), fuzzy.y(), fuzzy.z());
		item.setMotion(0, 0, 0);
	}

	private void applyGravity(AxisAlignedBB box, double force) {
		getItemsFiltered(box).forEach(item -> {
			double x = getPos().getX() + 0.5D - item.posX;
			double y = getPos().getY() + 0.5D - item.posY;
			double z = getPos().getZ() + 0.5D - item.posZ;
			double sqrt = Math.sqrt(x * x + y * y + z * z);
			double v = sqrt / 9;

			if(sqrt <= 10D) {
				double strength = (1 - v) * (1 - v);
				double power = 0.085D * force;

				item.motionX += (x / sqrt) * strength * power;
				item.motionY += (y / sqrt) * strength * power;
				item.motionZ += (z / sqrt) * strength * power;
			}
		});
	}

	private List<EntityTemporalItem> getItemsFiltered(AxisAlignedBB box) {
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, box, Entity::isEntityAlive);
		return list.stream().filter(entity -> {
			ItemStack stack = entity.getItem();
			return lookup.isEmpty() || ItemHandlerHelper.canItemStacksStack(lookup, stack);
		}).map(this::map).collect(Collectors.toList());
	}

	private EntityTemporalItem map(EntityItem entity) {
		if(entity instanceof EntityTemporalItem) {
			((EntityTemporalItem) entity).lifeTime = 360;
			return (EntityTemporalItem) entity;
		}
		EntityTemporalItem item = new EntityTemporalItem(entity);
		world.spawnEntity(item);
		entity.setDead();
		return item;
	}

	public void updateInventoryAccess() {
		this.from = getInventory(getFacingLazy().getOpposite());
		this.to = getInventory(getFacingLazy());
	}

	private Pair<IItemHandler, ISidedInventory> getInventory(EnumFacing facing) {
		BlockPos target = getPos().offset(facing);
		if(world.isBlockLoaded(target, false)) {
			TileEntity tile = world.getTileEntity(target);
			if(tile != null) {
				IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				if(handler == null) handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				return Pair.of(handler, tile instanceof ISidedInventory ? (ISidedInventory) tile : null);
			}
		}
		return Pair.of(null, null);
	}

	private Vector3 getOffSet(EnumFacing facing) {
		return FACING_MAP.get(facing).add(pos.getX(), pos.getY(), pos.getZ());
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public void setLookup(ItemStack stack) {
		lookup = stack;
		markDirty();
		sync();
	}

	public ItemStack getLookup() {
		return lookup;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey("lookup")) {
			lookup = new ItemStack((NBTTagCompound) compound.getTag("lookup"));
		} else lookup = ItemStack.EMPTY;
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(!lookup.isEmpty()) {
			compound.setTag("lookup", lookup.writeToNBT(new NBTTagCompound()));
		}
	}
}
