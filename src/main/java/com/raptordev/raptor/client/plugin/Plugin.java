package com.raptordev.raptor.client.plugin;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {

    String name();
    String Description();
    boolean DefaultEnabled() default true;
    double Version();
    String[] authors();
    String url() default "";

    class Main {
        public Plugin getPlugin() {
            return getClass().getAnnotation(Plugin.class);
        }

        public static Main instance;

        public Main() {
            instance = this;
        }

        public String Name = getPlugin().name();
        public String Description = getPlugin().Description();
        public Boolean DefaultEnabled = getPlugin().DefaultEnabled();
        public String[] authors = getPlugin().authors();
        public double Version = getPlugin().Version();

    }

}
