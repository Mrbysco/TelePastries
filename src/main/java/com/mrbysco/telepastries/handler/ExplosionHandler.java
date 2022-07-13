package com.mrbysco.telepastries.handler;

import com.mrbysco.telepastries.blocks.BlockPastryBase;
import com.mrbysco.telepastries.config.TeleConfig;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.ExplosionEvent;

public class ExplosionHandler {
	public static void onExplosion(ExplosionEvent.Detonate event) {
		if (TeleConfig.COMMON.explosionImmune.get()) {
			final Level level = event.getLevel();
			event.getAffectedBlocks().removeIf((pos) -> level.isAreaLoaded(pos, 1) && level.getBlockState(pos).getBlock() instanceof BlockPastryBase);
		}
	}
}
