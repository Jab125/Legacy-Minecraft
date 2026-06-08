package wily.legacy.mixin.base.client.sign;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SignEditScreen.class)
public abstract class SignEditScreenMixin extends AbstractSignEditScreen {


    public SignEditScreenMixin(SignBlockEntity signBlockEntity, boolean bl, boolean bl2) {
        super(signBlockEntity, bl, bl2);
    }

    @Unique
    private int getYOffset() {
        return this.sign.getBlockState().getBlock() instanceof StandingSignBlock ? -42 : 0;
    }

    @ModifyReturnValue(method = "getSignYOffset", at = @At("RETURN"))
    private float offsetSign(float original) {
        return height / 2f + 15.5f + getYOffset();
    }

    @Redirect(method = "extractSignBackground", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", remap = false))
    private Matrix3x2f renderSignBackground(Matrix3x2fStack instance, float x, float y) {
        return instance.scale(x * 144 / 93, y * 144 / 93);
    }
}
