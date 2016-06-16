package LilypadBreed;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = LilypadBreed.MOD_ID,
        name = LilypadBreed.MOD_NAME,
        version = LilypadBreed.MOD_VERSION,
        dependencies = LilypadBreed.MOD_DEPENDENCIES,
        useMetadata = true,
        acceptedMinecraftVersions = LilypadBreed.MOD_MC_VERSION)
public class LilypadBreed {

    public static final String MOD_ID = "lilypadbreed";
    public static final String MOD_NAME = "LilyPadBreed";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[11.14.0.1237,)";
    public static final String MOD_MC_VERSION = "[1.8,1.8.9]";

	public static int intLilyPadRate = 25;
	public static Block waterlily;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		intLilyPadRate = config.get(Configuration.CATEGORY_GENERAL, "LilypadRate", 25,
				"LilyPad Spown Rate(0%-100%), min = 0, max = 100").getInt();
		intLilyPadRate = (intLilyPadRate < 0) ? 0 : (intLilyPadRate > 100) ? 100 : intLilyPadRate;
		config.save();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void BreedEvent(BonemealEvent event) {
        if (!event.block.getBlock().equals(Blocks.waterlily)) return;
        BlockPos pos = event.pos;
		for (int x = -2 + pos.getX(); x < 3 + pos.getX(); x++) {
			for (int z = -2 + pos.getZ(); z < 3 + pos.getZ(); z++) {
                IBlockState state1 = event.world.getBlockState(new BlockPos(x, pos.getY() - 1, z));
                IBlockState state2 = event.world.getBlockState(new BlockPos(x, pos.getY(), z));
				if (state1.getBlock() == Blocks.water
						&& state2.getBlock() == Blocks.air)
				{
					if (x == pos.getX() && z == pos.getZ()
							|| event.world.rand.nextInt(100) < LilypadBreed.intLilyPadRate)
					{
						event.world.setBlockState(new BlockPos(x, pos.getY(), z), Blocks.waterlily.getDefaultState());
					}
				}
			}
		}
		event.setResult(Result.ALLOW);
	}
}