#include <jni.h>

#ifndef JNI_UTILS_H
#define JNI_UTILS_H

#ifdef __cplusplus
extern "C" {
#endif

static inline void throwJavaException(JNIEnv *env);

static inline void throwJavaError(JNIEnv *env);

static inline bool isStatic(jobject objectInstance);

#ifdef __cplusplus
}
#endif
#endif