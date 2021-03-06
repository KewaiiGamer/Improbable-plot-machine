/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.api.capability.relativity.RelativityHelper;
import arekkuusu.implom.api.capability.relativity.RelativityStackProvider;
import arekkuusu.implom.api.capability.relativity.data.IRelativeRedstone;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ItemBlinker extends ItemBaseBlock implements IUUIDDescription {

	ItemBlinker() {
		super(ModBlocks.BLINKER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		RelativityHelper.getRedstoneCapability(stack).flatMap(IRelativeRedstone::getKey).ifPresent(uuid -> {
			addInformation(uuid, tooltip);
		});
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return RelativityStackProvider.createRedstone(stack);
	}
}
