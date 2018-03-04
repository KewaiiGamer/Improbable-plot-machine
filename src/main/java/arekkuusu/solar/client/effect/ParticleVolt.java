/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.api.util.Vector3.ImmutableVector3;
import arekkuusu.solar.client.util.helper.ProfilerHelper;
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
public class ParticleVolt extends ParticleBase {

	private List<VoltSegment> segments = Lists.newArrayList();
	private final int generations;
	private final boolean branch;
	private final boolean fade;
	private float offset;

	ParticleVolt(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {
		super(world, from.copy().add(to).divide(2D), ImmutableVector3.NULL, 0, age, rgb);
		this.segments.add(new VoltSegment(from, to));
		this.generations = generations;
		this.branch = branch;
		this.offset = offset;
		this.fade = fade;
		calculateBolts();
	}

	private void calculateBolts() {
		ProfilerHelper.begin("[Particle Bolt] Calculating Bolts");
		List<VoltSegment> branched = Lists.newArrayList();
		for(int i = 0; i < generations; i++) {
			List<VoltSegment> temp = Lists.newArrayList();
			for(VoltSegment segment : segments) {
				ImmutableVector3 from = segment.from.toImmutable();
				ImmutableVector3 to = segment.to.toImmutable();
				Vector3 mid = average(from, to);
				Vector3 midOffset = to.subtract(from);
				mid.add(midOffset.normalize().cross(Vector3.create(1, 1, 1)).multiply(Vector3.getRandomVec(offset)));
				if(branch && rand.nextDouble() > 0.6D) {
					Vector3 direction = mid.copy().subtract(from);
					Vector3 splitEnd = direction
							.rotatePitchX((0.2F + 0.25F * rand.nextFloat()) * (rand.nextBoolean() ? 1 : -1))
							.rotatePitchZ((0.2F + 0.25F * rand.nextFloat()) * (rand.nextBoolean() ? 1 : -1))
							.multiply(0.7D)
							.add(mid);
					VoltSegment sub = new VoltSegment(mid, splitEnd);
					sub.alpha = segment.alpha;
					temp.add(sub);
				}
				VoltSegment one = new VoltSegment(from, mid);
				VoltSegment two = new VoltSegment(mid, to);
				if(fade) {
					one.alpha = segment.alpha * 0.5F;
					two.alpha = segment.alpha;
				}
				temp.add(one);
				temp.add(two);
				if(branched.isEmpty() || branched.contains(segment)) {
					branched.add(two);
				}
			}
			segments = temp;
			offset /= 2;
		}
		ProfilerHelper.end();
	}

	private Vector3 average(ImmutableVector3 one, ImmutableVector3 two) {
		return one.add(two).divide(2D);
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		Tessellator.getInstance().draw();

		GlStateManager.disableTexture2D();
		segments.forEach(VoltSegment::render);
		GlStateManager.enableTexture2D();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}

	@Override
	public void onUpdateGlow() {
		this.onUpdate();
		segments.forEach(r -> r.alpha *= 0.9F);
		float life = (float) particleAge / (float) particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 1.0f - life;
		if(this.particleAngle != 0.0F) {
			this.prevParticleAngle = particleAngle;
			this.particleAngle += 1.0F;
		}
	}

	private class VoltSegment {

		private final Vector3 from;
		private final Vector3 to;
		private float alpha = 1F;

		VoltSegment(Vector3 from, Vector3 to) {
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