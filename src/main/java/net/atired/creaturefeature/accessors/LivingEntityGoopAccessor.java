package net.atired.creaturefeature.accessors;

public interface LivingEntityGoopAccessor {
    void setGoop(float goop);
    float getGoop();
    void setSquashed(float squashed);
    float getSquashed();
    void setBacterial(float bacte);
    float getBacterial();
    int getDelay();
    void setFallDamageAmped(boolean fallDamageAmped);
    boolean isFallDamageAmped();
}
