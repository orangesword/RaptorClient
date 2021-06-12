package com.raptordev.raptor.client.module.modules.misc;

import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.event.events.RenderEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.util.render.RenderUtil;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

@Module.Declaration(name = "New Chunks", category = Category.Exploits, Description = "Highlights newly generated chunks")
public class NewChunks extends Module {

    public BooleanSetting render = registerBoolean("Render", true);
    private List<ChunkData> chunkDataList = new ArrayList<>();
    private ICamera frustum = new Frustum();

    @EventHandler
    private Listener<PacketEvent> PacketEvent = new Listener<>(p_Event ->
    {
        if (p_Event.getPacket() instanceof SPacketChunkData)
        {
            final SPacketChunkData packet = (SPacketChunkData) p_Event.getPacket();
            if (!packet.isFullChunk())
            {
                final ChunkData chunk = new ChunkData(packet.getChunkX() * 16, packet.getChunkZ() * 16);

                if (!this.chunkDataList.contains(chunk))
                {
                    this.chunkDataList.add(chunk);
                }
            }
        }
    });

    @EventHandler
    private Listener<RenderEvent> OnRenderEvent = new Listener<>(p_Event ->
    {
        if (!render.getValue())
            return;

        if (mc.getRenderManager() == null)
            return;

        for (ChunkData chunkData : new ArrayList<ChunkData>(this.chunkDataList))
        {
            if (chunkData != null)
            {
                this.frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);

                final AxisAlignedBB bb = new AxisAlignedBB(chunkData.x, 0, chunkData.z, chunkData.x + 16, 1, chunkData.z + 16);

                if (frustum.isBoundingBoxInFrustum(bb))
                {
                    RenderUtil.drawPlane(chunkData.x - mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY,
                            chunkData.z - mc.getRenderManager().viewerPosZ, new AxisAlignedBB(0, 0, 0, 16, 1, 16), 1, 0xFF9900EE);
                }
            }
        }
    });

    public static class ChunkData
    {
        private int x;
        private int z;

        public ChunkData(int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        public int getX()
        {
            return x;
        }

        public void setX(int x)
        {
            this.x = x;
        }

        public int getZ()
        {
            return z;
        }

        public void setZ(int z)
        {
            this.z = z;
        }
    }

}
