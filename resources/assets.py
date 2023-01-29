from mcresources import ResourceManager, ItemContext, BlockContext, block_states
from mcresources import utils, loot_tables

from constants import *

def generate(rm: ResourceManager):
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

def domain_divider(item: str):
    if item == 'ingot' or item == 'plate':
        return 'immersiveengineering:', '_'
    else:
        return 'tfc_ie_addon:metal/', '/'