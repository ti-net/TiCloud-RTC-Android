import Config.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk=ProjectConfig.minSdk
        targetSdk=ProjectConfig.targetSdk

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
}



afterEvaluate {
    publishing{
        publications.create<MavenPublication>("releaseVersion"){
            groupId = "com.github.ti-net"
            artifactId = "TiCloud-RTC-Android"
            version = PublicSdkConfig.versionName
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // kotlin 反射
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    // retrofit2 gson 转换库
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // json 解析库
    api("com.google.code.gson:gson:2.9.0")

    // 网络请求库
    api("com.squareup.okhttp3:okhttp:3.14.9")
    api("com.squareup.retrofit2:retrofit:2.9.0")

    // 观测云
    implementation("com.cloudcare.ft.mobile.sdk.tracker.agent:ft-sdk:1.5.0")
    implementation("com.cloudcare.ft.mobile.sdk.tracker.agent:ft-native:1.1.0")
}

val version = PublicSdkConfig.versionName
val sdkFile = "TiCloudRTC_Android_SDK_${PublicSdkConfig.versionName}_release.zip"
val downloadedSdkFile = File("$buildDir/downloaded_sdk/$sdkFile")

task<Delete>("deleteExpiredFiles"){
    delete = setOf("${projectDir.path}/libs","${projectDir.path}/src/main/jinLibs")
}

task<de.undercouch.gradle.tasks.download.Download>("downloadSdk") {
    dependsOn("deleteExpiredFiles")
    group = "custom"
    src("https://tinet-sdk-release.s3.cn-north-1.amazonaws.com.cn/TiCloudRTC/sdk/v$version/TiCloudRTC_Android_SDK_${version}_release.zip")
    dest(downloadedSdkFile)
    overwrite(true)
    onlyIfModified(true)
    doLast {
        println("start to copy libs")

        copy {
            from(zipTree(downloadedSdkFile))
            include("arm*/**","x86*/**")
            into("${project.projectDir}/src/main/jniLibs")
        }

        copy{
            from(zipTree(downloadedSdkFile))
            include("*.jar")
            into("${project.projectDir}/libs")
        }

        copy{
            from(zipTree(downloadedSdkFile))
            include("include/**")
            into("${project.projectDir}/src/main/jniLibs")
        }
    }

}

tasks.named("preBuild"){
    dependsOn("downloadSdk")
}