plugins {
    id("java")
    id("idea")
//    id("net.neoforged.gradle") version "[6.0.13,6.2)"
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("org.spongepowered.mixin") version "0.7.+"
}

val minecraftVersion: String = "1.20.1"
val forgeVersion: String = "47.2.6"
val parchmentVersion: String = "2023.09.03-1.20.1"
val mixinVersion: String = "0.8.5"
val tfcVersion: String = "4895012"
val jeiVersion: String = "15.2.0.27"
val patchouliVersion: String = "1.20.1-81-FORGE"
val jadeVersion: String = "4614153"
val topVersion: String = "4629624"
val ieVersion: String = "1.20.1-10.0.0-169.45"
val ipVersion: String = ""

val modId: String = "tfc_ie_addon"

base {
    archivesName.set("TFC-IE-Crossover-$minecraftVersion")
    group = "com.nmagpie.tfc_ie_addon"
    version = "1.1.3"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

idea {
    module {
        excludeDirs.add(file("run"))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://dvs1.progwml6.com/files/maven/") // JEI
    maven(url = "https://modmaven.k-4u.nl") // Mirror for JEI
    maven(url = "https://maven.blamejared.com") // Patchouli & IE
    maven(url = "https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    flatDir {
        dirs("libs")
    }
}

dependencies {
//    minecraft("net.neoforged", "forge", version = "$minecraftVersion-$forgeVersion")
    minecraft("net.minecraftforge", "forge", version = "$minecraftVersion-$forgeVersion")
    implementation(fg.deobf("curse.maven:tfc-302973:$tfcVersion"))

    // JEI
    compileOnly(fg.deobf("mezz.jei:jei-$minecraftVersion-forge-api:$jeiVersion"))
    compileOnly(fg.deobf("mezz.jei:jei-$minecraftVersion-common-api:$jeiVersion"))
    runtimeOnly(fg.deobf("mezz.jei:jei-$minecraftVersion-forge:$jeiVersion"))

    // Patchouli
    compileOnly(fg.deobf("vazkii.patchouli:Patchouli:$patchouliVersion"))
    runtimeOnly(fg.deobf("vazkii.patchouli:Patchouli:$patchouliVersion"))

    // Jade / The One Probe
    compileOnly(fg.deobf("curse.maven:jade-324717:$jadeVersion"))
    compileOnly(fg.deobf("curse.maven:top-245211:$topVersion"))

    // Only use Jade at runtime
    runtimeOnly(fg.deobf("curse.maven:jade-324717:$jadeVersion"))

    // Immersive Engineering
    implementation(fg.deobf("blusunrize.immersiveengineering:ImmersiveEngineering:$ieVersion"))
    implementation(fg.deobf("blusunrize.immersiveengineering:ImmersiveEngineering:$ieVersion:datagen"))

    // Immersive Petroleum
    //implementation(fg.deobf("curse.maven:immersive-petroleum-268250:$ipVersion"))

    if (System.getProperty("idea.sync.active") != "true") {
        annotationProcessor("org.spongepowered:mixin:$mixinVersion:processor")
    }
}

minecraft {
    mappings("parchment", parchmentVersion)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        all {
            args("-mixin.config=$modId.mixins.json")

            property("forge.logging.console.level", "debug")

            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "$projectDir/build/createSrgToMcp/output.srg")

            jvmArgs("-Xmx4G", "-Xms4G")

            jvmArg("-XX:+AllowEnhancedClassRedefinition")

            mods.create(modId) {
                source(sourceSets.main.get())
            }
        }

        register("client") {
            workingDirectory(project.file("run/client"))
        }

        register("server") {
            workingDirectory(project.file("run/server"))

            arg("--nogui")
        }
    }
}

mixin {
    add(sourceSets.main.get(), "$modId.refmap.json")
}

tasks {
    jar {
        manifest {
            attributes["Implementation-Version"] = project.version
            attributes["MixinConfigs"] = "$modId.mixins.json"
        }
    }
}
