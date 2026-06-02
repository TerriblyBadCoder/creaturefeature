package net.atired.creaturefeature.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class FlowerParticle extends TextureSheetParticle {
    private SpriteSet spriteSet;
    private int rotoff=0;
    private Vec2 angled = new Vec2(0,0);
    protected FlowerParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime=15;
        this.rotoff=(int)(Math.random()*300*3.14f);
        this.yd=ySpeed/1.0f;
        this.xd=xSpeed/1.0f;
        this.zd=zSpeed/1.0f;
        this.friction=0.95f;
        this.gravity=1.0f;
        this.quadSize=0.4f;
        this.spriteSet=sprite;
        this.roll=(float)Math.random()*4.0f*3.14f;
        this.oRoll=roll;
    }

    @Override
    public void tick() {
        this.oRoll=roll;
        float aged=  (float)this.age/(float)this.lifetime;
        Vec3 dir = new Vec3(xd,yd,zd).normalize();
        this.angled= new Vec2((float) Math.atan2(dir.x,dir.z),(float)Math.asin(dir.y));

        this.roll+=(1.0f-aged)*0.2f;
        alpha=Math.min((1.0f-aged)*4.0f,1.0f);
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float angle = this.oRoll*(1.0f-partialTicks)+this.roll*partialTicks;
        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,angled.x,-angled.y+3.14f/2.0f), partialTicks);

        this.renderRotatedQuad(buffer, renderInfo, new Quaternionf().rotationZYX(0,angled.x+3.14f,angled.y-3.14f/2.0f), partialTicks);
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
            FlowerParticle flameparticle = new FlowerParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,sprite);
            flameparticle.pickSprite(this.sprite);
            return flameparticle;
        }
    }
}
