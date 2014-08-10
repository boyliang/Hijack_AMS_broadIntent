/*
 * importdex.cpp
 *
 *  Created on: 2014年6月24日
 *      Author: boyliang
 */

#include <stdio.h>
#include <stddef.h>
#include <jni.h>
#include <android_runtime/AndroidRuntime.h>
#include <binder/IServiceManager.h>
#include <binder/Binder.h>
#include <utils/RefBase.h>

#include "log.h"
#include "proxybinder.h"
#include "DummyJavaBBinder.h"

using namespace android;

static const char JSTRING[] = "Ljava/lang/String;";
static const char JCLASS_LOADER[] = "Ljava/lang/ClassLoader;";
static const char JCLASS[] = "Ljava/lang/Class;";

static JNIEnv* jni_env;
static char sig_buffer[512];

//ClassLoader.getSystemClassLoader()
static jobject getSystemClassLoader(){
	jclass class_loader_claxx = jni_env->FindClass("java/lang/ClassLoader");
	snprintf(sig_buffer, 512, "()%s", JCLASS_LOADER);
	jmethodID getSystemClassLoader_method = jni_env->GetStaticMethodID(class_loader_claxx, "getSystemClassLoader", sig_buffer);
	return jni_env->CallStaticObjectMethod(class_loader_claxx, getSystemClassLoader_method);
}

void Main() {
	JavaVM* jvm = AndroidRuntime::getJavaVM();

	jvm->AttachCurrentThread(&jni_env, NULL);

	jstring apk_path = jni_env->NewStringUTF("/data/local/tmp/DemoInject3.apk");
	jstring dex_out_path = jni_env->NewStringUTF("/data/dexout");
	jclass dexloader_claxx = jni_env->FindClass("dalvik/system/DexClassLoader");

	snprintf(sig_buffer, 512, "(%s%s%s%s)V", JSTRING, JSTRING, JSTRING, JCLASS_LOADER);
	jmethodID dexloader_init_method = jni_env->GetMethodID(dexloader_claxx, "<init>", sig_buffer);

	snprintf(sig_buffer, 512, "(%s)%s", JSTRING, JCLASS);
	jmethodID loadClass_method = jni_env->GetMethodID(dexloader_claxx, "loadClass", sig_buffer);

	jobject class_loader = getSystemClassLoader();
	check_value(class_loader);

	jobject dex_loader_obj = jni_env->NewObject(dexloader_claxx, dexloader_init_method, apk_path, dex_out_path, NULL, class_loader);
	jstring class_name = jni_env->NewStringUTF("com.demo.inject3.EntryClass");
	jclass entry_class = static_cast<jclass>(jni_env->CallObjectMethod(dex_loader_obj, loadClass_method, class_name));

	jmethodID invoke_method = jni_env->GetStaticMethodID(entry_class, "invoke", "(I)[Ljava/lang/Object;");
	check_value(invoke_method);

	jobjectArray objectarray = (jobjectArray) jni_env->CallStaticObjectMethod(entry_class, invoke_method, 0);
	check_value(objectarray);

	jsize size = jni_env->GetArrayLength(objectarray);
	sp<IServiceManager> servicemanager = defaultServiceManager();
	for (jsize i = 0; i < size; i += 2) {
		jstring name = static_cast<jstring>(jni_env->GetObjectArrayElement(objectarray, i));
		jobject obj = jni_env->GetObjectArrayElement(objectarray, i + 1);

		const char* c_name = jni_env->GetStringUTFChars(name, NULL);
		DummyJavaBBinder* binder = (DummyJavaBBinder*) servicemanager->getService(String16(c_name)).get();
		binder->changObj(jni_env->NewGlobalRef(obj));
	}

	jvm->DetachCurrentThread();
}
