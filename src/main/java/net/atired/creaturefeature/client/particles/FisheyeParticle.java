package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class FisheyeParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    protected FisheyeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=10;
        this.age=0;
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.gravity=20f;
        this.yd=0;
        this.xd=xSpeed/3.0;
        this.zd=zSpeed/3.0;
        this.quadSize*=0.0f;
        this.spriteSet=sprite;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        float aged=  (float)this.age/(float)this.lifetime;
        this.alpha=Math.min(Mth.sin((this.age/(float)this.lifetime)*3.14f)*1.2f,1.0f);
        this.oRoll=this.roll;
        this.roll+=aged/8.0f;
        this.quadSize=1;
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float angle = this.roll;
        this.y+=0.03f;
        this.yo+=0.03f;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(3.14f,angle,3.14f/2.0f), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0.0f,-angle,3.14f/2.0f), partialTicks);
        this.yo-=0.03f;
        this.y-=0.03f;

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
            FisheyeParticle flameparticle = new FisheyeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
