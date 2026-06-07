package wily.legacy.mixin.base.client.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wily.legacy.client.CommonColor;

@Mixin(TextColor.class)
public class TextColorMixin {
    @Inject(method = "fromLegacyFormat", at = @At("HEAD"), cancellable = true)
    private static void fromLegacyFormat(ChatFormatting formatting, CallbackInfoReturnable<TextColor> cir) {
        if (formatting == null) return;
        CommonColor color = switch (formatting) {
            case BLACK -> CommonColor.BLACK;
            case DARK_BLUE -> CommonColor.DARK_BLUE;
            case DARK_GREEN -> CommonColor.DARK_GREEN;
            case DARK_AQUA -> CommonColor.DARK_AQUA;
            case DARK_RED -> CommonColor.DARK_RED;
            case DARK_PURPLE -> CommonColor.DARK_PURPLE;
            case GOLD -> CommonColor.GOLD;
            case GRAY -> CommonColor.GRAY;
            case DARK_GRAY -> CommonColor.DARK_GRAY;
            case BLUE -> CommonColor.BLUE;
            case GREEN -> CommonColor.GREEN;
            case AQUA -> CommonColor.AQUA;
            case RED -> CommonColor.RED;
            case LIGHT_PURPLE -> CommonColor.LIGHT_PURPLE;
            case YELLOW -> CommonColor.YELLOW;
            case WHITE -> CommonColor.WHITE;
            default -> null;
        };
        if (color != null) cir.setReturnValue(TextColor.fromRgb(color.get() & 0x00FFFFFF));
    }
}
