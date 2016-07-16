package LilypadBreed;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[12.17.0,)";
    public static final String MOD_MC_VERSION = "[1.9,1.10.99]";

    public static int intLilyPadRate = 25;
    public static Block waterlily;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        intLilyPadRate = config.get(Configuration.CATEGORY_GENERAL, "LilypadRate", 25,
                "LilyPad Spown Rate(0%-100%), min = 0, max = 100").getInt();
        intLilyPadRate = (intLilyPadRate < 0) ? 0 : (intLilyPadRate > 100) ? 100 : intLilyPadRate;
        config.save();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void BreedEvent(BonemealEvent event) {
        if (event.getWorld().isRemote) return;
        if (!event.getBlock().getBlock().equals(Blocks.WATERLILY)) return;
        BlockPos pos = event.getPos();
        for (int x = -2 + pos.getX(); x < 3 + pos.getX(); x++) {
            for (int z = -2 + pos.getZ(); z < 3 + pos.getZ(); z++) {
                IBlockState state1 = event.getWorld().getBlockState(new BlockPos(x, pos.getY() - 1, z));
                IBlockState state2 = event.getWorld().getBlockState(new BlockPos(x, pos.getY(), z));
                if (state1.getBlock() == Blocks.WATER
                        && state2.getBlock() == Blocks.AIR) {
                    if (x == pos.getX() && z == pos.getZ()
                            || event.getWorld().rand.nextInt(100) < LilypadBreed.intLilyPadRate) {
                        event.getWorld().setBlockState(new BlockPos(x, pos.getY(), z), Blocks.WATERLILY.getDefaultState());
                    }
                }
            }
        }
        event.setResult(Result.ALLOW);
    }
}