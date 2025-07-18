plugins {
    id("dev.kikugie.stonecutter") version "0.7-alpha.14"
    id("dev.architectury.loom") version "1.10-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("me.modmuss50.mod-publish-plugin") version "0.8.4" apply false
    id("dev.kikugie.j52j") version "2.0"
}
stonecutter active "1.20.1-fabric" /* [SC] DO NOT EDIT */

//builds every version into `build/libs/{mod.version}/{loader}`
//stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
//    group = "project"
//    ofTask("build")
//}

tasks.register("chiseledBuild") {
    group = "project"
    for (subproject in rootProject.subprojects) {
        dependsOn(stonecutter.tasks.switchTaskName(subproject.name))
        dependsOn(subproject.tasks.findByName("build"))
    }
    dependsOn("collect")
}

//stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
//    group = "project"
//    ofTask("publishMods")
//}
//stonecutter registerChiseled tasks.register("chiseledRunAllClients", stonecutter.chiseled) {
//    group = "project"
//    ofTask("runClient")
//}

//builds loader-specific versions into `build/libs/{mod.version}/{loader}`
//for (it in stonecutter.tree.branches) {
//    if (it.id.isEmpty()) continue
//    val loader = it.id.upperCaseFirst()
//    stonecutter registerChiseled tasks.register("chiseledBuild$loader", stonecutter.chiseled) {
//        group = "project"
//        versions { branch, _ -> branch == it.id }
//        ofTask("build")
//    }
//}

//runs active versions for each loader
for (it in stonecutter.tree.nodes) {
    if (it.metadata != stonecutter.current || it.branch.id.isEmpty()) continue
    val types = listOf("Client", "Server")
    val loader = it.branch.id.upperCaseFirst()
    for (type in types) it.project.tasks.register("runActive$type$loader") {
        group = "project"
        dependsOn("run$type")
    }
}

repositories {
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.nucleoid.xyz/")
    maven("https://maven.parchmentmc.org")
    maven("https://github.com/Progames723/maven/raw/main/maven/")
}