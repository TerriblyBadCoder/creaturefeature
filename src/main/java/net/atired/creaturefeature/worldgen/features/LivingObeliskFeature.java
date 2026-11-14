package net.atired.creaturefeature.worldgen.features;

import com.mojang.serialization.Codec;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.misc.CFsimplexNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LivingObeliskFeature  extends Feature<NoneFeatureConfiguration> {
    public LivingObeliskFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        BlockPos pos = featurePlaceContext.origin();
        float noisy = CFsimplexNoise.sampleNoise3D(pos.getX(),pos.getY(),pos.getZ(),300);
        noisy-=0.4f;
        noisy/=0.6f;
        int checks =10;
        while (checks>0){
            if(!level.isEmptyBlock(pos)&&level.isEmptyBlock(pos.above())){
                checks=1;
                break;
            }else{
                pos=pos.below();
                checks-=1;
            }
        }
        if(checks<=0||noisy<0.04){
            return false;
        }
        noisy=(float)Math.pow(noisy,0.5f);
        int count = (int)(featurePlaceContext.random().nextIntBetweenInclusive(0,3)*noisy)+1;
        int height = (4+featurePlaceContext.random().nextInt(6))*count;
        for (int x = 0; x < count; x++) {
            for (int z = 0; z < count; z++) {
                for (int y = -3; y < height; y++) {
                    BlockPos pos1 = pos.offset(x,y,z);
                    level.setBlock(pos1, CFBlockInit.OBELISK.get().defaultBlockState(),2);
                }
            }
        }
        return true;
    }
}
