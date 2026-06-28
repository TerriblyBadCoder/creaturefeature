package net.atired.creaturefeature.items;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class DreamCatcherContents  implements TooltipComponent  {
    public static final Codec<DreamCatcherContents> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, DreamCatcherContents> STREAM_CODEC;
    public static final DreamCatcherContents EMPTY = new DreamCatcherContents(List.of());
    final List<ItemStack> items;

    public DreamCatcherContents(List<ItemStack> items) {
        this.items = items;
    }
    public Iterable<ItemStack> items() {
        return this.items;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){return true;}
        else{
            if(obj instanceof DreamCatcherContents contents && ItemStack.listMatches(this.items, contents.items)){
                return true;
            }else{return false;}
        }
    }
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    static {
        CODEC = ItemStack.CODEC.listOf().xmap(DreamCatcherContents::new, (p_331551_) -> {
            return p_331551_.items;
        });
        STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(DreamCatcherContents::new, (p_331649_) -> {
            return p_331649_.items;
        });
    }
}
