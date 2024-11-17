#include <jni.h>
#include "dev_progames723_hmmm_utils_NativeReflectUtil_Primitives.h"

extern "C" {

const static JNINativeMethod methods[] = {
    {"getIntField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)I", (void*)&getIntField0},
    {"getLongField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)J", (void*)&getLongField0},
    {"getFloatField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)F", (void*)&getFloatField0},
    {"getDoubleField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)D", (void*)&getDoubleField0},
    {"getShortField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)S", (void*)&getShortField0},
    {"getByteField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)B", (void*)&getByteField0},
    {"getCharField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)C", (void*)&getCharField0},
    {"getBooleanField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)Z", (void*)&getBooleanField0},
    {"setIntField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;I)V", (void*)&setIntField0},
    {"setLongField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;J)V", (void*)&setLongField0},
    {"setFloatField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;F)V", (void*)&setFloatField0},
    {"setDoubleField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;D)V", (void*)&setDoubleField0},
    {"setShortField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;S)V", (void*)&setShortField0},
    {"setByteField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;B)V", (void*)&setByteField0},
    {"setCharField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;C)V", (void*)&setCharField0},
    {"setBooleanField0", "(Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;Z)V", (void*)&setBooleanField0},
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT jint JNICALL JNI_OnLoad_libNativeReflectUtil_Primitives(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT jint JNICALL JNI_OnLoad_NativeReflectUtil_Primitives(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    registerNatives
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_dev_progames723_hmmm_utils_NativeReflectUtil_00024Primitives_registerNatives(JNIEnv *env, jclass cls) {
    env->RegisterNatives(cls, methods, sizeof(methods) / sizeof(methods[0]));
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getIntField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL getIntField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jint ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "I") : env->GetFieldID(clazz, cName, "I");
    ret = isStatic ? env->GetStaticIntField(clazz, fieldID) : env->GetIntField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setIntField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;I)V
 */
JNIEXPORT void JNICALL setIntField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jint value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "I") : env->GetFieldID(clazz, cName, "I");
    if (isStatic) env->SetStaticIntField(clazz, fieldID, value);
    else env->SetIntField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getLongField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)J
 */
JNIEXPORT jlong JNICALL getLongField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jlong ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "I") : env->GetFieldID(clazz, cName, "I");
    ret = isStatic ? env->GetStaticLongField(clazz, fieldID) : env->GetLongField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setLongField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;J)V
 */
JNIEXPORT void JNICALL setLongField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jlong value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "J") : env->GetFieldID(clazz, cName, "J");
    if (isStatic) env->SetStaticLongField(clazz, fieldID, value);
    else env->SetLongField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getFloatField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)F
 */
JNIEXPORT jfloat JNICALL getFloatField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jfloat ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "F") : env->GetFieldID(clazz, cName, "F");
    ret = isStatic ? env->GetStaticFloatField(clazz, fieldID) : env->GetFloatField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setFloatField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;F)V
 */
JNIEXPORT void JNICALL setFloatField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jfloat value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "F") : env->GetFieldID(clazz, cName, "F");
    if (isStatic) env->SetStaticFloatField(clazz, fieldID, value);
    else env->SetFloatField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getDoubleField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)D
 */
JNIEXPORT jdouble JNICALL getDoubleField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jdouble ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "D") : env->GetFieldID(clazz, cName, "D");
    ret = isStatic ? env->GetStaticDoubleField(clazz, fieldID) : env->GetDoubleField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setDoubleField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;D)V
 */
JNIEXPORT void JNICALL setDoubleField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jdouble value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "D") : env->GetFieldID(clazz, cName, "D");
    if (isStatic) env->SetStaticDoubleField(clazz, fieldID, value);
    else env->SetDoubleField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getShortField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)S
 */
JNIEXPORT jshort JNICALL getShortField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jshort ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "S") : env->GetFieldID(clazz, cName, "S");
    ret = isStatic ? env->GetStaticShortField(clazz, fieldID) : env->GetShortField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setShortField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;S)V
 */
JNIEXPORT void JNICALL setShortField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jshort value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "S") : env->GetFieldID(clazz, cName, "S");
    if (isStatic) env->SetStaticShortField(clazz, fieldID, value);
    else env->SetShortField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getByteField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)B
 */
JNIEXPORT jbyte JNICALL getByteField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jlong ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "B") : env->GetFieldID(clazz, cName, "B");
    ret = isStatic ? env->GetStaticByteField(clazz, fieldID) : env->GetByteField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setByteField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;B)V
 */
JNIEXPORT void JNICALL setByteField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jbyte value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "B") : env->GetFieldID(clazz, cName, "B");
    if (isStatic) env->SetStaticByteField(clazz, fieldID, value);
    else env->SetByteField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getCharField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)C
 */
JNIEXPORT jchar JNICALL getCharField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jlong ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "C") : env->GetFieldID(clazz, cName, "C");
    ret = isStatic ? env->GetStaticCharField(clazz, fieldID) : env->GetCharField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setCharField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;C)V
 */
JNIEXPORT void JNICALL setCharField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jchar value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "C") : env->GetFieldID(clazz, cName, "C");
    if (isStatic) env->SetStaticCharField(clazz, fieldID, value);
    else env->SetCharField(objectInstance, fieldID, value);
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    getBooleanField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL getBooleanField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance) {
    bool isStatic = objectInstance != NULL;
    jlong ret;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "Z") : env->GetFieldID(clazz, cName, "Z");
    ret = isStatic ? env->GetStaticBooleanField(clazz, fieldID) : env->GetBooleanField(objectInstance, fieldID);
    return ret;
}

/*
 * Class:     dev_progames723_hmmm_utils_NativeReflectUtil_Primitives
 * Method:    setBooleanField0
 * Signature: (Ljava/lang/String;Ljava/lang/Class;Ljava/lang/Object;Z)V
 */
JNIEXPORT void JNICALL setBooleanField0(JNIEnv *env, jclass cls, jstring fieldName, jclass clazz, jobject objectInstance, jboolean value) {
    bool isStatic = objectInstance != NULL;
    const char* cName = env->GetStringUTFChars(fieldName, JNI_FALSE);
    jfieldID fieldID = isStatic ? env->GetStaticFieldID(clazz, cName, "Z") : env->GetFieldID(clazz, cName, "Z");
    if (isStatic) env->SetStaticBooleanField(clazz, fieldID, value);
    else env->SetBooleanField(objectInstance, fieldID, value);
}

}