/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.api.helper.Vector3.ImmutableVector3;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by <Arekkuusu> on 28/10/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ParticleBolt extends ParticleBase {

	private List<BoltSegment> segments = Lists.newArrayList();

	ParticleBolt(World world, Vector3 from, Vector3 to, int generations, float offset, int rgb, boolean branch) {
		super(world, (from.x + to.x) / 2, (from.y + to.y) / 2, (from.z + to.z) / 2, 0, 0, 0);
		float r = (rgb >>> 16 & 0xFF) / 256.0F;
		float g = (rgb >>> 8 & 0xFF) / 256.0F;
		float b = (rgb & 0xFF) / 256.0F;
		setRBGColorF(r, g, b);
		segments.add(new BoltSegment(from, to));
		calculateBolts(generations, offset, branch);

		particleMaxAge = 20;
	}

	private void calculateBolts(int generations, float offset, boolean branch) {
		for(int i = 0; i < generations; i++) {
			List<BoltSegment> temp = Lists.newArrayList();
			for(BoltSegment segment : segments) {
				ImmutableVector3 from = segment.from.toImmutable();
				ImmutableVector3 to = segment.to.toImmutable();

				Vector3 mid = average(from, to);
				Vector3 midOffset = to.subtract(from);
				if(midOffset.x == 0) {
					midOffset.x = 4D * rand.nextDouble();
				}
				if(midOffset.y == 0) {
					midOffset.y = 4D * rand.nextDouble();
				}
				if(midOffset.z == 0) {
					midOffset.z = 4D * rand.nextDouble();
				}
				mid.add(midOffset.normalize().multiply(Vector3.getRandomVec(offset)));

				if(branch && rand.nextInt(5) == 0) {
					Vector3 direction = mid.copy().subtract(from);
					Vector3 splitEnd = direction
							.rotatePitchX((rand.nextFloat() * 2F - 1F) * 0.45F)
							.rotatePitchZ((rand.nextFloat() * 2F - 1F) * 0.45F)
							.multiply(0.7D)
							.add(mid);

					BoltSegment sub = new BoltSegment(mid, splitEnd);
					sub.alpha = segment.alpha * 0.5F;
					temp.add(sub);
				}

				BoltSegment one = new BoltSegment(from, mid);
				one.alpha = segment.alpha;
				BoltSegment two = new BoltSegment(mid, to);
				two.alpha = segment.alpha * 0.75F;
				temp.add(one);
				temp.add(two);
			}
			segments = temp;
			offset /= 2;
		}
	}

	private Vector3 average(ImmutableVector3 one, ImmutableVector3 two) {
		return one.add(two).divide(2D);
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		Tessellator.getInstance().draw();

		GlStateManager.disableTexture2D();
		segments.forEach(BoltSegment::render);
		GlStateManager.enableTexture2D();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(rand.nextInt(6) == 0) {
			particleAge++;
		}
		float life = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleAlpha = 0.5F * (1.0F - life);
		if(particleAlpha < 0) particleAlpha = 0;
	}

	private class BoltSegment {

		private final Vector3 from;
		private final Vector3 to;
		private float alpha = 1F;

		public BoltSegment(Vector3 from, Vector3 to) {
			this.from = from;
			this.to = to;
		}

		void render() {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buff = tessellator.getBuffer();

			buff.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			buff.setTranslation(
					-TileEntityRendererDispatcher.staticPlayerX,
					-TileEntityRendererDispatcher.staticPlayerY,
					-TileEntityRendererDispatcher.staticPlayerZ
			);
			buff.pos(from.x, from.y, from.z).color(getRedColorF(), getGreenColorF(), getBlueColorF(), alpha * particleAlpha)
					.endVertex();
			buff.pos(to.x, to.y, to.z).color(getRedColorF(), getGreenColorF(), getBlueColorF(), alpha * particleAlpha)
					.endVertex();
			buff.setTranslation(0,0,0);
			tessellator.draw();
		}
	}
}