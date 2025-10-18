from mcresources import ResourceManager, loot_tables

from constants import *

MetalItem = NamedTuple('MetalItem', type=str, smelt_amount=int, parent_model=str, tag=Optional[str], mold=bool)

METAL_ITEMS: Dict[str, MetalItem] = {
    'ingot': MetalItem('all', 100, 'item/generated', 'c:ingots', True),
    'double_ingot': MetalItem('part', 200, 'item/generated', 'c:double_ingots', False),
    'sheet': MetalItem('part', 200, 'item/generated', 'c:sheets', False)
}

METAL_BLOCKS: Dict[str, MetalItem] = {
    'block': MetalItem('part', 100, 'block/block', None, False),
    'block_slab': MetalItem('part', 50, 'block/block', None, False),
    'block_stairs': MetalItem('part', 75, 'block/block', None, False)
}

METAL_ITEMS_AND_BLOCKS: Dict[str, MetalItem] = {**METAL_ITEMS, **METAL_BLOCKS}


def generate(rm: ResourceManager):
    for metal, metal_data in METALS.items():

        # Metal Items

        for metal_item, metal_item_data in METAL_ITEMS.items():
            if metal_item != 'ingot':
                texture = 'tfc_ie_addon:item/metal/%s/%s' % (metal_item, metal)
                the_item = rm.item_model(('metal', '%s' % metal_item, '%s' % metal), texture, parent=metal_item_data.parent_model)
                the_item.with_lang(lang('%s %s', metal, metal_item))

        # Metal Blocks
        for metal_block, metal_block_data in METAL_BLOCKS.items():
            if metal_block_data.type in metal_data.types:
                if metal_block == 'block' or metal_block == 'block_stairs' or metal_block == 'block_slab':
                    block = rm.blockstate(('metal', 'block', metal)).with_lang(lang('%s plated block', metal)).with_item_model().with_block_loot('tfc_ie_addon:metal/block/%s' % metal)
                    if metal == 'uranium':
                        block.with_block_model(parent='block/cube_bottom_top', textures={'side': 'tfc_ie_addon:block/metal/block/uranium', 'top': 'tfc_ie_addon:block/metal/block/uranium_top', 'bottom': 'tfc_ie_addon:block/metal/block/uranium_top'})
                        block.make_slab(side_texture='tfc_ie_addon:block/metal/block/uranium', top_texture='tfc_ie_addon:block/metal/block/uranium_top', bottom_texture='tfc_ie_addon:block/metal/block/uranium_top')
                        block.make_stairs('tfc_ie_addon:metal/block/uranium')
                    else:
                        block.with_block_model()
                        block.make_slab()
                    rm.block(('metal', 'block', '%s_slab' % metal)).with_lang(lang('%s plated slab', metal))
                    rm.block(('metal', 'block', '%s_stairs' % metal)).with_lang(lang('%s plated stairs', metal)).with_block_loot('tfc_ie_addon:metal/block/%s_stairs' % metal)
                    slab_loot(rm, 'tfc_ie_addon:metal/block/%s_slab' % metal)

        rm.blockstate(('fluid', 'metal', metal)).with_block_model({'particle': 'block/lava_still'}, parent=None).with_lang(lang('Molten %s', metal))
        rm.lang('fluid.tfc_ie_addon.metal.%s' % metal, lang('%s', metal))

        item = rm.custom_item_model(('bucket', 'metal', metal), 'neoforge:fluid_container', {
            'parent': 'neoforge:item/bucket',
            'fluid': 'tfc_ie_addon:metal/%s' % metal
        })

        item.with_lang(lang('molten %s bucket', metal))
        rm.lang('metal.tfc_ie_addon.%s' % metal, lang(metal))


def slab_loot(rm: ResourceManager, loot: str):
    return rm.block_loot(loot, {
        'name': loot,
        'functions': [{
            'function': 'minecraft:set_count',
            'conditions': [loot_tables.block_state_property(loot + '[type=double]')],
            'count': 2,
            'add': False
        }]
    })
