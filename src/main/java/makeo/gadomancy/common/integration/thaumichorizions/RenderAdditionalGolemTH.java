package makeo.gadomancy.common.integration.thaumichorizions;

import com.kentington.thaumichorizons.client.renderer.entity.RenderGolemTH;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.client.renderers.entity.RenderGolemHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 07.10.2015 13:09
 */
@SideOnly(Side.CLIENT)
public class RenderAdditionalGolemTH extends RenderGolemTH {
    private ItemStack toolItem;

    public RenderAdditionalGolemTH(ModelBase mainModel) {
        super(mainModel);
    }

    protected int shouldRenderPass(EntityLivingBase entity, int pass, float par3) {
        if (pass != 0) {
            return super.shouldRenderPass(entity, pass, par3);
        }

        EntityGolemBase golem = (EntityGolemBase) entity;

        AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
        if (core != null) {
            golem.getDataWatcher().getWatchedObject(21).setObject((byte) -1);
        }

        int ret = super.shouldRenderPass(entity, pass, par3);

        // fix for render bug in tc
        if (RenderGolemHelper.requiresRenderFix((EntityGolemBase) entity)) {
            RenderGolemHelper.renderCarriedItemsFix(golem);

            if (this.toolItem != null) {
                RenderGolemHelper.renderToolItem(golem, this.toolItem, this.mainModel, this.renderManager);
            }
        }

        if (core != null) {
            golem.getDataWatcher().getWatchedObject(21).setObject((byte) 1);
            RenderGolemHelper.renderCore(golem, core);
        }

        return ret;
    }

    @Override
    protected void renderCarriedItems(EntityGolemBase golem, float par2) {
        if (!RenderGolemHelper.requiresRenderFix(golem)) {
            if (this.toolItem != null) {
                RenderGolemHelper.renderToolItem(golem, this.toolItem, this.mainModel, this.renderManager);
            }
            super.renderCarriedItems(golem, par2);
        }
    }

    @Override
    public void render(EntityGolemBase golem, double par2, double par4, double par6, float par8, float par9) {
        AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
        ItemStack carriedItem = null;
        if (core != null) {
            this.toolItem = core.getToolItem(golem);

            carriedItem = golem.getCarriedForDisplay();
            golem.getDataWatcher().getWatchedObject(16).setObject(null);
        }

        super.render(golem, par2, par4, par6, par8, par9);

        if (carriedItem != null) {
            golem.getDataWatcher().getWatchedObject(16).setObject(carriedItem);
            this.toolItem = null;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        AdditionalGolemType type = GadomancyApi.getAdditionalGolemType(((EntityGolemBase) entity).getGolemType());
        if (type != null) {
            return type.getEntityTexture();
        }
        return super.getEntityTexture(entity);
    }
}
