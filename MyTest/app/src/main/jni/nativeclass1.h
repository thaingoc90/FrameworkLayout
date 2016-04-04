//
// Created by CPU11303-local on 3/17/2016.
//

#ifndef MYTEST_NATIVECLASS1_H
#define MYTEST_NATIVECLASS1_H

#include <jni.h>
#include <android/log.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL
Java_ngoctdn_vng_object_NativeClass_func_1test1(JNIEnv *env, jobject instance) {
    __android_log_print(ANDROID_LOG_ERROR, "Ngoctdn", "Print %d", 1);
}

JNIEXPORT void JNICALL
Java_ngoctdn_vng_mytest_MainActivity_func_1test1(JNIEnv *env, jobject obj);

#ifdef __cplusplus
}
#endif

#endif //MYTEST_NATIVECLASS1_H
