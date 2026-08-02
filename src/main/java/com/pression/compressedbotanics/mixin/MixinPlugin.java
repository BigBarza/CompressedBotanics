package com.pression.compressedbotanics.mixin;

import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        //The mixin involving ponders should only load if ponder is present.
        if(mixinClassName.contains("RedStringPonderCompatMixin")) return LoadingModList.get().getModFileById("ponder") != null;
        if(mixinClassName.contains("PonderSceneMixin")) return LoadingModList.get().getModFileById("ponder") != null;
        if(mixinClassName.contains("SparkPonderCompatMixin")) return LoadingModList.get().getModFileById("ponder") != null;
        if(mixinClassName.contains("JEI")) return LoadingModList.get().getModFileById("jei") != null;
        if(mixinClassName.contains("EMI")) return LoadingModList.get().getModFileById("emi") != null;
        else return true;
    }


    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return ""; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return List.of(); }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
