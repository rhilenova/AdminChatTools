package space.rhilenova.mc.admin_chat_tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class GuiRenderer
{
    protected Minecraft mc;
    protected boolean replace_gui;
    protected List chat_messages = null;
    protected GuiNewChat chat_gui = null;
    protected int chat_scroll = 0;
    protected boolean is_scrolled = false;

    protected static final int CHAT_HEIGHT = (-19 * 9) - 9;

    public GuiRenderer(Minecraft mcIn)
    {
        mc = mcIn;
        replace_gui = true;
    }

    public void drawChat(int update_counter, int posX, int posY)
    {
        // Push chat gui start location onto GL state matrix.
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, 0.0F);

        chat_gui = mc.ingameGUI.getChatGUI();

        try
        {
            Field private_scroll_pos = GuiNewChat.class.getDeclaredField("scrollPos");
            private_scroll_pos.setAccessible(true);
            chat_scroll = (Integer) private_scroll_pos.get(chat_gui);

            Field private_is_scrolled = GuiNewChat.class.getDeclaredField("isScrolled");
            private_is_scrolled.setAccessible(true);
            is_scrolled = (Boolean) private_is_scrolled.get(chat_gui);
        } catch (NoSuchFieldException nsfe)
        {
            replace_gui = false;
            return;
        } catch (IllegalAccessException iae)
        {
            replace_gui = false;
            return;
        }

        if (chat_messages == null)
        {
            // Get handles to private info of GuiNewChat. Stop intercepting
            // chat render requests if this fails (fall back to vanilla).
            try
            {
                Field private_chat_messages = GuiNewChat.class.getDeclaredField("field_146253_i");
                private_chat_messages.setAccessible(true);
                chat_messages = (List) private_chat_messages.get(chat_gui);
            } catch (NoSuchFieldException nsfe)
            {
                replace_gui = false;
                return;
            } catch (IllegalAccessException iae)
            {
                replace_gui = false;
                return;
            }
        }

        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            float opacity = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (this.chat_messages.size() > 0)
            {
                // Push GUI scale onto GL matrix
                float chat_scale = chat_gui.getChatScale();
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(chat_scale, chat_scale, 1.0F);
                int num_visible_messages = 0;

                for (int msg_pos = 0; msg_pos + this.chat_scroll < this.chat_messages.size() && msg_pos < chat_gui.getLineCount(); ++msg_pos)
                {
                    ChatLine chatline = (ChatLine) this.chat_messages.get(msg_pos + this.chat_scroll);

                    if (chatline != null)
                    {
                        int ticks_alive = update_counter - chatline.getUpdatedCounter();

                        // If should draw chat line, do so
                        if (ticks_alive < 200 || chat_gui.getChatOpen())
                        {
                            ++num_visible_messages;

                            // Get opacity modifier
                            double mod;
                            if (chat_gui.getChatOpen())
                            {
                                mod = 255.0D;
                            } else
                            {
                                mod = MathHelper.clamp_double((1.0D - (ticks_alive / 200.0D)) * 10, 0.0D, 1.0D);
                                mod *= mod;
                                mod = 255.0D * mod;
                            }

                            // Calculate actual opacity
                            int actual_opacity = (int) (mod * opacity);

                            // If chatline is visible, draw.
                            if (actual_opacity > 3)
                            {
                                int msg_x = -msg_pos * 9;
                                int width = MathHelper.ceiling_float_int((float) chat_gui.getChatWidth() / chat_gui.getChatScale());
                                // Change the background color based on highlight status
                                int color;
                                if (ShouldHaveHighlight(chatline))
                                {
                                    // TODO Change this color based on config
                                    color = (actual_opacity / 2 << 24) | 0xAAAA00;
                                }
                                else
                                {
                                    color = actual_opacity / 2 << 24;
                                }
                                GuiNewChat.drawRect(0, msg_x - 9, width + 4, msg_x, color);
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                this.mc.fontRendererObj.drawStringWithShadow(s, (float) 0, (float) (msg_x - 8), 16777215 + (actual_opacity << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                // If chat menu is open, draw scrollbar
                if (chat_gui.getChatOpen())
                {

                    if (num_visible_messages != this.chat_messages.size())
                    {
                        GlStateManager.translate(-3.0F, 0.0F, 0.0F);

                        int chat_size = this.chat_messages.size();

                        float bottom_pct = (float) chat_scroll / chat_size;
                        int bottom = (int)(bottom_pct * CHAT_HEIGHT);
                        bottom = MathHelper.clamp_int(bottom, CHAT_HEIGHT, 0);

                        float top_pct = (float) (chat_scroll + num_visible_messages) / chat_size;
                        int top = (int)(top_pct * CHAT_HEIGHT);
                        top = MathHelper.clamp_int(top, CHAT_HEIGHT, 0);

                        GuiNewChat.drawRect(0, top, 2, bottom, 0xFFFFFFFF);
                    }
                }

                // Pop GUI scale
                GlStateManager.popMatrix();
            }
        }

        // Pop chat gui start location
        GlStateManager.popMatrix();
    }

    protected boolean ShouldHaveHighlight(ChatLine line)
    {
        // TODO load this from config
        final String[] special_words_a = {"staff"};
        final List<String> special_words = Arrays.asList(special_words_a);
        boolean found = false;
        for (String word : special_words)
        {
            if (line.getChatComponent().getUnformattedText().contains(word))
            {
                found = true;
                break;
            }
        }
        return found;
    }

    @SubscribeEvent
    public void RenderGameOverlayEvent(RenderGameOverlayEvent event)
    {
        // Handle only chat pre-events
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT && event.isCancelable())
        {
            // Intercept and call custom gui script if no errors.
            if (replace_gui)
            {
                RenderGameOverlayEvent.Chat chat_event = (RenderGameOverlayEvent.Chat) event;
                event.setCanceled(true);
                this.drawChat(this.mc.ingameGUI.getUpdateCounter(), chat_event.posX, chat_event.posY);
            }
        }
    }
}
