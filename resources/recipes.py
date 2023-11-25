from enum import Enum
from typing import Union, Sequence, Optional, List, Dict, Tuple

from assets import domain_divider

from mcresources import ResourceManager, RecipeContext, utils
from mcresources.type_definitions import Json, JsonObject, ResourceIdentifier

from constants import *


class Rules(Enum):
    hit_any = 'hit_any'
    hit_not_last = 'hit_not_last'
    hit_last = 'hit_last'
    hit_second_last = 'hit_second_last'
    hit_third_last = 'hit_third_last'
    draw_any = 'draw_any'
    draw_last = 'draw_last'
    draw_not_last = 'draw_not_last'
    draw_second_last = 'draw_second_last'
    draw_third_last = 'draw_third_last'
    punch_any = 'punch_any'
    punch_last = 'punch_last'
    punch_not_last = 'punch_not_last'
    punch_second_last = 'punch_second_last'
    punch_third_last = 'punch_third_last'
    bend_any = 'bend_any'
    bend_last = 'bend_last'
    bend_not_last = 'bend_not_last'
    bend_second_last = 'bend_second_last'
    bend_third_last = 'bend_third_last'
    upset_any = 'upset_any'
    upset_last = 'upset_last'
    upset_not_last = 'upset_not_last'
    upset_second_last = 'upset_second_last'
    upset_third_last = 'upset_third_last'
    shrink_any = 'shrink_any'
    shrink_last = 'shrink_last'
    shrink_not_last = 'shrink_not_last'
    shrink_second_last = 'shrink_second_last'
    shrink_third_last = 'shrink_third_last'


def generate(rm: ResourceManager):
    rm.domain = 'tfc_ie_addon'

    def craft_decorations(recipe_name: str, base_block: str, has_wall: bool = True):
        rm.crafting_shaped(recipe_name + '_slab', ['XXX'], base_block, (6, base_block + '_slab')).with_advancement(base_block)
        rm.crafting_shaped(recipe_name + '_stairs', ['X  ', 'XX ', 'XXX'], base_block, (8, base_block + '_stairs')).with_advancement(base_block)
        if has_wall:
            rm.crafting_shaped(recipe_name + '_wall', ['XXX', 'XXX'], base_block, (6, base_block + '_wall')).with_advancement(base_block)

    # RECIPE REMOVALS

    [disable_recipe(rm, recipe) for recipe in TO_REMOVE_RECIPES]

    # CRAFTING RECIPE

    PLATE_METALS = IE_METALS + ['copper', 'gold']
    for metal in PLATE_METALS:
        damage_shapeless(rm, 'crafting/%s_sheet_to_plate' % metal, ('%s:metal/sheet/%s' % ('tfc_ie_addon' if metal in ADDON_METALS else 'tfc', metal), 'immersiveengineering:wirecutter'), (2, 'immersiveengineering:plate_%s' % metal))
    damage_shapeless(rm, 'crafting/wrought_iron_sheet_to_plate', ('tfc:metal/sheet/wrought_iron', 'immersiveengineering:wirecutter'), (2, 'immersiveengineering:plate_iron'))

    for metal in ADDON_METALS:
        damage_shaped(rm, 'crafting/metal/block/%s' % metal, [' SH', 'SWS', ' S '], {'S': '#forge:sheets/%s' % metal, 'W': '#minecraft:planks', 'H': '#tfc:hammers'}, '8 tfc_ie_addon:metal/block/%s' % metal)
        craft_decorations('crafting/metal/block/%s' % metal, 'tfc_ie_addon:metal/block/%s' % metal, has_wall=False)

    for ore, ore_data in ORES.items():
        if ore_data.dye_color:
            rm.crafting_shapeless('crafting/vanilla/%s_dye_from_%s' % (ore_data.dye_color, ore), ('tfc_ie_addon:powder/%s' % ore), 'minecraft:%s_dye' % ore_data.dye_color).with_advancement('tfc_ie_addon:powder/%s' % ore)

    rm.crafting_shaped('crafting/treated_wood_planks', ['XX', 'XX'], {'X': 'tfc_ie_addon:treated_wood_lumber'}, 'immersiveengineering:treated_wood_horizontal')

    # HEAT RECIPES

    heat_recipe(rm, 'slag', 'immersiveengineering:slag', 380, 'immersiveengineering:slag_glass')

    heat_recipe(rm, 'hop_graphite_ingot', 'immersiveengineering:dust_hop_graphite', 2000, 'immersiveengineering:ingot_hop_graphite')

    # ORE RECIPES

    for ore, ore_data in ORES.items():
        for rock, data in TFC_ROCKS.items():
            cobble = 'tfc:rock/cobble/%s' % rock
            collapse_recipe(rm, '%s_cobble' % rock, [
                'tfc_ie_addon:ore/poor_%s/%s' % (ore, rock),
                'tfc_ie_addon:ore/normal_%s/%s' % (ore, rock),
                'tfc_ie_addon:ore/rich_%s/%s' % (ore, rock)
            ], cobble)
            for grade in ORE_GRADES.keys():
                rm.block_tag('tfc:can_start_collapse', 'tfc_ie_addon:ore/%s_%s/%s' % (grade, ore, rock))
                rm.block_tag('tfc:can_collapse', 'tfc_ie_addon:ore/%s_%s/%s' % (grade, ore, rock))

    # QUERN RECIPES
        if ore_data.graded:
            for grade, data in ORE_GRADES.items():
                quern_recipe(rm, '%s_%s' % (grade, ore), 'tfc_ie_addon:ore/%s_%s' % (grade, ore), 'tfc_ie_addon:powder/%s' % ore, count=data.grind_amount)
            quern_recipe(rm, 'small_%s' % ore, 'tfc_ie_addon:ore/small_%s' % ore, 'tfc_ie_addon:powder/%s' % ore, count=2)

    # BARREL RECIPES

    barrel_instant_recipe(rm, 'redstone_acid', '#forge:dusts/redstone', '250 minecraft:water', output_fluid='250 immersiveengineering:redstone_acid')
    barrel_sealed_recipe(rm, 'treated_wood', 'Treated Wood', 8000, '#tfc:lumber', '50 immersiveengineering:creosote',  output_item='tfc_ie_addon:treated_wood_lumber')

    # CHISEL RECIPES

    chisel_recipe(rm, 'concrete_stairs', 'immersiveengineering:concrete', 'immersiveengineering:stairs_concrete', 'stair')
    chisel_recipe(rm, 'concrete_slab', 'immersiveengineering:concrete', 'immersiveengineering:slab_concrete', 'slab')

    # GLASSWORKING RECIPES
    for name, op, result in (('glass_pane', 'table_pour', 'tfc:%s_poured_glass'), ('glass_block', 'basin_pour', 'minecraft:%s_stained_glass')):
        glass_recipe(rm, 'lime_' + name, ['uranium', op], '#tfc:glass_batches_tier_2', result % 'lime')
        glass_recipe(rm, 'yellow_' + name, ['lead', op], '#tfc:glass_batches_tier_2', result % 'yellow')

    # COKE OVEN RECIPES

    tfc_coals = ['bituminous_coal', 'lignite']
    [coke_oven_recipe(rm, coal, {'item': 'tfc:ore/' + coal}, {'tag': 'forge:coal_coke'}, 500, 1800) for coal in tfc_coals]

    # FERMENTER RECIPES

    berries = ['blackberry', 'blueberry', 'bunchberry', 'cloudberry', 'cranberry', 'elderberry', 'gooseberry', 'raspberry',
               'snowberry', 'strawberry', 'wintergreen_berry', 'cherry']

    other_vegetables = ['squash', 'beet', 'cabbage', 'carrot', 'green_bean', 'soybean']

    fruits = ['banana', 'green_apple', 'lemon', 'orange', 'peach', 'plum', 'red_apple']

    food_amount = [(berry, 50) for berry in berries]

    [food_amount.append((vegetable, 40)) for vegetable in other_vegetables]

    [food_amount.append((fruit, 80)) for fruit in fruits]

    food_amount.append(('potato', 80))

    food_amount.append(('sugarcane', 200))

    [fermenter_recipe(rm, food, 'tfc:food/%s' % food, amount) for (food, amount) in food_amount]

    fermenter_recipe(rm, 'grains', '#tfc:foods/grains', 80)

    # CLOCHE RECIPES

    growable_food = ['barley', 'oat', 'rye', 'maize', 'wheat', 'rice', 'beet', 'cabbage', 'carrot', 'garlic', 'green_bean', 'potato',
                     'onion', 'soybean', 'squash', 'sugarcane', 'tomato']

    [cloche_recipe(rm, type, utils.ingredient('tfc:seeds/%s' % type), utils.item_stack_list(('tfc:food/%s' % type, 'tfc:seeds/%s' % type)), 128_000, 'tfc:crop/%s' % type) for type in growable_food]

    other_growables = ['jute', 'pumpkin', 'melon']

    [cloche_recipe(rm, type, utils.ingredient('tfc:seeds/%s' % type), utils.item_stack_list(('tfc:%s' % type, 'tfc:seeds/%s' % type)), 128_000, 'tfc:crop/%s' % type) for type in other_growables]

    # SQUEEZER RECIPES

    grain_seeds = ['barley', 'oat', 'rye', 'maize', 'wheat', 'rice']

    other_seeds = ['beet', 'cabbage', 'carrot', 'garlic', 'green_bean', 'potato',
                   'onion', 'soybean', 'squash', 'sugarcane', 'tomato']

    squeezer_inputs = [('tfc:seeds/jute', 120)]

    [squeezer_inputs.append(('tfc:seeds/%s' % seed, 80)) for seed in grain_seeds]

    [squeezer_inputs.append(('tfc:seeds/%s' % seed, 30)) for seed in other_seeds]

    squeezer_inputs.append(('tfc:food/olive', 120))

    [plant_oil_recipe(rm, item.split('/')[1], item, amount, 6400) for (item, amount) in squeezer_inputs]

    # ANVIL RECIPES

    anvil_recipe(rm, 'wirecutter_head', 'tfc:metal/ingot/wrought_iron', 'tfc_ie_addon:tool_head/wirecutter', 3, Rules.hit_last, Rules.shrink_not_last, Rules.bend_not_last)

    anvil_recipe(rm, 'hammer_head', 'tfc:metal/ingot/wrought_iron', 'tfc_ie_addon:tool_head/ie_hammer', 3, Rules.upset_last, Rules.draw_second_last, Rules.hit_third_last)

    anvil_recipe(rm, 'toolupgrade_revolver_bayonet', 'tfc:metal/ingot/steel', 'immersiveengineering:toolupgrade_revolver_bayonet', 4, Rules.hit_last, Rules.bend_any, Rules.draw_any)

    anvil_recipe(rm, 'drillhead_steel', 'tfc:metal/double_sheet/steel', 'immersiveengineering:drillhead_steel', 4, Rules.hit_last, Rules.draw_second_last, Rules.bend_third_last)

    anvil_recipe(rm, 'drillhead_iron', 'tfc:metal/double_sheet/wrought_iron', 'immersiveengineering:drillhead_iron', 3, Rules.hit_last, Rules.draw_second_last, Rules.bend_third_last)

    anvil_recipe(rm, 'drillhead_black_steel', 'tfc:metal/double_sheet/black_steel', 'tfc_ie_addon:drillhead_black_steel', 5, Rules.hit_last, Rules.draw_second_last, Rules.bend_third_last)

    anvil_recipe(rm, 'drillhead_blue_steel', 'tfc:metal/double_sheet/blue_steel', 'tfc_ie_addon:drillhead_blue_steel', 6, Rules.hit_last, Rules.draw_second_last, Rules.bend_third_last)

    anvil_recipe(rm, 'drillhead_red_steel', 'tfc:metal/double_sheet/red_steel', 'tfc_ie_addon:drillhead_red_steel', 6, Rules.hit_last, Rules.draw_second_last, Rules.bend_third_last)

    anvil_recipe(rm, 'gunpart_barrel', 'tfc:metal/ingot/steel', 'immersiveengineering:gunpart_barrel', 4, Rules.draw_last, Rules.hit_any, Rules.shrink_any)

    anvil_recipe(rm, 'empty_casing', 'tfc:metal/ingot/copper', '2 immersiveengineering:empty_casing', 1, Rules.shrink_last, Rules.upset_any, Rules.punch_any)

    armor_parts = ['boots', 'leggings', 'chestplate', 'helmet']

    [anvil_recipe(rm, ('armor', 'armor_faraday_%s' % armor_part),
                  'tfc:metal/double_sheet/steel',
                  'immersiveengineering:armor_faraday_%s' % armor_part, 1,
                  Rules.draw_any, Rules.hit_any, Rules.punch_not_last)
     for armor_part in armor_parts]

    # ARC FURNACE RECIPES

    for ore, ore_data in (ORES | TFC_ORES).items():
        input_prefix = 'tfc_ie_addon' if ore in ORES.keys() else 'tfc'
        output_prefix = 'immersiveengineering:ingot_' if ore in ORES.keys() else 'tfc:metal/ingot/'
        arc_furnace_recipe(rm, '%s' % ore,
                           input='20 %s:powder/%s' % (input_prefix, ore),
                           results='%s%s' % (output_prefix, ore_data.metal),
                           time=100,
                           energy=25600)

    # CRUSHER RECIPES

    for ore in list(ORES.keys()) + list(TFC_ORES.keys()):
        prefix = 'tfc_ie_addon' if ore in ORES.keys() else 'tfc'
        for grade, data in ORE_GRADES.items():
            crusher_recipe(rm, 'ore/%s_%s' % (grade, ore),
                           input='%s:ore/%s_%s' % (prefix, grade, ore),
                           result='%s %s:powder/%s' % (data.grind_amount * 2, prefix, ore),
                           energy=data.grind_amount * 500)
        crusher_recipe(rm, 'ore/small_%s' % ore,
                       input='%s:ore/small_%s' % (prefix, ore),
                       result='4 %s:powder/%s' % (prefix, ore),
                       energy=1000)

    for type in SANDSTONE_TYPES:
        for color in SANDSTONE_COLORS:
            crusher_recipe(rm, 'sandstone/%s_%s' % (type, color),
                           input='tfc:%s_sandstone/%s' % (type, color),
                           result='2 tfc:sand/%s' % color,
                           energy=3200,
                           secondaries=[{'chance': 0.5, 'output': utils.item_stack('tfc:powder/saltpeter')}])

    # METAL PRESS RECIPES

    ALL_METALS = set(IE_METALS + TFC_METALS)

    for metal in ALL_METALS:
        metalpress_recipe(rm, 'sheet_%s' % metal, '2 #forge:ingots/%s' % metal, 'tfc_ie_addon:mold_sheet', '%s:metal/sheet/%s' % ('tfc_ie_addon' if metal in ADDON_METALS else 'tfc', metal), 2400)

    for metal in TFC_METALS:
        metalpress_recipe(rm, 'rod_%s' % metal, '#forge:ingots/%s' % metal, 'immersiveengineering:mold_rod', '2 tfc:metal/rod/%s' % metal, 2400)

    metalpress_recipe(rm, 'plate_wrought_iron', '#forge:ingots/wrought_iron', 'immersiveengineering:mold_plate', '#forge:plates/iron', 2400)

    for metal in ['steel', 'uranium']:
        metalpress_recipe(rm, '%s_block' % metal, '9 #forge:ingots/%s' % metal, 'tfc_ie_addon:mold_block', 'immersiveengineering:storage_%s' % metal, 4800)

    # SAWMILL RECIPES

    for wood_type in TFC_WOOD_TYPES:
        for wood_item, count, energy in TFC_WOOD_ITEMS:
            sawmill_recipe(rm, '%s/%s' % (wood_type, wood_item),
                           'tfc:wood/planks/%s_%s' % (wood_type, wood_item),
                           '%s tfc:wood/lumber/%s' % (count, wood_type),
                           energy)

        for wood_item, count, energy in TFC_OTHER_WOOD_ITEMS:
            sawmill_recipe(rm, '%s/%s' % (wood_type, wood_item),
                           'tfc:wood/%s/%s' % (wood_item, wood_type),
                           '%s tfc:wood/lumber/%s' % (count, wood_type),
                           energy)

        for log_type in ['wood', 'log']:
            sawmill_recipe(rm, '%s_%s' % (wood_type, log_type),
                           'tfc:wood/%s/%s' % (log_type, wood_type),
                           '12 tfc:wood/lumber/%s' % wood_type,
                           1600,
                           stripped='tfc:wood/stripped_%s/%s' % (log_type, wood_type),
                           secondaries2=True)

            sawmill_recipe(rm, '%s_stripped_%s' % (wood_type, log_type),
                           'tfc:wood/stripped_%s/%s' % (log_type, wood_type),
                           '12 tfc:wood/lumber/%s' % wood_type,
                           1600)

        sawmill_recipe(rm, '%s/planks' % wood_type,
                       'tfc:wood/planks/%s' % wood_type,
                       '4 tfc:wood/lumber/%s' % wood_type,
                       1600)

    # FIRMALIFE COMPAT

    for metal in FL_METALS:
        metalpress_recipe(rm, 'sheet_%s' % metal, '2 #forge:ingots/%s' % metal, 'tfc_ie_addon:mold_sheet', 'firmalife:metal/sheet/%s' % metal, 2400, 'firmalife')
        metalpress_recipe(rm, 'rod_%s' % metal, '#forge:ingots/%s' % metal, 'immersiveengineering:mold_rod', '2 firmalife:metal/rod/%s' % metal, 2400, 'firmalife')

    fermenter_recipe(rm, 'raw_honey', 'firmalife:raw_honey', 200, 'firmalife')

    # for grade, count, output in grades:
    #     arc_furnace_recipe(rm, '%s_chromite' % grade,
    #                        input='%s firmalife:ore/%s_chromite' % (count, grade),
    #                        results=('%s #forge:ingots/chromium' % output),
    #                        time=100,
    #                        energy=25600,
    #                        slag=True,
    #                        conditional_modid='firmalife')

    # IMMERSIVE PETROLEUM COMPAT

    chisel_recipe(rm, 'asphalt_stairs', 'immersivepetroleum:asphalt', 'immersivepetroleum:asphalt_stair', 'stair', 'immersivepetroleum')
    chisel_recipe(rm, 'asphalt_slab', 'immersivepetroleum:asphalt', 'immersivepetroleum:asphalt_slab', 'slab', 'immersivepetroleum')


def damage_shaped(rm: ResourceManager, name_parts: utils.ResourceIdentifier, pattern: Sequence[str], ingredients: Json, result: Json, group: str = None, conditions: Optional[Json] = None) -> RecipeContext:
    return delegate_recipe(rm, name_parts, 'tfc:damage_inputs_shaped_crafting', {
        'type': 'minecraft:crafting_shaped',
        'group': group,
        'pattern': pattern,
        'key': utils.item_stack_dict(ingredients, ''.join(pattern)[0]),
        'result': utils.item_stack(result),
        'conditions': utils.recipe_condition(conditions)
    })


def damage_shapeless(rm: ResourceManager, name_parts: ResourceIdentifier, ingredients: Json, result: Json, group: str = None, conditions: utils.Json = None) -> RecipeContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'data', res.domain, 'recipes', res.path), {
        'type': 'tfc:damage_inputs_shapeless_crafting',
        'recipe': {
            'type': 'minecraft:crafting_shapeless',
            'group': group,
            'ingredients': utils.item_stack_list(ingredients),
            'result': utils.item_stack(result),
            'conditions': utils.recipe_condition(conditions)
        }
    })
    return RecipeContext(rm, res)


def barrel_instant_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input_item: Optional[Json] = None, input_fluid: Optional[Json] = None, output_item: Optional[Json] = None, output_fluid: Optional[Json] = None, sound: Optional[str] = None):
    rm.recipe(('barrel', name_parts), 'tfc:barrel_instant', {
        'input_item': item_stack_ingredient(input_item) if input_item is not None else None,
        'input_fluid': fluid_stack_ingredient(input_fluid) if input_fluid is not None else None,
        'output_item': item_stack_provider(output_item) if output_item is not None else None,
        'output_fluid': fluid_stack(output_fluid) if output_fluid is not None else None,
        'sound': sound
    })


def barrel_sealed_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, translation: str, duration: int, input_item: Optional[Json] = None, input_fluid: Optional[Json] = None, output_item: Optional[Json] = None, output_fluid: Optional[Json] = None, on_seal: Optional[Json] = None, on_unseal: Optional[Json] = None, sound: Optional[str] = None):
    rm.recipe(('barrel', name_parts), 'tfc:barrel_sealed', {
        'input_item': item_stack_ingredient(input_item) if input_item is not None else None,
        'input_fluid': fluid_stack_ingredient(input_fluid) if input_fluid is not None else None,
        'output_item': item_stack_provider(output_item) if isinstance(output_item, str) else output_item,
        'output_fluid': fluid_stack(output_fluid) if output_fluid is not None else None,
        'duration': duration,
        'on_seal': on_seal,
        'on_unseal': on_unseal,
        'sound': sound
    })
    res = utils.resource_location('tfc_ie_addon', name_parts)
    rm.lang('tfc.recipe.barrel.' + res.domain + '.barrel.' + res.path.replace('/', '.'), lang(translation))


def write_crafting_recipe(rm: ResourceManager, name_parts: ResourceIdentifier, data: Json) -> RecipeContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'data', res.domain, 'recipes', 'crafting', res.path), data)
    return RecipeContext(rm, res)


def advanced_shapeless(rm: ResourceManager, name_parts: ResourceIdentifier, ingredients: Json, result: Json, primary_ingredient: Json = None, group: str = None, conditions: Optional[Json] = None) -> RecipeContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'data', res.domain, 'recipes', res.path), {
        'type': 'tfc:advanced_shapeless_crafting',
        'group': group,
        'ingredients': utils.item_stack_list(ingredients),
        'result': result,
        'primary_ingredient': None if primary_ingredient is None else utils.ingredient(primary_ingredient),
        'conditions': utils.recipe_condition(conditions)
    })
    return RecipeContext(rm, res)


def advanced_shaped(rm: ResourceManager, name_parts: ResourceIdentifier, pattern: Sequence[str], ingredients: Json, result: Json, input_xy: Tuple[int, int], group: str = None, conditions: Optional[Json] = None) -> RecipeContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'data', res.domain, 'recipes', res.path), {
        'type': 'tfc:advanced_shaped_crafting',
        'group': group,
        'pattern': pattern,
        'key': utils.item_stack_dict(ingredients, ''.join(pattern)[0]),
        'result': item_stack_provider(result),
        'input_row': input_xy[1],
        'input_column': input_xy[0],
        'conditions': utils.recipe_condition(conditions)
    })
    return RecipeContext(rm, res)


def collapse_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient, result: Optional[utils.Json] = None, copy_input: Optional[bool] = None):
    assert result is not None or copy_input
    rm.recipe(('collapse', name_parts), 'tfc:collapse', {
        'ingredient': ingredient,
        'result': result,
        'copy_input': copy_input
    })


def fluid_item_ingredient(fluid: Json, delegate: Json = None):
    return {
        'type': 'tfc:fluid_item',
        'ingredient': delegate,
        'fluid_ingredient': fluid_stack_ingredient(fluid)
    }


def has_trait(ingredient: Json, trait: str, invert: bool = False) -> Json:
    return {
        'type': 'tfc:lacks_trait' if invert else 'tfc:has_trait',
        'trait': trait,
        'ingredient': utils.ingredient(ingredient)
    }


def lacks_trait(ingredient: Json, trait: str) -> Json:
    return has_trait(ingredient, trait, True)


def item_stack_provider(data_in: Json = None, copy_input: bool = False, copy_heat: bool = False, copy_food: bool = False, copy_oldest_food: bool = False, reset_food: bool = False, add_heat: float = None, add_trait: str = None, remove_trait: str = None, empty_bowl: bool = False, copy_forging: bool = False, other_modifier: str = None, other_other_modifier: str = None) -> Json:
    if isinstance(data_in, dict):
        return data_in
    stack = utils.item_stack(data_in) if data_in is not None else None
    modifiers = [k for k, v in (
        ('tfc:copy_input', copy_input),
        ('tfc:copy_heat', copy_heat),
        ('tfc:copy_food', copy_food),
        ('tfc:reset_food', reset_food),
        ('tfc:empty_bowl', empty_bowl),
        ('tfc:copy_forging_bonus', copy_forging),
        ('tfc:copy_oldest_food', copy_oldest_food),
        (other_modifier, other_modifier is not None),
        (other_other_modifier, other_other_modifier is not None),
        ({'type': 'tfc:add_heat', 'temperature': add_heat}, add_heat is not None),
        ({'type': 'tfc:add_trait', 'trait': add_trait}, add_trait is not None),
        ({'type': 'tfc:remove_trait', 'trait': remove_trait}, remove_trait is not None)
    ) if v]
    if modifiers:
        return {
            'stack': stack,
            'modifiers': modifiers
        }
    return stack


def ingredient_with_size(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    item, tag, amount, _ = utils.parse_item_stack(data_in, False)
    if amount is None:
        amount = 1
    if amount > 1:
        return {'base_ingredient': {'tag' if tag else 'item': item}, 'count': amount}
    else:
        return {'tag' if tag else 'item': item}


def ingredient_with_size_list(data_in: Json) -> List[JsonObject]:
    if isinstance(data_in, str) or isinstance(data_in, Dict):
        return [ingredient_with_size(data_in)]
    elif utils.is_sequence(data_in):
        return [*utils.flatten_list([ingredient_with_size(s) for s in data_in])]
    else:
        raise ValueError('Unknown object %s at ingredient_with_size_list' % str(data_in))


def anvil_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: Json, result: Json, tier: int, *rules: Rules, bonus: bool = None):
    rm.recipe(('anvil', name_parts), 'tfc:anvil', {
        'input': utils.ingredient(ingredient),
        'result': item_stack_provider(result),
        'tier': tier,
        'rules': [r.name for r in rules],
        'apply_forging_bonus': bonus
    })


def welding_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, first_input: Json, second_input: Json, result: Json, tier: int, ):
    rm.recipe(('welding', name_parts), 'tfc:welding', {
        'first_input': utils.ingredient(first_input),
        'second_input': utils.ingredient(second_input),
        'tier': tier,
        'result': item_stack_provider(result)
    })


def simple_pot_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredients: Json, fluid: str, output_fluid: str = None, output_items: Json = None, duration: int = 2000, temp: int = 300):
    rm.recipe(('pot', name_parts), 'tfc:pot', {
        'ingredients': ingredients,
        'fluid_ingredient': fluid_stack_ingredient(fluid),
        'duration': duration,
        'temperature': temp,
        'fluid_output': fluid_stack(output_fluid) if output_fluid is not None else None,
        'item_output': [utils.item_stack(item) for item in output_items] if output_items is not None else None
    })


def oven_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, temperature: float, result_item: Optional[Union[str, Json]], duration: int = 1000) -> RecipeContext:
    result_item = item_stack_provider(result_item) if isinstance(result_item, str) else result_item
    return rm.recipe(('oven', name_parts), 'tfc_ie_addon:oven', {
        'ingredient': utils.ingredient(ingredient),
        'result_item': result_item,
        'temperature': temperature,
        'duration': duration
    })


def heat_recipe(rm: ResourceManager, name_parts: ResourceIdentifier, ingredient: Json, temperature: float, result_item: Optional[Union[str, Json]] = None, result_fluid: Optional[str] = None, use_durability: Optional[bool] = None) -> RecipeContext:
    result_item = item_stack_provider(result_item) if isinstance(result_item, str) else result_item
    result_fluid = None if result_fluid is None else fluid_stack(result_fluid)
    return rm.recipe(('heating', name_parts), 'tfc:heating', {
        'ingredient': utils.ingredient(ingredient),
        'result_item': result_item,
        'result_fluid': result_fluid,
        'temperature': temperature,
        'use_durability': use_durability if use_durability else None,
    })


def fluid_stack(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    assert not tag, 'fluid_stack() cannot be a tag'
    return {
        'fluid': fluid,
        'amount': amount
    }


def casting_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, mold: str, metal: str, amount: int, break_chance: float):
    domain, divider = domain_divider(mold)
    rm.recipe(('casting', name_parts), 'tfc:casting', {
        'mold': {'item': 'tfc:ceramic/%s_mold' % mold},
        'fluid': fluid_stack_ingredient('%d tfc_ie_addon:metal/%s' % (amount, metal)),
        'result': utils.item_stack('%s%s%s%s' % (domain, mold, divider, metal)),
        'break_chance': break_chance
    })


def fluid_stack_ingredient(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return {
            'ingredient': fluid_ingredient(data_in['ingredient']),
            'amount': data_in['amount']
        }
    if pair := utils.maybe_unordered_pair(data_in, int, object):
        amount, fluid = pair
        return {'ingredient': fluid_ingredient(fluid), 'amount': amount}
    fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
    if tag:
        return {'ingredient': {'tag': fluid}, 'amount': amount}
    else:
        return {'ingredient': fluid, 'amount': amount}


def fluid_ingredient(data_in: Json) -> Json:
    if isinstance(data_in, dict):
        return data_in
    elif isinstance(data_in, List):
        return [*utils.flatten_list([fluid_ingredient(e) for e in data_in])]
    else:
        fluid, tag, amount, _ = utils.parse_item_stack(data_in, False)
        if tag:
            return {'tag': fluid}
        else:
            return fluid


def item_stack_ingredient(data_in: Json):
    if isinstance(data_in, dict):
        return {
            'ingredient': utils.ingredient(data_in['ingredient']),
            'count': data_in['count'] if data_in.get('count') is not None else None
        }
    if pair := utils.maybe_unordered_pair(data_in, int, object):
        count, item = pair
        return {'ingredient': fluid_ingredient(item), 'count': count}
    item, tag, count, _ = utils.parse_item_stack(data_in, False)
    if tag:
        return {'ingredient': {'tag': item}, 'count': count}
    else:
        return {'ingredient': {'item': item}, 'count': count}


def delegate_recipe(rm: ResourceManager, name_parts: ResourceIdentifier, recipe_type: str, delegate: Json) -> RecipeContext:
    res = utils.resource_location(rm.domain, name_parts)
    rm.write((*rm.resource_dir, 'data', res.domain, 'recipes', res.path), {
        'type': recipe_type,
        'recipe': delegate
    })
    return RecipeContext(rm, res)


def knapping_recipe(rm: ResourceManager, knapping_type: str, name_parts: utils.ResourceIdentifier, pattern: List[str], result: utils.Json, outside_slot_required: bool = None):
    rm.recipe((knapping_type, name_parts), knapping_type, {
        'outside_slot_required': outside_slot_required,
        'pattern': pattern,
        'result': utils.item_stack(result)
    })


def alloy_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, metal: str, *parts: Tuple[str, float, float]):
    rm.recipe(('alloy', name_parts), 'tfc:alloy', {
        'result': 'tfc_ie_addon:%s' % metal,
        'contents': [{
            'metal': p[0],
            'min': p[1],
            'max': p[2]
        } for p in parts]
    })


def quern_recipe(rm: ResourceManager, name: ResourceIdentifier, item: str, result: str, count: int = 1) -> RecipeContext:
    result = result if not isinstance(result, str) else utils.item_stack((count, result))
    return rm.recipe(('quern', name), 'tfc:quern', {
        'ingredient': utils.ingredient(item),
        'result': result
    })


def chisel_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: utils.Json, result: str, mode: str, conditional_modid: str = None):
    rm.recipe(('chisel', mode, name_parts), 'tfc:chisel', {
        'ingredient': ingredient,
        'result': result,
        'mode': mode,
        'extra_drop': item_stack_provider(result) if mode == 'slab' else None
    }, conditions={'type': 'forge:mod_loaded', 'modid': conditional_modid} if conditional_modid is not None else None)


def glass_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, steps: List[str], batch: str, result: str):
    rm.recipe(('glassworking', name_parts), 'tfc:glassworking', {
        'operations': steps,
        'batch': utils.ingredient(batch),
        'result': utils.item_stack(result)
    })


def coke_oven_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: Json, result: Json, creosote: int, time: int):
    rm.recipe(('cokeoven', name_parts), 'immersiveengineering:coke_oven', {
        'creosote': creosote,
        'input': input,
        'result': result,
        'time': time
    })


def fermenter_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, ingredient: Json, amount: int = 80, conditional_modid: str = None):
    rm.recipe(('fermenter', name_parts), 'immersiveengineering:fermenter', {
        'fluid': fluid_stack(str(amount) + ' immersiveengineering:ethanol'),
        'input': utils.ingredient(ingredient),
        'energy': 6400,
    }, conditions={'type': 'forge:mod_loaded', 'modid': conditional_modid} if conditional_modid is not None else None)


def cloche_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: Json, result: Json, time: int, render_block: str):
    rm.recipe(('cloche', name_parts), 'immersiveengineering:cloche', {
        'results': result,
        'input': input,
        'soil': utils.item_stack_list(('tfc:dirt/silt', 'tfc:dirt/loam', 'tfc:dirt/silty_loam', 'tfc:dirt/sandy_loam')),
        'time': time,
        'render': {
            'type': 'crop',
            'block': render_block
        }
    })


def plant_oil_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: str, amount: int, energy: int):
    rm.recipe(('squeezer', name_parts), 'immersiveengineering:squeezer', {
        'fluid': fluid_stack(str(amount) + ' immersiveengineering:plantoil'),
        'input': utils.ingredient(input),
        'energy': energy,
    })


def disable_recipe(rm: ResourceManager, name_parts: ResourceIdentifier):
    rm.recipe(name_parts, None, {}, conditions='forge:false')


def arc_furnace_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: Json, results: Json, time: int, energy: int, additives: list = [], secondaries: list = [], slag: bool = False, conditional_modid: str = None):
    recipe = {'input': ingredient_with_size(input), 'results': ingredient_with_size_list(results), 'additives': additives}

    if secondaries:
        recipe['secondaries'] = secondaries

    if slag:
        recipe['slag'] = utils.item_stack('#forge:slag')

    recipe['time'] = time
    recipe['energy'] = energy

    rm.recipe(('arc_furnace', name_parts), 'immersiveengineering:arc_furnace', recipe, conditions={'type': 'forge:mod_loaded', 'modid': conditional_modid} if conditional_modid is not None else None)


def crusher_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: Json, result: Json, energy: int, secondaries: list = []):
    recipe = {'input': utils.ingredient(input), 'result': utils.item_stack(result), 'secondaries': secondaries, 'energy': energy}

    rm.recipe(('crusher', name_parts), 'immersiveengineering:crusher', recipe)


def metalpress_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: Json, mold: str, result: Json, energy: int, conditional_modid: str = None):
    recipe = {
        'mold': mold,
        'input': ingredient_with_size(input),
        'result': utils.item_stack(result),
        'energy': energy,
    }

    rm.recipe(('metalpress', name_parts), 'immersiveengineering:metal_press', recipe, conditions={'type': 'forge:mod_loaded', 'modid': conditional_modid} if conditional_modid is not None else None)


def sawmill_recipe(rm: ResourceManager, name_parts: utils.ResourceIdentifier, input: Json, result: Json, energy: int, stripped: Json = None, secondaries1: bool = True, secondaries2: bool = False):
    recipe = {
        'input': utils.ingredient(input),
        'result': utils.item_stack(result),
        'energy': energy,
    }

    if stripped:
        recipe['stripped'] = utils.item_stack(stripped)

    recipe['secondaries'] = []

    if secondaries1:
        recipe['secondaries'].append({
            'output': utils.item_stack('#forge:dusts/wood'),
            'stripping': False
        })

    if secondaries2:
        recipe['secondaries'].append({
            'output': utils.item_stack('#forge:dusts/wood'),
            'stripping': True
        })

    rm.recipe(('sawmill', name_parts), 'immersiveengineering:sawmill', recipe)
