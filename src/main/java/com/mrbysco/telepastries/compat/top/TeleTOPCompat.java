package com.mrbysco.telepastries.compat.top;

public class TeleTOPCompat {
//    private static boolean registered;
//
//    public static void register() {
//        if (registered)
//            return;
//        registered = true;
//        InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> "com.mrbysco.telepastries.compat.top.TeleTOPCompat$GetTheOneProbe");
//    }
//
//    public static final class GetTheOneProbe implements Function<ITheOneProbe, Void> {
//
//        @Override
//        public Void apply(ITheOneProbe input) {
//            input.registerProvider(new PastryInfo());
//            return null;
//        }
//    }
//
//    public static final class PastryInfo implements IProbeInfoProvider {
//
//        @Override
//        public String getID() {
//            return Reference.MOD_ID;
//        }
//
//        @Override
//        public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
//            final Block block = world.getBlockState(data.getPos()).getBlock();
//            if (block instanceof BlockCakeBase) {
//                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
//                        .item(new ItemStack(block))
//                        .text(TextFormatting.GREEN + "Bites: ")
//                        .progress(6 - blockState.getValue(BlockCakeBase.BITES), 6);
//            }
//        }
//    }
}
