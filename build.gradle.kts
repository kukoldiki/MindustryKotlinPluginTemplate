plugins {
    java
    kotlin("jvm") version "2.4.10"
}

sourceSets.main {
    java.setSrcDirs(listOf("src"))
}

repositories {
    mavenCentral()

    //Downloads the dependencies JAR file from Mindustry releases; does not use any real repository. Surprisingly, this is the most reliable option.
    ivy {
        url = uri("https://github.com/")
        patternLayout { artifact("/[organisation]/[module]/releases/download/[revision]/dependencies.jar") }
        metadataSources { artifact() }
    }

    //If the version is set to 'latest', downloads the latest Mindustry *release* as a dependency
    ivy {
        url = uri("https://github.com/")
        patternLayout { artifact("/[organisation]/[module]/releases/[revision]/download/dependencies.jar") }
        metadataSources { artifact() }
    }

    //For depending on the absolute newest commit for Mindustry
    ivy {
        url = uri("https://github.com/")
        patternLayout { artifact("/[organisation]/[module]/releases/download/master/[revision].jar") }
        metadataSources { artifact() }
    }
}

// Mindustry version to depend on.
// Valid values:
// - latest: depend on the latest release of mindustry
// - be: depend on the very latest commit of mindustry
// - v<number>: depend on a specific commit (e.g. v158.1)
val mindustryVersion = "latest"

dependencies {
    compileOnly(if (mindustryVersion == "be") "Anuken:MindustryBuilds:latest" else "Anuken:Mindustry:$mindustryVersion")
    testImplementation(kotlin("test"))
}

tasks.jar {
    archiveFileName.set("${project.name}.jar")

    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}

kotlin {
    jvmToolchain(17)
}
