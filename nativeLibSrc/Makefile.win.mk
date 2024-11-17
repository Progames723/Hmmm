CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "$$JAVA_HOME" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/\2\4/' | tr '\\' '/')#msys quirks lol
JAVA_CYGWIN = $(shell echo "$$JAVA_HOME" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/cygdrive\/\2\4/' | tr '\\' '/')#cygwin lol

CYGWIN_EXISTS = $(shell [[ -d $(JAVA_CYGWIN) ]] && echo 1 || echo 0)

ifeq (CYGWIN_EXISTS, 1)
override CXXFLAGS += -I"$(JAVA_CYGWIN)\include" -I"$(JAVA_CYGWIN)\include\win32" -O3 -std=c++23 -fPIC -D__int64_t="int64_t" -D__int64="int64_t" -include "cstdint"
else
override CXXFLAGS += -I"$(JAVA)\include" -I"$(JAVA)\include\win32" -O3 -std=c++23 -fPIC -D__int64_t="int64_t" -D__int64="int64_t" -include "cstdint"
endif

all: gxx_compile gxx_link

gxx_compile:
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_GMP.cpp -o GMP.o -static -lgmp -lmpfr -lgmpxx
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_utils_MathUtil.cpp -o MathUtil.o -static
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_utils_NativeReflectUtil.cpp -o NativeReflectUtil.o -static
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_utils_NativeReflectUtil_Primitives.cpp -o NativeReflectUtil_Primitives.o -static

gxx_link: 
	$(CXX) $(CXXFLAGS) -o GMP.dll GMP.o -shared -lgmp -lmpfr -lgmpxx -Wl,--add-stdcall-alias
	$(CXX) $(CXXFLAGS) -o MathUtil.dll MathUtil.o -shared -Wl,--add-stdcall-alias
	$(CXX) $(CXXFLAGS) -o NativeReflectUtil.dll NativeReflectUtil.o -shared -Wl,--add-stdcall-alias
	$(CXX) $(CXXFLAGS) -o NativeReflectUtil_Primitives.dll NativeReflectUtil_Primitives.o -shared -Wl,--add-stdcall-alias

clean:
	rm *.o
	rm *.dll
	rm *.so