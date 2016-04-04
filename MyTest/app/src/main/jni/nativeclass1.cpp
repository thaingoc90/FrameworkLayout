//
// Created by CPU11303-local on 3/17/2016.
//

#include "nativeclass1.h"


void Java_ngoctdn_vng_mytest_MainActivity_func_1test1(JNIEnv *env, jobject obj) {

    __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Print %d", 2);
    jclass clazz = env->FindClass("ngoctdn/vng/mytest/MainActivity");
    if (clazz) {
        __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Find class");
        jmethodID messageMe = env->GetMethodID(clazz, "funccallback", "(I)V");
        if (messageMe) {
            __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Find method");
            env->CallVoidMethod(obj, messageMe, 2);
        }
    }
}