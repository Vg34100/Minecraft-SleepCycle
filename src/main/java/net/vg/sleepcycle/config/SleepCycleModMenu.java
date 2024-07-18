package net.vg.sleepcycle.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class SleepCycleModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CustomConfigScreen::new;
    }

    private static class CustomConfigScreen extends Screen {

        private final Screen parent;
        private final List<Object> configWidgets = new ArrayList<>();
        private ScrollableWidget scrollableArea;

        private int buttonWidth = 150;
        private int buttonHeight = 20;
        private int buttonSpacing = 10;

        protected CustomConfigScreen(Screen parent) {
            super(Text.literal("Sleep Cycle Options"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            super.init();
            configWidgets.clear();

            // Add header text
            this.addDrawable(new TextWidget(this.width / 2 - 75, 20, 150, 20, Text.literal("Sleep Cycle Options"), this.textRenderer).alignCenter());

            // Example row with two buttons
            // Boolean configs (using toggle buttons)
            addToggleButton("sleep.allow.day.sleep", ModConfigs.ALLOW_DAY_SLEEPING, "sleep.allow.day.sleep");
            addToggleButton("sleep.change.tick.speed", ModConfigs.CHANGE_TICK_SPEED, "sleep.change.tick.speed");
            addToggleButton("sleep.change.camera.pos", ModConfigs.CHANGE_CAMERA_POS, "sleep.change.camera.pos");
            addToggleButton("sleep.grant.buffs", ModConfigs.GRANT_BUFFS, "sleep.grant.buffs");
            addToggleButton("sleep.do.regeneration", ModConfigs.DO_REGEN, "sleep.do.regeneration");

            // Numeric configs (using sliders)
            addSlider("sleep.day.skip.speed", ModConfigs.DAY_SKIP_SPEED, "sleep.day.skip.speed", 0, 200);
            addSlider("sleep.sleep.tick.speed", ModConfigs.SLEEP_TICK_SPEED, "sleep.sleep.tick.speed", 0, 200);
            addSlider("sleep.well.rested.wait", ModConfigs.WELL_RESTED_WAIT, "sleep.well.rested.wait", 0, 5000);
            addSlider("sleep.tired.wait", ModConfigs.TIRED_WAIT, "sleep.tired.wait", 0, 5000);
            addSlider("sleep.well.rested.length", ModConfigs.WELL_RESTED_LENGTH, "sleep.well.rested.length", 0, 5000);
            addSlider("sleep.tired.length", ModConfigs.TIRED_LENGTH, "sleep.tired.length", 0, 1000);
            addSlider("sleep.sleep.button.height", ModConfigs.SLEEP_BUTTON_HEIGHT, "sleep.sleep.button.height", -100, 100);

            // Example row with a button and text field
            //addSlider("config.sleep.well_rest_wait", ModConfigs.WELL_RESTED_WAIT, "sleep.well.rested.wait");

            // Calculate the height of all widgets
            int totalContentHeight = (configWidgets.size() + 1) / 2 * (buttonHeight + buttonSpacing);

            // Create scrollable area
            int scrollableAreaTop = 50;
            int scrollableAreaBottom = this.height - 40;
            int scrollableAreaHeight = scrollableAreaBottom - scrollableAreaTop;
            int scrollableAreaWidth = this.width - 20; // Reduce width by 20 pixels


            scrollableArea = new ScrollableWidget(
                    10, scrollableAreaTop, // Move 10 pixels from the left edge
                    scrollableAreaWidth, scrollableAreaHeight, Text.empty()
            ) {
                @Override
                protected int getContentsHeight() {
                    return totalContentHeight;
                }

                @Override
                protected double getDeltaYPerScroll() {
                    return 16;
                }


                @Override
                protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
                    int startY = 10;
                    int leftX = (this.width - (buttonWidth * 2 + buttonSpacing)) / 2;
                    int rightX = leftX + buttonWidth + buttonSpacing;

                    for (int i = 0; i < configWidgets.size(); i++) {
                        Object widget = configWidgets.get(i);
                        int x = (i % 2 == 0) ? leftX : rightX;
                        int y = startY + (i / 2) * (buttonHeight + buttonSpacing);

                        if (widget instanceof ButtonWidget button) {
                            button.setX(x + this.getX());
                            button.setY((int) (y + this.getY() - this.getScrollY()));
                            button.render(context, mouseX, mouseY, delta);
                        } else if (widget instanceof SliderWidget slider) {
                            slider.setX(x + this.getX());
                            slider.setY((int) (y + this.getY() - this.getScrollY()));
                            slider.render(context, mouseX, mouseY, delta);
                        } else if (widget instanceof TextFieldWidget textField) {
                            textField.setX(x + this.getX());
                            textField.setY((int) (y + this.getY() - this.getScrollY()));
                            textField.render(context, mouseX, mouseY, delta);
                        }
                    }
                }
                @Override
                protected void drawBox(DrawContext context) {
                    // Custom background
                    context.fill(this.getX() - 10, this.getY(), this.getX() + this.width + 10, this.getY() + this.height, 0x40303030); // Semi-transparent dark gray

                    // Custom border
                    context.drawBorder(this.getX() - 20, this.getY(), this.width + 40, this.height, 0xFFAAAAAA); // Light gray border
                }

                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int button) {
                    double adjustedMouseY = mouseY + this.getScrollY();
                    for (Object widget : configWidgets) {
                        if (widget instanceof ButtonWidget buttonWidget) {
                            if (buttonWidget.isMouseOver(mouseX, adjustedMouseY)) {
                                return buttonWidget.mouseClicked(mouseX, adjustedMouseY, button);
                            }
                        } else if (widget instanceof SliderWidget sliderWidget) {
                            if (sliderWidget.isMouseOver(mouseX, adjustedMouseY)) {
                                return sliderWidget.mouseClicked(mouseX, adjustedMouseY, button);
                            }
                        } else if (widget instanceof TextFieldWidget textFieldWidget) {
                            if (textFieldWidget.isMouseOver(mouseX, adjustedMouseY)) {
                                return textFieldWidget.mouseClicked(mouseX, adjustedMouseY, button);
                            }
                        }
                    }
                    return super.mouseClicked(mouseX, mouseY, button);
                }

                @Override
                protected void appendClickableNarrations(NarrationMessageBuilder builder) {

                }
            };

            this.addDrawableChild(scrollableArea);
            // Save button
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> {
                ModConfigs.saveConfigs();
                this.close();
            }).dimensions(this.width / 2 - 90, this.height - 30, 180, 20).build());

            // Reset to Defaults button
            this.addDrawableChild(ButtonWidget.builder(Text.literal("⟳"), button -> {
                resetToDefaults();
                this.close();
            }).dimensions(this.width / 2 + 95, this.height - 30, 20, 20).build());
        }

        private void addToggleButton(String key, boolean initialValue, String translationKey) {
            ButtonWidget toggleButton = ButtonWidget.builder(Text.translatable(translationKey).append(": ").append(getOnOffText(initialValue)), button -> {
                boolean newValue = !ModConfigs.ALLOW_DAY_SLEEPING;
                ModConfigs.ALLOW_DAY_SLEEPING = newValue;
                ModConfigs.CONFIG.set(key, newValue);
                button.setMessage(Text.translatable(translationKey).append(": ").append(getOnOffText(newValue)));
            }).dimensions(0, 0, buttonWidth, buttonHeight).build();

            configWidgets.add(toggleButton);
        }

        private void addSlider(String translationKey, Number initialValue, String configKey, double min, double max) {
            SliderWidget slider = new SliderWidget(0, 0, buttonWidth, buttonHeight, Text.translatable(translationKey).append(": ").append(initialValue.toString()), (initialValue.doubleValue() - min) / (max - min)) {
                @Override
                protected void updateMessage() {
                    double value = min + (max - min) * this.value;
                    String displayValue = initialValue instanceof Integer ? String.format("%d", (int)value) : String.format("%.1f", value);
                    this.setMessage(Text.translatable(translationKey).append(": ").append(displayValue));
                }

                @Override
                protected void applyValue() {
                    double newValue = min + (max - min) * this.value;
                    if (initialValue instanceof Integer) {
                        int intValue = (int) Math.round(newValue);
                        ModConfigs.CONFIG.set(configKey, intValue);
                        // Update the corresponding ModConfigs field
                        switch (configKey) {
                            case "sleep.well.rested.wait": ModConfigs.WELL_RESTED_WAIT = intValue; break;
                            case "sleep.tired.wait": ModConfigs.TIRED_WAIT = intValue; break;
                            case "sleep.well.rested.length": ModConfigs.WELL_RESTED_LENGTH = intValue; break;
                            case "sleep.tired.length": ModConfigs.TIRED_LENGTH = intValue; break;
                            case "sleep.sleep.button.height": ModConfigs.SLEEP_BUTTON_HEIGHT = intValue; break;
                        }
                    } else {
                        ModConfigs.CONFIG.set(configKey, newValue);
                        // Update the corresponding ModConfigs field
                        switch (configKey) {
                            case "sleep.day.skip.speed": ModConfigs.DAY_SKIP_SPEED = newValue; break;
                            case "sleep.sleep.tick.speed": ModConfigs.SLEEP_TICK_SPEED = newValue; break;
                        }
                    }
                }
            };
            configWidgets.add(slider);
        }

        private void addTextField(String translationKey, int initialValue, String configKey) {
            TextFieldWidget textField = new TextFieldWidget(this.textRenderer, 0, 0, buttonWidth, buttonHeight, Text.translatable(translationKey));
            textField.setText(Integer.toString(initialValue));
            textField.setChangedListener(newValue -> {
                try {
                    int value = Integer.parseInt(newValue);
                    ModConfigs.WELL_RESTED_WAIT = value;
                    ModConfigs.CONFIG.set(configKey, value);
                } catch (NumberFormatException e) {
                    // Revert to the previous value if input is invalid
                    textField.setText(Integer.toString(ModConfigs.WELL_RESTED_WAIT));
                }
            });
            configWidgets.add(textField);
        }


        private void updateWidgetPositions() {
            int startY = 60;
            int leftX = this.width / 2 - buttonWidth - buttonSpacing / 2;
            int rightX = this.width / 2 + buttonSpacing / 2;

            for (int i = 0; i < configWidgets.size(); i++) {
                Object widget = configWidgets.get(i);
                int x = (i % 2 == 0) ? leftX : rightX;
                int y = startY + (i / 2) * (buttonHeight + buttonSpacing);

                if (widget instanceof ButtonWidget button) {
                    button.setX(x);
                    button.setY(y);
                    this.addDrawableChild(button);
                } else if (widget instanceof SliderWidget slider) {
                    slider.setX(x);
                    slider.setY(y);
                    this.addDrawableChild(slider);
                } else if (widget instanceof TextFieldWidget textField) {
                    textField.setX(x);
                    textField.setY(y);
                    this.addDrawableChild(textField);
                }
            }
        }

        private void resetToDefaults() {
            // Reset boolean configs
            ModConfigs.ALLOW_DAY_SLEEPING = true;
            ModConfigs.CHANGE_TICK_SPEED = true;
            ModConfigs.CHANGE_CAMERA_POS = true;
            ModConfigs.GRANT_BUFFS = true;
            ModConfigs.DO_REGEN = true;

            // Reset double configs
            ModConfigs.DAY_SKIP_SPEED = 25.0;
            ModConfigs.SLEEP_TICK_SPEED = 100.0;

            // Reset integer configs
            ModConfigs.WELL_RESTED_WAIT = 500;
            ModConfigs.TIRED_WAIT = 800;
            ModConfigs.WELL_RESTED_LENGTH = 2400;
            ModConfigs.TIRED_LENGTH = 600;
            ModConfigs.SLEEP_BUTTON_HEIGHT = 30;

            // Update CONFIG object
            ModConfigs.CONFIG.set("sleep.allow.day.sleep", ModConfigs.ALLOW_DAY_SLEEPING);
            ModConfigs.CONFIG.set("sleep.change.tick.speed", ModConfigs.CHANGE_TICK_SPEED);
            ModConfigs.CONFIG.set("sleep.change.camera.pos", ModConfigs.CHANGE_CAMERA_POS);
            ModConfigs.CONFIG.set("sleep.grant.buffs", ModConfigs.GRANT_BUFFS);
            ModConfigs.CONFIG.set("sleep.do.regeneration", ModConfigs.DO_REGEN);
            ModConfigs.CONFIG.set("sleep.day.skip.speed", ModConfigs.DAY_SKIP_SPEED);
            ModConfigs.CONFIG.set("sleep.sleep.tick.speed", ModConfigs.SLEEP_TICK_SPEED);
            ModConfigs.CONFIG.set("sleep.well.rested.wait", ModConfigs.WELL_RESTED_WAIT);
            ModConfigs.CONFIG.set("sleep.tired.wait", ModConfigs.TIRED_WAIT);
            ModConfigs.CONFIG.set("sleep.well.rested.length", ModConfigs.WELL_RESTED_LENGTH);
            ModConfigs.CONFIG.set("sleep.tired.length", ModConfigs.TIRED_LENGTH);
            ModConfigs.CONFIG.set("sleep.sleep.button.height", ModConfigs.SLEEP_BUTTON_HEIGHT);

            ModConfigs.saveConfigs();
            this.init(); // Reinitialize the screen to update all widgets
        }

        private String getOnOffText(boolean value) {
            return value ? "ON" : "OFF";
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            this.renderBackground(context, mouseX, mouseY, delta);
            scrollableArea.render(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return scrollableArea.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return scrollableArea.mouseReleased(mouseX, mouseY, button) || super.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return scrollableArea.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
            return scrollableArea.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        @Override
        public void close() {
            MinecraftClient.getInstance().setScreen(parent);
            ModConfigs.saveConfigs();
        }

        @Override
        public boolean shouldCloseOnEsc() {
            return true;
        }
    }
}
