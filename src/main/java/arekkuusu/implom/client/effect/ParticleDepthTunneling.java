/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.world.World;

/*
 * Created by <Arekkuusu> on 04/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ParticleDepthTunneling extends ParticleSpeck {

	ParticleDepthTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		super(world, pos, speed, scale, age, rgb, glow);
	}

	@Override
	public boolean shouldDisableDepth() {
		return true;
	}
}
