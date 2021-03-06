/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.implom.common.entity.Megumin;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 25/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class BlockSchrodingerGlyph extends BlockBase {

	public BlockSchrodingerGlyph() {
		super(LibNames.SCHRODINGER_GLYPH, Material.ROCK);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(4F);
		setResistance(2000F);
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
		Vector3 vec = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
		Megumin.chant(world, vec, 5F, false).explosion();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(!world.isRemote) {
			Optional<EntityPlayer> optional = getClosestPlayer(world, pos);
			if(optional.isPresent() && !optional.get().capabilities.isCreativeMode) {
				double distance = optional.get().getDistanceSqToCenter(pos);
				distance = MathHelper.sqrt(distance);
				boolean spawn = false;
				for(int i = 0, tries = (int) (11 - distance); i < tries; i++) {
					if(rand.nextInt(5) == 0) continue;
					BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
					for(int j = 0, randomized = rand.nextInt(6); j < randomized; j++) {
						EnumFacing facing = EnumFacing.values()[rand.nextInt(5)];
						mutable.move(facing, rand.nextInt(3));
					}
					EntityEyeOfSchrodinger eye = new EntityEyeOfSchrodinger(world);
					eye.setPosition(mutable.getX() + 0.5D, mutable.getY() + 0.5D, mutable.getZ() + 0.5D);
					if(eye.getCanSpawnHere() && !world.getBlockState(mutable).causesSuffocation()) {
						world.spawnEntity(eye);
						spawn = true;
					}
				}
				world.scheduleUpdate(pos, this, spawn ? 20 + rand.nextInt(60) : 20);
			} else {
				world.scheduleUpdate(pos, this, tickRate(world));
			}
		}
	}

	@Override
	public int tickRate(World worldIn) {
		return 60;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(world.isRemote && rand.nextInt(15) == 0 && getClosestPlayer(world, pos).isPresent()) {
			for(EnumFacing facing : EnumFacing.values()) {
				Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
				Vector3 vec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable().multiply(0.025D);
				IPM.getProxy().spawnNeutronBlast(world, from, vec, 60, 0.25F, 0xFF0303, false);
			}
		}
	}

	private Optional<EntityPlayer> getClosestPlayer(World world, BlockPos pos) {
		EntityPlayer player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false);
		return Optional.ofNullable(player);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		return 30 + RANDOM.nextInt(30);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}
}
