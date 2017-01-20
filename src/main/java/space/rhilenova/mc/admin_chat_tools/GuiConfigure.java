package space.rhilenova.mc.admin_chat_tools;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiConfigure extends GuiScreen
{
    private GuiTextField group_name_field;
    private GuiTextField pattern_field;
    private GuiTextField fore_field;
    private GuiTextField back_field;

    public void initGui()
    {
        int x = 0;
        this.buttonList.clear();
        this.group_name_field = new GuiTextField(x++, this.fontRendererObj, 115, 28, 172, 20);
        this.buttonList.add(new GuiButton(x++, 291, 28, 20, 20, "+"));
        this.pattern_field = new GuiTextField(x++, this.fontRendererObj, 15, 52, 172, 20);
        this.buttonList.add(new GuiButton(x++, 191, 52, 20, 20, "+"));
        this.fore_field = new GuiTextField(x++, this.fontRendererObj, 216, 52, 96, 20);
        this.back_field = new GuiTextField(x++, this.fontRendererObj, 316, 52, 96, 20);
        this.buttonList.add(new GuiButton(x++, 111, 205, 200, 20, "Done"));
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
    }

    public void updateScreen()
    {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Notification Settings", this.width / 2, 15, 16777215);
        this.drawString(this.fontRendererObj, "Groups", 15, 76, 16777215);
        this.drawString(this.fontRendererObj, "Tokens", 216, 76, 16777215);
        this.group_name_field.drawTextBox();
        this.pattern_field.drawTextBox();
        this.fore_field.drawTextBox();
        this.back_field.drawTextBox();
        this.drawGradientRect(15, 85, 210, 200, 0xFF000000, 0xFF000000);
        this.drawGradientRect(216, 85, 411, 200, 0xFF000000, 0xFF000000);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
