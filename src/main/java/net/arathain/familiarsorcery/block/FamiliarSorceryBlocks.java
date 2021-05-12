package net.arathain.familiarsorcery.block;


import net.arathain.familiarsorcery.FamiliarSorcery;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FamiliarSorceryBlocks {
    private static final Map<Identifier, BlockItem> ITEMS = new HashMap<>();
    private static final Map<Identifier, Block> BLOCKS = new HashMap<>();

    public static Block AMPLIFIER_CRYSTAL_BLOCK = create("amplifier_crystal_block", new PillarBlock(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK).breakByTool(FabricToolTags.PICKAXES).requiresTool().luminance(8)), ItemGroup.BUILDING_BLOCKS);
    public static Block AMPLIFIER_CRYSTAL = create("amplifier_crystal", new AmplifierCrystal(FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK).breakByTool(FabricToolTags.PICKAXES).requiresTool().nonOpaque().noCollision().luminance(8)), ItemGroup.BUILDING_BLOCKS);
    public static Block LIVINGWOOD_LOG = create("livingwood_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.WARPED_HYPHAE).breakByTool(FabricToolTags.AXES).ticksRandomly()), ItemGroup.BUILDING_BLOCKS);
    public static Block LIVINGWOOD = create("livingwood", new PillarBlock(FabricBlockSettings.copyOf(Blocks.WARPED_HYPHAE).breakByTool(FabricToolTags.AXES).ticksRandomly()), ItemGroup.BUILDING_BLOCKS);
    public static Block LIVINGLEAF = create("livingleaf", new LeavesBlock(FabricBlockSettings.copyOf(Blocks.DARK_OAK_LEAVES).breakByTool(FabricToolTags.HOES)), ItemGroup.BUILDING_BLOCKS);

    public static void init() {
        for (Identifier id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, id, ITEMS.get(id));
        }
        for (Identifier id : BLOCKS.keySet()) {
            Registry.register(Registry.BLOCK, id, BLOCKS.get(id));
        }
    }
    public static void registerCutouts() {
        BlockRenderLayerMap.INSTANCE.putBlock(FamiliarSorceryBlocks.AMPLIFIER_CRYSTAL, RenderLayer.getCutout());
    }

    private static <B extends Block> B create(String name, B block, ItemGroup tab) {
        return create(name, block, new BlockItem(block, new Item.Settings().group(tab)));
    }
    private static <B extends Block> B create(String name, B block, BlockItem item) {
        create(name, block);
        if (item != null) {
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            ITEMS.put(new Identifier(FamiliarSorcery.MODID, name), item);
        }
        return block;
    }

    private static <B extends Block> B create(String name, B block) {
        BLOCKS.put(new Identifier(FamiliarSorcery.MODID, name), block);
        return block;
    }

    private static <I extends BlockItem> I create(String name, I item) {
        item.appendBlocks(Item.BLOCK_ITEMS, item);
        ITEMS.put(new Identifier(FamiliarSorcery.MODID, name), item);
        return item;
    }
}
