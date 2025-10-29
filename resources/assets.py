from mcresources import ResourceManager, ItemContext, loot_tables
from mcresources import utils

from constants import *


def generate(rm: ResourceManager):
    # ORE STUFF

    for ore, ore_data in ORES.items():
        for grade in ORE_GRADES.keys():
            rm.item_model('tfc_ie_addon:ore/%s_%s' % (grade, ore)).with_lang(lang('%s %s', grade, ore))
        block = rm.blockstate('ore/small_%s' % ore, variants={'': four_ways('tfc_ie_addon:block/small_%s' % ore)}, use_default_model=False)
        block.with_lang(lang('small %s', ore)).with_block_loot('tfc_ie_addon:ore/small_%s' % ore)
        rm.item_model('ore/small_%s' % ore).with_lang(lang('small %s', ore))
        rm.item_model('powder/%s' % ore).with_lang(lang('%s powder', ore))
        for rock, data in TFC_ROCKS.items():
            for grade in ORE_GRADES.keys():
                block = rm.blockstate(('ore', grade + '_' + ore, rock), 'tfc_ie_addon:block/ore/%s_%s/%s' % (grade, ore, rock))
                block.with_block_model({
                    'all': 'tfc:block/rock/raw/%s' % rock,
                    'particle': 'tfc:block/rock/raw/%s' % rock,
                    'overlay': 'tfc_ie_addon:block/ore/%s_%s' % (grade, ore)
                }, parent='tfc:block/ore')
                block.with_item_model().with_lang(lang('%s %s %s', grade, rock, ore)).with_block_loot('tfc_ie_addon:ore/%s_%s' % (grade, ore))
                rm.block('tfc_ie_addon:ore/%s_%s/%s/prospected' % (grade, ore, rock)).with_lang(lang(ore))

    # MINERAL STUFF

    mineral = 'quartz'

    quartz_parts = mineral_parts(mineral)

    rm.item_model('mineral/%s_shard' % mineral).with_lang(lang('%s shard' % mineral))

    for (block_name, block_lang, texture, parent) in quartz_parts:

        if 'budding' in block_name or 'block' in block_name:
            block = rm.blockstate('mineral/%s' % block_name, variants={'': {'model': 'tfc_ie_addon:block/mineral/%s' % block_name}}, use_default_model=False)

            block.with_item_model().with_lang(lang(block_lang))

            if 'block' in block_name:
                block.with_block_loot('tfc_ie_addon:mineral/%s' % block_name)

        else:
            block = rm.blockstate('mineral/%s' % block_name, variants=six_ways('tfc_ie_addon:block/mineral/%s' % block_name), use_default_model=False)

        if 'cluster' in block_name:
            block.with_block_loot('2 tfc_ie_addon:mineral/quartz_shard')

        block.with_block_model({
            texture: 'tfc_ie_addon:block/mineral/%s' % block_name,
            'particle': 'tfc_ie_addon:block/mineral/%s' % block_name,
        }, parent='minecraft:block/' + parent, ).with_lang(lang(block_lang))

        rm.block('tfc_ie_addon:mineral/%s/prospected' % block_name).with_lang(lang('quartz'))

    # CROPS

    crop = 'hemp'

    block = rm.blockstate(('crop', crop), variants={
        **dict(('age=%d' % i, {'model': 'immersiveengineering:block/hemp/bottom%d' % i}) for i in range(4)),
        'age=4,part=bottom': {'model': 'immersiveengineering:block/hemp/bottom4'},
        'age=4,part=top': {'model': 'immersiveengineering:block/hemp/top'}
    })
    block.with_lang(lang(crop))

    block.with_block_loot({
        'name': 'immersiveengineering:hemp_fiber',
        'conditions': loot_tables.block_state_property('tfc_ie_addon:crop/%s[age=4,part=bottom]' % crop),
        'functions': crop_yield(0, (6, 10))
    }, {
        'name': 'immersiveengineering:seed',
        'conditions': loot_tables.block_state_property('tfc_ie_addon:crop/%s[part=bottom,part=bottom]' % crop)
    })

    block = rm.blockstate(('dead_crop', crop), variants={
        'mature=false': {'model': 'tfc_ie_addon:block/dead_crop/%s_young' % crop},
        'mature=true,part=top': {'model': 'tfc_ie_addon:block/dead_crop/%s_top' % crop},
        'mature=true,part=bottom': {'model': 'tfc_ie_addon:block/dead_crop/%s_bottom' % crop}
    })
    block.with_lang(lang('dead %s', crop))
    for variant in ('young', 'top', 'bottom'):
        rm.block_model(('dead_crop', '%s_%s' % (crop, variant)), {'crop': 'tfc_ie_addon:block/crop/%s_dead_%s' % (crop, variant)}, parent='block/crop')

    block.with_block_loot(loot_tables.alternatives({
        'name': 'immersiveengineering:seed',
        'conditions': loot_tables.block_state_property('tfc_ie_addon:dead_crop/%s[mature=true,part=bottom]' % crop),
        'functions': loot_tables.set_count(1, 3)
    }, {
        'name': 'immersiveengineering:seed',
        'conditions': loot_tables.block_state_property('tfc_ie_addon:dead_crop/%s[mature=false,part=bottom]' % crop)
    }))

    block = rm.blockstate(('wild_crop', crop), variants={
        'part=top,mature=true': {'model': 'tfc_ie_addon:block/wild_crop/%s_top' % crop},
        'part=top,mature=false': {'model': 'tfc_ie_addon:block/dead_crop/%s_top' % crop},
        'part=bottom,mature=true': {'model': 'tfc_ie_addon:block/wild_crop/%s_bottom' % crop},
        'part=bottom,mature=false': {'model': 'tfc_ie_addon:block/dead_crop/%s_bottom' % crop}
    })
    rm.item_model(('wild_crop', crop), parent='tfc_ie_addon:block/wild_crop/%s_bottom' % crop, no_textures=True)
    block.with_lang(lang('wild %s', crop))
    rm.block_model(('wild_crop', '%s_top' % crop), {'crop': 'immersiveengineering:block/hemp/top0'}, parent='block/crop')
    rm.block_model(('wild_crop', '%s_bottom' % crop), {'crop': 'immersiveengineering:block/hemp/bottom4'}, parent='tfc:block/wild_crop/crop')

    block.with_block_loot({
        'name': 'immersiveengineering:hemp_fiber',
        'conditions': loot_tables.block_state_property('tfc_ie_addon:wild_crop/%s[part=bottom,mature=true]' % crop),
        'functions': loot_tables.set_count(1, 3)
    }, {
        'name': 'immersiveengineering:seed',
        'conditions': loot_tables.block_state_property('tfc_ie_addon:wild_crop/%s[part=bottom]' % crop)
    })

    # TOOLS

    rm.item_model('tool_head/wirecutter').with_lang('Wirecutter Head')
    rm.item_model('tool_head/ie_hammer').with_lang('Engineer\'s Hammer Head')

    # MOLD

    rm.item_model('mold_sheet').with_lang('Metal Press Mold: Sheet')
    rm.item_model('mold_block').with_lang('Metal Press Mold: Block')

    # DRILLHEADS

    rm.item_model('drillhead_black_steel').with_lang('Black Steel Drill Head')
    rm.item_model('drillhead_blue_steel').with_lang('Blue Steel Drill Head')
    rm.item_model('drillhead_red_steel').with_lang('Red Steel Drill Head')

    # TREATED WOOD

    rm.item_model('treated_wood_lumber').with_lang(lang('treated_wood_lumber'))

    for key, value in DEFAULT_LANG.items():
        rm.lang(key, value)


def contained_fluid(rm: ResourceManager, name_parts: utils.ResourceIdentifier, base: str, overlay: str) -> 'ItemContext':
    return rm.custom_item_model(name_parts, 'tfc:contained_fluid', {
        'parent': 'neoforge:item/default',
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

    item = rm.custom_item_model(('bucket', name), 'neoforge:bucket', {
        'parent': 'neoforge:item/bucket',
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


def crop_yield(lo: int, hi: Tuple[int, int]) -> utils.Json:
    return {
        'function': 'minecraft:set_count',
        'count': {
            'type': 'tfc:crop_yield_uniform',
            'min': lo,
            'max': {
                'type': 'minecraft:uniform',
                'min': hi[0],
                'max': hi[1]
            }
        }
    }
