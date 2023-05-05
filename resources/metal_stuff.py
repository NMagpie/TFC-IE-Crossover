from typing import NamedTuple, Dict, Optional, Set

from mcresources import ResourceManager, utils

from assets import domain_divider

from constants import lang
from data import item_heat
from recipes import anvil_recipe, Rules, welding_recipe, heat_recipe, casting_recipe

class Metal(NamedTuple):
    tier: int
    types: Set[str]
    heat_capacity_base: float  # Do not access directly, use one of specific or ingot heat capacity.
    melt_temperature: float
    melt_metal: Optional[str]

    def specific_heat_capacity(self) -> float: return round(300 / self.heat_capacity_base) / 100_000
    def ingot_heat_capacity(self) -> float: return 1 / self.heat_capacity_base


MetalItem = NamedTuple('MetalItem', type=str, smelt_amount=int, parent_model=str, tag=Optional[str], mold=bool)

METALS: Dict[str, Metal] = {
    'aluminum': Metal(1, {'part'}, 0.35, 650, None),
    'lead': Metal(2, {'part'}, 0.35, 500, None),
    'uranium': Metal(3, {'part'}, 0.35, 1250, None),

    'constantan': Metal(2, {'part'}, 0.35, 750, None),
    'electrum': Metal(3, {'part'}, 0.35, 900, None),
}

METAL_ITEMS: Dict[str, MetalItem] = {
    'ingot': MetalItem('all', 100, 'item/generated', 'forge:ingots', True),
    'double_ingot': MetalItem('part', 200, 'item/generated', 'forge:double_ingots', False),
    'sheet': MetalItem('part', 200, 'item/generated', 'forge:sheets', False),
}

def generate(rm: ResourceManager):
    ore_heats(rm)
    for metal, metal_data in METALS.items():
        rm.data(('tfc', 'metals', metal), {
            'tier': metal_data.tier,
            'fluid': 'tfc_ie_addon:metal/%s' % metal,
            'melt_temperature': metal_data.melt_temperature,
            'specific_heat_capacity': metal_data.specific_heat_capacity(),
            'ingots': utils.ingredient('#forge:ingots/%s' % metal),
            'sheets': utils.ingredient('#forge:sheets/%s' % metal)
        })

        for item, item_data in METAL_ITEMS.items():
            (domain, divider) = domain_divider(item)

            item_name = '%s%s%s%s' % (domain, item, divider, metal)
            if item_data.tag is not None:
                rm.item_tag(item_data.tag, '#%s/%s' % (item_data.tag, metal))
                rm.item_tag(item_data.tag + '/' + metal, item_name)
                ingredient = utils.item_stack('#%s/%s' % (item_data.tag, metal))
            else:
                ingredient = utils.item_stack(item_name)
            rm.item_tag('tfc:metal_item/%s' % metal, item_name)
            item_heat(rm, ('metal', metal + '_' + item), ingredient, metal_data.ingot_heat_capacity(), metal_data.melt_temperature, mb=item_data.smelt_amount)

        def item(_variant: str) -> str:
                return '#forge:%s/%s' % (_variant, metal)

        # Metal Items

        for metal_item, metal_item_data in METAL_ITEMS.items():
            if metal_item != 'ingot':
                texture = 'tfc_ie_addon:item/metal/%s/%s' % (metal_item, metal)
                the_item = rm.item_model(('metal', '%s' % metal_item, '%s' % metal), texture, parent=metal_item_data.parent_model)
                the_item.with_lang(lang('%s %s', metal, metal_item))

        anvil_recipe(rm, '%s_sheet' % metal, 'tfc_ie_addon:metal/double_ingot/%s' % metal, 'tfc_ie_addon:metal/sheet/%s' % metal, metal_data.tier, Rules.hit_last, Rules.hit_second_last, Rules.hit_third_last)

        if metal == 'aluminum':
            anvil_recipe(rm, 'aluminum_rod', item('ingots'), '2 immersiveengineering:stick_aluminum', metal_data.tier, Rules.bend_last, Rules.draw_second_last, Rules.draw_third_last)

        welding_recipe(rm, '%s_double_ingot' % metal, item('ingots'), item('ingots'), 'tfc_ie_addon:metal/double_ingot/%s' % metal, metal_data.tier - 1)

        for item, item_data in METAL_ITEMS.items():
            if item_data.type == 'all' or item_data.type in metal_data.types:
                (domain, divider) = domain_divider(item)
                heat_recipe(rm, ('metal', '%s_%s' % (metal, item)), '%s%s%s%s' % (domain, item, divider, metal), metal_data.melt_temperature, None, '%d tfc_ie_addon:metal/%s' % (item_data.smelt_amount, metal))

        for item, item_data in METAL_ITEMS.items():
            if item == 'ingot' or (item_data.mold and 'tool' in metal_data.types and metal_data.tier <= 2):
                casting_recipe(rm, '%s_%s' % (item, metal), item, metal, item_data.smelt_amount, 0.1 if item == 'ingot' else 1)

        rm.blockstate(('fluid', 'metal', metal)).with_block_model({'particle': 'block/lava_still'}, parent=None).with_lang(lang('Molten %s', metal))
        rm.lang('fluid.tfc_ie_addon.metal.%s' % metal, lang('Molten %s', metal))
        rm.fluid_tag(metal, 'tfc_ie_addon:metal/%s' % metal, 'tfc_ie_addon:metal/flowing_%s' % metal)
        rm.fluid_tag('tfc:molten_metals', *['tfc_ie_addon:metal/%s' % metal])

        item = rm.custom_item_model(('bucket', 'metal', metal), 'forge:bucket', {
            'parent': 'forge:item/bucket',
            'fluid': 'tfc_ie_addon:metal/%s' % metal
        })

        item.with_lang(lang('molten %s bucket', metal))
        rm.lang('metal.tfc_ie_addon.%s' % metal, lang(metal))

        rm.item_tag('tfc:pileable_ingots', 'immersiveengineering:ingot_%s' % metal)
        rm.item_tag('tfc:pileable_sheets', 'tfc_ie_addon:metal/sheet/%s' % metal)

        rm.item_tag('forge:plates/%s' % metal, 'tfc_ie_addon:metal/sheet/%s' % metal)

        rm.item_tag('forge:plates/iron', 'tfc:metal/sheet/wrought_iron')

def ore_heats(rm: ResourceManager):
    for ore in ['aluminum', 'lead', 'uranium']:
        metal_data = METALS[ore]
        item_heat(rm, ('ore', ore), ['tfc_ie_addon:ore/small_%s' % ore, 'tfc_ie_addon:ore/normal_%s' % ore, 'tfc_ie_addon:ore/poor_%s' % ore, 'tfc_ie_addon:ore/rich_%s' % ore], metal_data.ingot_heat_capacity(), int(metal_data.melt_temperature), mb=40)
        temp = METALS['%s' % ore].melt_temperature
        heat_recipe(rm, ('ore', 'small_%s' % ore), 'tfc_ie_addon:ore/small_%s' % ore, temp, None, '10 tfc_ie_addon:metal/%s' % ore)
        heat_recipe(rm, ('ore', 'poor_%s' % ore), 'tfc_ie_addon:ore/poor_%s' % ore, temp, None, '15 tfc_ie_addon:metal/%s' % ore)
        heat_recipe(rm, ('ore', 'normal_%s' % ore), 'tfc_ie_addon:ore/normal_%s' % ore, temp, None, '25 tfc_ie_addon:metal/%s' % ore)
        heat_recipe(rm, ('ore', 'rich_%s' % ore), 'tfc_ie_addon:ore/rich_%s' % ore, temp, None, '35 tfc_ie_addon:metal/%s' % ore)
