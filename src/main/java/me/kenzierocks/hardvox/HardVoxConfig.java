package me.kenzierocks.hardvox;

import static com.google.common.base.Preconditions.checkNotNull;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = HardVox.MODID)
@Config.LangKey(HardVox.MODID + ".config.title.general")
public final class HardVoxConfig {

    @Config.Comment("The item to use for the selection wand.")
    @Config.LangKey(HardVox.MODID + ".config.entry.wand.selection")
    public static String selectionWand = Items.WOODEN_AXE.getRegistryName().toString();

    public static Item getSelectionWand() {
        return checkNotNull(Item.getByNameOrId(selectionWand), "invalid wand set: %s", selectionWand);
    }

    @Config.LangKey(HardVox.MODID + ".config.title.operations")
    public static final Operations operations = new Operations();

    public static class Operations {

        @Config.Comment("Number of operations per tick, per session.")
        @Config.RangeInt(min = 0)
        public int operationsPerTick = 10000;

        private Operations() {
        }

    }

    @Mod.EventBusSubscriber(modid = HardVox.MODID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has
         * been changed from the GUI.
         *
         * @param event
         *            The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(HardVox.MODID)) {
                if (!validateConfig()) {
                    event.setResult(Result.DENY);
                    return;
                }
                ConfigManager.sync(HardVox.MODID, Config.Type.INSTANCE);
            }
        }

        private static boolean validateConfig() {
            if (!validateItem(selectionWand)) {
                return false;
            }
            return true;
        }

        private static boolean validateItem(String item) {
            return Item.REGISTRY.containsKey(new ResourceLocation(item));
        }
    }

}