from typing import Union, get_args, Any, List, Dict

from mcresources import ResourceManager, utils
from mcresources.type_definitions import ResourceIdentifier, JsonObject, Json, VerticalAnchor

from constants import *


def generate(rm: ResourceManager):
    placed_feature_tag(rm, 'tfc:in_biome/veins', *[*('tfc_ie_addon:vein/%s' % v for v in ORE_VEINS.keys()), 'tfc_ie_addon:quartz_geode'])

    for vein_name, vein in ORE_VEINS.items():
        rocks = expand_rocks(vein.rocks)
        configured_placed_feature(rm, ('vein', vein_name), vein.vein_type, {
            **vein.config(),
            'random_name': vein_name,
            'blocks': [{
                'replace': ['tfc:rock/raw/%s' % rock],
                'with': vein_ore_blocks(vein, rock)
            } for rock in rocks],
            'indicator': {
                'rarity': vein.indicator_rarity,
                'depth': 35,
                'underground_rarity': vein.underground_rarity,
                'underground_count': vein.underground_count,
                'blocks': [{
                    'block': 'tfc_ie_addon:ore/small_%s' % vein.ore
                }]
            },
            })

    rm.configured_feature('quartz_geode', 'tfc_ie_addon:quartz_geode',
                          {'outer': 'tfc:rock/hardened/basalt',
                           'middle': 'tfc:rock/raw/quartzite',
                           'inner': [
                               {'data': 'tfc_ie_addon:mineral/budding_quartz', 'weight': 2},
                               {'data': 'tfc_ie_addon:mineral/quartz_block', 'weight': 5},
                               {'data': 'tfc:rock/raw/quartzite', 'weight': 1}
                           ],
                           'filling': [
                               {'data': 'minecraft:air', 'weight': 1},
                           ],
                           'inner_placements': [
                               {'data': 'tfc_ie_addon:mineral/quartz_cluster', 'weight': 1},
                               {'data': 'tfc_ie_addon:mineral/large_quartz_bud', 'weight': 3},
                               {'data': 'tfc_ie_addon:mineral/medium_quartz_bud', 'weight': 5},
                               {'data': 'tfc_ie_addon:mineral/small_quartz_bud', 'weight': 8},
                           ]
                           })

    rm.placed_feature('quartz_geode', 'tfc_ie_addon:quartz_geode', decorate_chance(350), decorate_square(), decorate_range(30, 80), decorate_biome())


Heightmap = Literal['motion_blocking', 'motion_blocking_no_leaves', 'ocean_floor', 'ocean_floor_wg', 'world_surface', 'world_surface_wg']


class PatchConfig(NamedTuple):
    block: str
    y_spread: int
    xz_spread: int
    tries: int
    any_water: bool
    salt_water: bool
    custom_feature: str
    custom_config: Json


def decorate_climate(min_temp: Optional[float] = None, max_temp: Optional[float] = None, min_rain: Optional[float] = None, max_rain: Optional[float] = None, needs_forest: Optional[bool] = False, fuzzy: Optional[bool] = None, min_forest: Optional[str] = None, max_forest: Optional[str] = None) -> Json:
    return {
        'type': 'tfc:climate',
        'min_temperature': min_temp,
        'max_temperature': max_temp,
        'min_rainfall': min_rain,
        'max_rainfall': max_rain,
        'min_forest': 'normal' if needs_forest else min_forest,
        'max_forest': max_forest,
        'fuzzy': fuzzy
    }


def patch_config(block: str, y_spread: int, xz_spread: int, tries: int = 64, water: Union[bool, Literal['salt']] = False, custom_feature: Optional[str] = None, custom_config: Json = None) -> PatchConfig:
    return PatchConfig(block, y_spread, xz_spread, tries, water == 'salt' or water == True, water == 'salt', custom_feature, custom_config)


def configured_patch_feature(rm: ResourceManager, name_parts: ResourceIdentifier, patch: PatchConfig, *patch_decorators: Json, extra_singular_decorators: Optional[List[Json]] = None, biome_check: bool = True):
    feature = 'minecraft:simple_block'
    config = {'to_place': {'type': 'minecraft:simple_state_provider', 'state': utils.block_state(patch.block)}}
    singular_decorators = []

    if patch.any_water:
        feature = 'tfc:block_with_fluid'
        if patch.salt_water:
            singular_decorators.append(decorate_matching_blocks('tfc:fluid/salt_water'))
        else:
            singular_decorators.append(decorate_air_or_empty_fluid())
    else:
        singular_decorators.append(decorate_replaceable())

    if patch.custom_feature is not None:
        assert patch.custom_config
        feature = patch.custom_feature
        config = patch.custom_config

    heightmap: Heightmap = 'world_surface_wg'
    if patch.any_water:
        heightmap = 'ocean_floor_wg'
        singular_decorators.append(decorate_would_survive_with_fluid(patch.block))
    else:
        singular_decorators.append(decorate_would_survive(patch.block))

    if extra_singular_decorators is not None:
        singular_decorators += extra_singular_decorators
    if biome_check:
        patch_decorators = [*patch_decorators, decorate_biome()]

    res = utils.resource_location(rm.domain, name_parts)
    patch_feature = res.join() + '_patch'
    singular_feature = utils.resource_location(rm.domain, name_parts)

    rm.configured_feature(patch_feature, 'minecraft:random_patch', {
        'tries': patch.tries,
        'xz_spread': patch.xz_spread,
        'y_spread': patch.y_spread,
        'feature': singular_feature.join()
    })
    rm.configured_feature(singular_feature, feature, config)
    rm.placed_feature(patch_feature, patch_feature, *patch_decorators)
    rm.placed_feature(singular_feature, singular_feature, decorate_heightmap(heightmap), *singular_decorators)


def decorate_matching_blocks(*blocks: str) -> Json:
    return decorate_block_predicate({
        'type': 'matching_blocks',
        'blocks': list(blocks)
    })


def decorate_biome() -> Json:
    return 'tfc:biome'


def decorate_would_survive(block: str) -> Json:
    return decorate_block_predicate({
        'type': 'would_survive',
        'state': utils.block_state(block)
    })


def decorate_would_survive_with_fluid(block: str) -> Json:
    return decorate_block_predicate({
        'type': 'tfc:would_survive_with_fluid',
        'state': utils.block_state(block)
    })


def decorate_replaceable() -> Json:
    return decorate_block_predicate({'type': 'tfc:replaceable'})


def decorate_air_or_empty_fluid() -> Json:
    return decorate_block_predicate({'type': 'tfc:air_or_empty_fluid'})


def decorate_block_predicate(predicate: Json) -> Json:
    return {
        'type': 'block_predicate_filter',
        'predicate': predicate
    }


def decorate_range(min_y: VerticalAnchor, max_y: VerticalAnchor) -> Json:
    return {
        'type': 'minecraft:height_range',
        'height': {
            'type': 'uniform',
            'min_inclusive': {
                'absolute': min_y
            },
            'max_inclusive': {
                'absolute': max_y
            },
        }
    }


def decorate_heightmap(heightmap: Heightmap) -> Json:
    assert heightmap in get_args(Heightmap)
    return 'minecraft:heightmap', {'heightmap': heightmap.upper()}


def decorate_square() -> Json:
    return 'minecraft:in_square'


def decorate_chance(rarity_or_probability: Union[int, float]) -> Json:
    return {'type': 'minecraft:rarity_filter', 'chance': round(1 / rarity_or_probability) if isinstance(rarity_or_probability, float) else rarity_or_probability}


def placed_feature_tag(rm: ResourceManager, name_parts: ResourceIdentifier, *values: ResourceIdentifier):
    return rm.tag(name_parts, 'worldgen/placed_feature', *values)


def configured_feature_tag(rm: ResourceManager, name_parts: ResourceIdentifier, *values: ResourceIdentifier):
    return rm.tag(name_parts, 'worldgen/configured_feature', *values)


def biome_tag(rm: ResourceManager, name_parts: ResourceIdentifier, *values: ResourceIdentifier):
    return rm.tag(name_parts, 'worldgen/biome', *values)


def vein_ore_blocks(vein: Vein, rock: str) -> List[Dict[str, Any]]:
    poor, normal, rich = vein.grade
    ore_blocks = [{
        'weight': poor,
        'block': 'tfc_ie_addon:ore/poor_%s/%s' % (vein.ore, rock)
    }, {
        'weight': normal,
        'block': 'tfc_ie_addon:ore/normal_%s/%s' % (vein.ore, rock)
    }, {
        'weight': rich,
        'block': 'tfc_ie_addon:ore/rich_%s/%s' % (vein.ore, rock)
    }]
    return ore_blocks


def expand_rocks(rocks_list: List[str], path: Optional[str] = None) -> List[str]:
    rocks = []
    for rock_spec in rocks_list:
        if rock_spec in TFC_ROCKS:
            rocks.append(rock_spec)
        elif rock_spec in ROCK_CATEGORIES:
            rocks += [r for r, d in TFC_ROCKS.items() if d.category == rock_spec]
        else:
            raise RuntimeError('Unknown rock or rock category specification: %s at %s' % (rock_spec, path if path is not None else '??'))
    return rocks


def vein_density(density: int) -> float:
    assert 0 <= density <= 100, 'Invalid density: %s' % str(density)
    return round(density * 0.01, 2)


def configured_placed_feature(rm: ResourceManager, name_parts: ResourceIdentifier, feature: Optional[ResourceIdentifier] = None, config: JsonObject = None, *placements: Json):
    res = utils.resource_location(rm.domain, name_parts)
    if feature is None:
        feature = res
    rm.configured_feature(res, feature, config)
    rm.placed_feature(res, res, *placements)
