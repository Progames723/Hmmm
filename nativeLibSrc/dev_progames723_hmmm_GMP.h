/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class dev_progames723_hmmm_GMP */

#ifndef _Included_dev_progames723_hmmm_GMP
#define _Included_dev_progames723_hmmm_GMP
#ifdef __cplusplus
extern "C" {
#endif
#undef dev_progames723_hmmm_GMP_serialVersionUID
#define dev_progames723_hmmm_GMP_serialVersionUID -8742448824652078965i64
/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    registerNatives
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_dev_progames723_hmmm_GMP_registerNatives(JNIEnv *, jclass);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    intValue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL intValue(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    longValue
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL longValue(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    floatValue
 * Signature: ()F
 */
JNIEXPORT jfloat JNICALL floatValue(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    doubleValue
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL doubleValue(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    getAsString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL getAsString(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    create
 * Signature: (D)V
 */
JNIEXPORT void JNICALL createD(JNIEnv *, jobject, jdouble);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    create
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL createString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    multiply
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL multiplyGMP(JNIEnv *, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    multiply
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL multiplyString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    divide
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL divideGMP(JNIEnv *, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    divide
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL divideString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    pow
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL powGMP(JNIEnv *, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    pow
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL powString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    sqrt
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL GMPsqrt(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    add
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL addGMP(JNIEnv *, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    add
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL addString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    subtract
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL subtractGMP(JNIEnv *, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    subtract
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL subtractString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    ceil
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL GMPceil(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    floor
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL GMPfloor(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    abs
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL GMPabs(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    truncate
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL GMPtruncate(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL clear(JNIEnv *, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    set
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL setGMP(JNIEnv *, jobject, jobject);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    set
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL setString(JNIEnv *, jobject, jstring);

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    equals
 * Signature: (Ljava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL equals(JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
