# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class io.agora.**{*;}
-keep class com.tinet.ticloudrtc.bean.**{*;}
-keep class com.tinet.ticloudrtc.utils.**{*;}
-keep class com.tinet.ticloudrtc.http.**{*;}
-keep class com.tinet.ticloudrtc.validator.**{*;}

-dontwarn com.ft.sdk.**

### ft-sdk 库
-keep class com.ft.sdk.**{*;}

### ft-native 库
-keep class ftnative.*{*;}

### 防止 Action 获取时 action_name 中类名被混淆###
-keepnames class * extends android.view.View
-keepnames class * extends android.view.MenuItem