package trinsdar.gt4r.loader.machines;

import com.google.common.collect.ImmutableSet;
import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.pipe.PipeSize;
import muramasa.antimatter.pipe.types.Cable;
import muramasa.antimatter.pipe.types.Wire;
import muramasa.antimatter.recipe.ingredient.AntimatterIngredient;
import muramasa.antimatter.recipe.ingredient.TagIngredient;
import muramasa.antimatter.util.Utils;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static com.google.common.collect.ImmutableMap.of;
import static muramasa.antimatter.Data.*;
import static muramasa.antimatter.recipe.ingredient.AntimatterIngredient.of;
import static trinsdar.gt4r.data.GT4RData.*;
import static trinsdar.gt4r.data.Materials.*;
import static trinsdar.gt4r.data.RecipeMaps.ASSEMBLING;

public class AssemblyLoader {
    public static void init() {
        AntimatterAPI.all(Wire.class,t -> {
            Cable<?> cable = AntimatterAPI.get(Cable.class, "cable" + "_" + t.getMaterial().getId());
            if (cable == null) return;
            ImmutableSet<PipeSize> sizes = t.getSizes();
            sizes.forEach(size -> {
                Item wireItem = t.getBlockItem(size);
                Item cableItem = cable.getBlockItem(size);
                ASSEMBLING.RB().ii(of(wireItem,1), of(INGOT.get(Rubber, size.getCableThickness()))).io(new ItemStack(cableItem,1)).add(size.getCableThickness()* 20L,8);
            });
        });
        ASSEMBLING.RB().ii(of(getTag("forge", "rods/wooden"), 1), of(getTag("minecraft", "coals"), 1)).io(new ItemStack(Items.TORCH, 4)).add(400, 1);
        ASSEMBLING.RB().ii(of(getTag("forge", "string"), 1), of(getTag("forge", "slimeballs"), 1)).io(new ItemStack(Items.LEAD, 2)).add(200, 2);
        ASSEMBLING.RB().ii(of(CircuitBoardAdv, 1), of(AdvCircuitParts, 2)).io(new ItemStack(CircuitAdv, 1)).add(1600, 2);
        ASSEMBLING.RB().ii(of(CircuitBoardProcessor, 1), of(CircuitDataStorage, 1)).io(new ItemStack(CircuitDataControl, 2)).add(3200, 4);
    }

    public static ITag.INamedTag<Item> getTag(String domain, String path){
        return Utils.getItemTag(new ResourceLocation(domain, path));
    }
}
