LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := mathkitso
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	E:\example\Project824\app\src\main\jni\zhangc.c \

LOCAL_C_INCLUDES += E:\example\Project824\app\src\main\jni
LOCAL_C_INCLUDES += E:\example\Project824\app\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
