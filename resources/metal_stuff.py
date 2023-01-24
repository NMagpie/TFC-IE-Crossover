from typing import NamedTuple, Dict, Optional, Set

from mcresources import ResourceManager, utils

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
            'sheets': utils.ingredient('#forge:plates/%s' % metal)
        })

        # def item(_variant: str) -> str:
        #     return 'forge:%s/%s' % (_variant, metal)

        def item(_variant: str) -> str:
            return 'immersiveengineering:%s_%s' % (_variant, metal)

        # Metal Items

        #TODO ADD ANVIL RECIPES
        # anvil_recipe(rm, '%s_plate' % metal, item('ingots'), item('plates'), metal_data.tier, Rules.hit_last, Rules.hit_second_last, Rules.hit_third_last)
        # anvil_recipe(rm, '%s_rod' % metal, item('ingots'), '2 forge:rods/%s' % metal, metal_data.tier, Rules.bend_last, Rules.draw_second_last, Rules.draw_third_last)

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

        # rm.item_tag('tfc:pileable_ingots', '#immersiveengineering:ingots/%s' % metal)
        # rm.item_tag('tfc:pileable_sheets', '#immersiveengineering:plates/%s' % metal)

        rm.item_tag('tfc:pileable_ingots', 'immersiveengineering:ingot_%s' % metal)
        rm.item_tag('tfc:pileable_sheets', 'immersiveengineering:plate_%s' % metal)

def ore_heats(rm: ResourceManager):
    for ore in METALS.keys():
        metal_data = METALS['%s' % ore]
        item_heat(rm, ('ore', ore), ['tfc_ie_addon:ore/small_%s' % ore, 'tfc_ie_addon:ore/normal_%s' % ore, 'tfc_ie_addon:ore/poor_%s' % ore, 'tfc_ie_addon:ore/rich_%s' % ore], metal_data.ingot_heat_capacity(), int(metal_data.melt_temperature), mb=40)
        temp = METALS['%s' % ore].melt_temperature
        heat_recipe(rm, ('ore', 'small_%s' % ore), 'tfc_ie_addon:ore/small_%s' % ore, temp, None, '10 tfc_ie_addon:metal/%s' % ore)
        heat_recipe(rm, ('ore', 'poor_%s' % ore), 'tfc_ie_addon:ore/poor_%s' % ore, temp, None, '15 tfc_ie_addon:metal/%s' % ore)
        heat_recipe(rm, ('ore', 'normal_%s' % ore), 'tfc_ie_addon:ore/normal_%s' % ore, temp, None, '25 tfc_ie_addon:metal/%s' % ore)
        heat_recipe(rm, ('ore', 'rich_%s' % ore), 'tfc_ie_addon:ore/rich_%s' % ore, temp, None, '35 tfc_ie_addon:metal/%s' % ore)
