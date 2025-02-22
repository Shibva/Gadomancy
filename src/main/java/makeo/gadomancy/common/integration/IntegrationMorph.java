package makeo.gadomancy.common.integration;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Method;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 09.07.2015 14:45
 */
public class IntegrationMorph extends IntegrationMod {
    private static final Injector INJECTOR = new Injector();

    private Method methodHasMorph;

    @Override
    public String getModId() {
        return "Morph";
    }

    @Override
    protected void doInit() {
        // methodHasMorph = Injector.getMethod("hasMorph", "morph.common.core.ApiHandler", String.class, Boolean.class);

        // if(methodHasMorph == null) {
        //    FMLLog.severe("Morph mod is loaded but failed to find method \"hasMorph\"!");
        // }
    }

    @SideOnly(Side.CLIENT)
    public boolean isMorphed() {
        if (this.methodHasMorph != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            Boolean result =
                    IntegrationMorph.INJECTOR.invokeMethod(this.methodHasMorph, player.getCommandSenderName(), true);
            return result != null ? result : false;
        }
        return false;
    }
}
