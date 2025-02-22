package makeo.gadomancy.client.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import makeo.gadomancy.client.renderers.item.ItemRenderFamiliar;
import makeo.gadomancy.common.data.DataFamiliar;
import makeo.gadomancy.common.network.packets.PacketFamiliarBolt;
import makeo.gadomancy.common.utils.world.fake.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.client.renderers.entity.RenderWisp;
import thaumcraft.common.entities.monster.EntityWisp;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 12:13
 */
public class FamiliarHandlerClient {

    private static EntityWisp ENTITY_WISP;

    private static RenderWisp fallbackRenderer;

    private static Map<String, ExFamiliarData> clientFamiliars = new HashMap<String, ExFamiliarData>();

    @SideOnly(Side.CLIENT)
    public static void processBoltPacket(PacketFamiliarBolt pkt) {
        EntityPlayer p = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(pkt.owner);
        if (p == null || p.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > 1024) return; // 32^2
        ExFamiliarData data = FamiliarHandlerClient.clientFamiliars.get(p.getCommandSenderName());
        if (data == null) return;
        PartialEntityFamiliar fam = data.familiar;
        float y = (float) (fam.posY + 1.22F);
        if (!Minecraft.getMinecraft().thePlayer.getCommandSenderName().equalsIgnoreCase(pkt.owner)) {
            y += 0.2F;
        }
        FXLightningBolt bolt = new FXLightningBolt(
                Minecraft.getMinecraft().theWorld,
                (float) fam.posX,
                y,
                (float) fam.posZ,
                pkt.targetX,
                pkt.targetY,
                pkt.targetZ,
                Minecraft.getMinecraft().theWorld.rand.nextLong(),
                10,
                4.0F,
                5);
        bolt.defaultFractal();
        bolt.setType(pkt.type);
        bolt.finalizeBolt();
    }

    @SideOnly(Side.CLIENT)
    public static void playerRenderEvent(EntityPlayer player, float partialTicks) {
        String ownerName = player.getCommandSenderName();
        if (!FamiliarHandlerClient.clientFamiliars.containsKey(ownerName)) return;

        ExFamiliarData data = FamiliarHandlerClient.clientFamiliars.get(ownerName);
        PartialEntityFamiliar fam = data.familiar;

        Aspect aspect = Aspect.getAspect(data.data.aspectTag);

        if (FamiliarHandlerClient.ENTITY_WISP == null)
            FamiliarHandlerClient.ENTITY_WISP = new EntityWisp(new FakeWorld());
        FamiliarHandlerClient.ENTITY_WISP.setType(aspect.getTag());
        FamiliarHandlerClient.ENTITY_WISP.ticksExisted = fam.dummyEntity.ticksExisted;
        GL11.glPushMatrix();
        if (fam.owner == null || fam.owner.get() == null) {
            fam.owner = new WeakReference<EntityPlayer>(player);
        }
        EntityPlayer current = Minecraft.getMinecraft().thePlayer;
        double diffX = fam.renderX - current.posX + player.posX;
        double diffY = fam.renderY - current.posY + player.posY + 0.5;
        double diffZ = fam.renderZ - current.posZ + player.posZ;

        String currentPl = current.getCommandSenderName();
        String otherPl = player.getCommandSenderName();

        if (!currentPl.equals(otherPl)) {
            diffY += 1.32;

            EntityLivingBase entity = Minecraft.getMinecraft().renderViewEntity;
            diffX -= ((entity.posX - entity.lastTickPosX) * partialTicks);
            diffY -= ((entity.posY - entity.lastTickPosY) * partialTicks);
            diffZ -= ((entity.posZ - entity.lastTickPosZ) * partialTicks);
        }

        ItemRenderFamiliar.renderEntityWispFor(
                fam.owner.get(), FamiliarHandlerClient.ENTITY_WISP, diffX, diffY, diffZ, 0, partialTicks);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public static void playerTickEvent() {
        if (Minecraft.getMinecraft().theWorld == null) return;
        for (ExFamiliarData data : FamiliarHandlerClient.clientFamiliars.values()) {
            data.familiar.tick();
        }
        PartialEntityFamiliar.DUMMY_FAMILIAR.tick();
    }

    static {
        FamiliarHandlerClient.fallbackRenderer = new RenderWisp();
        FamiliarHandlerClient.fallbackRenderer.setRenderManager(RenderManager.instance);
    }

    public static void handleAdditions(List<DataFamiliar.FamiliarData> toAdd) {
        for (DataFamiliar.FamiliarData data : toAdd) {
            PartialEntityFamiliar familiar = new PartialEntityFamiliar(
                    Minecraft.getMinecraft().theWorld.getPlayerEntityByName(data.owner), data.owner);
            ExFamiliarData exData = new ExFamiliarData(data, familiar);
            FamiliarHandlerClient.clientFamiliars.put(data.owner, exData);
        }
    }

    public static void handleRemovals(List<DataFamiliar.FamiliarData> toRemove) {
        for (DataFamiliar.FamiliarData data : toRemove) {
            FamiliarHandlerClient.clientFamiliars.remove(data.owner);
        }
    }

    public static class ExFamiliarData {

        public final DataFamiliar.FamiliarData data;
        public final PartialEntityFamiliar familiar;

        public ExFamiliarData(DataFamiliar.FamiliarData data, PartialEntityFamiliar familiar) {
            this.data = data;
            this.familiar = familiar;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;

            ExFamiliarData that = (ExFamiliarData) o;

            return (this.data != null ? this.data.equals(that.data) : that.data == null)
                    && (this.familiar != null ? this.familiar.equals(that.familiar) : that.familiar == null);
        }

        @Override
        public int hashCode() {
            int result = this.data != null ? this.data.hashCode() : 0;
            result = 31 * result + (this.familiar != null ? this.familiar.hashCode() : 0);
            return result;
        }
    }

    public static class DummyEntityFamiliar extends Entity {

        public DummyEntityFamiliar(World p_i1582_1_) {
            super(p_i1582_1_);
        }

        @Override
        protected void entityInit() {}

        @Override
        protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}

        @Override
        protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}
    }

    public static class PartialEntityFamiliar {

        public static final PartialEntityFamiliar DUMMY_FAMILIAR = new PartialEntityFamiliar(null, "§Invalid§");

        private static final double RAD_CAP = 2 * Math.PI;
        private static final int CRICLE_TIME_TICKS = 160;
        private static final double RADIUS = 1;

        public DummyEntityFamiliar dummyEntity = new DummyEntityFamiliar(null);
        public WeakReference<EntityPlayer> owner;
        public String potentialOwnerName;
        public double posX, posY, posZ;
        public double renderX, renderY, renderZ;
        public double prevPosX, prevPosY, prevPosZ;
        public int ticksExisted;

        public PartialEntityFamiliar(EntityPlayer owner, String ownerNameToSearch) {
            this.owner = new WeakReference<EntityPlayer>(owner);
            this.potentialOwnerName = ownerNameToSearch;
        }

        public void tick() {
            this.ticksExisted++;
            this.dummyEntity.ticksExisted = this.ticksExisted;

            if (this.owner.get() == null) {
                if (this.potentialOwnerName != null) {
                    EntityPlayer player =
                            Minecraft.getMinecraft().theWorld.getPlayerEntityByName(this.potentialOwnerName);
                    if (player != null) {
                        this.owner = new WeakReference<EntityPlayer>(player);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                if (this.owner.get().worldObj.provider.dimensionId
                        != Minecraft.getMinecraft().renderViewEntity.worldObj.provider.dimensionId) {
                    this.owner = new WeakReference<EntityPlayer>(null);
                    return;
                }
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            double part = ((double) this.ticksExisted) / ((double) PartialEntityFamiliar.CRICLE_TIME_TICKS);
            double theta = PartialEntityFamiliar.RAD_CAP * part;
            this.renderX = PartialEntityFamiliar.RADIUS * Math.cos(theta);
            this.renderZ = PartialEntityFamiliar.RADIUS * Math.sin(theta);
            this.renderY = Math.sin(theta * 2) / 2D;

            this.posX = this.owner.get().posX + this.renderX;
            this.posZ = this.owner.get().posZ + this.renderZ;
            this.posY = this.owner.get().posY + this.renderY;
        }
    }
}
