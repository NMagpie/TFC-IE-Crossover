from typing import Dict, List, NamedTuple, Optional, Literal, Tuple

OreGrade = NamedTuple('OreGrade', weight=int, grind_amount=int)
RockCategory = Literal['sedimentary', 'metamorphic', 'igneous_extrusive', 'igneous_intrusive']
Rock = NamedTuple('Rock', category=RockCategory, sand=str)
Vein = NamedTuple('Vein', ore=str, type=str, rarity=int, size=int, min_y=int, max_y=int, density=float, poor=float, normal=float, rich=float, rocks=List[str], spoiler_ore=str, spoiler_rarity=int, spoiler_rocks=List[str], biomes=Optional[str], height=Optional[int], deposits=bool)

DEFAULT_FORGE_ORE_TAGS: Tuple[str, ...] = ('coal', 'diamond', 'emerald', 'gold', 'iron', 'lapis', 'netherite_scrap', 'quartz', 'redstone')

TFC_ROCKS: Dict[str, Rock] = {
    'granite': Rock('igneous_intrusive', 'white'),
    'diorite': Rock('igneous_intrusive', 'white'),
    'gabbro': Rock('igneous_intrusive', 'black'),
    'shale': Rock('sedimentary', 'black'),
    'claystone': Rock('sedimentary', 'brown'),
    'limestone': Rock('sedimentary', 'white'),
    'conglomerate': Rock('sedimentary', 'green'),
    'dolomite': Rock('sedimentary', 'black'),
    'chert': Rock('sedimentary', 'yellow'),
    'chalk': Rock('sedimentary', 'white'),
    'rhyolite': Rock('igneous_extrusive', 'red'),
    'basalt': Rock('igneous_extrusive', 'red'),
    'andesite': Rock('igneous_extrusive', 'red'),
    'dacite': Rock('igneous_extrusive', 'red'),
    'quartzite': Rock('metamorphic', 'white'),
    'slate': Rock('metamorphic', 'brown'),
    'phyllite': Rock('metamorphic', 'brown'),
    'schist': Rock('metamorphic', 'green'),
    'gneiss': Rock('metamorphic', 'green'),
    'marble': Rock('metamorphic', 'yellow')
}

ROCK_CATEGORIES: List[str] = ['sedimentary', 'metamorphic', 'igneous_extrusive', 'igneous_intrusive']
SOIL_VARIANTS: List[str] = ['silt', 'loam', 'sandy_loam', 'silty_loam']
ORE_GRADES: Dict[str, OreGrade] = {
    'normal': OreGrade(50, 5),
    'poor': OreGrade(30, 3),
    'rich': OreGrade(20, 7)
}

# Default parameters for common ore veins
# rarity, size, min_y, max_y, density, poor, normal, rich
POOR_METAL_ORE = (80, 15, 0, 100, 40, 40, 30, 10)
NORMAL_METAL_ORE = (60, 20, -32, 75, 60, 20, 50, 30)
DEEP_METAL_ORE = (100, 30, -64, 30, 70, 10, 30, 60)
SURFACE_METAL_ORE = (20, 15, 60, 210, 50, 60, 30, 10)

POOR_S_METAL_ORE = (100, 12, 0, 100, 40, 60, 30, 10)
NORMAL_S_METAL_ORE = (70, 15, -32, 60, 60, 20, 50, 30)
DEEP_S_METAL_ORE = (110, 25, -64, 30, 70, 10, 30, 60)

DEEP_MINERAL_ORE = (90, 10, -48, 100, 60, 0, 0, 0)
HIGH_MINERAL_ORE = (90, 10, 0, 210, 60, 0, 0, 0)


def vein(ore: str, vein_type: str, rarity: int, size: int, min_y: int, max_y: int, density: float, poor: float, normal: float, rich: float, rocks: List[str], spoiler_ore: Optional[str] = None, spoiler_rarity: int = 0, spoiler_rocks: List[str] = None, biomes: str = None, height: int = 0, deposits: bool = False):
    # Factory method to allow default values
    return Vein(ore, vein_type, rarity, size, min_y, max_y, density, poor, normal, rich, rocks, spoiler_ore, spoiler_rarity, spoiler_rocks, biomes, height, deposits)


def preset_vein(ore: str, vein_type: str, rocks: List[str], spoiler_ore: Optional[str] = None, spoiler_rarity: int = 0, spoiler_rocks: List[str] = None, biomes: str = None, height: int = 0, preset: Tuple[int, int, int, int, int, int, int, int] = None, deposits: bool = False):
    assert preset is not None
    return Vein(ore, vein_type, preset[0], preset[1], preset[2], preset[3], preset[4], preset[5], preset[6], preset[7], rocks, spoiler_ore, spoiler_rarity, spoiler_rocks, biomes, height, deposits)


ALLOYS: Dict[str, Tuple[Tuple[str, float, float], ...]] = {
    'electrum': (('gold', 0.4, 0.6), ('silver', 0.4, 0.6)),
    'constantan': (('copper', 0.4, 0.6), ('nickel', 0.4, 0.6))
}

ORE_VEINS: Dict[str, Vein] = {
    'normal_aluminum': preset_vein('aluminum', 'cluster', ['sedimentary', 'metamorphic'], preset=SURFACE_METAL_ORE),
    'deep_aluminum': preset_vein('aluminum', 'cluster', ['sedimentary', 'metamorphic'], preset=NORMAL_METAL_ORE),
    'normal_lead': preset_vein('lead', 'cluster', ['metamorphic', 'igneous_extrusive', 'igneous_intrusive'], preset=NORMAL_METAL_ORE),
    'deep_lead': preset_vein('lead', 'cluster', ['metamorphic', 'igneous_extrusive', 'igneous_intrusive'], preset=DEEP_METAL_ORE),
    'normal_uranium': preset_vein('uranium', 'cluster', ['metamorphic', 'igneous_extrusive'], preset=NORMAL_METAL_ORE),
    'deep_uranium': preset_vein('uranium', 'cluster', ['metamorphic', 'igneous_extrusive'], preset=DEEP_METAL_ORE),
}

ORES = [
    ('native_copper', 'copper'),
    ('native_gold', 'gold'),
    ('hematite', 'cast_iron'),
    ('native_silver', 'silver'),
    ('cassiterite', 'tin'),
    ('bismuthinite', 'bismuth'),
    ('garnierite', 'nickel'),
    ('malachite', 'copper'),
    ('magnetite', 'cast_iron'),
    ('limonite', 'cast_iron'),
    ('sphalerite', 'zinc'),
    ('tetrahedrite', 'copper'),
]

ADDON_ORES: Dict[str, str] = {
    'aluminum': 'copper',
    'lead': 'copper',
    'uranium': 'iron',
}

TFC_METALS = [
    'bismuth', 'bismuth_bronze', 'black_bronze', 'bronze', 'brass', 'copper', 'gold', 'nickel', 'rose_gold', 'silver',
    'tin', 'zinc', 'sterling_silver', 'wrought_iron', 'cast_iron', 'steel', 'black_steel', 'blue_steel',
    'red_steel',
]

TFC_OTHER_METALS = ['pig_iron', 'weak_steel', 'weak_blue_steel', 'weak_red_steel', 'high_carbon_steel', 'high_carbon_black_steel',
                    'high_carbon_blue_steel', 'high_carbon_red_steel']

ADDON_METALS = ['aluminum', 'constantan', 'electrum', 'lead', 'uranium']

DEFAULT_LANG = {
    'block.immersiveengineering.acetaldehyde_fluid_block': 'Acetaldehyde',
    'block.immersiveengineering.biodiesel_fluid_block': 'Biodiesel',
    'block.immersiveengineering.concrete_fluid_block': 'Concrete',
    'block.immersiveengineering.creosote_fluid_block': 'Creosote',
    'block.immersiveengineering.ethanol_fluid_block': 'Ethanol',
    'block.immersiveengineering.fake_light': 'Light',
    'block.immersiveengineering.herbicide_fluid_block': 'Herbicide',
    'block.immersiveengineering.phenolic_resin_fluid_block': 'Phelonic Resin',
    'block.immersiveengineering.plantoil_fluid_block': 'Plant Oil',
    'block.immersiveengineering.post_transformer': 'Post Transformer',
    'block.immersiveengineering.potted_hemp': 'Potted Hemp',
    'block.immersiveengineering.redstone_acid_fluid_block': 'Redstone Acid',
    'block.immersiveengineering.shader_banner': 'Shader Banner',
    'block.immersiveengineering.shader_banner_wall': 'Shader Banner Wall',
    'block.immersiveengineering.toolbox_block': 'Toolbox',
    'entity.immersiveengineering.chemthrower_shot': 'Chemthrower Shot',
    'entity.immersiveengineering.explosive': 'Explosive',
    'entity.immersiveengineering.fluorescent_tube': 'Fluorescent Tube',
    'entity.immersiveengineering.railgun_shot': 'Railgun Shot',
    'entity.immersiveengineering.revolver_shot': 'Revolver Shot',
    'entity.immersiveengineering.revolver_shot_flare': 'Revolver Flare',
    'entity.immersiveengineering.revolver_shot_homing': 'Revolver Homing',
    'entity.immersiveengineering.revolver_shot_wolfpack': 'Revolver Wolfpack',
    'entity.immersiveengineering.sawblade': 'Sawblade',
    'entity.immersiveengineering.skyline_hook': 'Skyline Hook',
    'item.immersiveengineering.armor_piercing': 'Armor Piercing',
    'item.immersiveengineering.buckshot': 'Buckshot',
    'item.immersiveengineering.casull': 'Casull',
    'item.immersiveengineering.dragons_breath': 'Dragon\'s breath',
    'item.immersiveengineering.fake_icon_birthday': 'Birthday',
    'item.immersiveengineering.fake_icon_bttf': 'BTTF',
    'item.immersiveengineering.fake_icon_drillbreak': 'Drillbreak',
    'item.immersiveengineering.fake_icon_fried': 'Fried',
    'item.immersiveengineering.fake_icon_lucky': 'Lucky',
    'item.immersiveengineering.fake_icon_ravenholm': 'Ravenholm',
    'item.immersiveengineering.firework': 'Firework',
    'item.immersiveengineering.flare': 'Flare',
    'item.immersiveengineering.he': 'HE',
    'item.immersiveengineering.homing': 'Homing',
    'item.immersiveengineering.potion': 'Potion',
    'item.immersiveengineering.silver': 'Silver',
    'item.immersiveengineering.wolfpack': 'Wolfpack',
    'item.immersiveengineering.wirecoil_structure_rope': 'Rope Coil',

    'desc.immersiveengineering.info.mineral.stannite': 'Stannite',
    'desc.immersiveengineering.info.mineral.aikinite': 'Aikinite',
    'desc.immersiveengineering.info.mineral.franklinite': 'Franklinite',
    'desc.immersiveengineering.info.mineral.quartzite': 'Quartzite',

    'manual.tfc_ie_addon.tfc_ie_changes': 'Changes with TerraFirmaCraft'
}

TO_REMOVE_RECIPES = [
    'immersiveengineering:crafting/armor_faraday_chest',
    'immersiveengineering:crafting/armor_faraday_feet',
    'immersiveengineering:crafting/armor_faraday_head',
    'immersiveengineering:crafting/armor_faraday_legs',

    'immersiveengineering:crafting/armor_steel_chest',
    'immersiveengineering:crafting/armor_steel_feet',
    'immersiveengineering:crafting/armor_steel_head',
    'immersiveengineering:crafting/armor_steel_legs',

    'immersiveengineering:crafting/axe_steel',
    'immersiveengineering:crafting/sword_steel',
    'immersiveengineering:crafting/pickaxe_steel',
    'immersiveengineering:crafting/hoe_steel',
    'immersiveengineering:crafting/axe_steel',
    'immersiveengineering:crafting/shovel_steel',

    'immersiveengineering:crafting/drillhead_steel',

    'immersiveengineering:crafting/stick_aluminum',
    'immersiveengineering:crafting/stick_iron',
    'immersiveengineering:crafting/stick_steel',

    'immersiveengineering:crafting/toolupgrade_revolver_bayonet',
    'immersiveengineering:crafting/gunpart_barrel',

    'immersiveengineering:crafting/empty_casing',

    'immersiveengineering:crafting/gunpowder_from_dusts',

    'immersiveengineering:crusher/wool',

    'immersiveengineering:metalpress/rod_steel',

    'immersiveengineering:mineral/beryl',
    'immersiveengineering:mineral/cassiterite',

    'immersiveengineering:crafting/alloybrick',
    'immersiveengineering:crafting/cokebrick',
    'immersiveengineering:crafting/blastbrick_reinforced',

    'immersiveengineering:crafting/storage_steel_to_ingot_steel',
    'immersiveengineering:crafting/storage_uranium_to_ingot_uranium'
]

IE_METALS = [
    'aluminum',
    'constantan',
    'electrum',
    'lead',
    'steel',
    'uranium',
    'nickel',
    'silver'
]

FL_METALS = [
    'chromium',
    'stainless_steel'
]

[TO_REMOVE_RECIPES.extend([
    'immersiveengineering:crafting/ingot_%s_to_storage_%s' % (metal, metal),
]) for metal in IE_METALS]

HAMMERED_METALS = IE_METALS + ['copper', 'gold', 'iron']

[TO_REMOVE_RECIPES.append(
    'immersiveengineering:crafting/plate_%s_hammering' % metal
) for metal in HAMMERED_METALS]

SANDSTONE_COLORS = ['brown', 'white', 'black', 'red', 'yellow', 'green', 'pink']
SANDSTONE_TYPES = ['raw', 'smooth', 'cut']

TFC_WOOD_ITEMS = [
    ('bookshelf', 6, 1600),
    ('door', 3, 800),
    ('trapdoor', 6, 800),
    ('fence', 2, 800),
    ('log_fence', 4, 800),
    ('fence_gate', 6, 800),
    ('button', 1, 400),
    ('pressure_plate', 2, 400),
    ('slab', 2, 800),
    ('stairs', 3, 1600),
    ('tool_rack', 6, 800),
    ('loom', 7, 1600),
]

TFC_OTHER_WOOD_ITEMS = [
    ('boat', 16, 1600),
    ('sign', 2, 800),
    ('trapped_chest', 8, 1600),
    ('chest', 8, 1600),
    ('sluice', 3, 1600),
    ('barrel', 7, 1600),
    ('lectern', 10, 1600),
]

TFC_WOOD_TYPES = [
    'acacia',
    'ash',
    'aspen',
    'birch',
    'blackwood',
    'chestnut',
    'douglas_fir',
    'hickory',
    'kapok',
    'maple',
    'oak',
    'palm',
    'pine',
    'rosewood',
    'sequoia',
    'spruce',
    'sycamore',
    'white_cedar',
    'willow'
]


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
