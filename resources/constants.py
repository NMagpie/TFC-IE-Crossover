from typing import Dict, List, NamedTuple, Optional, Literal, Tuple, Any, Set


class Ore(NamedTuple):
    metal: Optional[str]
    graded: bool
    required_tool: str
    tag: str
    dye_color: Optional[str] = None


class Metal(NamedTuple):
    tier: int
    types: Set[str]
    heat_capacity_base: float  # Do not access directly, use one of specific or ingot heat capacity.
    melt_temperature: float
    melt_metal: Optional[str]

    def specific_heat_capacity(self) -> float: return round(300 / self.heat_capacity_base) / 100_000

    def ingot_heat_capacity(self) -> float: return 1 / self.heat_capacity_base


class Vein(NamedTuple):
    ore: str  # The name of the ore (as found in ORES)
    vein_type: str  # Either 'cluster', 'pipe' or 'disc'
    rarity: int
    size: int
    min_y: int
    max_y: int
    density: float
    grade: tuple[int, int, int]  # (poor, normal, rich) weights
    rocks: tuple[str, ...]  # Rock, or rock categories
    biomes: str | None
    height: int
    radius: int
    deposits: bool
    indicator_rarity: int  # Above-ground indicators
    underground_rarity: int  # Underground indicators
    underground_count: int
    project: bool | None  # Project to surface
    project_offset: bool | None  # Project offset

    @staticmethod
    def new(
        ore: str,
        rarity: int,
        size: int,
        min_y: int,
        max_y: int,
        density: float,
        rocks: tuple[str, ...],

        vein_type: str = 'cluster',
        grade: tuple[int, int, int] = (),
        biomes: str = None,
        height: int = 2,  # For disc type veins, `size` is the width
        radius: int = 5,  # For pipe type veins, `size` is the height
        deposits: bool = False,
        indicator: int = 12,  # Indicator rarity
        deep_indicator: tuple[int, int] = (1, 0),  # Pair of (rarity, count) for underground indicators
        project: str | bool = None,  # Projects to surface. Either True or 'offset'
    ):
        assert 0 < density < 1
        assert isinstance(rocks, tuple), 'Forgot the trailing comma in a single element tuple: %s' % repr(rocks)
        assert vein_type in ('cluster', 'disc', 'pipe')
        assert project is None or project is True or project == 'offset'

        underground_rarity, underground_count = deep_indicator
        return Vein(ore, 'tfc:%s_vein' % vein_type, rarity, size, min_y, max_y, density, grade, rocks, biomes, height, radius, deposits, indicator, underground_rarity, underground_count, None if project is None else True, None if project != 'offset' else True)

    def config(self) -> dict[str, Any]:
        cfg = {
            'rarity': self.rarity,
            'density': self.density,
            'min_y': self.min_y,
            'max_y': self.max_y,
            'project': self.project,
            'project_offset': self.project_offset,
            'biomes': self.biomes
        }
        if self.vein_type == 'tfc:cluster_vein':
            cfg.update(size=self.size)
        elif self.vein_type == 'tfc:pipe_vein':
            cfg.update(min_skew=5, max_skew=13, min_slant=0, max_slant=2, sign=0, height=self.size, radius=self.radius)
        else:
            cfg.update(size=self.size, height=self.height)
        return cfg


ORES: Dict[str, Ore] = {
    'bauxite': Ore('aluminum', True, 'copper', 'aluminum', 'red'),
    'galena': Ore('lead', True, 'copper', 'lead', 'black'),
    'uraninite': Ore('uranium', True, 'iron', 'uranium', 'green')
}

METALS: Dict[str, Metal] = {
    'aluminum': Metal(1, {'part'}, 0.35, 650, None),
    'lead': Metal(2, {'part'}, 0.35, 500, None),
    'uranium': Metal(3, {'part'}, 0.35, 1250, None),

    'constantan': Metal(2, {'part'}, 0.35, 750, None),
    'electrum': Metal(3, {'part'}, 0.35, 900, None)
}

OreGrade = NamedTuple('OreGrade', grind_amount=int)
RockCategory = Literal['sedimentary', 'metamorphic', 'igneous_extrusive', 'igneous_intrusive']
Rock = NamedTuple('Rock', category=RockCategory, sand=str)

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
    'normal': OreGrade(5),
    'poor': OreGrade(3),
    'rich': OreGrade(7)
}

ALLOYS: Dict[str, Tuple[Tuple[str, float, float], ...]] = {
    'electrum': (('gold', 0.4, 0.6), ('silver', 0.4, 0.6)),
    'constantan': (('copper', 0.4, 0.6), ('nickel', 0.4, 0.6))
}

POOR = 70, 25, 5  # = 1550
NORMAL = 35, 40, 25  # = 2400
RICH = 15, 25, 60  # = 2550

ORE_VEINS: Dict[str, Vein] = {
    'surface_bauxite': Vein.new('bauxite', 40, 15, 48, 100, 0.3, ('sedimentary', 'metamorphic'), grade=POOR),
    'normal_bauxite': Vein.new('bauxite', 60, 25, -64, 32, 0.6, ('sedimentary', 'metamorphic'), grade=RICH, indicator=0, deep_indicator=(1, 4)),
    'surface_galena': Vein.new('galena', 40, 15, 32, 75, 0.3, ('metamorphic', 'igneous_extrusive', 'igneous_intrusive'), grade=POOR),
    'normal_galena': Vein.new('galena', 60, 25, -80, 20, 0.6, ('metamorphic', 'igneous_extrusive', 'igneous_intrusive'), grade=RICH, indicator=0, deep_indicator=(1, 4)),
    'normal_uraninite': Vein.new('uraninite', 50, 25, -80, 20, 0.6, ('metamorphic', 'igneous_extrusive'), grade=RICH, indicator=0, deep_indicator=(1, 4))
}

TFC_ORES: Dict[str, Ore] = {
    'native_copper': Ore('copper', True, 'copper', 'copper', 'orange'),
    'native_gold': Ore('gold', True, 'copper', 'gold'),
    'hematite': Ore('cast_iron', True, 'copper', 'iron', 'red'),
    'native_silver': Ore('silver', True, 'copper', 'silver', 'light_gray'),
    'cassiterite': Ore('tin', True, 'copper', 'tin', 'gray'),
    'bismuthinite': Ore('bismuth', True, 'copper', 'bismuth', 'green'),
    'garnierite': Ore('nickel', True, 'bronze', 'nickel', 'brown'),
    'malachite': Ore('copper', True, 'copper', 'copper', 'green'),
    'magnetite': Ore('cast_iron', True, 'copper', 'iron', 'gray'),
    'limonite': Ore('cast_iron', True, 'copper', 'iron', 'yellow'),
    'sphalerite': Ore('zinc', True, 'copper', 'zinc', 'gray'),
    'tetrahedrite': Ore('copper', True, 'copper', 'copper', 'gray')
}

TFC_METALS = [
    'bismuth', 'bismuth_bronze', 'black_bronze', 'bronze', 'brass', 'copper', 'gold', 'nickel', 'rose_gold', 'silver',
    'tin', 'zinc', 'sterling_silver', 'wrought_iron', 'cast_iron', 'steel', 'black_steel', 'blue_steel',
    'red_steel'
]

TFC_OTHER_METALS = ['pig_iron', 'weak_steel', 'weak_blue_steel', 'weak_red_steel', 'high_carbon_steel', 'high_carbon_black_steel',
                    'high_carbon_blue_steel', 'high_carbon_red_steel']

ADDON_METALS = ['aluminum', 'constantan', 'electrum', 'lead', 'uranium']

DEFAULT_LANG = {
    'desc.immersiveengineering.info.mineral.stannite': 'Stannite',
    'desc.immersiveengineering.info.mineral.aikinite': 'Aikinite',
    'desc.immersiveengineering.info.mineral.franklinite': 'Franklinite',
    'desc.immersiveengineering.info.mineral.quartzite': 'Quartzite',

    'tfc.enum.glassoperation.lead': 'Galena Powder',
    'tfc.enum.glassoperation.uranium': 'Uraninite Powder',

    'manual.tfc_ie_addon.tfc_ie_changes': 'Changes with TerraFirmaCraft',

    'pack.tfc_ie_addon.immersivepetroleum.title': 'TFC + IE Addon Immersive Petroleum Override',
    'pack.tfc_ie_addon.immersivepetroleum.description': 'Compatibility with Immersive Petroleum by TFC + IE Addon',

    'tfc_ie_addon.creative_tab.main': 'TFC + IE Crossover'
}

TO_REMOVE_RECIPES = [
    'immersiveengineering:crafting/armor_faraday_chestplate',
    'immersiveengineering:crafting/armor_faraday_boots',
    'immersiveengineering:crafting/armor_faraday_helmet',
    'immersiveengineering:crafting/armor_faraday_leggings',

    'immersiveengineering:crafting/armor_steel_chestplate',
    'immersiveengineering:crafting/armor_steel_boots',
    'immersiveengineering:crafting/armor_steel_helmet',
    'immersiveengineering:crafting/armor_steel_leggings',

    'immersiveengineering:crafting/axe_steel',
    'immersiveengineering:crafting/sword_steel',
    'immersiveengineering:crafting/pickaxe_steel',
    'immersiveengineering:crafting/hoe_steel',
    'immersiveengineering:crafting/axe_steel',
    'immersiveengineering:crafting/shovel_steel',

    'immersiveengineering:crafting/drillhead_steel',
    'immersiveengineering:crafting/drillhead_iron',

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
    'immersiveengineering:crafting/blastbrick',
    'immersiveengineering:crafting/blastbrick_reinforced',

    'immersiveengineering:crafting/storage_steel_to_ingot_steel',
    'immersiveengineering:crafting/storage_uranium_to_ingot_uranium',

    'immersiveengineering:crafting/treated_wood_horizontal'
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

AFC_WOOD_TYPES = [
    'cypress',
    'tualang',
    'hevea',
    'teak',
    'eucalyptus',
    'baobab',
    'fig',
    'mahogany'
]


def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()
