package wily.legacy.util.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastManager;
//~ if >=26.2 'LevelRenderer' -> 'extract.LevelExtractor'
import net.minecraft.client.renderer.extract.LevelExtractor;

public class LegacyMCUtil {
	private LegacyMCUtil() {

	}


	public static ToastManager getToastManager(Minecraft minecraft) {
		//? if >=26.2 {
		return minecraft.gui.toastManager();
		//?} else {
		/*return minecraft.getToastManager();
		*///?}
	}

	//~ if >=26.2 'LevelRenderer' -> 'LevelExtractor'
	public static LevelExtractor levelRendererOrExtractor(Minecraft minecraft) {
		//~ if >=26.2 'levelRenderer' -> 'levelExtractor'
		return minecraft.levelExtractor;
	}
}
