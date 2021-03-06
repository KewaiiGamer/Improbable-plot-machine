/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker.baked;

import arekkuusu.implom.api.state.Direction;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.common.block.BlockQelaion;
import net.katsstuff.teamnightclipse.mirror.client.baked.Baked;
import net.katsstuff.teamnightclipse.mirror.client.baked.QuadBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/*
 * Created by <Arekkuusu> on 24/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class BakedQelaion extends BakedBrightBase {

	private TextureAtlasSprite base;
	private TextureAtlasSprite off;
	private TextureAtlasSprite on;

	@Override
	public ResourceLocation[] getTextures() {
		return new ResourceLocation[]{
				ResourceLibrary.QELAION_BASE,
				ResourceLibrary.QELAION_ON,
				ResourceLibrary.QELAION_OFF
		};
	}

	@Override
	public ResourceLocation getParticle() {
		return ResourceLibrary.QELAION_BASE;
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		this.base = sprites.apply(ResourceLibrary.QELAION_BASE);
		this.off = sprites.apply(ResourceLibrary.QELAION_OFF);
		this.on = sprites.apply(ResourceLibrary.QELAION_ON);
		this.cache.reloadTextures();
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state) {
		return cache.compute(state, quads -> {
			boolean hasNode = state != null && state.getValue(BlockQelaion.HAS_NODE);
			VertexFormat format = state != null ? format() : DefaultVertexFormats.ITEM;
			quads.addAll(BlockBaker.QELAION.getQuads());
			quads.addAll(QuadBuilder.withFormat(format)
					.setFrom(5, 5, 5)
					.setTo(11, 11, 11)
					.addAll(0, 9, 0, 9, hasNode ? on : off)
					.setHasBrightness(true)
					.bakeJava()
			);
			if(state instanceof IExtendedBlockState) {
				Direction direction = ((IExtendedBlockState) state).getValue(Direction.DIR_UNLISTED);
				if(direction != null && direction != Direction.NON) {
					QuadBuilder builder = QuadBuilder.withFormat(format)
							.setFrom(2, 2, 2)
							.setTo(14, 14, 14);
					for(EnumFacing facing : direction.getFacings())
						builder = builder.addFace(facing, 2, 14, 2, 14, base);
					quads.addAll(builder.bakeJava());
				}
			}
		});
	}
}
