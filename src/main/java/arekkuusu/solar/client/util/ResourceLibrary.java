/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.resource.Location;
import arekkuusu.solar.client.util.resource.ResourceBuilder;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static arekkuusu.solar.client.util.ResourceLibrary.TextureLocation.BLOCKS;
import static arekkuusu.solar.client.util.ResourceLibrary.TextureLocation.MODEL;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ResourceLibrary {

	//Textures
	public static final ResourceLocation NOTHING = getAtlas(BLOCKS, "nothing");
	public static final ResourceLocation PRIMAL_STONE = getAtlas(BLOCKS, "primal_stone");
	public static final ResourceLocation[] PRIMAL_GLYPH = ResourceBuilder.toArray(16, "glyph/primal_glyph_", name ->
			getAtlas(BLOCKS, name)
	);
	public static final ResourceLocation GRAVITY_HOPPER = getAtlas(BLOCKS, "gravity_hopper/side");
	public static final ResourceLocation[] GRAVITY_HOPPER_GLYPH = ResourceBuilder.toArray(6, "gravity_hopper/glyph_", name ->
			getAtlas(BLOCKS, name)
	);
	public static final ResourceLocation EYE_OF_SCHRODINGER = getTexture(MODEL, "eye_of_schrodinger");
	public static final ResourceLocation SCHRODINGER_GLYPH = getAtlas(BLOCKS, "schrodinger_glyph");
	public static final ResourceLocation BLINKER_BASE = getAtlas(BLOCKS, "blinker/blinker_base");
	public static final ResourceLocation BLINKER_TOP_ON = getAtlas(BLOCKS, "blinker/blinker_top_on");
	public static final ResourceLocation BLINKER_BOTTOM_ON = getAtlas(BLOCKS, "blinker/blinker_bottom_on");
	public static final ResourceLocation BLINKER_TOP_OFF = getAtlas(BLOCKS, "blinker/blinker_top_off");
	public static final ResourceLocation BLINKER_BOTTOM_OFF = getAtlas(BLOCKS, "blinker/blinker_bottom_off");
	public static final ResourceLocation Q_SQUARED = getAtlas(BLOCKS, "q_squared");
	public static final ResourceLocation THEOREMA = getTexture(BLOCKS, "theorema");

	public static ResourceLocation getLocation(AssetLocation asset, Location location, String name, String suffix) {
		StringBuilder builder = new StringBuilder();
		if(asset != null) builder.append(asset.getPath());
		if(location != null) builder.append(location.getPath());
		builder.append(name).append(suffix);
		return new ResourceLocation(LibMod.MOD_ID, builder.toString());
	}

	public static ModelResourceLocation getModel(String name, String variant) {
		ResourceLocation atlas = getAtlas(null, name);
		return new ModelResourceLocation(atlas, variant);
	}

	public static ResourceLocation getAtlas(Location location, String name) {
		return getLocation(null, location, name, "");
	}

	public static ResourceLocation getTexture(Location location, String name) {
		return getLocation(AssetLocation.TEXTURES, location, name, ".png");
	}

	public static ResourceLocation getSimpleLocation(String name) {
		return getLocation(null, null, name, "");
	}

	public enum ModelLocation implements Location {
		BLOCK("block"),
		ITEM("item"),
		OBJ("obj");

		private final String path;

		ModelLocation(String path) {
			this.path = path;
		}

		@Override
		public String getPath() {
			return path + "/";
		}
	}

	public enum TextureLocation implements Location {
		BLOCKS("blocks"),
		ITEMS("items"),
		EFFECT("effect"),
		GUI("gui"),
		MODEL("model");

		private final String path;

		TextureLocation(String path) {
			this.path = path;
		}

		@Override
		public String getPath() {
			return path + "/";
		}
	}

	public enum ShaderLocation implements Location {
		POST("post"),
		PROGRAM("program");

		private final String path;

		ShaderLocation(String path) {
			this.path = path;
		}

		@Override
		public String getPath() {
			return path + "/";
		}
	}

	public enum AssetLocation {
		MODELS("models"),
		TEXTURES("textures"),
		SHADERS("shaders");

		private final String path;

		AssetLocation(String path) {
			this.path = path;
		}

		public String getPath() {
			return path + "/";
		}
	}
}
