#include <jni.h>
#include <gmpxx.h>
#include <mpfr.h>
#include <regex>
#include <string>
#include <../headers/dev_progames723_hmmm_GMP.h>

extern "C" {
#undef dev_progames723_hmmm_GMP_serialVersionUID
#define dev_progames723_hmmm_GMP_serialVersionUID -8742448824652078965i64

static JNINativeMethod methods[] = {
    {"intValue", "()I", (void*)&intValue},
    {"longValue", "()J", (void*)&longValue},
    {"floatValue", "()F", (void*)&floatValue},
    {"doubleValue", "()D", (void*)&doubleValue},
    {"getAsString", "()Ljava/lang/String;", (void*)&getAsString},
    {"create", "(D)V", (void*)&create},
    {"create", "(Ljava/lang/String;)V", (void*)&createString},
    {"multiply", "(Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;", (void*)&multiply},
    {"multiply", "(Ljava/lang/String;)Ldev/progames723/hmmm/GMP;", (void*)&multiplyStr},
    {"divide", "(Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;", (void*)&divide},
    {"divide", "(Ljava/lang/String;)Ldev/progames723/hmmm/GMP;", (void*)&divideStr},
    {"pow", "(Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;", (void*)&pow},
    {"pow", "(Ljava/lang/String;)Ldev/progames723/hmmm/GMP;", (void*)&powStr},
    {"sqrt", "()Ldev/progames723/hmmm/GMP;", (void*)&sqrtGMP},
    {"add", "(Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;", (void*)&add},
    {"add", "(Ljava/lang/String;)Ldev/progames723/hmmm/GMP;", (void*)&addStr},
    {"subtract", "(Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;", (void*)&subtract},
    {"subtract", "(Ljava/lang/String;)Ldev/progames723/hmmm/GMP;", (void*)&subtractStr},
    {"ceil", "()Ldev/progames723/hmmm/GMP;", (void*)&ceilGMP},
    {"floor", "()Ldev/progames723/hmmm/GMP;", (void*)&floorGMP},
    {"abs", "()Ldev/progames723/hmmm/GMP;", (void*)&absGMP},
    {"truncate", "()Ldev/progames723/hmmm/GMP;", (void*)&truncate},
    {"clear", "()V", (void*)&clear},
    {"set", "(Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;", (void*)&set},
    {"set", "(Ljava/lang/String;)Ldev/progames723/hmmm/GMP;", (void*)&setStr},
    {"equals", "(Ljava/lang/Object;)Z", (void*)&equals}
};

static inline mpf_class getGMP(const char* str) {
  mpf_class temp; temp = str;
  return temp;
}

static inline void setGMPValue(mpf_class* cls, mpf_ptr ptr) {
  char* str;
  mpf_get_str(str, &ptr->_mp_exp, 10, mpf_size(ptr), ptr);
  cls->set_str(str, 10);
  mpf_clear(ptr);
  free(str);
}

/*
 * multiprecision float class
 * should be initialized most of the time
 * if not the sheer amount if statements will save me
*/
mpf_class var;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT jint JNICALL JNI_OnLoad_libHmmm(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT jint JNICALL JNI_OnLoad_hmmm(JavaVM *vm, void *reserved) {
  return JNI_VERSION_10;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
  var.~__gmp_expr();
}

JNIEXPORT void JNICALL JNI_OnUnload_libHmmm(JavaVM *vm, void *reserved) {
  var.~__gmp_expr();
}

JNIEXPORT void JNICALL JNI_OnUnload_hmmm(JavaVM *vm, void *reserved) {
  var.~__gmp_expr();
}

/*
 * Class:     dev.progames723.hmmm.GMP
 * Method:    registerNatives
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_dev_progames723_hmmm_GMP_registerNatives(JNIEnv *env, jclass cls) {
  env->RegisterNatives(cls, methods, sizeof(methods)/sizeof(methods[0]));
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    intValue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL intValue(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  return static_cast<int>(var.get_si());
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    longValue
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL longValue(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  return var.get_si();
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    floatValue
 * Signature: ()F
 */
JNIEXPORT jfloat JNICALL floatValue(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  return static_cast<float>(var.get_d());
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    doubleValue
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL doubleValue(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  return var.get_d();
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    getAsString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL getAsString(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  return env->NewStringUTF(var.get_str(var.get_mpf_t()->_mp_exp).c_str());
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    create
 * Signature: (D)V
 */
JNIEXPORT void JNICALL create(JNIEnv *env, jobject o, jdouble d) {
  var = static_cast<double>(d);
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    create
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL createString(JNIEnv *env, jobject o, jstring str) {
  std::string s = env->GetStringUTFChars(str, JNI_FALSE);
  if (std::regex_match(s, std::regex("[^\\-0-9.]"))) var = 0;
  else var = s;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    multiply
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL multiply(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "getAsString", "()Ljava/lang/String;")), JNI_FALSE));
  var *= temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    multiply
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL multiplyStr(JNIEnv *env, jobject o, jstring str) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  const char* s = env->GetStringUTFChars(str, JNI_FALSE);
  if (std::regex_match(s, std::regex("[^\\-0-9.]"))) return o;
  mpf_class temp = getGMP(s);
  var *= temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    divide
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL divide(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "getAsString", "()Ljava/lang/String;")), JNI_FALSE));
  var /= temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    divide
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL divideStr(JNIEnv *env, jobject o, jstring str) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  const char* s = env->GetStringUTFChars(str, JNI_FALSE);
  if (std::regex_match(s, std::regex("[^\\-0-9.]"))) return o;
  mpf_class temp = getGMP(s);
  var /= temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    pow
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL pow(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "getAsString", "()Ljava/lang/String;")), JNI_FALSE));
  mpfr_t var_1, temp_1;
  mpfr_init_set_str(var_1, var.get_str(var.get_mpf_t()->_mp_exp).c_str(), 10, MPFR_RNDN);
  mpfr_init_set_str(temp_1, temp.get_str(temp.get_mpf_t()->_mp_exp).c_str(), 10, MPFR_RNDN);
  mpfr_pow(var_1, var_1, temp_1, GMP_RNDN);
  char* result;
  mpfr_get_str(result, &var_1->_mpfr_exp, 10, mpfr_custom_get_size(var_1->_mpfr_prec), var_1, MPFR_RNDN);
  var = result;
  free(result);
  mpfr_clears(var_1, temp_1);
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    pow
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL powStr(JNIEnv *env, jobject o, jstring str) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  const char* s = env->GetStringUTFChars(str, JNI_FALSE);
  if (std::regex_match(s, std::regex("[^\\-0-9.]"))) return o;
  mpf_class temp = getGMP(s);
  mpfr_t var_1, temp_1;
  mpfr_init_set_str(var_1, var.get_str(var.get_mpf_t()->_mp_exp).c_str(), 10, MPFR_RNDN);
  mpfr_init_set_str(temp_1, temp.get_str(temp.get_mpf_t()->_mp_exp).c_str(), 10, MPFR_RNDN);
  mpfr_pow(var_1, var_1, temp_1, GMP_RNDN);
  char* result;
  mpfr_get_str(result, &var_1->_mpfr_exp, 10, mpfr_custom_get_size(var_1->_mpfr_prec), var_1, MPFR_RNDN);
  var = result;
  free(result);
  mpfr_clears(var_1, temp_1);
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    sqrt
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL sqrtGMP(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_t temp;
  mpf_init_set_str(temp, var.get_str(var.get_mpf_t()->_mp_exp).c_str(), 10);
  mpf_sqrt(temp, var.get_mpf_t());
  setGMPValue(&var, temp);
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    add
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL add(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "getAsString", "()Ljava/lang/String;")), JNI_FALSE));
  var += temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    add
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL addStr(JNIEnv *env, jobject o, jstring str) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  const char* s = env->GetStringUTFChars(str, JNI_FALSE);
  if (std::regex_match(s, std::regex("[^\\-0-9.]"))) return o;
  mpf_class temp = getGMP(s);
  var += temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    subtract
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL subtract(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "getAsString", "()Ljava/lang/String;")), JNI_FALSE));
  var -= temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    subtract
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL subtractStr(JNIEnv *env, jobject o, jstring str) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  const char* s = env->GetStringUTFChars(str, JNI_FALSE);
  if (std::regex_match(s, std::regex("[^\\-0-9.]"))) return o;
  mpf_class temp = getGMP(s);
  var -= temp;
  mpf_clear(temp.get_mpf_t());
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    ceil
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL ceilGMP(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_t temp;
  mpf_init(temp);
  mpf_ceil(temp, var.get_mpf_t());
  setGMPValue(&var, temp);
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    floor
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL floorGMP(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_t temp;
  mpf_init(temp);
  mpf_floor(temp, var.get_mpf_t());
  setGMPValue(&var, temp);
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    abs
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL absGMP(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_t temp;
  mpf_init(temp);
  mpf_abs(temp, var.get_mpf_t());
  setGMPValue(&var, temp);
  return o;
}
/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    truncate
 * Signature: ()Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL truncate(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_t temp;
  mpf_init(temp);
  mpf_trunc(temp, var.get_mpf_t());
  setGMPValue(&var, temp);
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL clear(JNIEnv *env, jobject o) {
  #ifndef var
    env->FatalError("Cannot free a \"nullptr\"!");
    env->ExceptionClear();
    return;
  #endif
  mpf_clear(var.get_mpf_t());
  var.~__gmp_expr();
  #undef var
  env->SetBooleanField(o, env->GetFieldID(env->GetObjectClass(o), "isCleared", "Z"), JNI_TRUE);
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    set
 * Signature: (Ldev/progames723/hmmm/GMP;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL set(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, env->GetMethodID(env->GetObjectClass(obj), "getAsString", "()Ljava/lang/String;")), JNI_FALSE));
  setGMPValue(&var, temp.get_mpf_t());
  mpf_clear(temp.get_mpf_t());
  temp.~__gmp_expr();
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    set
 * Signature: (Ljava/lang/String;)Ldev/progames723/hmmm/GMP;
 */
JNIEXPORT jobject JNICALL setStr(JNIEnv *env, jobject o, jstring str) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return NULL;
  #endif
  var = env->GetStringUTFChars(str, JNI_FALSE);
  return o;
}

/*
 * Class:     dev_progames723_hmmm_GMP
 * Method:    equals
 * Signature: (Ljava/lang/Object;)Z;
 */
JNIEXPORT jboolean JNICALL equals(JNIEnv *env, jobject o, jobject obj) {
  #ifndef var
    env->FatalError("Cannot use \"var\" after #clear() call!");
    env->ExceptionClear();
    return JNI_FALSE;
  #endif
  jclass cls = env->GetObjectClass(obj);

  jmethodID mid = env->GetMethodID(cls, "getClass", "()Ljava/lang/Class;");
  jobject clsObj = env->CallObjectMethod(obj, mid);

  cls = env->GetObjectClass(clsObj);

  // Find the getName() method on the class object
  mid = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");

  // Call the getName() to get a jstring object back
  jstring strObj = (jstring)env->CallObjectMethod(clsObj, mid);

  // Now get the c string from the java jstring object
  std::string str = env->GetStringUTFChars(strObj, NULL);

  env->DeleteLocalRef(cls);
  env->DeleteLocalRef(clsObj);
  env->DeleteLocalRef(strObj);

  if (str.contains("dev.progames723.hmmm.GMP")) {
    auto obj_cls = env->GetObjectClass(obj);
    auto methodID = env->GetMethodID(obj_cls, "getAsString", "()Ljava/lang/String;");
    mpf_class temp = getGMP(env->GetStringUTFChars((jstring) env->CallObjectMethod(obj, methodID), JNI_FALSE));
    bool eq = var == temp;
    mpf_clear(temp.get_mpf_t());
    env->DeleteLocalRef(obj_cls);
    return eq ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}
}