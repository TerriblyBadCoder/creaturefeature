package net.atired.creaturefeature.init;

import com.mojang.serialization.MapCodec;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.recipe.FlowerCloneRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CFRecipeSerialisers<T extends Recipe<?>> {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreatureFeature.MODID);

    public static Supplier<RecipeSerializer<FlowerCloneRecipe>> FLOWER_CLONE = RECIPES.register("crafting_special_flowerclone",()-> new SimpleCraftingRecipeSerializer(FlowerCloneRecipe::new));


    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String key, S recipeSerializer) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, CreatureFeature.getId(key), recipeSerializer);
    }
    public static void init(){

    }
}
