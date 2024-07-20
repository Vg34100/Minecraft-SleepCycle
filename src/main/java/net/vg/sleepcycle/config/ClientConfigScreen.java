package net.vg.sleepcycle.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class ClientConfigScreen extends GameOptionsScreen {

    public ClientConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("config.client.title"));
    }

    @Override
    protected void addOptions() {
        // Define client-specific options here

        SimpleOption<Boolean> changeCameraPos = SimpleOption.ofBoolean(
                "sleep.change.camera.pos",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.change.camera.pos")), // Tooltip for changeCameraPos
                ModConfigs.CHANGE_CAMERA_POS,
                value -> ModConfigs.CHANGE_CAMERA_POS = value
        );

        SimpleOption<Integer> sleepButtonHeight = new SimpleOption<>(
                "sleep.sleep.button.height",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.sleep.button.height")),
                (optionText, value) -> getGenericValueText(optionText, Text.literal(String.valueOf(value))),
                new SimpleOption.ValidatingIntSliderCallbacks(0, 50),
                (int) ModConfigs.SLEEP_BUTTON_HEIGHT,
                value -> ModConfigs.SLEEP_BUTTON_HEIGHT = value
        );

        SimpleOption<?>[] options = new SimpleOption[]{
                changeCameraPos,
                sleepButtonHeight
                // Add more options here as needed
        };

        this.body.addAll(options);
    }
    @Override
    public void close() {
        super.close();
        ModConfigs.saveConfigs();
    }
    private static Text getGenericValueText(Text prefix, Text value) {
        return Text.translatable("options.generic_value", prefix, value);
    }
}
