package net.atired.creaturefeature.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.renderer.Sheets.ARMOR_TRIMS_SHEET;

public class CFSheets {
    private static final RenderType ARMOR_TRIMS_SHEET_TYPE  = armorCutoutEvilCull(ARMOR_TRIMS_SHEET,0);
    public static RenderType armorCutoutEvilCull(ResourceLocation location,int typed) {
        return (RenderType)CFRenderTypes.ARMOR_CUTOUT_EVIL_CULL.apply(location,typed);
    }
    public static RenderType armorTrimsSheet(boolean decal,int typed) {
        return armorCutoutEvilCull(ARMOR_TRIMS_SHEET,typed);
    }
}
