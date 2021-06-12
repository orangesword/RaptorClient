package com.raptordev.raptor.client.module.modules.client.hud;

import com.raptordev.raptor.api.setting.values.ColorSetting;
import com.raptordev.raptor.api.util.render.RCColor;
import com.raptordev.raptor.client.module.Category;
import com.raptordev.raptor.client.module.HUDModule;
import com.raptordev.raptor.client.module.Module;
import com.lukflug.panelstudio.hud.HUDList;
import com.lukflug.panelstudio.hud.ListComponent;
import com.lukflug.panelstudio.theme.Theme;

import java.awt.*;
import java.util.Calendar;

@Module.Declaration(name = "Welcomer", category = Category.HUD,toggleMsg = false)
@HUDModule.Declaration(posX = 450, posZ = 0)
public class Welcomer extends HUDModule {

    Calendar c = Calendar.getInstance();
    int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

    private ColorSetting color = registerColor("Color", new RCColor(255, 0, 0, 255));

    @Override
    public void populate(Theme theme) {
        component = new ListComponent(getName(), theme.getPanelRenderer(), position, new WelcomerList());
    }

    private class WelcomerList implements HUDList {

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String getItem(int index) {
            if (timeOfDay >= 6 && timeOfDay < 12)
            {
                return "Good Morning, " + mc.getSession().getUsername();
            }
            else if (timeOfDay >= 12 && timeOfDay < 17)
            {
                return "Good Afternoon, " + mc.getSession().getUsername();
            }
            else if (timeOfDay >= 17 && timeOfDay < 22)
            {
                return "Good Evening, " + mc.getSession().getUsername();
            }
            else if (timeOfDay >= 22 || timeOfDay < 6)
            {
                return "Good Night, " + mc.getSession().getUsername();
            }
            else
            {
                return "Hello, " + mc.getSession().getUsername();
            }
        }

        @Override
        public Color getItemColor(int index) {
            return color.getValue();
        }

        @Override
        public boolean sortUp() {
            return false;
        }

        @Override
        public boolean sortRight() {
            return false;
        }
    }
}