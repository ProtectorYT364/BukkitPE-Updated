package net.BukkitPE.block;

import net.BukkitPE.entity.Entity;
import net.BukkitPE.item.Item;
import net.BukkitPE.item.ItemTool;
import net.BukkitPE.utils.BlockColor;

/**
 * Created on 2015/12/2 by xtypr.
 * Package net.BukkitPE.block in project BukkitPE .
 */
public class BlockCobweb extends BlockFlowable {
    public BlockCobweb() {
        this(0);
    }

    public BlockCobweb(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Cobweb";
    }

    @Override
    public int getId() {
        return COBWEB;
    }

    @Override
    public double getHardness() {
        return 4;
    }

    @Override
    public double getResistance() {
        return 20;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SWORD;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isShears() || item.isSword()) {
            return new int[][]{
                    {Item.STRING, 0, 1}
            };
        } else {
            return new int[0][0];
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLOTH_BLOCK_COLOR;
    }
}
