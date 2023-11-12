import os
from argparse import ArgumentParser

from mcresources.type_definitions import ResourceIdentifier

from patchouli import *
from i18n import I18n

GRADES = ['poor', 'normal', 'rich']
GRADES_ALL = ['small', 'poor', 'normal', 'rich']


class LocalInstance:
    INSTANCE_DIR = os.getenv('LOCAL_MINECRAFT_INSTANCE')  # The location of a local .minecraft directory, for testing in external minecraft instance (as hot reloading works much better)

    @staticmethod
    def wrap(rm: ResourceManager):
        def data(name_parts: ResourceIdentifier, data_in: JsonObject):
            return rm.write((LocalInstance.INSTANCE_DIR, '/'.join(utils.str_path(name_parts))), data_in)

        if LocalInstance.INSTANCE_DIR is not None:
            rm.data = data
            return rm
        return None


def main():
    parser = ArgumentParser('generate_book.py')
    parser.add_argument('--translate', type=str, default='en_us')

    args = parser.parse_args()

    rm = ResourceManager('tfc', '../src/main/resources')
    i18n = I18n.create(args.translate)

    print('Writing book')
    make_book(rm, i18n)

    i18n.flush()

    if LocalInstance.wrap(rm):
        print('Copying into local instance at: %s' % LocalInstance.INSTANCE_DIR)
        make_book(rm, I18n.create('en_us'), local_instance=True)

    print('Done')


def make_book(rm: ResourceManager, i18n: I18n, local_instance: bool = False):
    book = Book(rm, 'field_guide', {}, i18n, local_instance)

    book.template('tri_anvil_recipe', header_component(-1, -1), custom_component(0, 0, 'TriAnvilComponent', {'recipe': '#recipe', 'recipe2': '#recipe2', 'recipe3': '#recipe3'}))

    book.category('tfc_ie_changes', 'TFC + IE Crossover', 'Changes with TerraFirmaCraft.', 'immersiveengineering:manual', is_sorted=True, entries=(
        entry('ores', 'New Ores', 'tfc_ie_addon:ore/normal_aluminum', pages=(
            text('Bauxite is an ore of $(thing)Aluminum$(), which is an integral part of high-voltage architecture. It can be found at any elevation, but deeper veins are often richer. It can be found in $(l:the_world/geology#sedimentary)Sedimentary$() and $(l:the_world/geology#metamorphic)Metamorphic$() rocks.', title='Bauxite').link(*['tfc_ie_addon:ore/%s_%s' % (g, 'aluminum') for g in GRADES_ALL]).anchor('aluminum'),
            multimultiblock('Bauxite Ores in Marble.', *[block_spotlight('', '', 'tfc_ie_addon:ore/%s_%s/%s' % (g, 'aluminum', 'marble')) for g in GRADES]),
            text('Lead is used in creating early accumulators. It can be found at elevations below y=75. It can be found in $(l:the_world/geology#metamorphic)Metamorphic$(), $(l:the_world/geology#igneous_extrusive)Igneous Extrusive$() and $(l:the_world/geology#igneous_intrusive)Igneous Intrusive$() rocks.', title='Lead').link(*['tfc_ie_addon:ore/%s_%s' % (g, 'lead') for g in GRADES_ALL]).anchor('lead'),
            multimultiblock('Lead Ores in Marble.', *[block_spotlight('', '', 'tfc_ie_addon:ore/%s_%s/%s' % (g, 'lead', 'marble')) for g in GRADES]),
            text('Uranium can be found at elevations below y=75. It can be found in $(l:the_world/geology#metamorphic)Metamorphic$() and $(l:the_world/geology#igneous_extrusive)Igneous Extrusive$() rocks. A uranium block can serve as a heatsource for the $(thing)Thermoelectric Generator$().', title='Uranium').link(*['tfc_ie_addon:ore/%s_%s' % (g, 'uranium') for g in GRADES_ALL]).anchor('uranium'),
            multimultiblock('Uranium Ores in Marble.', *[block_spotlight('', '', 'tfc_ie_addon:ore/%s_%s/%s' % (g, 'uranium', 'marble')) for g in GRADES])
        )),
        entry('alloys', 'New Alloys', 'immersiveengineering:ingot_electrum', pages=(
            text('Since TerraFirmaCraft adds alloy crafting system, now you have possibility to create Immersive Engineering Alloys like $(thing)Electrum$() or $(thing)Constantan$() in TFC style! All you have to know are proportions for both alloys (they are pretty easy, trust me).'),
            alloy_recipe('Electrum', 'electrum', '').link('immersiveengineering:ingot_electrum'),
            alloy_recipe('Constantan', 'constantan', '').link('immersiveengineering:ingot_constantan'),
            empty_last_page()
        )),
        entry('mold', 'New Molds', 'tfc_ie_addon:mold_sheet', pages=(
            non_text_first_page(),
            item_spotlight('tfc_ie_addon:mold_sheet', 'Sheet Mold', text_contents='The $(thing)Sheet Mold$() is created with an $(thing)Engineering\'s Blueprint$() at the $(thing)Engineering\'s Workbench$(). With it, you can create metal sheets of all metals at the $(thing)Metal Press$().'),
            item_spotlight('tfc_ie_addon:mold_block', 'Block Mold', text_contents='The $(thing)Block Mold$() is created in the same way. You can create $(thing)Block of Steel$() and $(thing)Block of Uranium$() with it.')
        )),
        entry('chemicals', 'Chemical Ingredients', 'tfc:powder/saltpeter', pages=(
            text('Not all materials in engineer- ing are as straightforward as wood, stone and various metals. A basic understanding of chemical processes is essential to appreciate the breadth of materials available to you.'),
            quern_recipe('tfc:quern/saltpeter', '$(thing)Saltpeter$() is an alkali salt obtained by processing $(l:the_world/ores_and_minerals#saltpeter)Saltpeter Ores$() and $(thing)Sandstone$() in a $(thing)Crusher$(). $(thing)Saltpeter Ores$() can also be processed in a $(l:mechanics/quern)Quern$().'),
            crafting('immersiveengineering:crafting/fertilizer', text_contents='$(thing)Saltpeter$() is used as a catalyst in the creation of $(thing)Biodiesel$(), and can be used to craft $(thing)Fertilizer$() as shown above.'),
            quern_recipe('tfc:quern/sulfur', '$(thing)Sulfur$() is a powder found in certain ores and commonly in $(thing)Mineral Deposits$(). It can be obtained by processing $(l:the_world/ores_and_minerals#sulfur)Sulfur Ores$() and $(thing)Quartz Blocks$() in a $(thing)Crusher$(). $(thing)Sulfur Ores$() can also be processed in a $(l:mechanics/quern)Quern$().').anchor('sulfur'),
            crafting('tfc:crafting/gunpowder', 'tfc:crafting/gunpowder_graphite'),
            text('Mixing $(thing)Saltpeter$(), $(thing)Sulfur$() and $(thing)Charcoal$() allows for the creation of $(thing)Gunpowder$(), a far more scientific process than looting it from slain monsters.')
        )),
        entry('multiblocks', 'Multiblock Changes', 'tfc:fire_bricks', pages=(
            text('Now, there are changes in creating multiblock structures. To create $(thing)Coke Oven$() and $(thing)Arc Furnace$() you will need $(thing)Fire Bricks$() from TerraFirmaCraft instead of Blast Bricks.$(br)Also, keep in mind that you don\'t need Blast Furnace from Immersive Engineering since you have it in TerraFirmaCraft! Additionally, you don\'t need Alloy Kiln because, you know, you can make alloys in TerraFirmaCraft.').link('immersiveengineering:cokebrick').link('immersiveengineering:blastbrick'),
            crafting('tfc:crafting/fire_bricks'),
            multiblock('Arc Furnace', '', True, pattern=(
                ('     ', 'S    ', 'LLL  ', 'S    ', '     '),
                ('     ', 'SBBB ', 'LBBB ', 'SBBB ', '     '),
                ('M T  ', 'LBBBB', 'LBBBB', 'LBBBB', 'M T  '),
                ('HHT  ', 'LBB  ', 'LBB  ', 'LBB  ', 'HHT R'),
                ('MMTmm', 'Mmmmm', 'Hmmm0', 'Mmmmm', 'MMTmS')), mapping={
                'S': 'immersiveengineering:steel_scaffolding_standard',
                'T': 'immersiveengineering:storage_steel',
                'M': 'immersiveengineering:sheetmetal_steel',
                'm': 'immersiveengineering:slab_sheetmetal_steel',
                'B': 'tfc:fire_bricks',
                'L': 'immersiveengineering:light_engineering',
                'H': 'immersiveengineering:heavy_engineering',
                'R': 'immersiveengineering:rs_engineering',
                '0': 'minecraft:cauldron'
            }),
            multiblock('Coke Oven', '', True, pattern=(
                ('BBB', 'BBB', 'BBB'),
                ('BBB', 'BBB', 'BBB'),
                ('BBB', 'BB0', 'BBB')), mapping={
                'B': 'tfc:fire_bricks',
                '0': 'tfc:fire_bricks'
            })
        )),
        entry('blueprint', 'Blueprint Recipes', 'immersiveengineering:blueprint', pages=(
            text('Since TerraFirmaCraft world does not provide village generation, you won\'t be able to meet Villagers that could sell you some Blueprints. Thus, you can now craft it on the crafting table! It will be a little difficult, but it will worth it.'),
            crafting('tfc_ie_addon:crafting/blueprint_electrode', 'tfc_ie_addon:crafting/blueprint_special_bullet')
        )),
        entry('jute', 'Jute', 'tfc:jute_fiber', pages=(
            non_text_first_page(),
            item_spotlight('tfc:jute_fiber', 'Jute', text_contents='Industrial Hemp is a remarkable plant, but today we are going to talk about $(thing)Jute$()! Not only are its seeds useful for the creation of $(thing)Biodiesel$(), $(thing)Jute Fiber$() is also used for the creation of $(thing)Burlap Cloth$(), which is an alternative of Tough Fabric. The seeds can be obtained by harvesting $(l:the_world/wild_crops)Wild Jute$().'),
            loom_recipe('tfc:loom/burlap_cloth', 'Similar to Tough Fabric, $(thing)Burlap Cloth$() is a resilient weave made from $(thing)Jute Fiber$(). It\'s used to create $(thing)Improved Windmills$() as well as $(thing)Balloons$() and $(thing)Jump Cushions$()')
        )),
        entry('quartz', 'Quartz', 'tfc_ie_addon:mineral/quartz_shard', pages=(
            non_text_first_page(),
            item_spotlight('tfc_ie_addon:mineral/quartz_shard', 'Quartz', text_contents='You cannot visit Nether anymore, but you still need some Quartz for Electronics? Not a problem! Now you can find in TFC Overworld $(thing)Quartz Geodes$(), which can contain $(thing)Quartz Blocks$() and $(thing)Quartz Clusters$()! It can be helpful for crafting electronics from the Immersive Engineering!').link('tfc_ie_addon:mineral/quartz_shard').link('tfc_ie_addon:mineral/quartz_block'),
            multiblock('Quartz Cluster', 'Keep in mind that with time $(thing)Budding Quartz Blocks$() can produce more $(thing)Quartz Clusters$().', False, pattern=(
                ('   ', ' Q ', '   '),
                ('   ', ' 0 ', '   ')), mapping={
                'Q': 'tfc_ie_addon:mineral/quartz_cluster',
                '0': 'tfc_ie_addon:mineral/budding_quartz'
            })
        )),
        entry('graphite', 'Graphite', 'immersiveengineering:ingot_hop_graphite', pages=(
            text('Highly Ordered Pyrolytic Graphite (HOP) is a complex, highly compressed, carbon material used in special engineering constructs. $(thing)HOP Graphite Dust$() is created by compressing eight pieces of $(thing)Coke Dust$() or $(thing)Graphite Powder$() in the $(thing)Industrial Squeezer$(). That dust can then be smelted into an ingot.'),
            item_spotlight('immersiveengineering:graphite_electrode', text_contents='The most common use for HOP Graphite is the creation of $(thing)electrodes$() to be used in the $(thing)Arc Furnace$(). These $(thing)electrodes$() are created with an $(l:tfc:tfc_ie_changes/blueprint)Engineer\'s Blueprint$(), which can be crafted. You can also create $(thing)electrodes$() in the $(thing)Metal Press$(), using the Rod Mold on 4 ingots, but the $(thing)electrodes$() crafted this way only have half the durability.')
        )),
        entry('steel_obtain', 'Steel Creation', 'tfc:metal/ingot/steel', pages=(
            text('In the TerraFirmaCraft World it is not so easy to create a $(l:mechanics/steel)Steel Ingot$(). Now you have to follow full process of forging the ingots from $(thing)Pig Iron$() to $(thing)Steel$(). Luckily, this process can be simplified later.').link('immersiveengineering:slag'),
            item_spotlight('immersiveengineering:slag', text_contents='Also, $(thing)Slag$() now can be obtained while you are forging $(thing)Pig Iron Ingot$() to get $(thing)High Carbon Steel Ingot$().')
        )),
        entry('herbicide', 'Herbicide Additions', 'immersiveengineering:herbicide_bucket', pages=(
            text('$(thing)Herbicide$() works in the TerraFirmaCraft World too! When loaded into a $(thing)Chemical Thrower$() it is dispersed as a fine mist, which withers and destroys any plants it comes in contact with. The fluid will destroy flowers and crops and strip the grass from dirt. It will also dehydrate and ruin farmland. It can therefore be used as an alternative to $(thing)Rotten Compost$().'),
            item_spotlight('immersiveengineering:herbicide_bucket', text_contents='To craft $(thing)Herbicide$(), mix half a bucket of $(thing)Ethanol$() with $(thing)Malachite Powder$() and $(l:tfc:tfc_ie_changes/chemicals#sulfur)Sulfur$() in the $(thing)Mixer$().')
        )),
        entry('external_heater', 'Electric Crucible?', 'tfc:crucible', pages=(
            text('There is no Electric Crucible in this mod, but the $(thing)External Heater$() is made to be able to heat a $(l:mechanics/crucible)Crucible$() just like a $(l:mechanics/charcoal_forge)Charcoal Forge$().').link('immersiveengineering:furnace_heater'),
            crafting('immersiveengineering:crafting/furnace_heater', text_contents='By default, it heats a $(thing)Crucible$() up to 2000°C, consuming 20IF/t.')
        )),
        entry('drill', 'High-End Drill Heads', 'tfc_ie_addon:drillhead_black_steel', pages=(
            text('Does mining with the drill feel too slow? Are you not satisfied with the steel drill head?$(br)You can now create drill heads of coloured steels! They have better mining speed and size, as well as higher durability.'),
            tri_anvil_recipe('Drill Heads', 'tfc_ie_addon:anvil/drillhead_black_steel', 'tfc_ie_addon:anvil/drillhead_blue_steel', 'tfc_ie_addon:anvil/drillhead_red_steel')
        ))
    ))


if __name__ == '__main__':
    main()
