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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
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
    private final IDrawable overlay;
    private final IDrawable zoomLens;
    private final ResourceLocation texPath = new ResourceLocation(CompressedBotanics.MODID, "textures/gui/jei.png");

    public FloralEntropyRecipeCategory(IGuiHelper guiHelper){
        this.title = Component.literal("Floral Entropy");
        this.background = guiHelper.createBlankDrawable(120, 58);
        this.overlay = guiHelper.createDrawable(texPath, 0,0, 110 ,58);
        this.zoomLens = guiHelper.createDrawable(texPath, 111,1, 7 ,7);
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
        RenderSystem.enableBlend();
        overlay.draw(ms, 5, 0);
        if(recipe.getMinTalliedMana() > 0) { //If no mana is required, don't draw the mana bar.
            String zoomFactor = getZoomFactor(recipe.getMinTalliedMana());
            HUDHandler.renderManaBar(ms, 9, 49, 0x0000FF, 0.75F, recipe.getMinTalliedMana(), (int) (ManaPoolBlockEntity.MAX_MANA / Float.parseFloat(zoomFactor))); //NOTE: This method always creates a mana bar 102 units wide and 5 units tall.
            Component zoomText = Component.literal("x" + zoomFactor); //Whenever the overlay is reimplemented, add a little magnifying glass icon.
            Font font = Minecraft.getInstance().font;
            font.draw(ms, zoomText, 22, 37, 0x888888);
            zoomLens.draw(ms, 12, 37);
        }
        RenderSystem.disableBlend();
    }

    @Override //The role can be INPUT, OUTPUT, CATALYST or RENDER_ONLY. It's what determines whether the recipe shows up when searching recipes or uses for an item.
    public void setRecipe(IRecipeLayoutBuilder builder, FloralEntropyRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 16).addItemStack(recipe.getFlowerAsItemStack())
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.addAll(getInputTooltip(recipe)));

        ItemStack decayed = recipe.getBlockAsItemStack();
        FluidStack fluid = FluidUtil.getFluidContained(decayed).orElse(FluidStack.EMPTY);

        if(fluid.isEmpty()){
            builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 28).addItemStack(decayed)
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.literal("Left over after entropic decay.")));
        }else{
            builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 28).addFluidStack(fluid.getFluid(), 1000)
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.literal("Left over after entropic decay.")));
        }


        List<ChanceOutput> outputs = recipe.getResult();
        ItemStack firstOutput = prepareItemNBT(outputs.get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 4).addItemStack(firstOutput)
                .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.addAll(getOutputTooltip(firstOutput)));

        List<ItemStack> secondaries = new ArrayList<>();
        for(int i=1; i<outputs.size(); i++){
            ItemStack item = prepareItemNBT(outputs.get(i));
            secondaries.add(item);
        }
        if(!secondaries.isEmpty()){
            builder.addSlot(RecipeIngredientRole.OUTPUT, 92,4).addItemStacks(secondaries)
                    .addTooltipCallback((recipeSlotView, tooltip) -> tooltip.addAll(getOutputTooltip(recipeSlotView.getDisplayedItemStack().get())));
        }

    }
    @NotNull
    @Override
    public List<Component> getTooltipStrings(FloralEntropyRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY){
        if(mouseY >= 50){ //TODO: Pad this a bit more.
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
    private ItemStack prepareItemNBT(ChanceOutput output){
        ItemStack item = output.getItem();
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("chance", output.getChance());
        nbt.putBoolean("allornothing", output.isAllOrNothingFlag());
        nbt.putBoolean("enchanted", output.isSpecial());
        item.setTag(nbt);
        return item;
    }

    private Collection<Component> getOutputTooltip(ItemStack output){
        List<Component> tooltips = new ArrayList<>();
        CompoundTag nbt = output.getTag();
        tooltips.add(Component.literal("Chance: "+(nbt.getFloat("chance"))));
        tooltips.add(Component.literal(nbt.getBoolean("allornothing") ? "All or nothing" : "Individual rolls"));
        if(nbt.getBoolean("enchanted")) tooltips.add(Component.literal("Enchanted soil required"));
        return tooltips;
    }

    private String getZoomFactor(int amount){
        if(amount > 10000000) return "0.01"; //Over 10 mana pools, i HOPE this doesn't happen, but y'know.
        if(amount > 1000000) return "0.10"; //Over one mana pool, but less than 10.
        if(amount < 10000) return "100"; //Less than one hundredth of a mana pool. I guess it's ok for stuff like the hydroangeas.
        if(amount < 100000) return "10.0"; //Between one hundredth and one tenth of a mana pool.
        return "1"; //between one tenth of a pool and one full pool.
    }

}
