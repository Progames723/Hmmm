@file:Suppress("UnstableApiUsage")
import java.util.*

plugins {
//	id("stonecutter") version "0.6"
	id("dev.architectury.loom")
	id("architectury-plugin")
	id("me.modmuss50.mod-publish-plugin")
	id("com.github.johnrengelman.shadow")
}

val minecraft = stonecutter.current.version
val loader = loom.platform.get().name.lowercase()

version = "${mod.version}+$minecraft"
group = mod.group
base {
	archivesName.set("${mod.id}-$loader")
}

architectury.common(stonecutter.tree.branches.mapNotNull {
	if (stonecutter.current.project !in it) null
	else it.project.prop("loom.platform")
})

loom.silentMojangMappingsLicense()

stonecutter {
	//loader constants
	constants {
		put("fabric", loader == "fabric")
		put("forge", loader == "forge")
		put("neoforge", loader == "neoforge")
		put("forgelike", loader == "forge" || loader == "neoforge")
	}
	swaps["clientOnly"] = when (loader) {
		"forge" -> "@net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)"
		"neoforge" -> "@net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.CLIENT)"
		else -> "@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)"
	}
	swaps["serverOnly"] = when (loader) {
		"forge" -> "@net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.SERVER)"
		"neoforge" -> "@net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.SERVER)"
		else -> "@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.SERVER)"
	}
}

repositories {
	maven("https://maven.neoforged.net/releases/")
	maven("https://maven.terraformersmc.com/")
	maven("https://maven.nucleoid.xyz/")
	maven("https://maven.parchmentmc.org")
	maven("https://github.com/Progames723/maven/raw/main/maven/")
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraft")
	mappings(loom.layered {
		officialMojangMappings()
		parchment("org.parchmentmc.data:parchment-${minecraft}:${mod.dep("parchment_version")}@zip")
	})
	"dev.progames723.hmmm_natives:hmmm_natives-platform:1.2".let {api(it); include(it)}
	"io.github.classgraph:classgraph:4.8.179".let {api(it); include(it)}
	"com.esotericsoftware.kryo:kryo5:5.6.2".let {implementation(it); includeInternal(it)}
	"org.burningwave:core:12.66.2".let {api(it); include(it)}
	"org.mozilla:rhino-tools:1.8.0".let {implementation(it); includeInternal(it)}
	"org.mozilla:rhino:1.8.0".let {implementation(it); includeInternal(it)}
	if (loader == "fabric") {
		modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_version")}")
		modApi("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric-api_version")}")
	}
	"io.github.llamalad7:mixinextras-$loader:${mod.dep("mixin_extras")}".let {modApi(it); include(it)}
	if (loader == "forge") {
		"forge"("net.minecraftforge:forge:${minecraft}-${mod.dep("forge_version")}")
	}
	if (loader == "neoforge") {
		"neoForge"("net.neoforged:neoforge:${mod.dep("neoforge_version")}")
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/${mod.id}.accesswidener")

	decompilers {
		get("vineflower").apply {
			options.putAll(mapOf(
				"mark-corresponding-synthetics" to "1",
				"keep-literal" to "1",
				"rename-members" to "1"
			))
		}
	}
	if (loader == "forge") {
		forge.convertAccessWideners = true
		forge.mixinConfigs(
			"${mod.id}-common.mixins.json",
			"${mod.id}-forge.mixins.json"
		)
	}
	runs.all {
		this.programArgs.addAll(mutableListOf("--username", "Progames723"))
	}
}

//yes this file doesnt exist so you'll have to create it
//i might make a workflow for this later
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
	localProperties.load(localPropertiesFile.inputStream())
}

publishMods {
	val modrinthToken = localProperties.getProperty("publish.modrinthToken", "")
	val curseforgeToken = localProperties.getProperty("publish.curseforgeToken", "")

	file = project.tasks.remapJar.get().archiveFile

	displayName = "${mod.name} ${loader.replaceFirstChar { it.uppercase() }} ${property("mod.mc_title")}-${mod.version}"
	version = mod.version
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = BETA//TODO change this

	modLoaders.add(loader)

	val targets = property("mod.mc_targets").toString().split(' ')
	if (modrinthToken != "") {
		modrinth {
			projectId = property("publish.modrinth").toString()
			accessToken = modrinthToken
			targets.forEach(minecraftVersions::add)
			if (loader == "fabric") {
				requires("fabric-api")
			}
		}
	}
	if (curseforgeToken != "") {
		curseforge {
			projectId = property("publish.curseforge").toString()
			accessToken = curseforgeToken.toString()
			targets.forEach(minecraftVersions::add)
			if (loader == "fabric") {
				requires("fabric-api")
			}
		}
	}
}

java {
	withSourcesJar()
	val java = if (stonecutter.eval(minecraft, ">=1.20.5")) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
	targetCompatibility = java
	sourceCompatibility = java
}

val shadowBundle: Configuration by configurations.creating {
	isCanBeConsumed = true
	isCanBeResolved = true
	isTransitive = true
}

tasks.shadowJar {
	configurations = listOf(shadowBundle)
	archiveClassifier = "dev-shadow"
}

tasks.remapJar {
	injectAccessWidener = true
	input = tasks.shadowJar.get().archiveFile
	archiveClassifier = null
	if (loader != "fabric") atAccessWideners.add("${mod.id}.accesswidener")
	dependsOn(tasks.shadowJar)
}

tasks.jar {
	archiveClassifier = "dev"
}

val collect = tasks.register<Copy>("collect") {
	group = "versioned"
	description = "Must run through 'chiseledBuild'"
	from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
	into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
	group = "versioned"
	description = "Must run through 'chiseledBuild'"
	from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
	into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
	dependsOn("build")
}

if (stonecutter.current.isActive) {
	rootProject.tasks.register("buildActive") {
		group = "project"
		dependsOn(tasks.build)
	}

	rootProject.tasks.register("runActive") {
		group = "project"
		dependsOn(tasks.named("runClient"))
	}
}

tasks.processResources {
	properties(
		listOf("fabric.mod.json"),
		"id" to mod.id,
		"name" to mod.name,
		"version" to mod.version,
		"minecraft" to mod.prop("mc_dep_fabric"),
		"description" to mod.description,
		"author" to mod.author,
		"license" to mod.license,
		"url" to mod.url,
		"fabric_api" to mod.dep("fabric-api_version")
	)
	properties(
		listOf("META-INF/mods.toml", "pack.mcmeta"),
		"id" to mod.id,
		"name" to mod.name,
		"version" to mod.version,
		"description" to mod.description,
		"author" to mod.author,
		"license" to mod.license,
		"url" to mod.url,
		"minecraft" to mod.prop("mc_dep_forgelike")
	)
	properties(
		listOf("META-INF/neoforge.mods.toml", "pack.mcmeta"),
		"id" to mod.id,
		"name" to mod.name,
		"version" to mod.version,
		"description" to mod.description,
		"author" to mod.author,
		"license" to mod.license,
		"url" to mod.url,
		"minecraft" to mod.prop("mc_dep_forgelike")
	)
}

tasks.build {
	group = "versioned"
	description = "Must run through 'chiseledBuild'"
}

val cleanBuilds = tasks.register("cleanBuilds") {
	group = "build setup"
	for (subproject in rootProject.subprojects) {
		subproject.file("build").deleteRecursively()
	}
	rootProject.file("build").deleteRecursively()
	enabled = false
}