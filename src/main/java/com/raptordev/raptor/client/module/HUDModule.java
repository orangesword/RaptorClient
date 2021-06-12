package com.raptordev.raptor.client.module;

import com.raptordev.raptor.client.RaptorClient;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.theme.Theme;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lukflug
 */

public abstract class HUDModule extends Module {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Declaration {
        int posX();

        int posZ();
    }

    private Declaration getDeclaration() {
        return getClass().getAnnotation(Declaration.class);
    }

    protected FixedComponent component;
    protected Point position = new Point(getDeclaration().posX(), getDeclaration().posZ());

    public abstract void populate(Theme theme);

    public FixedComponent getComponent() {
        return component;
    }

    public void resetPosition() {
        component.setPosition(RaptorClient.INSTANCE.raptorGUI.guiInterface, position);
    }
}