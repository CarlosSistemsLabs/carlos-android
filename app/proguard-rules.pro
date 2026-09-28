# Carlos ERP Android — R8/ProGuard rules (task 49.1).

# --- Kotlin metadata ---
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions

# --- kotlinx.serialization ---
# Keep @Serializable classes and their generated serializers.
-keepattributes RuntimeVisibleAnnotations, AnnotationDefault
-keep,includedescriptorclasses class com.carloserp.android.**$$serializer { *; }
-keepclassmembers class com.carloserp.android.** {
    *** Companion;
}
-keepclasseswithmembers class com.carloserp.android.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# --- Retrofit / OkHttp ---
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keepattributes Signature, Exceptions
# Retrofit does reflection on generic parameters and annotations.
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# --- Hilt / Dagger generate their own keep rules; nothing extra required. ---
