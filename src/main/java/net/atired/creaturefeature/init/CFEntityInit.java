package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.*;
import net.atired.creaturefeature.misc.SableCarrier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CFEntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, CreatureFeature.MODID);

    public static final Supplier<EntityType<MindsEntity>> MINDS =
            ENTITIES.register("minds", () -> EntityType.Builder.of(MindsEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight()).eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8)
                    .build("minds"));
    public static final Supplier<EntityType<MachinationEntity>> MACHINATION =
            ENTITIES.register("machination", () -> EntityType.Builder.of(MachinationEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIE.getWidth()*1.1f, 1.7f).eyeHeight(1.58F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("machination"));
    public static final Supplier<EntityType<SinisterEntity>> SINISTER =
            ENTITIES.register("sinister", () -> EntityType.Builder.of(SinisterEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.9f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("sinister"));
    public static final Supplier<EntityType<MinedFlayerEntity>> MINEDFLAYER =
            ENTITIES.register("minedflayer", () -> EntityType.Builder.of(MinedFlayerEntity::new, MobCategory.MONSTER)
                    .sized(1.1f, 1.4f).eyeHeight(0.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("minedflayer"));
    public static Supplier<EntityType<ToadstoolEntity>> TOADSTOOL;
    static {
        TOADSTOOL=null;
        if(ModList.get().isLoaded("sable")){
            SableCarrier.initEntity();
        }
    }
    public static final Supplier<EntityType<MockingBirdEntity>> MOCKINGBIRD =
            ENTITIES.register("mockingbird", () -> EntityType.Builder.of(MockingBirdEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 2.8f).eyeHeight(2.48F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("mockingbird"));
    public static final Supplier<EntityType<PathogenesisEntity>> PATHOGEN =
            ENTITIES.register("pathogen", () -> EntityType.Builder.of(PathogenesisEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.37f).eyeHeight(1.18F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("pathogen"));
    public static final Supplier<EntityType<BlossomEntity>> BLOSSOM =
            ENTITIES.register("blossom", () -> EntityType.Builder.of(BlossomEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight()).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("blossom"));
    public static final Supplier<EntityType<SaintSolisEntity>> SAINT_SOLIS =
            ENTITIES.register("saint_solis", () -> EntityType.Builder.of(SaintSolisEntity::new, MobCategory.MONSTER)
                    .sized(1.1f, 1.5f).eyeHeight(1.38F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("saint_solis"));
    public static final Supplier<EntityType<DetritusEntity>> DETRITUS =
            ENTITIES.register("detritus", () -> EntityType.Builder.of(DetritusEntity::new, MobCategory.MONSTER)
                    .sized(EntityType.ZOMBIE.getWidth(), EntityType.ZOMBIE.getHeight()).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("detritus"));
    public static final Supplier<EntityType<StainedGlassEntity>> STAINED_GLASS =
            ENTITIES.register("stained_glass", () -> EntityType.Builder.of(StainedGlassEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.9f).eyeHeight(0.48F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("stained_glass"));
    public static final Supplier<EntityType<CoatOfArmsEntity>> COATOFARMS =
            ENTITIES.register("coat_of_arms", () -> EntityType.Builder.of(CoatOfArmsEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 1.9f).eyeHeight(1.48F).passengerAttachments(1.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("coat_of_arms"));
    public static final Supplier<EntityType<BeautyEntity>> BEAUTY =
            ENTITIES.register("beauty", () -> EntityType.Builder.of(BeautyEntity::new, MobCategory.MONSTER)
                    .sized(0.8f,1.9f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("beauty"));
    public static final Supplier<EntityType<FiendEntity>> FIEND =
            ENTITIES.register("fiend", () -> EntityType.Builder.of(FiendEntity::new, MobCategory.MONSTER)
                    .sized(0.8f,1.85f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("fiend"));
    public static final Supplier<EntityType<FendEntity>> FEND =
            ENTITIES.register("fend", () -> EntityType.Builder.of(FendEntity::new, MobCategory.MONSTER)
                    .sized(0.68f,1.9f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("fend"));
    public static final Supplier<EntityType<FriendEntity>> FRIEND =
            ENTITIES.register("friend", () -> EntityType.Builder.of(FriendEntity::new, MobCategory.MONSTER)
                    .sized(1.9f,1.95f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("friend"));
    public static final Supplier<EntityType<BlitzEntity>> BLITZ =
            ENTITIES.register("blitz", () -> EntityType.Builder.of(BlitzEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.9f).eyeHeight(0.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("blitz"));
    public static final Supplier<EntityType<EeperEntity>> EEPER =
            ENTITIES.register("eeper", () -> EntityType.Builder.of(EeperEntity::new, MobCategory.MONSTER)
                    .sized(0.65f, 2.1f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("eeper"));
    public static final Supplier<EntityType<NoThingEntity>> NOTHING =
            ENTITIES.register("nothing", () -> EntityType.Builder.of(NoThingEntity::new, MobCategory.MONSTER)
                    .sized(0.65f, 1.8f).eyeHeight(1.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("nothing"));
    public static final Supplier<EntityType<CanaryEntity>> CANARY =
            ENTITIES.register("canary", () -> EntityType.Builder.of(CanaryEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.9f).eyeHeight(0.5F).passengerAttachments(0.9125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("canary"));
    public static final Supplier<EntityType<CanaryPart>> CANARY_PART =
            ENTITIES.register("canary_part", () -> EntityType.Builder.of(CanaryPart::new, MobCategory.MONSTER)
                    .sized(0.6f, 0.6f).eyeHeight(0.3F).passengerAttachments(0.3125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("canary_part"));
    public static final Supplier<EntityType<VertigoEntity>> VERTIGO =
            ENTITIES.register("vertigo", () -> EntityType.Builder.of(VertigoEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.95f).eyeHeight(1.1F).passengerAttachments(0.3125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("vertigo"));
    public static final Supplier<EntityType<DreamWeaverEntity>> DREAMWEAVER =
            ENTITIES.register("dreamweaver", () -> EntityType.Builder.of(DreamWeaverEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 0.9f).eyeHeight(0.4F).passengerAttachments(0.3125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("dreamweaver"));
    public static final Supplier<EntityType<CannonballCrabEntity>> CANNONBALL_CRAB =
            ENTITIES.register("cannonball_crab", () -> EntityType.Builder.of(CannonballCrabEntity::new, MobCategory.MONSTER)
                    .sized(1.13f, 1.95f).eyeHeight(1.1F).passengerAttachments(0.3125F).ridingOffset(-0.7F).clientTrackingRange(12)
                    .build("cannonball_crab"));
    public static final Supplier<EntityType<MurkyPearlEntity>> MURKY_PEARL =
            ENTITIES.register("murky_pearl", () -> EntityType.Builder.<MurkyPearlEntity>of(MurkyPearlEntity::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f).eyeHeight(0.2f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("murky_pearl"));
    public static final Supplier<EntityType<FeatherEntity>> FEATHER =
            ENTITIES.register("feather", () -> EntityType.Builder.<FeatherEntity>of(FeatherEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).eyeHeight(0.2f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("feather"));
    public static final Supplier<EntityType<VitricArrowEntity>> VITRIC_ARROW =
            ENTITIES.register("vitric_arrow", () -> EntityType.Builder.<VitricArrowEntity>of(VitricArrowEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).eyeHeight(0.2f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("vitric_arrow"));
    public static final Supplier<EntityType<StarProjEntity>> STAR =
            ENTITIES.register("star", () -> EntityType.Builder.<StarProjEntity>of(StarProjEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).eyeHeight(0.2f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("star"));
    public static final Supplier<EntityType<BulletEntity>> BULLET =
            ENTITIES.register("bullet", () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.2f).eyeHeight(0.2f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("bullet"));
    public static final Supplier<EntityType<KickedBlockEntity>> KICKED_BLOCK =
            ENTITIES.register("kicked_block", () -> EntityType.Builder.<KickedBlockEntity>of(KickedBlockEntity::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f).eyeHeight(0.3f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("kicked_block"));
    public static final Supplier<EntityType<SpatBlockEntity>> SPAT_BLOCK =
            ENTITIES.register("spat_block", () -> EntityType.Builder.<SpatBlockEntity>of(SpatBlockEntity::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f).eyeHeight(0.3f).passengerAttachments(0.3125F).ridingOffset(-0.1F).clientTrackingRange(12)
                    .build("spat_block"));
    public static final Supplier<EntityType<PrimedEepEntity>> EEP =
            ENTITIES.register("eep", () -> EntityType.Builder.<PrimedEepEntity>of(PrimedEepEntity::new, MobCategory.MISC)
                    .sized(0.98f, 0.98f).eyeHeight(0.5F).passengerAttachments(0.3125F).ridingOffset(-0.5F).clientTrackingRange(12)
                    .build("eep"));
    public static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.EntityFactory<T> entity, MobCategory category, float width, float height) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity, category).sized(width, height).build(name));
    }
}
