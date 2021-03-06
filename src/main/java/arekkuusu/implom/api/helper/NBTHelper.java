/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.helper;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("unused")
public final class NBTHelper {

	private NBTHelper() {
	}

	public static NBTTagCompound fixNBT(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		return tagCompound;
	}

	public static void setByte(ItemStack stack, String tag, byte i) {
		fixNBT(stack).setByte(tag, i);
	}

	public static void setInteger(ItemStack stack, String tag, int i) {
		fixNBT(stack).setInteger(tag, i);
	}

	public static void setFloat(ItemStack stack, String tag, float i) {
		fixNBT(stack).setFloat(tag, i);
	}

	public static void setBoolean(ItemStack stack, String tag, boolean i) {
		fixNBT(stack).setBoolean(tag, i);
	}

	public static void setString(ItemStack stack, String tag, String i) {
		fixNBT(stack).setString(tag, i);
	}

	public static void setUniqueID(ItemStack stack, String tag, UUID i) {
		fixNBT(stack).setUniqueId(tag, i);
	}

	public static byte getByte(ItemStack stack, String tag) {
		return fixNBT(stack).getByte(tag);
	}

	public static int getInteger(ItemStack stack, String tag) {
		return fixNBT(stack).getInteger(tag);
	}

	public static float getFloat(ItemStack stack, String tag) {
		return fixNBT(stack).getFloat(tag);
	}

	public static boolean getBoolean(ItemStack stack, String tag) {
		return fixNBT(stack).getBoolean(tag);
	}

	public static String getString(ItemStack stack, String tag) {
		return fixNBT(stack).getString(tag);
	}

	@Nullable
	public static UUID getUniqueID(ItemStack stack, String tag) {
		NBTTagCompound compound = fixNBT(stack);
		return compound.hasUniqueId(tag) ? compound.getUniqueId(tag) : null;
	}

	public static <T extends Enum<T> & IStringSerializable> ItemStack setEnum(ItemStack stack, T t, String tag) {
		fixNBT(stack).setString(tag, t.getName());
		return stack;
	}

	public static <T extends Enum<T> & IStringSerializable> Optional<T> getEnum(Class<T> clazz, ItemStack stack, String tag) {
		String value = fixNBT(stack).getString(tag);
		return Stream.of(clazz.getEnumConstants()).filter(e -> e.getName().equals(value)).findAny();
	}

	public static <T extends NBTBase> T setNBT(ItemStack stack, String tag, T base) {
		fixNBT(stack).setTag(tag, base);
		return base;
	}

	public static Optional<NBTTagCompound> getNBTTag(ItemStack stack, String tag) {
		return hasTag(stack, tag, NBTType.COMPOUND) ? Optional.of(fixNBT(stack).getCompoundTag(tag)) : Optional.empty();
	}

	public static Optional<NBTTagList> getNBTList(ItemStack stack, String tag) {
		return hasTag(stack, tag, NBTType.COMPOUND) ? Optional.of(fixNBT(stack).getTagList(tag, NBTType.LIST.ordinal())) : Optional.empty();
	}

	public static <T extends Entity> Optional<T> getEntityByUUID(Class<T> clazz, UUID uuid, World world) {
		for(Entity entity : world.loadedEntityList) {
			if(clazz.isInstance(entity) && entity.getUniqueID().equals(uuid)) return Optional.of(clazz.cast(entity));
		}
		return Optional.empty();
	}

	public static boolean hasTag(ItemStack stack, String tag, NBTType type) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		return tagCompound != null && tagCompound.hasKey(tag, type.ordinal());
	}

	public static boolean hasTag(ItemStack stack, String tag) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		return tagCompound != null && tagCompound.hasKey(tag);
	}

	public static boolean hasUniqueID(ItemStack stack, String tag) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		return tagCompound != null && tagCompound.hasUniqueId(tag);
	}

	public static void removeTag(ItemStack stack, String tag) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if(tagCompound != null && tagCompound.hasKey(tag)) {
			tagCompound.removeTag(tag);
		}
	}

	public enum NBTType {
		END,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		BYTE_ARRAY,
		STRING,
		LIST,
		COMPOUND,
		INT_ARRAY,
		LONG_ARRAY
	}
}
