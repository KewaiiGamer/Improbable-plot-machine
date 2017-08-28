/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.resource.FrameSpriteResource;
import arekkuusu.solar.client.util.resource.SpriteLoader;
import arekkuusu.solar.client.util.resource.SpriteResource;
import arekkuusu.solar.common.Solar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static arekkuusu.solar.client.util.ResourceLibrary.TextureLocation.*;

/**
 * Created by <Arekkuusu> on 03/07/2017.
 * It's distributed as part of solar.
 */
@SideOnly(Side.CLIENT)
public final class SpriteLibrary {

	//Particle
	public static final FrameSpriteResource QUORN_PARTICLE = SpriteLoader.load(EFFECT, "quorn_particle", 7, 1);
	public static final SpriteResource NEUTRON_PARTICLE = SpriteLoader.load(EFFECT, "neutron_particle");
	public static final SpriteResource LIGHT_PARTICLE = SpriteLoader.load(EFFECT, "light_particle");
	//Other
	public static final FrameSpriteResource QUANTUM_MIRROR = SpriteLoader.load(BLOCKS, "quantum_mirror", 9, 1);
	public static final SpriteResource PRISM_PETAL = SpriteLoader.load(BLOCKS, "prism_flower/petal");
	public static final SpriteResource EYE_OF_SCHRODINGER_LAYER = SpriteLoader.load(MODEL, "eye_of_schrodinger_layer");
	public static final FrameSpriteResource QUINGENTILLIARD = SpriteLoader.load(ITEMS, "quingentilliard", 8, 1);
	public static final FrameSpriteResource RED_GLYPH = SpriteLoader.load(BLOCKS, "red_glyph", 4, 1);

	public static void init() {
		Solar.LOG.warn("[NOT EVEN A SUPER QUANTUM COMPUTER CALCULATING FOR A THOUSAND YEARS COULD EVEN MEASURE THE NUMBER OF FUCKS I DO NOT GIVE]");
	}
}
