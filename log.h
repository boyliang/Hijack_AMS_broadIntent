/*
 * log_util.h
 *
 *  Created on: 2011-12-7
 *      Author: boyliang
 */

#ifndef LOG_UTIL_H_
#define LOG_UTIL_H_

#include <cutils/log.h>

#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "TTT"

#ifdef DEBUG

#define check_value(x) 														\
	ALOGI("%s: %p", #x, x);									\
	if(x == NULL){ 															\
		ALOGE("%s was NULL", #x);											\
	   	exit(0);															\
	}																		\

#define LOGI(...) ALOGI(__VA_ARGS__)										\

#else

#define check_value(x) if(0)
#define LOGI(...) if(0)

#endif

#endif /* LOG_UTIL_H_ */
