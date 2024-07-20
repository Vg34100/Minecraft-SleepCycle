package net.vg.sleepcycle.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;

public class SleepCycleModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return MainConfigScreen::new;
    }


    public class MainConfigScreen extends GameOptionsScreen {
        private static final Text TITLE_TEXT = Text.translatable("config.general.title");
        private static final Text SERVER_SETTINGS_TEXT = Text.translatable("config.server.title");
        private static final Text CLIENT_SETTINGS_TEXT = Text.translatable("config.client.title");

        public MainConfigScreen(Screen parent) {
            super(parent, MinecraftClient.getInstance().options, Text.translatable("config.general.title"));
        }

        @Override
        protected void addOptions() {
            // Define client-specific options here
            // List to hold clickable widgets
            List<ClickableWidget> clickableWidgets = new ArrayList<>();

            // Add server settings button
            ButtonWidget serverSettingsButton = ButtonWidget.builder(SERVER_SETTINGS_TEXT, button -> this.client.setScreen(new ServerConfigScreen(this)))
                    .width(150).build();
            clickableWidgets.add(serverSettingsButton);

            // Add client settings button
            ButtonWidget clientSettingsButton = ButtonWidget.builder(CLIENT_SETTINGS_TEXT, button -> this.client.setScreen(new ClientConfigScreen(this)))
                    .width(150).build();
            clickableWidgets.add(clientSettingsButton);

            this.body.addAll(clickableWidgets);
        }

        private static Text getGenericValueText(Text prefix, Text value) {
            return Text.translatable("options.generic_value", prefix, value);
        }
    }
}