/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.UP;

/**
 * Created by <Arekkuusu> on 05/09/2017.
 * It's distributed as part of Solar.
 */
public class BakedQimranut extends BakedBrightness {

	private final TextureAtlasSprite base;
	private final TextureAtlasSprite base_;
	private final TextureAtlasSprite overlay_front;
	private final TextureAtlasSprite overlay_back;

	public BakedQimranut(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		base = getter.apply(ResourceLibrary.QIMRANUT_BASE);
		base_ = getter.apply(ResourceLibrary.QIMRANUT_BASE_);
		overlay_front = getter.apply(ResourceLibrary.QIMRANUT_OVERLAY_FRONT);
		overlay_back = getter.apply(ResourceLibrary.QIMRANUT_OVERLAY_BACK);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		switch(MinecraftForgeClient.getRenderLayer()) {
			case SOLID:
				//Base
				QuadBuilder base_quads = QuadBuilder.withFormat(format)
						.setFrom(3, 3, 3)
						.setTo(13, 4, 13)
						.addAll(0F, 9F, 7F, 7F, base)
						.addFace(UP, 0F, 9F, 7F, 16F, base_)
						.addFace(DOWN, 0F, 9F, 7F, 16F, base)
						.rotate(facing, DOWN);
				quads.addAll(base_quads.bake());
				break;
			case CUTOUT_MIPPED:
				//Overlay
				QuadBuilder overlay_quads = QuadBuilder.withFormat(format)
						.setFrom(3, 3, 3)
						.setTo(13, 4, 13)
						.addAll(0F, 9F, 15F, 16F, overlay_front)
						.addFace(DOWN, 0F, 9F, 7F, 16F, overlay_front)
						.addFace(UP, 0F, 9F, 7F, 16F, overlay_back)
						.setHasBrightness(true)
						.rotate(facing, DOWN);
				quads.addAll(overlay_quads.bake());
				break;
		}
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
