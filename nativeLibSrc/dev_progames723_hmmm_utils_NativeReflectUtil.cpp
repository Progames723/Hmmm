#include <jni.h>
#include "dev_progames723_hmmm_utils_NativeReflectUtil.h"

extern "C" {
static const JNINativeMethod methods[] = {
    {"setFieldValue0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)V", (void*)&setFieldValue0},
    {"getFieldValue0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;", (void*)&getFieldValue0},
    {"setAccessible0", "(Ljava/lang/reflect/AccessibleObject;Z)V", (void*)&setAccessible0},
    {"invokeMethod0", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", (void*)&invokeMethod0},
    {"invokeConstructor0", "(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;", (void*)&invokeConstructor0}
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT jint JNICALL JNI_OnLoad_libNativeReflectUtil(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT jint JNICALL JNI_OnLoad_NativeReflectUtil(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    registerNatives
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_dev_progames723_hmmm_utils_NativeReflectUtil_registerNatives(JNIEnv *env, jclass cls) {
  env->RegisterNatives(cls, methods, sizeof(methods) / sizeof(methods[0]));
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    setFieldValue0
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL setFieldValue0(JNIEnv *env, jclass cls, jstring fieldName, jstring signature, jclass clazz, jobject objectInstance, jobject value) {
  bool isStatic = objectInstance != NULL;
  const char* cFieldName = env->GetStringUTFChars(fieldName, JNI_FALSE);
  const char* cSignature = env->GetStringUTFChars(signature, JNI_FALSE);
  jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cFieldName, cSignature) : env->GetFieldID(clazz, cFieldName, cSignature);
  delete cFieldName;
  delete cSignature;
  if (objectInstance == NULL) env->SetStaticObjectField(clazz, fieldID, value);
  else env->SetObjectField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    getFieldValue0
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL getFieldValue0(JNIEnv *env, jclass cls, jstring fieldName, jstring signature, jclass clazz, jobject objectInstance) {
  bool isStatic = objectInstance != NULL;
  const char* cFieldName = env->GetStringUTFChars(fieldName, JNI_FALSE);
  const char* cSignature = env->GetStringUTFChars(signature, JNI_FALSE);
  jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cFieldName, cSignature) : env->GetFieldID(clazz, cFieldName, cSignature);
  delete cFieldName;
  delete cSignature;
  if (objectInstance == NULL) return env->GetStaticObjectField(clazz, fieldID);
  return env->GetObjectField(objectInstance, fieldID);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    setAccessible0
 * Signature: (Ljava/lang/reflect/AccessibleObject;Z)V
 */
JNIEXPORT void JNICALL setAccessible0(JNIEnv *env, jclass cls, jobject o, jboolean boolean) {
  jclass fieldClass = env->GetObjectClass(o);
  jfieldID fieldID = env->GetFieldID(fieldClass, "override", "Z");
  env->SetBooleanField(o, fieldID, boolean);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    invokeMethod0
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL invokeMethod0(JNIEnv *env, jclass cls, jstring methodName, jstring signature, jclass clazz, jobject objectInstance, jobjectArray objArr) {
  bool isStatic = objectInstance != NULL;
  const char* cMethodName = env->GetStringUTFChars(methodName, JNI_FALSE);
  const char* cSignature = env->GetStringUTFChars(signature, JNI_FALSE);
  jmethodID methodID = isStatic ? env->GetStaticMethodID(clazz, cMethodName, cSignature) : env->GetMethodID(clazz, cMethodName, cSignature);
  delete cMethodName;
  delete cSignature;
  jobject ret;
  if (isStatic) ret = env->CallStaticObjectMethod(clazz, methodID, objArr);
  else ret = env->CallObjectMethod(objectInstance, methodID, objArr);
  if (env->ExceptionCheck() == JNI_TRUE) {
    jthrowable exception = env->ExceptionOccurred();
    env->ExceptionClear();
    jclass exceptionClass = env->FindClass("dev/progames723/hmmm/HmmmException");
    jmethodID methodID = env->GetStaticMethodID(exceptionClass, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Throwable;)V");
    jthrowable o = (jthrowable) env->NewObject(exceptionClass, methodID, NULL, env->NewStringUTF("Exception when executing method!"), exception);
    env->Throw(o);
    return NULL;
  }
  return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil
 * Method:    invokeConstructor0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL invokeConstructor0(JNIEnv *env, jclass cls, jstring signature, jclass clazz, jobjectArray objArr) {
  const char* cSignature = env->GetStringUTFChars(signature, JNI_FALSE);
  jmethodID methodID = env->GetStaticMethodID(clazz, "<init>", cSignature);
  delete cSignature;
  jobject ret = env->CallStaticObjectMethod(clazz, methodID, objArr);
  if (env->ExceptionCheck() == JNI_TRUE) {
    jthrowable exception = env->ExceptionOccurred();
    env->ExceptionClear();
    jclass exceptionClass = env->FindClass("dev/progames723/hmmm/HmmmException");
    jmethodID methodID = env->GetStaticMethodID(exceptionClass, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Throwable;)V");
    jthrowable o = (jthrowable) env->NewObject(exceptionClass, methodID, NULL, env->NewStringUTF("Exception when executing method!"), exception);
    env->Throw(o);
    return NULL;
  }
  return ret;
}

}