//? if >=26.2 {
package wily.legacy.mixin.base.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wily.factoryapi.FactoryAPIClient;
import wily.legacy.Legacy4JClient;
import wily.legacy.client.LegacyOptions;
import wily.legacy.client.LegacyTipManager;
import wily.legacy.client.ScreenshotToast;
import wily.legacy.client.screen.ControlTooltip;
import wily.legacy.client.screen.LegacyAdvancementsScreen;
import wily.legacy.client.screen.LegacyLoading;
import wily.legacy.client.screen.OverlayPanelScreen;
import wily.legacy.util.client.LegacyGuiElements;
import wily.legacy.util.client.LegacyRenderUtil;
import wily.legacy.util.client.LegacySoundUtil;

@Mixin(Gui.class)
public abstract class ActualGuiMixin implements ControlTooltip.Event {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Nullable
    private Screen screen;
    @Unique
    private Screen oldScreen;

    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;applyCursor(Lcom/mojang/blaze3d/platform/Window;)V"))
    private void extractRenderState(DeltaTracker deltaTracker, boolean renderHud, boolean renderScreen, CallbackInfo ci, @Local GuiGraphicsExtractor graphics) {
        LegacyRenderUtil.renderGameOverlay(graphics);
        ScreenshotToast.render(graphics);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSleeping()Z", ordinal = 0))
    private boolean tick(boolean original) {
        return false;
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(Screen newScreen, CallbackInfo ci) {
        oldScreen = this.screen;
        Screen replacement = Legacy4JClient.getReplacementScreen(newScreen);
        if (replacement != newScreen) {
            ci.cancel();
            FactoryAPIClient.setScreen(replacement);
            return;
        }
        if (FactoryAPIClient.getScreen() == null && minecraft.level != null && newScreen != null && !(newScreen instanceof LegacyLoading) && (newScreen instanceof PauseScreen || !newScreen.isPauseScreen()))
            LegacySoundUtil.playSimpleUISound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0f);
        if (newScreen == null && minecraft.level != null) {
            LegacyGuiElements.lastGui = Util.getMillis();
            Gui gui = (Gui) (Object) this;
            ControlTooltip.Event.of(gui).setupControlTooltips();
            ControlTooltip.Renderer.GUI_EVENT.invoker.accept(gui, ControlTooltip.Event.of(gui).getControlTooltips());
        }
    }

    @ModifyArg(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", ordinal = 0))
    private Screen handleKeybinds(Screen arg) {
        return LegacyOptions.legacyAdvancements.get() ? new LegacyAdvancementsScreen(null) : arg;
    }

    @WrapWithCondition(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;removed()V"))
    private boolean removedScreen(Screen instance, Screen newScreen) {
        return !(newScreen instanceof OverlayPanelScreen s) || s.parent != instance;
    }

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;added()V"))
    private void addedScreen(Screen screen, CallbackInfo ci) {
        ControlTooltip.Event.of(screen).setupControlTooltips();
        ControlTooltip.Renderer.SCREEN_EVENT.invoker.accept(screen, ControlTooltip.Event.of(screen).getControlTooltips());
        LegacyTipManager.resetTipOffset(true);
    }

    @WrapWithCondition(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init(II)V"))
    private boolean initScreen(Screen instance, int i, int j) {
        if (oldScreen instanceof OverlayPanelScreen s && s.parent == instance) {
            instance.resize(i, j);
            return false;
        }
        return true;
    }
}
//?}