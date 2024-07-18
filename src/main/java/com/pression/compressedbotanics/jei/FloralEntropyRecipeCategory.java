package com.pression.compressedbotanics.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pression.compressedbotanics.CompressedBotanics;
import com.pression.compressedbotanics.recipe.ChanceOutput;
import com.pression.compressedbotanics.recipe.FloralEntropyRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//This handles the rendering of the recipe in JEI
public class FloralEntropyRecipeCategory implements IRecipeCategory<FloralEntropyRecipe> {
    public static final RecipeType<FloralEntropyRecipe> TYPE = RecipeType.create(CompressedBotanics.MODID, "floral_entropy", FloralEntropyRecipe.class);

    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    public FloralEntropyRecipeCategory(IGuiHelper guiHelper){
        this.title = Component.literal("Floral Entropy");
        this.background = guiHelper.createBlankDrawable(160, 66);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaItems.clockEye));
    }

    @Override
    public RecipeType<FloralEntropyRecipe> getRecipeType() {
        return TYPE;
    }
    @Override
    public Component getTitle(){
        return title;
    }
    @Override
    public IDrawable getBackground(){
        return background;
    }
    @Override
    public IDrawable getIcon(){
        return icon;
    }

    @Override
    public void draw(FloralEntropyRecipe recipe, IRecipeSlotsView slotsView, PoseStack ms, double mouseX, double mouseY) {
        RenderSystem.disableBlend();
        if(recipe.getMinTalliedMana() > 0) { //If no mana is required, don't draw the mana bar.
            float zoomFactor = recipe.getMinTalliedMana() < 100000 ? 10F : (recipe.getMinTalliedMana() > 1000000 ? 0.1F : 1F); //If the mana is less than 1/10th of a pool, zoom in. IF it's more than a pool, zoom out.
            HUDHandler.renderManaBar(ms, 30, 55, 0x0000FF, 0.75F, recipe.getMinTalliedMana(), (int) (ManaPoolBlockEntity.MAX_MANA / zoomFactor)); //NOTE: This method always creates a mana bar 100 units wide.
            Component zoomText = Component.literal("x" + zoomFactor); //Whenever the overlay is reimplemented, add a little magnifying glass icon.
            Font font = Minecraft.getInstance().font;
            font.draw(ms, zoomText, 0, 53, 0x888888);
        }
        RenderSystem.enableBlend();
    }

    @Override //The role can be INPUT, OUTPUT, CATALYST or RENDER_ONLY. It's what determines whether the recipe shows up when searching recipes or uses for an item.
    public void setRecipe(IRecipeLayoutBuilder builder, FloralEntropyRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 72, 24).addItemStack(recipe.getFlowerAsItemStack())
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.addAll(getInputTooltip(recipe)));

        if(!recipe.getBlock().equals(new ResourceLocation(CompressedBotanics.MODID,"null"))){
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 24).addItemStack(recipe.getBlockAsItemStack())
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.literal("Left over after entropic decay.")));
        }

        int posX = 81 - recipe.getResult().size() * 9;
        for (ChanceOutput output : recipe.getResult()){
            builder.addSlot(RecipeIngredientRole.OUTPUT, posX, 4).addItemStack(output.getItem())
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.addAll(getOutputTooltip(output)));
            posX += 18;
        }

    }
    @NotNull
    @Override
    public List<Component> getTooltipStrings(FloralEntropyRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY){
        if(mouseY >= 55){ //TODO: Pad this a bit more.
            List<Component> tooltips = new ArrayList<>();
            if(recipe.getMinTalliedMana() > 0){ //There's no mana bar in this case.
                tooltips.add(Component.literal("Must produce at least "+(recipe.getMinTalliedMana()<1000 ? "a small amount" : ((double) recipe.getMinTalliedMana()/ManaPoolBlockEntity.MAX_MANA+" Mana Pool(s)' worth"))+" of mana before entropic decay is possible."));
                //That thing right above? It's ugly, i know. But it's hard to get that number to render semi-properly.
            }
            return tooltips;
        }
        return Collections.emptyList();
    }

    //TODO: Move this somewhere else in the renderer. Maybe to the right of the mana bar?
    private Collection<Component> getInputTooltip(FloralEntropyRecipe recipe){
        List<Component> tooltips = new ArrayList<>();
        tooltips.add(Component.literal("Minimum time: "+recipe.getMinDecayTicks()+" ticks"));
        tooltips.add(Component.literal("Maximum time: "+recipe.getMaxDecayTicks()+" ticks"));
        tooltips.add(Component.literal("Chance of entropic decay scales between the two time intervals"));
        return tooltips;
    }

    private Collection<Component> getOutputTooltip(ChanceOutput output){
        List<Component> tooltips = new ArrayList<>();
        tooltips.add(Component.literal("Chance: "+(output.getChance() >= 1 ? 100 : 100*output.getChance())+"%"));
        tooltips.add(Component.literal(output.isAllOrNothingFlag() ? "All or nothing" : "Individual rolls"));
        return tooltips;
    }


}
