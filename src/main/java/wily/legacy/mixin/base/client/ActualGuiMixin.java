//? if >=26.2 {
package wily.legacy.mixin.base.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wily.legacy.client.ScreenshotToast;
import wily.legacy.util.client.LegacyRenderUtil;

@Mixin(Gui.class)
public class ActualGuiMixin {
    @Inject(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;applyCursor(Lcom/mojang/blaze3d/platform/Window;)V"))
    private void extractRenderState(DeltaTracker deltaTracker, boolean renderHud, boolean renderScreen, CallbackInfo ci, @Local GuiGraphicsExtractor graphics) {
        LegacyRenderUtil.renderGameOverlay(graphics);
        ScreenshotToast.render(graphics);
    }
}
//?}