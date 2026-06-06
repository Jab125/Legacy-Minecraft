package wily.legacy.mixin.base.client;

import net.minecraft.client.renderer.SubmitNodeStorage;
//? if >=26.2 {
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
//?}
import org.spongepowered.asm.mixin.Mixin;
import wily.legacy.client.LegacyNameTag;

//~ if >=26.2 'SubmitNodeStorage.NameTagSubmit' -> 'NameTagFeatureRenderer.Submit'
@Mixin(NameTagFeatureRenderer.Submit.class)
public class NameTagSubmitMixin implements LegacyNameTag {
    float[] nameTagColor = null;

    @Override
    public float[] getNameTagColor() {
        return nameTagColor;
    }

    @Override
    public void setNameTagColor(float[] color) {
        nameTagColor = color;
    }
}
