/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import arekkuusu.solar.api.entanglement.energy.data.LumenTileWrapper;
import arekkuusu.solar.common.block.BlockNeutronBattery;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public class TileNeutronBattery extends TileBase implements IEntangledTile {

	private LumenTileWrapper<TileNeutronBattery> handler;
	private UUID key;

	public TileNeutronBattery(Capacity capacity) {
		this.handler = new LumenTileWrapper<>(this, capacity.max);
	}

	public TileNeutronBattery() {} //Why...

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState != newState;
	}

	public Capacity getCapacityLazy() {
		return getStateValue(BlockNeutronBattery.CAPACITY, pos).orElse(Capacity.BLUE);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY && facing == EnumFacing.UP || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY && facing == EnumFacing.UP
				? ModCapability.LUMEN_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
		markDirty();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			this.key = compound.getUniqueId("key");
		}
		handler = new LumenTileWrapper<>(this, compound.getInteger("capacity"));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(key != null) {
			compound.setUniqueId("key", key);
		}
		compound.setInteger("capacity", getCapacityLazy().max);
	}

	public enum Capacity implements IStringSerializable {
		BLUE(64, 0x2FFEEB),
		GREEN(512, 0x29FF75),
		PINK(4096, 0xFF39BA);

		public final int max;
		public final int color;

		Capacity(int max, int color) {
			this.max = max;
			this.color = color;
		}

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
