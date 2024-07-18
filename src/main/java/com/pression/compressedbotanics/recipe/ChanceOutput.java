package com.pression.compressedbotanics.recipe;

import net.minecraft.world.item.ItemStack;

public class ChanceOutput { //This is a data structure to hold output items and their changes and let me roll them when needed.
    private ItemStack item;
    private float chance;
    private boolean allOrNothingFlag;
    private boolean specialFlag;

    public ChanceOutput(ItemStack item, float chance, boolean aonFlag, boolean special){
        this.item = item.copy();
        this.chance = chance;
        this.allOrNothingFlag = aonFlag;
        this.specialFlag = special;
    }

    public ItemStack rollItem(){
        if(allOrNothingFlag){
            if(Math.random() < chance) return item.copy(); //All or nothing, either get nothing or the full amount
            else return ItemStack.EMPTY;
        }

        int amount = 0;
        for (int i = 0; i<item.getCount(); i++){ //TODO: replace with an actual method to just roll once.
            if(Math.random() < chance) amount++;
        }
        if(amount == 0) return ItemStack.EMPTY;
        else return new ItemStack(item.getItem(), amount);
    }

    public ItemStack getItem(){
        return item.copy();
    }

    public float getChance() {
        return chance;
    }

    public boolean isAllOrNothingFlag() {
        return allOrNothingFlag;
    }

    public boolean isSpecial(){return specialFlag; };
}
