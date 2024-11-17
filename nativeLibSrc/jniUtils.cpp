#include "jniUtils.h"

extern "C" {
    static inline void throwJavaException(JNIEnv *env) {
        jthrowable exception = env->ExceptionOccurred();
        env->ExceptionClear();
        jclass exceptionClass = env->FindClass("dev/progames723/hmmm/HmmmException");
        jmethodID methodID = env->GetStaticMethodID(exceptionClass, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Throwable;)V");
        jthrowable o = (jthrowable) env->NewObject(exceptionClass, methodID, NULL, env->NewStringUTF("Exception when executing method!"), exception);
        env->Throw(o);
    }
    static inline void throwJavaError(JNIEnv *env) {
        jthrowable exception = env->ExceptionOccurred();
        env->ExceptionClear();
        jclass exceptionClass = env->FindClass("dev/progames723/hmmm/HmmmError");
        jmethodID methodID = env->GetStaticMethodID(exceptionClass, "<init>", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Throwable;)V");
        jthrowable o = (jthrowable) env->NewObject(exceptionClass, methodID, NULL, env->NewStringUTF("Error when executing method!"), exception);
        env->Throw(o);
    }

    static inline bool isStatic(jobject objectInstance) {
        return objectInstance != NULL;
    }
}