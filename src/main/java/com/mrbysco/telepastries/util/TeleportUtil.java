package com.mrbysco.telepastries.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportUtil {

    /*
    * Borrowed from Bookshelf#PlayerUtils
     * */
    public static void changeDimension(EntityPlayerMP player, int dimension, PlayerList playerData) {
        if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(player, dimension)) {
            return;
        }

        final int oldDim = player.dimension;
        final boolean wasAlive = player.isEntityAlive();
        final WorldServer worldOld = playerData.getServerInstance().getWorld(player.dimension);
        final WorldServer worldNew = playerData.getServerInstance().getWorld(dimension);

        if (player.isBeingRidden()) {

            player.removePassengers();
        }

        if (player.isRiding()) {

            player.dismountRidingEntity();
        }

        player.dimension = dimension;
        player.connection.sendPacket(new SPacketRespawn(player.dimension, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
        worldOld.removeEntityDangerously(player);

        changeWorld(player, worldOld, worldNew);
        playerData.preparePlayer(player, worldOld);
        player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.interactionManager.setWorld(worldNew);
        playerData.updateTimeAndWeatherForPlayer(player, worldNew);
        playerData.syncPlayerInventory(player);

        if (player.isDead && wasAlive) {

            player.isDead = false;
        }

        for (final PotionEffect potioneffect : player.getActivePotionEffects()) {

            player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
        }

        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }

    public static void changeWorld (Entity entity, WorldServer worldOld, WorldServer worldNew) {

        final WorldProvider providerOld = worldOld.provider;
        final WorldProvider providerNew = worldNew.provider;
        final double moveFactor = providerOld.getMovementFactor() / providerNew.getMovementFactor();
        final double x = MathHelper.clamp(entity.posX * moveFactor, -29999872, 29999872);
        final double z = MathHelper.clamp(entity.posZ * moveFactor, -29999872, 29999872);

        if (entity.isEntityAlive()) {

            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            worldNew.spawnEntity(entity);
            worldNew.updateEntityWithOptionalForce(entity, false);
        }

        entity.setWorld(worldNew);
    }
}
