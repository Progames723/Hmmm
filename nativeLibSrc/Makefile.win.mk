CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "$$JAVA_HOME" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/\2\4/' | tr '\\' '/')#msys quirks lol
JAVA_CYGWIN = $(shell echo "$$JAVA_HOME" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/cygdrive\/\2\4/' | tr '\\' '/')#cygwin lol

CYGWIN_EXISTS = $(shell [[ -d $(JAVA_CYGWIN) ]] && echo 1 || echo 0)

ifeq (CYGWIN_EXISTS, 1)
override CXXFLAGS += -I"$(JAVA_CYGWIN)\include" -I"$(JAVA_CYGWIN)\include\win32" -O3 -std=c++23 -fPIC
else
override CXXFLAGS += -I"$(JAVA)\include" -I"$(JAVA)\include\win32" -O3 -std=c++23 -fPIC
endif

all: gxx_compile gxx_link

gxx_compile:
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_GMP.cpp -o GMP.o -static -lgmp -lmpfr -lgmpxx -D__int64_t="long long" -D__int64="long long"
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_utils_MathUtil.cpp -o MathUtil.o -static -D__int64_t="long long" -D__int64="long long"

gxx_link: 
	$(CXX) $(CXXFLAGS) -o GMP.dll GMP.o -shared -lgmp -lmpfr -lgmpxx -Wl,--add-stdcall-alias
	$(CXX) $(CXXFLAGS) -o MathUtil.dll MathUtil.o -shared -Wl,--add-stdcall-alias

clean:
	rm *.o
	rm *.dll
	rm *.so