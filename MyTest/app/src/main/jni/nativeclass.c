//#include <jni.h>
//#include <android/log.h>
//
//JNIEXPORT void JNICALL
//Java_ngoctdn_vng_object_NativeClass_func_1test1(JNIEnv *env, jobject instance) {
//    __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Print %d", 1);
//}
//
//JNIEXPORT void JNICALL
//Java_ngoctdn_vng_mytest_MainActivity_func_1test1(JNIEnv *env, jobject obj) {
//
//    __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Print %d", 2);
//    jclass clazz = (*env)->FindClass(env, "ngoctdn/vng/mytest/MainActivity");
//    if (clazz) {
//        __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Find class");
//        jmethodID messageMe = (*env)->GetMethodID(env, clazz, "funccallback", "(I)V");
//        if (messageMe) {
//            __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Find method");
//            (*env)->CallVoidMethod(env, obj, messageMe, 1);
//        }
//    }
//}