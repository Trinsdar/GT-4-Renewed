package trinsdar.gt4r.loader.crafting;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.datagen.providers.AntimatterRecipeProvider;
import muramasa.antimatter.pipe.PipeSize;
import muramasa.antimatter.pipe.PipeSize.*;

import muramasa.antimatter.pipe.types.Cable;
import muramasa.antimatter.pipe.types.Wire;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static muramasa.antimatter.Data.*;
import static muramasa.antimatter.pipe.PipeSize.*;
import static trinsdar.gt4r.data.Materials.Rubber;
import static trinsdar.gt4r.loader.crafting.CraftingHelper.criterion;

public class WireCablesPlates {
    @SuppressWarnings("unchecked")
    public static void loadRecipes(Consumer<IFinishedRecipe> output, AntimatterRecipeProvider provider) {
        AntimatterAPI.all(Wire.class, wire -> {
            Cable<?> cable = AntimatterAPI.get(Cable.class, "cable" + "_" + wire.getMaterial().getId());
            ImmutableSet<PipeSize> sizes = wire.getSizes();
            Map<PipeSize, Item> wires = sizes.stream().map(s -> new Pair<>(s, wire.getBlockItem(s))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            PipeSize[] val = values();
            for (int i = 1; i < val.length; i += 2) {
                twoToOne(wires, val[i-1], val[i], output,provider);
                oneToTwo(wires, val[i], val[i-1], output, provider);
                if (i > 1) {
                    fourToOne(wires, val[i-2], val[i], output, provider);
                }
            }
            if (cable != null){
                provider.shapeless(output, wire.getId() + "_cable", "cables", "has_wire", provider.hasSafeItem(wire.getBlockItem(VTINY)), new ItemStack(cable.getBlockItem(VTINY)), wire.getBlockItem(VTINY), INGOT.getMaterialTag(Rubber));
            }
            if (wire.getMaterial().has(PLATE)) {
                provider.shapeless(output, "platewire","wire","has_cutter", criterion(WIRE_CUTTER.getTag(), provider),
                        new ItemStack(wires.get(VTINY)),
                        WIRE_CUTTER.getTag(), PLATE.getMaterialTag(wire.getMaterial()));
            }
        });

        INGOT.all().stream().filter(p -> p.has(PLATE)).forEach(p -> provider.shapeless(output, "ingothammer","plate", "has_hammer", criterion(HAMMER.getTag(), provider), new ItemStack(PLATE.get(p),1),
                HAMMER.getTag(), INGOT.getMaterialTag(p)));
    }

    private static void twoToOne(Map<PipeSize, Item> wires, PipeSize from, PipeSize to, Consumer<IFinishedRecipe> output, AntimatterRecipeProvider provider) {
        provider.shapeless(output,"twoone","wire","has_cutter",criterion(WIRE_CUTTER.getTag(), provider),
                new ItemStack(wires.get(to),1),wires.get(from),wires.get(from));
    }

    private static void oneToTwo(Map<PipeSize, Item> wires, PipeSize from, PipeSize to, Consumer<IFinishedRecipe> output, AntimatterRecipeProvider provider) {
        provider.shapeless(output,"onetwo","wire","has_cutter",criterion(WIRE_CUTTER.getTag(), provider),
                new ItemStack(wires.get(to),2),wires.get(from));
    }

    private static void fourToOne(Map<PipeSize, Item> wires, PipeSize from, PipeSize to, Consumer<IFinishedRecipe> output, AntimatterRecipeProvider provider) {
        provider.shapeless(output,"fourone","wire","has_cutter",criterion(WIRE_CUTTER.getTag(), provider),
                new ItemStack(wires.get(to),1),wires.get(from),wires.get(from),wires.get(from),wires.get(from));
    }
}
