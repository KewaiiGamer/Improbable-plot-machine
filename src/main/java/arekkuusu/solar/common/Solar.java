/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.entity.ModEntities;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.network.PacketHandler;
import arekkuusu.solar.common.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
@Mod(modid = LibMod.MOD_ID, name = LibMod.MOD_NAME, version = LibMod.MOD_VERSION, acceptedMinecraftVersions = "[1.12]")
public class Solar {

	@SidedProxy(clientSide = LibMod.CLIENT_PROXY, serverSide = LibMod.SERVER_PROXY)
	public static IProxy PROXY;
	@Mod.Instance
	public static Solar INSTANCE;

	public static Logger LOG = LogManager.getLogger(LibMod.MOD_NAME);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PROXY.preInit(event);
		PacketHandler.init();
		ModEntities.init();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@Mod.EventHandler
	public void serverStop(FMLServerStoppedEvent event) {
		SolarApi.QUANTUM_ITEMS.clear();
	}
}
