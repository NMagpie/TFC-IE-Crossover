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
    for ore in ADDON_ORES.keys():
        rm.block_tag('forge:ores', '#forge:ores/%s' % ore)
        rm.block_tag('forge:ores/%s' % ore, '#tfc_ie_addon:ores/%s/poor' % ore, '#tfc_ie_addon:ores/%s/normal' % ore, '#tfc_ie_addon:ores/%s/rich' % ore)
        rm.item_tag('tfc:ore_pieces', 'tfc_ie_addon:ore/poor_%s' % ore, 'tfc_ie_addon:ore/normal_%s' % ore, 'tfc_ie_addon:ore/rich_%s' % ore)
        rm.item_tag('tfc:small_ore_pieces', 'tfc_ie_addon:ore/small_%s' % ore)

        for rock in TFC_ROCKS.keys():
            rm.block_tag('ores/%s/poor' % ore, 'tfc_ie_addon:ore/poor_%s/%s' % (ore, rock))
            rm.block_tag('ores/%s/normal' % ore, 'tfc_ie_addon:ore/normal_%s/%s' % (ore, rock))
            rm.block_tag('ores/%s/rich' % ore, 'tfc_ie_addon:ore/rich_%s/%s' % (ore, rock))

            rm.block_tag(needs_tool(ADDON_ORES[ore]), 'tfc_ie_addon:ore/poor_%s/%s' % (ore, rock), 'tfc_ie_addon:ore/normal_%s/%s' % (ore, rock), 'tfc_ie_addon:ore/rich_%s/%s' % (ore, rock))

    # JUTE AND FIBER TAGS

    rm.item_tag('forge:fiber_hemp', 'tfc:jute_fiber')

    rm.item_tag('forge:fabric_hemp', 'tfc:burlap_cloth')

    # FLUIDS AND BUCKETS TAGS

    ie_fluids = [
        'acetaldehyde',
        'biodiesel',
        'concrete',
        'creosote',
        'ethanol',
        'herbicide',
        'phenolic_resin',
        'plantoil',
        'redstone_acid',
    ]

    for fluid in ie_fluids:
        rm.fluid_tag('tfc:usable_in_wooden_bucket', 'immersiveengineering:%s' % fluid)
        rm.fluid_tag('tfc:usable_in_red_steel_bucket', 'immersiveengineering:%s' % fluid)

    # DUSTS TAGS

    rm.item_tag('forge:dusts/sulfur', 'tfc:powder/sulfur')

    rm.item_tag('forge:dusts/coal_coke', 'tfc:powder/graphite')

    rm.item_tag('forge:dusts/saltpeter', 'tfc:powder/saltpeter')

    # METAL & SHEETS TAGS

    [rm.item_tag('forge:plates/%s' % metal, 'tfc:metal/sheet/%s' % metal) for metal in TFC_METALS]

    # Item Heats

    item_heat(rm, 'slag', 'immersiveengineering:slag', 0.6)

    # TOOL TAGS

    metal_hammers = ['bismuth_bronze', 'black_bronze', 'bronze', 'copper', 'wrought_iron', 'steel',
                     'black_steel', 'blue_steel', 'red_steel']

    [rm.item_tag('immersiveengineering:tools/hammers', 'tfc:metal/hammer/%s' % metal) for metal in metal_hammers]

    stone_hammers = ['igneous_intrusive', 'igneous_extrusive', 'metamorphic', 'sedimentary']

    [rm.item_tag('immersiveengineering:tools/hammers', 'tfc:stone/hammer/%s' % stone) for stone in stone_hammers]

    # SANDSTONE TAG

    # [[
    #     rm.item_tag('tfc:sandstone', 'tfc:%s_sandstone/%s' % (type, color))
    #     for color in SANDSTONE_COLORS]
    #     for type in SANDSTONE_TYPES]

def needs_tool(_tool: str) -> str:
    return {
        'wood': 'forge:needs_wood_tool', 'stone': 'forge:needs_wood_tool',
        'copper': 'minecraft:needs_stone_tool',
        'bronze': 'minecraft:needs_iron_tool',
        'iron': 'minecraft:needs_iron_tool', 'wrought_iron': 'minecraft:needs_iron_tool',
        'diamond': 'minecraft:needs_diamond_tool', 'steel': 'minecraft:needs_diamond_tool',
        'netherite': 'forge:needs_netherite_tool', 'black_steel': 'forge:needs_netherite_tool',
        'colored_steel': 'tfc:needs_colored_steel_tool'
    }[_tool]

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
