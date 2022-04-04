package com.mrbysco.telepastries.handler;

import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.world.ExplosionEvent;

public class ExplosionHandler {
	public static void onExplosion(ExplosionEvent.Detonate event) {
		if (TeleConfig.COMMON.explosionImmune.get()) {
			Level level = event.getWorld();
			event.getAffectedBlocks().removeIf((pos) -> level.isAreaLoaded(pos, 1) && level.getBlockState(pos).getBlock() instanceof BlockPastryBase);
		}
	}
}
