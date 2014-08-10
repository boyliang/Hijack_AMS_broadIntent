/*
 * DummyJavaBBinder.h
 *
 *  Created on: 2011-12-6
 *      Author: boyliang
 */

#ifndef DUMMYJAVABBINDER_H_
#define DUMMYJAVABBINDER_H_

#include <binder/Binder.h>
#include <jni.h>

using namespace android;

class DummyJavaBBinder : public BBinder{
public:
	virtual status_t onTransact(uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags = 0) {
		return NO_ERROR;
	}

	jobject object() const {
		return mObject;
	}

	JavaVM* javaVM() const {
		return mVM;
	}

	void changObj(jobject newobj){
		const jobject* p_old_obj = &mObject;
		jobject* p_old_obj_noconst = const_cast<jobject *>(p_old_obj);
		*p_old_obj_noconst = newobj;
	}

private:
    JavaVM* const   mVM;
    jobject const   mObject;
};

#endif /* DUMMYJAVABBINDER_H_ */
