package ic2.advancedmachines.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.advancedmachines.blocks.BlockAdvancedBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderAdvBlock implements ISimpleBlockRenderingHandler {

    public static final int renderId = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks) {
        Tessellator tess = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        block.setBlockBoundsForItemRender();
        renderblocks.setRenderBoundsFromBlock(block);
        tess.startDrawingQuads();

        // Bottom
        tess.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, renderblocks.getBlockIconFromSideAndMetadata(block, 0, metadata));
        // Top
        tess.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0, 0.0, 0.0, renderblocks.getBlockIconFromSideAndMetadata(block, 1, metadata));
        // North
        tess.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, renderblocks.getBlockIconFromSideAndMetadata(block, 2, metadata));
        // South
        tess.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceZPos(block, 0.0, 0.0, 0.0, renderblocks.getBlockIconFromSideAndMetadata(block, 3, metadata));
        // West
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, renderblocks.getBlockIconFromSideAndMetadata(block, 4, metadata));
        // East
        tess.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXPos(block, 0.0, 0.0, 0.0, renderblocks.getBlockIconFromSideAndMetadata(block, 5, metadata));

        // 1 draw call
        tess.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderblocks) {
        ((BlockAdvancedBlock) block).onRender(blockAccess, x, y, z);
        return renderblocks.renderStandardBlock(block, x, y, z);
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }
}
