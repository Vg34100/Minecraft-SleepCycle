package net.vg.sleepcycle.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class ServerConfigScreen extends GameOptionsScreen {

    public ServerConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("config.server.title"));
    }

    @Override
    protected void addOptions() {
        // Boolean Options
        SimpleOption<Boolean> allowDaySleeping = SimpleOption.ofBoolean(
                "sleep.allow.day.sleep",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.allow.day.sleep")),
                ModConfigs.ALLOW_DAY_SLEEPING,
                value -> ModConfigs.ALLOW_DAY_SLEEPING = value
        );

        SimpleOption<Boolean> grantBuffs = SimpleOption.ofBoolean(
                "sleep.grant.buffs",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.grant.buffs")),
                ModConfigs.GRANT_BUFFS,
                value -> ModConfigs.GRANT_BUFFS = value
        );

        SimpleOption<Boolean> doRegen = SimpleOption.ofBoolean(
                "sleep.do.regeneration",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.do.regeneration")),
                ModConfigs.DO_REGEN,
                value -> ModConfigs.DO_REGEN = value
        );

        SimpleOption<Boolean> changeTickSpeed = SimpleOption.ofBoolean(
                "sleep.change.tick.speed",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.change.tick.speed")),
                ModConfigs.CHANGE_TICK_SPEED,
                value -> ModConfigs.CHANGE_TICK_SPEED = value
        );

        // Integer Options
        SimpleOption<Integer> wellRestedWait = new SimpleOption<>(
                "sleep.well.rested.wait",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.well.rested.wait")),
                (optionText, value) -> getGenericValueText(optionText, Text.literal(String.valueOf(value))),
                new SimpleOption.ValidatingIntSliderCallbacks(0, 1000),  // Adjust range as needed
                ModConfigs.WELL_RESTED_WAIT,
                value -> ModConfigs.WELL_RESTED_WAIT = value
        );

        SimpleOption<Integer> tiredWait = new SimpleOption<>(
                "sleep.tired.wait",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.tired.wait")),
                (optionText, value) -> getGenericValueText(optionText, Text.literal(String.valueOf(value))),
                new SimpleOption.ValidatingIntSliderCallbacks(0, 1000),  // Adjust range as needed
                ModConfigs.TIRED_WAIT,
                value -> ModConfigs.TIRED_WAIT = value
        );

        SimpleOption<Integer> wellRestedLength = new SimpleOption<>(
                "sleep.well.rested.length",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.well.rested.length")),
                (optionText, value) -> getGenericValueText(optionText, Text.literal(String.valueOf(value))),
                new SimpleOption.ValidatingIntSliderCallbacks(0, 3000),  // Adjust range as needed
                ModConfigs.WELL_RESTED_LENGTH,
                value -> ModConfigs.WELL_RESTED_LENGTH = value
        );

        SimpleOption<Integer> tiredLength = new SimpleOption<>(
                "sleep.tired.length",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.tired.length")),
                (optionText, value) -> getGenericValueText(optionText, Text.literal(String.valueOf(value))),
                new SimpleOption.ValidatingIntSliderCallbacks(0, 3000),  // Adjust range as needed
                ModConfigs.TIRED_LENGTH,
                value -> ModConfigs.TIRED_LENGTH = value
        );

        // Double Options
        SimpleOption<Integer> daySkipSpeed = new SimpleOption<>(
                "sleep.day.skip.speed",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.day.skip.speed")),
                (optionText, value) -> getGenericValueText(optionText, Text.literal(String.valueOf(value))),
                new SimpleOption.ValidatingIntSliderCallbacks(0, 200),
                (int) ModConfigs.DAY_SKIP_SPEED,
                value -> ModConfigs.DAY_SKIP_SPEED = (double) value
        );

        SimpleOption<Double> sleepTickSpeed = new SimpleOption<>(
                "sleep.sleep.tick.speed",
                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.sleep.tick.speed")),
                (optionText, value) -> Text.translatable("options.generic_value",
                        optionText,
                        Text.translatable("options.multiplier", String.format("%.1f", value))
                ),
                (new SimpleOption.ValidatingIntSliderCallbacks(0, 300)).withModifier(
                        (sliderProgressValue) -> (double)sliderProgressValue / 100.0,
                        (value) -> (int)(value * 100.0)
                ),
                Codec.doubleRange(0.0, 3.0),
                1.0, // Default value (1.0x)
                (value) -> ModConfigs.SLEEP_TICK_SPEED = value
        );


// Good example for percentage based one
//        SimpleOption<Double> sleepTickSpeed = new SimpleOption<>(
//                "sleep.sleep.tick.speed",
//                SimpleOption.constantTooltip(Text.translatable("tooltip.sleep.sleep.tick.speed")),
//                (optionText, value) -> Text.translatable("options.percent_value", optionText, (int)(value * 100)),
//                (new SimpleOption.ValidatingIntSliderCallbacks(0, 300)).withModifier(
//                        (sliderProgressValue) -> (double)sliderProgressValue / 100.0,
//                        (value) -> (int)(value * 100.0)
//                ),
//                Codec.doubleRange(0.0, 3.0),
//                1.0, // Default value (1.0 -> 100%)
//                (value) -> ModConfigs.SLEEP_TICK_SPEED = value
//        );



        SimpleOption<?>[] options = new SimpleOption[]{
                allowDaySleeping,
                grantBuffs,
                doRegen,
                changeTickSpeed,
                wellRestedWait,
                tiredWait,
                wellRestedLength,
                tiredLength,
                daySkipSpeed,
                sleepTickSpeed
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
