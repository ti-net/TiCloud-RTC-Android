-keep class io.agora.**{*;}
-keep class com.tinet.ticloudrtc.bean.**{*;}
-keep class com.tinet.ticloudrtc.utils.**{*;}
-keep class com.tinet.ticloudrtc.http.**{*;}
-keep class com.tinet.ticloudrtc.validator.**{*;}
-keep class com.tinet.ticloudrtc.services.**{*;}

-dontwarn com.ft.sdk.**

### ft-sdk 库
-keep class com.ft.sdk.**{*;}

### ft-native 库
-keep class ftnative.*{*;}

### 防止 Action 获取时 action_name 中类名被混淆###
-keepnames class * extends android.view.View
-keepnames class * extends android.view.MenuItem