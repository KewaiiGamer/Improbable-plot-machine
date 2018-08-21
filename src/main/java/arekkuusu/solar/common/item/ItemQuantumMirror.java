/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.capability.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.capability.inventory.EntangledIItemHelper;
import arekkuusu.solar.api.capability.inventory.EntangledStackProvider;
import arekkuusu.solar.api.capability.inventory.data.EntangledStackWrapper;
import arekkuusu.solar.common.block.BlockQuantumMirror;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.network.PacketHelper;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public class ItemQuantumMirror extends ItemBaseBlock implements IUUIDDescription {

	public ItemQuantumMirror() {
		super(ModBlocks.QUANTUM_MIRROR);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
		EntangledIItemHelper.getCapability(stack).ifPresent(entangled -> {
			entangled.getKey().ifPresent(uuid -> addInformation(uuid, tooltip));
		});
	}

	@Override
	public void addInformation(UUID uuid, List<String> tooltip) {
		Tooltip.inline().condition(KeyCondition$.MODULE$.shiftKeyDown()).ifTrueJ(builder ->
				builder.condition(() -> !EntangledIItemHandler.getEntanglementStack(uuid, 0).isEmpty()).ifTrueJ(sub -> {
					ItemStack stack = EntangledIItemHandler.getEntanglementStack(uuid, 0);
					return sub.addI18n("tlp.quantum_data", Tooltip.DarkGrayItalic()).add(": ").newline()
							.add("    - ", TextFormatting.DARK_GRAY)
							.add(stack.getDisplayName(), Tooltip.GrayItalic())
							.add(" x " + stack.getCount())
							.newline();
				}).apply().condition(KeyCondition$.MODULE$.controlKeyDown()).ifTrueJ(b -> getInfo(b, uuid)).apply()
		).apply().build(tooltip);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return EntangledStackProvider.create(new EntangledStackWrapper(stack, BlockQuantumMirror.SLOTS) {
			@Override
			protected void onChange(int slot) {
				getKey().ifPresent(uuid -> {
					if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
						PacketHelper.sendQuantumChange(uuid, getStackInSlot(slot), slot);
					}
				});
			}
		});
	}
}
