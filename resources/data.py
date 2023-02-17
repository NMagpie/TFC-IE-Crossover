from enum import Enum, auto

from mcresources import ResourceManager, loot_tables
from mcresources.type_definitions import Json

from constants import *
from mcresources import utils

from recipes import fluid_ingredient

class Size(Enum):
    tiny = auto()
    very_small = auto()
    small = auto()
    normal = auto()
    large = auto()
    very_large = auto()
    huge = auto()


class Weight(Enum):
    very_light = auto()
    light = auto()
    medium = auto()
    heavy = auto()
    very_heavy = auto()

class Category(Enum):
    other = auto()

def generate(rm: ResourceManager):
    ### TAGS ###

    # Ore tags
    ores = ['aluminum', 'lead', 'uranium']
    for ore in ores:
        rm.block_tag('forge:ores', '#forge:ores/%s' % ore)
        rm.block_tag('forge:ores/%s' % ore, '#tfc_ie_addon:ores/%s/poor' % ore, '#tfc_ie_addon:ores/%s/normal' % ore, '#tfc_ie_addon:ores/%s/rich' % ore)
        rm.item_tag('ore_pieces', 'tfc_ie_addon:ore/poor_%s' % ore, 'tfc_ie_addon:ore/normal_%s' % ore, 'tfc_ie_addon:ore/rich_%s' % ore)
        rm.item_tag('small_ore_pieces', 'tfc_ie_addon:ore/small_%s' % ore)

        for rock in TFC_ROCKS.keys():
            rm.block_tag('ores/%s/poor' % ore, 'tfc_ie_addon:ore/poor_%s/%s' % (ore, rock))
            rm.block_tag('ores/%s/normal' % ore, 'tfc_ie_addon:ore/normal_%s/%s' % (ore, rock))
            rm.block_tag('ores/%s/rich' % ore, 'tfc_ie_addon:ore/rich_%s/%s' % (ore, rock))

    rm.item_tag('forge:fiber_hemp', 'tfc:jute_fiber')

    rm.item_tag('forge:fabric_hemp', 'tfc:burlap_cloth')

def match_entity_tag(tag: str):
    return {
        'condition': 'minecraft:entity_properties',
        'predicate': {
            'type': '#' + tag
        },
        'entity': 'this'
    }

def item_heat(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, heat_capacity: float, melt_temperature: Optional[float] = None, mb: Optional[int] = None):
    if melt_temperature is not None:
        forging_temperature = round(melt_temperature * 0.6)
        welding_temperature = round(melt_temperature * 0.8)
    else:
        forging_temperature = welding_temperature = None
    if mb is not None:
        # Interpret heat capacity as a specific heat capacity - so we need to scale by the mB present. Baseline is 100 mB (an ingot)
        # Higher mB = higher heat capacity = heats and cools slower = consumes proportionally more fuel
        heat_capacity = round(10 * heat_capacity * mb) / 1000
    rm.data(('tfc', 'item_heats', name_parts), {
        'ingredient': utils.ingredient(ingredient),
        'heat_capacity': heat_capacity,
        'forging_temperature': forging_temperature,
        'welding_temperature': welding_temperature
    })

def item_size(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, size: Size, weight: Weight):
    rm.data(('tfc', 'item_sizes', name_parts), {
        'ingredient': utils.ingredient(ingredient),
        'size': size.name,
        'weight': weight.name
    })
