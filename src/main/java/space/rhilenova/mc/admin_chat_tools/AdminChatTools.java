package space.rhilenova.mc.admin_chat_tools;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = AdminChatTools.MODID, version = AdminChatTools.VERSION, clientSideOnly = true)
public class AdminChatTools
{
    static final String MODID = "rn_adm_tools";
    static final String VERSION = "0.1";

    private static final GuiRenderer gui_renderer = new GuiRenderer(Minecraft.getMinecraft());

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        String[] special_words = config.getStringList("special_words", Configuration.CATEGORY_GENERAL,
                new String[]{}, "A case insensitve set of words to highlight in chat.");
        config.save();

        gui_renderer.set_special_words(special_words);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(gui_renderer);
        FMLCommonHandler.instance().bus().register(new KeyHandler(Minecraft.getMinecraft()));
    }
}
