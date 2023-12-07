from mcresources import ResourceManager, utils, loot_tables

from constants import *
from data import item_heat
from recipes import anvil_recipe, Rules, welding_recipe, heat_recipe, casting_recipe

MetalItem = NamedTuple('MetalItem', type=str, smelt_amount=int, parent_model=str, tag=Optional[str], mold=bool)

METAL_ITEMS: Dict[str, MetalItem] = {
    'ingot': MetalItem('all', 100, 'item/generated', 'forge:ingots', True),
    'double_ingot': MetalItem('part', 200, 'item/generated', 'forge:double_ingots', False),
    'sheet': MetalItem('part', 200, 'item/generated', 'forge:sheets', False)
}

METAL_BLOCKS: Dict[str, MetalItem] = {
    'block': MetalItem('part', 100, 'block/block', None, False),
    'block_slab': MetalItem('part', 50, 'block/block', None, False),
    'block_stairs': MetalItem('part', 75, 'block/block', None, False)
}

METAL_ITEMS_AND_BLOCKS: Dict[str, MetalItem] = {**METAL_ITEMS, **METAL_BLOCKS}


def generate(rm: ResourceManager):
    ore_heats(rm)
    for metal, metal_data in METALS.items():
        rm.data(('tfc', 'metals', metal), {
            'tier': metal_data.tier,
            'fluid': 'tfc_ie_addon:metal/%s' % metal,
            'melt_temperature': metal_data.melt_temperature,
            'specific_heat_capacity': metal_data.specific_heat_capacity(),
            'ingots': utils.ingredient('#forge:ingots/%s' % metal),
            'double_ingots': utils.ingredient('#forge:double_ingots/%s' % metal),
            'sheets': utils.ingredient('#forge:sheets/%s' % metal)
        })

        for item, item_data in METAL_ITEMS_AND_BLOCKS.items():
            if item_data.type in metal_data.types or item_data.type == 'all':
                if 'block_' in item:
                    item_name = 'tfc_ie_addon:metal/block/%s_%s' % (metal, item.replace('block_', ''))
                elif 'ingot' == item:
                    item_name = 'immersiveengineering:ingot_%s' % metal
                else:
                    item_name = 'tfc_ie_addon:metal/%s/%s' % (item, metal)
                if item_data.tag is not None:
                    rm.item_tag(item_data.tag, '#%s/%s' % (item_data.tag, metal))
                    rm.item_tag(item_data.tag + '/' + metal, item_name)

                rm.item_tag('tfc:metal_item/%s' % metal, item_name)
                item_heat(rm, 'metal/%s_%s' % (metal, item), '#%s/%s' % (item_data.tag, metal) if item_data.tag else item_name, metal_data.ingot_heat_capacity(), metal_data.melt_temperature, mb=item_data.smelt_amount)

        def item(_variant: str) -> str:
            return '#forge:%s/%s' % (_variant, metal)

        # Metal Items

        for metal_item, metal_item_data in METAL_ITEMS.items():
            if metal_item != 'ingot':
                texture = 'tfc_ie_addon:item/metal/%s/%s' % (metal_item, metal)
                the_item = rm.item_model(('metal', '%s' % metal_item, '%s' % metal), texture, parent=metal_item_data.parent_model)
                the_item.with_lang(lang('%s %s', metal, metal_item))

        # Metal Blocks
        for metal_block, metal_block_data in METAL_BLOCKS.items():
            if metal_block_data.type in metal_data.types:
                rm.block_tag('minecraft:mineable/pickaxe', 'tfc_ie_addon:metal/%s/%s' % (metal_block, metal) if metal_block != 'block_slab' and metal_block != 'block_stairs' else 'tfc_ie_addon:metal/block/%s_%s' % (metal, metal_block.replace('block_', '')))
                if metal_block == 'block' or metal_block == 'block_stairs' or metal_block == 'block_slab':
                    block = rm.blockstate(('metal', 'block', metal)).with_lang(lang('%s plated block', metal)).with_item_model().with_block_loot('tfc_ie_addon:metal/block/%s' % metal).with_tag('minecraft:mineable/pickaxe')
                    if metal == 'uranium':
                        block.with_block_model(parent='block/cube_bottom_top', textures={'side': 'tfc_ie_addon:block/metal/block/uranium', 'top': 'tfc_ie_addon:block/metal/block/uranium_top', 'bottom': 'tfc_ie_addon:block/metal/block/uranium_top'})
                        block.make_slab(side_texture='tfc_ie_addon:block/metal/block/uranium', top_texture='tfc_ie_addon:block/metal/block/uranium_top', bottom_texture='tfc_ie_addon:block/metal/block/uranium_top')
                        block.make_stairs('tfc_ie_addon:metal/block/uranium')
                    else:
                        block.with_block_model()
                        block.make_slab()
                    rm.block(('metal', 'block', '%s_slab' % metal)).with_lang(lang('%s plated slab', metal)).with_tag('minecraft:slab')
                    rm.block(('metal', 'block', '%s_stairs' % metal)).with_lang(lang('%s plated stairs', metal)).with_block_loot('tfc_ie_addon:metal/block/%s_stairs' % metal).with_tag('minecraft:stairs')
                    slab_loot(rm, 'tfc_ie_addon:metal/block/%s_slab' % metal)

        anvil_recipe(rm, '%s_sheet' % metal, 'tfc_ie_addon:metal/double_ingot/%s' % metal, 'tfc_ie_addon:metal/sheet/%s' % metal, metal_data.tier, Rules.hit_last, Rules.hit_second_last, Rules.hit_third_last)

        if metal == 'aluminum':
            anvil_recipe(rm, 'aluminum_rod', item('ingots'), '2 immersiveengineering:stick_aluminum', metal_data.tier, Rules.bend_last, Rules.draw_second_last, Rules.draw_third_last)

        welding_recipe(rm, '%s_double_ingot' % metal, item('ingots'), item('ingots'), 'tfc_ie_addon:metal/double_ingot/%s' % metal, metal_data.tier - 1)

        melt_metal = metal if metal_data.melt_metal is None else metal_data.melt_metal
        for item, item_data in METAL_ITEMS_AND_BLOCKS.items():
            if item == 'ingot':
                item_name = 'immersiveengineering:ingot_%s' % metal
            elif item_data.type in metal_data.types:
                item_name = 'tfc_ie_addon:metal/block/%s_%s' % (metal, item.replace('block_', '')) if 'block_' in item else 'tfc_ie_addon:metal/%s/%s' % (item, metal)
            heat_recipe(rm, ('metal', '%s_%s' % (metal, item)), item_name, metal_data.melt_temperature, None, '%d tfc_ie_addon:metal/%s' % (item_data.smelt_amount, melt_metal))

        for item, item_data in METAL_ITEMS.items():
            if item == 'ingot' or (item_data.mold and 'tool' in metal_data.types and metal_data.tier <= 2):
                casting_recipe(rm, '%s_%s' % (item, metal), item, metal, item_data.smelt_amount, 0.1 if item == 'ingot' else 1)

        rm.blockstate(('fluid', 'metal', metal)).with_block_model({'particle': 'block/lava_still'}, parent=None).with_lang(lang('Molten %s', metal))
        rm.lang('fluid.tfc_ie_addon.metal.%s' % metal, lang('Molten %s', metal))
        rm.fluid_tag(metal, 'tfc_ie_addon:metal/%s' % metal, 'tfc_ie_addon:metal/flowing_%s' % metal)
        rm.fluid_tag('tfc:molten_metals', *['tfc_ie_addon:metal/%s' % metal])

        item = rm.custom_item_model(('bucket', 'metal', metal), 'forge:fluid_container', {
            'parent': 'forge:item/bucket',
            'fluid': 'tfc_ie_addon:metal/%s' % metal
        })

        item.with_lang(lang('molten %s bucket', metal))
        rm.lang('metal.tfc_ie_addon.%s' % metal, lang(metal))

        rm.item_tag('tfc:pileable_ingots', 'immersiveengineering:ingot_%s' % metal)
        rm.item_tag('tfc:pileable_double_ingots', 'tfc_ie_addon:metal/double_ingot/%s' % metal)
        rm.item_tag('tfc:pileable_sheets', 'tfc_ie_addon:metal/sheet/%s' % metal)

        rm.item_tag('forge:rods/all_metal', '#forge:rods/wrought_iron')


def ore_heats(rm: ResourceManager):
    for ore, ore_data in ORES.items():
        if ore_data.metal and ore_data.graded:
            metal_data = METALS[ore_data.metal]
            temp = METALS[ore_data.metal].melt_temperature
            item_heat(rm, ('ore', ore), ['tfc_ie_addon:ore/small_%s' % ore, 'tfc_ie_addon:ore/normal_%s' % ore, 'tfc_ie_addon:ore/poor_%s' % ore, 'tfc_ie_addon:ore/rich_%s' % ore], metal_data.ingot_heat_capacity(), int(metal_data.melt_temperature), mb=40)
            heat_recipe(rm, ('ore', 'small_%s' % ore), 'tfc_ie_addon:ore/small_%s' % ore, temp, None, '10 tfc_ie_addon:metal/%s' % ore_data.metal)
            heat_recipe(rm, ('ore', 'poor_%s' % ore), 'tfc_ie_addon:ore/poor_%s' % ore, temp, None, '15 tfc_ie_addon:metal/%s' % ore_data.metal)
            heat_recipe(rm, ('ore', 'normal_%s' % ore), 'tfc_ie_addon:ore/normal_%s' % ore, temp, None, '25 tfc_ie_addon:metal/%s' % ore_data.metal)
            heat_recipe(rm, ('ore', 'rich_%s' % ore), 'tfc_ie_addon:ore/rich_%s' % ore, temp, None, '35 tfc_ie_addon:metal/%s' % ore_data.metal)


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
