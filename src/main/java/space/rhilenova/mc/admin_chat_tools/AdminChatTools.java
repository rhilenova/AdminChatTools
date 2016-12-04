package space.rhilenova.mc.admin_chat_tools;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = AdminChatTools.MODID, version = AdminChatTools.VERSION, clientSideOnly = true)
public class AdminChatTools
{
    public static final String MODID = "rn_adm_tools";
    public static final String VERSION = "0.1";

    private static final GuiRenderer gui_renderer = new GuiRenderer(Minecraft.getMinecraft());

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(gui_renderer);
    }
}
