package space.rhilenova.mc.admin_chat_tools;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiConfigure extends GuiScreen
{
    public void initGui()
    {
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
    }

    public void updateScreen()
    {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawCenteredString(this.fontRendererObj, "I'm working!", this.width / 2, 40, 16777215);
    }
}
