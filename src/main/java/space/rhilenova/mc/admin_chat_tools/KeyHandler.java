package space.rhilenova.mc.admin_chat_tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler
{
    private KeyBinding ping;
    private Minecraft mc;

    KeyHandler(Minecraft mc)
    {
        this.mc = mc;
        this.ping = new KeyBinding("key.ping", Keyboard.KEY_G, "key.categories.admin_chat_tools");
        ClientRegistry.registerKeyBinding(this.ping);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if(this.ping.isPressed()) this.mc.displayGuiScreen(new GuiConfigure());
    }
}
