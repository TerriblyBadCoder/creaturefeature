package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class LeafParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    protected LeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=60+(int)(Math.random()*20);
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.gravity=0.3f;
        this.yd=0.2f;

        if(Math.random()>0.66){
            this.age+=40;
            this.yd-=0.15f;
        }
        this.quadSize*=2.0f;
        this.spriteSet=sprite;
        this.bCol=0.5f;
        this.gCol=0.8f;
        this.rCol=0.4f;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    public void tick() {
        xd= Mth.sin(age/2.0f+rotoff)/8.0f;
        zd= Mth.sin(age/2.0f+rotoff)/8.0f;
        this.oRoll=roll;
        float aged=  (float)this.age/(float)this.lifetime;
        this.roll+=(1.0f-aged)*0.2f;
        this.rCol=0.4f+aged*0.4f;
        this.gCol=0.8f-aged*0.6f;
        this.bCol=0.5f-aged*0.3f;
        alpha=Math.min((1.0f-aged)*4.0f,1.0f);
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LeafParticle flameparticle = new LeafParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
