package me.taylorkelly.bigbrother.datablock.explosions;

import java.util.List;

import me.taylorkelly.bigbrother.datablock.BBDataBlock;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class CreeperExplosion extends Explosion {

    public CreeperExplosion(String player, Block block, int world) {
        super(Action.CREEPER_EXPLOSION, player, block, world);
    }

    public CreeperExplosion(Block block, int world) {
        super(Action.CREEPER_EXPLOSION, ENVIRONMENT, block, world);
    }

    protected Explosion newInstance(String player, Block block) {
        return new CreeperExplosion(player, block, world);
    }

    public static void create(Location location, List<Block> blockList, int world) {
        for(Block block: blockList) {
            BBDataBlock dataBlock = new CreeperExplosion(ENVIRONMENT, block, world);
            dataBlock.send();
        }
    }

    private CreeperExplosion(String player, int world, int x, int y, int z, int type, String data) {
        super(player, Action.CREEPER_EXPLOSION, world, x, y, z, type, data);
    }

    public static BBDataBlock getBBDataBlock(String player, int world, int x, int y, int z, int type, String data) {
        return new CreeperExplosion(player, world, x, y, z, type, data);
    }

}
