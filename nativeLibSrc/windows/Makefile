# beware that this is for windows with the use of MINGW or any other *nix terminal like thing

CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "${JAVA_HOME}" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/\2\4/' | tr '\\' '/')#msys quirks lol
JAVA_CYGWIN = $(shell echo "${JAVA_HOME}" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/cygdrive\/\2\4/' | tr '\\' '/')#cygwin lol

JAVA_EXISTS = $(shell [[ -d $(JAVA) ]] && echo 1 || echo 0)
CYGWIN_EXISTS = $(shell [[ -d $(JAVA_CYGWIN) ]] && echo 1 || echo 0)

SOURCES = $(shell find .. -name '*.cpp' -print | tr '\n' ' ')
HEADERS = $(shell [[ -d ./src/main/headers ]] && echo "./src/main/headers" || echo "../src/main/headers")
OBJ = $(shell find .. -name '*.o' -print | tr '\n' ' ')
OUT = hmmm.dll

ifeq (JAVA_EXISTS, 0)
override JAVA = $(shell echo "${JAVA_HOME}")//should work for regular winows shell
endif

ifeq (CYGWIN_EXISTS, 1)
override CXXFLAGS += -I"$(JAVA_CYGWIN)\include" -I"$(JAVA_CYGWIN)\include\win32" -I"$(HEADERS)" -O3 -std=c++23
else
override CXXFLAGS += -I"$(JAVA)\include" -I"$(JAVA)\include\win32" -I"$(HEADERS)" -O3 -std=c++23
endif

make_obj:
	$(CXX) -c $(CXXFLAGS) $(SOURCES) -static -D__int64="long long" -D__int64_t="long long" -lmpfr -lgmp -lgmpxx

clang_make_obj:
	$(CXX_2) -c $(CXXFLAGS) $(SOURCES) -static -D__int64="long long" -D__int64_t="long long" -lmpfr -lgmp -lgmpxx

compile:
	$(CXX) -shared $(CXXFLAGS) $(OBJ) -o $(OUT) -D_JNI_IMPLEMENTATION_ -Wl,--kill-at,--add-stdcall-alias -lmpfr -lgmp -lgmpxx

clang_compile:
	$(CXX_2) -shared -o $(OUT) $(OBJ) $(CXXFLAGS) -D_JNI_IMPLEMENTATION_ -Wl,--kill-at,--add-stdcall-alias -lmpfr -lgmp -lgmpxx

clean:
	rm $(OBJ) && rm $(OUT)