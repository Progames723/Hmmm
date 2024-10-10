#include <jni.h>

#include "../headers/dev_progames723_hmmm_utils_MathUtil.h"

extern "C" {
static JNINativeMethod methods[] = {
    {"fastSqrt", "(D)D", (void*)&Java_dev_progames723_hmmm_utils_MathUtil_fastSqrt},
    {"fastInvSqrt", "(D)D", (void*)&Java_dev_progames723_hmmm_utils_MathUtil_fastInvSqrt__D},
    {"fastInvSqrt", "(F)F", (void*)&Java_dev_progames723_hmmm_utils_MathUtil_fastInvSqrt__F},
    {"fastPow", "(DD)D", (void*)&Java_dev_progames723_hmmm_utils_MathUtil_fastPow},
    {"nthRoot", "(DD)D", (void*)&Java_dev_progames723_hmmm_utils_MathUtil_nthRoot}
};

static double fast_pow(double x, double y)
{
  union {
    double d;
    int x[2];
  } u = { x };
  u.x[1] = (int)(y * (u.x[1] - 1072632447) + 1072632447);
  u.x[0] = 0;
  return u.d;
}

static double fast_sqrt(double x) {
  if (x > 0 && x < 1) return 1 / fast_sqrt(1 / x);

  double l = 0, r = x;

  // precision
  double epsilon = 1e-15;

  while (l <= r) {
    double mid = (l + r) / 2;
    if (mid * mid > x)
      r = mid;
    else {
      if (x - mid * mid < epsilon) return mid;
      l = mid;
    }
  }
  return -1;
}

/*
 * Class:     dev.progames723.hmmm.utils.MathUtil
 * Method:    registerNatives
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_dev_progames723_hmmm_utils_MathUtil_registerNatives(JNIEnv *env, jclass cls) {
    env->RegisterNatives(cls, methods, sizeof(methods)/sizeof(methods[0]));
  }

/*
 * Class:     dev.progames723.hmmm.utils.MathUtil
 * Method:    fastSqrt
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_dev_progames723_hmmm_utils_MathUtil_fastSqrt(JNIEnv *env, jclass cls, jdouble j_double) {
    return fast_sqrt(j_double);
  }

/*
 * Class:     dev.progames723.hmmm.utils.MathUtil
 * Method:    fastInvSqrt
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_dev_progames723_hmmm_utils_MathUtil_fastInvSqrt__D(JNIEnv *env, jclass cls, jdouble j_double) {
    double y = j_double;
    double x2 = y * 0.5;

    long long i = *(long long *) &y;

    // value is pre-assumed
    i = 0x5fe6eb50c7b537a9 - (i >> 1);
    y = *(double *) &i;

    y = y * (1.5 - (x2 * y * y));   // 1st iteration
    //y = y * (1.5 - (x2 * y * y));   // 2nd iteration
    return y;
  }

/*
 * Class:     dev.progames723.hmmm.utils.MathUtil
 * Method:    fastInvSqrt
 * Signature: (F)F
 */
JNIEXPORT jfloat JNICALL Java_dev_progames723_hmmm_utils_MathUtil_fastInvSqrt__F(JNIEnv *env, jclass cls, jfloat j_float) {
    float x2 = j_float * 0.5F;
    float y = j_float;

    long i = *(long *) &y;

    // value is pre-assumed
    i = 0x5f3759df - (i >> 1);
    y = *(float *) &i;

    y = y * (1.5F - (x2 * y * y)); // 1st iteration
    //y = y * (1.5F - (x2 * y * y)); // 2nd iteration
    return y;
  }

  /*
 * Class:     dev.progames723.hmmm.utils.MathUtil
 * Method:    fastPow
 * Signature: (DD)D
 */
JNIEXPORT jdouble JNICALL Java_dev_progames723_hmmm_utils_MathUtil_fastPow(JNIEnv *env, jclass cls, jdouble x, jdouble y) {
    return fast_pow(x, y);
  }

/*
 * Class:     dev.progames723.hmmm.utils.MathUtil
 * Method:    nthRoot
 * Signature: (DD)D
 */
JNIEXPORT jdouble JNICALL Java_dev_progames723_hmmm_utils_MathUtil_nthRoot(JNIEnv *env, jclass cls, jdouble x, jdouble y) {
    return fast_pow(x, 1 / y);
  }
}