package com.raptordev.raptor.client.plugin;

public class IPlugin {

   private boolean enabled;

   public void onLoad() {}
   public void onUnload() {}

   public void enable() {
      onLoad();
      this.enabled = true;
   }

   public void disable() {
      onUnload();
      this.enabled = false;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

}
