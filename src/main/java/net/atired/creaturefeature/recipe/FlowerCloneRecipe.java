package net.atired.creaturefeature.recipe;

import com.google.common.collect.Lists;
import net.atired.creaturefeature.init.CFRecipeSerialisers;
import net.atired.creaturefeature.items.BouquetItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;

public class FlowerCloneRecipe  extends CustomRecipe {
    public FlowerCloneRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, Level level) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack2 = ItemStack.EMPTY;
        if(input.size()!=2){
            return false;
        }
        for (int i = 0; i < input.size(); i++) {
            ItemStack itemstack1 = input.getItem(i);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.is(ItemTags.FLOWERS)) {
                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (!(itemstack1.getItem() instanceof BouquetItem)) {
                        return false;
                    }
                    itemstack2=itemstack1;
                }
            }
        }

        return !itemstack.isEmpty() && !itemstack2.isEmpty();
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack itemstack2 = ItemStack.EMPTY;
        ItemStack itemstack = ItemStack.EMPTY;
        if(input.size()!=2){
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < input.size(); i++) {
            ItemStack itemstack1 = input.getItem(i);
            if (!itemstack1.isEmpty()) {
                if (itemstack1.is(ItemTags.FLOWERS)) {
                    if (!itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1.copy();
                } else {
                    if (!(itemstack1.getItem() instanceof BouquetItem)) {
                        return ItemStack.EMPTY;
                    }
                    itemstack2=itemstack1;
                }
            }
        }
        ItemStack clone = itemstack.copy();
        clone.setCount(2);
        return !itemstack.isEmpty() && !itemstack2.isEmpty() ? clone : ItemStack.EMPTY;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CFRecipeSerialisers.FLOWER_CLONE.get();
    }
}
