package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;

import java.util.ArrayList;
import java.util.List;

public class CFTrimMaterials {
    public static final ArrayList<ResourceKey<TrimMaterial>> MATERIALS = new ArrayList<>();

    public static final ResourceKey<TrimMaterial> ECTOPLASM = registryKey("ectoplasm");
    public static final ResourceKey<TrimMaterial> SCROLL_TRANS = registryKey("scroll_trans");
    public static final ResourceKey<TrimMaterial> SCROLL_PAN = registryKey("scroll_pan");
    public static final ResourceKey<TrimMaterial> SCROLL_PRIDE = registryKey("scroll_pride");
    private static ResourceKey<TrimMaterial> registryKey(String key) {
        ResourceKey<TrimMaterial> mat = ResourceKey.create(Registries.TRIM_MATERIAL, CreatureFeature.getId(key));
        MATERIALS.add(mat);
        return mat;
    }
    public static int test(ResourceKey<TrimMaterial> materialResourceKey){
        if(!MATERIALS.contains(materialResourceKey)){
            return -1;
        }else{
            return MATERIALS.indexOf(materialResourceKey);
        }
    }
}
