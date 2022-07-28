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

val version = PublicSdkConfig.versionName
val sdkFile = "TiCloudRTC_Android_SDK_${PublicSdkConfig.versionName}_release.zip"
val downloadedSdkFile = File("$buildDir/downloaded_sdk/$sdkFile")

task<de.undercouch.gradle.tasks.download.Download>("downloadSdk") {
    group = "custom"
    src("https://tinet-sdk-release.s3.cn-north-1.amazonaws.com.cn/TiCloudRTC/sdk/v${PublicSdkConfig.versionName}/TiCloudRTC_Android_SDK_${PublicSdkConfig.versionName}_release.zip")
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
            include("include")
            into("${project.projectDir}/src/main/jniLibs")
        }
    }

}

tasks.named("preBuild"){
    dependsOn("downloadSdk")
}

afterEvaluate {
    publishing{
        publications.create<MavenPublication>("releaseVersion"){
            groupId = "com.tinet.ticloudrtc"
            artifactId = "TiCloud-RTC-Android_test"
            version = PublicSdkConfig.versionName
            from(components["release"])
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    println("public ------> find and setting libs")
    File("${projectDir.path}/libs").list()?.forEach {
        println("public ------> use libs '$it'")
        implementation(files("libs/$it"))
    }
}