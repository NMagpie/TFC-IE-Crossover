from mcresources import ResourceManager, ItemContext, BlockContext, block_states
from mcresources import utils, loot_tables

from constants import *

def generate(rm: ResourceManager):

    # ORE STUFF

    ores = ['aluminum', 'lead', 'uranium']
    for ore in ores:
        for grade in ORE_GRADES.keys():
            rm.item_model('tfc_ie_addon:ore/%s_%s' % (grade, ore)).with_lang(lang('%s %s', grade, ore))
        block = rm.blockstate('ore/small_%s' % ore, variants={"": four_ways('tfc_ie_addon:block/small_%s' % ore)}, use_default_model=False)
        block.with_lang(lang('small %s', ore)).with_block_loot('tfc_ie_addon:ore/small_%s' % ore).with_tag('tfc:can_be_snow_piled')
        rm.item_model('ore/small_%s' % ore).with_lang(lang('small %s', ore))
        for rock, data in TFC_ROCKS.items():
            for grade in ORE_GRADES.keys():
                block = rm.blockstate(('ore', grade + '_' + ore, rock), 'tfc_ie_addon:block/ore/%s_%s/%s' % (grade, ore, rock))
                block.with_block_model({
                    'all': 'tfc:block/rock/raw/%s' % rock,
                    'particle': 'tfc:block/rock/raw/%s' % rock,
                    'overlay': 'tfc_ie_addon:block/ore/%s_%s' % (grade, ore)
                }, parent='tfc:block/ore')
                block.with_item_model().with_lang(lang('%s %s %s', grade, rock, ore)).with_block_loot('tfc_ie_addon:ore/%s_%s' % (grade, ore)).with_tag('minecraft:mineable/pickaxe').with_tag('tfc:prospectable')
                rm.block('tfc_ie_addon:ore/%s_%s/%s/prospected' % (grade, ore, rock)).with_lang(lang(ore))

    # MINERAL STUFF

    mineral = 'quartz'

    quartz_parts = mineral_parts(mineral)

    rm.item_model('mineral/%s_shard' % mineral).with_lang(lang('%s shard' % mineral)).with_tag('forge:gems').with_tag('forge:gems/%s' % mineral)

    for (block_name, block_lang, texture, parent) in quartz_parts:

        if 'budding' in block_name or 'block' in block_name:
            block = rm.blockstate('mineral/%s' % block_name, variants = {"": {'model': 'tfc_ie_addon:block/mineral/%s' % block_name}}, use_default_model=False)

            block.with_item_model().with_lang(lang(block_lang))

            if 'block' in block_name:
                block.with_block_loot('tfc_ie_addon:mineral/%s' % block_name).with_tag('minecraft:mineable/pickaxe').with_tag('tfc:prospectable')

        else:
            block = rm.blockstate('mineral/%s' % block_name, variants = six_ways('tfc_ie_addon:block/mineral/%s' % block_name), use_default_model=False)

        block.with_block_loot()

        if "cluster" in block_name:
            block.with_block_loot('tfc_ie_addon:mineral/quartz_shard')

        block.with_block_model({
            texture: 'tfc_ie_addon:block/mineral/%s' % block_name,
            'particle': 'tfc_ie_addon:block/mineral/%s' % block_name,
        }, parent='minecraft:block/' + parent,)
        block.with_lang(lang(block_lang)).with_tag('tfc:prospectable')

        if texture == 'all':
            block.with_tag('minecraft:mineable/pickaxe')

        rm.block('tfc_ie_addon:mineral/%s/prospected' % block_name).with_lang(lang('quartz'))

    # FLUIDS AND BUCKETS STUFF

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
        #rm.fluid_tag('minecraft:water', 'immersiveengineering:%s' % fluid)
        rm.fluid_tag('tfc:usable_in_wooden_bucket', 'immersiveengineering:%s' % fluid)
        rm.fluid_tag('tfc:usable_in_red_steel_bucket', 'immersiveengineering:%s' % fluid)

    for key, value in DEFAULT_LANG.items():
        rm.lang(key, value)

def contained_fluid(rm: ResourceManager, name_parts: utils.ResourceIdentifier, base: str, overlay: str) -> 'ItemContext':
    return rm.custom_item_model(name_parts, 'tfc:contained_fluid', {
        'parent': 'forge:item/default',
        'textures': {
            'base': base,
            'fluid': overlay
        }
    })

def item_model_property(rm: ResourceManager, name_parts: utils.ResourceIdentifier, overrides: utils.Json, data: Dict[str, Any]) -> ItemContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'assets', res.domain, 'models', 'item', res.path), {
        **data,
        'overrides': overrides
    })
    return ItemContext(rm, res)

def water_based_fluid(rm: ResourceManager, name: str):
    rm.blockstate(('fluid', name)).with_block_model({'particle': 'minecraft:block/water_still'}, parent=None).with_lang(lang(name))
    rm.fluid_tag(name, 'tfc_ie_addon:%s' % name, 'tfc_ie_addon:flowing_%s' % name)
    rm.fluid_tag('minecraft:water', 'tfc_ie_addon:%s' % name, 'tfc_ie_addon:flowing_%s' % name)  # Need to use water fluid tag for behavior
    rm.fluid_tag('mixable', 'tfc_ie_addon:%s' % name, 'tfc_ie_addon:flowing_%s' % name)

    item = rm.custom_item_model(('bucket', name), 'forge:bucket', {
        'parent': 'forge:item/bucket',
        'fluid': 'tfc_ie_addon:%s' % name
    })
    item.with_lang(lang('%s bucket', name))
    rm.lang('fluid.tfc_ie_addon.%s' % name, lang(name))

def four_ways(model: str) -> List[Dict[str, Any]]:
    return [
        {'model': model, 'y': 90},
        {'model': model},
        {'model': model, 'y': 180},
        {'model': model, 'y': 270}
    ]

def six_ways(model: str) -> Dict[str, Any]:
    return {
        'facing=up': {'model': model},
        'facing=down': {'model': model, 'x': 180},
        'facing=east': {'model': model, 'x': 90, 'y': 90},
        'facing=north': {'model': model, 'x': 90},
        'facing=south': {'model': model, 'x': 90, 'y': 180},
        'facing=west': {'model': model, 'x': 90, 'y': 270},
    }

def domain_divider(item: str):
    if item == 'ingot' or item == 'plate':
        return 'immersiveengineering:', '_'
    else:
        return 'tfc_ie_addon:metal/', '/'

def mineral_parts(mineral: str):
    return [
            ('%s_block' % mineral, '%s Block' % mineral, 'all', 'cube_all'),
            ('budding_%s' % mineral, 'Budding %s' % mineral, 'all', 'cube_all'),
            ('%s_cluster' % mineral, '%s Cluster' % mineral, 'cross', 'cross'),
            ('large_%s_bud' % mineral, 'Large %s Bud' % mineral, 'cross', 'cross'),
            ('medium_%s_bud' % mineral, 'Medium %s Bud' % mineral, 'cross', 'cross'),
            ('small_%s_bud' % mineral, 'Small %s Bud' % mineral, 'cross', 'cross')]