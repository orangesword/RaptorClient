package com.raptordev.raptor.client.module.modules.client;

import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.Module;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Module.Declaration(name = "BeMyFrined", category = Category.CLIENT, Description = "I need to feend my 69 kinds please star github repostary")
public class BeMyFriend extends Module {

    protected void onEnable() {
        try {
            URL url = new URL("https://github.com/RaptorClientDevelopment/RaptorClient");
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
