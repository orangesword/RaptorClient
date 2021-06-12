package com.raptordev.raptor.client.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.raptordev.raptor.api.event.events.PacketEvent;
import com.raptordev.raptor.api.setting.values.BooleanSetting;
import com.raptordev.raptor.api.util.misc.MessageBus;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;

@Module.Declaration(name = "Anti for Chat", category = Category.Misc)
public class AntiChat extends Module {

    BooleanSetting AntiDiscord = registerBoolean("Anti Discord", true);
    BooleanSetting AntiEz = registerBoolean("Anti Ez", true);
    BooleanSetting NameHighlight = registerBoolean("NameHighlight", true);


    @EventHandler
    private Listener<PacketEvent> PacketEvent = new Listener<>(p_Event ->
    {
        if (p_Event.getPacket() instanceof SPacketChat)
        {
            final SPacketChat packet = (SPacketChat) p_Event.getPacket();

            if (packet.getChatComponent() instanceof TextComponentString)
            {
                final TextComponentString component = (TextComponentString) packet.getChatComponent();

                String date = "";


                if (component.getFormattedText().contains("> "))
                {
                    String l_Text = component.getFormattedText().substring(component.getFormattedText().indexOf("> "));

                    if (l_Text.toLowerCase().contains("ez") && AntiEz.getValue())
                        p_Event.cancel();

                    if (AntiDiscord.getValue() && l_Text.toLowerCase().contains("discord"))
                        p_Event.cancel();

                    if (p_Event.isCancelled())
                        return;
                }

                String l_Text = component.getFormattedText();

                if (NameHighlight.getValue() && mc.player != null)
                {
                    if (l_Text.toLowerCase().contains(mc.player.getName().toLowerCase()))
                    {
                        l_Text = l_Text.replaceAll("(?i)" + mc.player.getName(), ChatFormatting.GOLD + mc.player.getName() + ChatFormatting.RESET);
                        p_Event.cancel();
                        MessageBus.sendClientRawMessage(l_Text);
                    }
                }
            }
        }
    });

}
