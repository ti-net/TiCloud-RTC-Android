
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

object PublicSdkConfig{
    const val versionName = "4.3.2"
}

android {

    namespace = "com.tinet.ticloudrtc"

    compileSdk = 34

    defaultConfig {
        minSdk = 21
        lint.targetSdk = 34
        testOptions.targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled=false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release")
    }
}


publishing{
    publications.create<MavenPublication>("releaseVersion"){
        groupId = "com.github.ti-net"
        artifactId = "TiCloud-RTC-Android"
        version = PublicSdkConfig.versionName

        afterEvaluate {
            from(components["release"])
        }
    }
}

dependencies {
    api(
        fileTree(
            mapOf(
                "include" to "*.jar",
                "dir" to "libs"
            )
        )
    )

    // kotlin 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // kotlin 反射
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")

    // retrofit2 gson 转换库
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // json 解析库
    api("com.google.code.gson:gson:2.11.0")

    // 网络请求库
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // 观测云
    compileOnly("com.cloudcare.ft.mobile.sdk.tracker.agent:ft-sdk:1.5.0")
    compileOnly("com.cloudcare.ft.mobile.sdk.tracker.agent:ft-native:1.1.0")
}

val version = PublicSdkConfig.versionName
val sdkFile = "TiCloudRTC_Android_SDK_${PublicSdkConfig.versionName}_release.zip"
val zipPackagesPath = File("${rootDir.absolutePath}/zip_packages/$sdkFile")

task<Delete>("deleteExpiredFiles"){
    dependsOn("clean")

    delete = setOf(
        "${projectDir.path}/libs",
        "${projectDir.path}/src/main/jinLibs",
        "${projectDir.path}/src/main/res",
        "${projectDir.path}/src/main/AndroidManifest.xml",
        "${projectDir.path}/proguard-rules.pro",
        "${projectDir.path}/consumer-rules.pro"
    )

    doLast {
        println("start to copy libs")

        copy {
            from(zipTree(zipPackagesPath))
            include("arm*/**","x86*/**")
            into("${project.projectDir}/src/main/jniLibs")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("*.jar")
            into("${project.projectDir}/libs")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("include/**")
            into("${project.projectDir}/src/main/jniLibs")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("raw/**")
            into("${project.projectDir}/src/main/res")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("AndroidManifest.xml")
            into("${project.projectDir}/src/main")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("proguard-rules.pro")
            into("${project.projectDir}")
        }

        copy{
            from(zipTree(zipPackagesPath))
            include("consumer-rules.pro")
            into("${project.projectDir}")
        }
    }
}

tasks.named("preBuild"){
    dependsOn("deleteExpiredFiles")
}