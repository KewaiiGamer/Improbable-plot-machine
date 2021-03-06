/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.entity;

import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class ModEntities {

	private static int id;

	public static void init() {
		register(EntityStaticItem.class, "static_item");
		register(EntityTemporalItem.class, "temporal_item");
		register(EntityEyeOfSchrodinger.class, "eye_of_schrodinger", 0x222222);
		register(EntityLumen.class, "lumen");
	}

	private static <T extends Entity> void register(Class<T> clazz, String name) {
		EntityRegistry.registerModEntity(getLocation(name), clazz, name, id++, IPM.getInstance(), 64, 1, true);
	}

	private static <T extends Entity> void register(Class<T> clazz, String name, int color) {
		EntityRegistry.registerModEntity(getLocation(name), clazz, name, id++, IPM.getInstance(), 64, 1, true, color, color);
	}

	private static ResourceLocation getLocation(String name) {
		return new ResourceLocation(LibMod.MOD_ID, name);
	}
}
