/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class dev_progames723_hmmm_utils_NativeReflectUtil */

#ifndef _Included_dev_progames723_hmmm_utils_NativeReflectUtil
#define _Included_dev_progames723_hmmm_utils_NativeReflectUtil
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    registerNatives
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_dev_progames723_hmmm_utils_NativeReflectUtil_registerNatives(JNIEnv *, jclass);

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    setFieldValue0
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL setFieldValue0(JNIEnv *, jclass, jstring, jstring, jclass, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    getFieldValue0
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL getFieldValue0(JNIEnv *, jclass, jstring, jstring, jclass, jobject);

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    setAccessible0
 * Signature: (Ljava/lang/reflect/AccessibleObject;Z)V
 */
JNIEXPORT void JNICALL setAccessible0(JNIEnv *, jclass, jobject, jboolean);

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    invokeMethod0
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL invokeMethod0(JNIEnv *, jclass, jstring, jstring, jclass, jobject, jobjectArray);

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    invokeConstructor0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL invokeConstructor0(JNIEnv *, jclass, jstring, jclass, jobjectArray);

#ifdef __cplusplus
}
#endif
#endif
