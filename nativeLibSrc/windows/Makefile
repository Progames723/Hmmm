# beware that this is for windows with the use of MINGW or any other *nix terminal like thing

CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "${JAVA_HOME}" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/\2\4/' | tr '\\' '/')#msys quirks lol
JAVA_CYGWIN = $(shell echo "${JAVA_HOME}" | sed -e 's/^./\L&\E/' | sed 's/^/\//' | sed 's/^\(.\)\(.\)\(.\)\(.*\)/\/cygdrive\/\2\4/' | tr '\\' '/')#cygwin lol

CYGWIN_EXISTS = $(shell [[ -d $(JAVA_CYGWIN) ]] && echo 1 || echo 0)

ifeq (CYGWIN_EXISTS, 1)
override CXXFLAGS += -I"$(JAVA_CYGWIN)\include" -I"$(JAVA_CYGWIN)\include\win32" -O3
else
override CXXFLAGS += -I"$(JAVA)\include" -I"$(JAVA)\include\win32" -O3
endif

SOURCES = $(shell find .. -name '*.cpp' -print | tr '\n' ' ')
HEADERS = $(shell find .. -name '*.h' -print | tr '\n' ' ')
OBJ = temp.o
OUT = libHmmm.dll

.PHONY: all

.DEFAULT: all

all: use_gxx

use_gxx: make_obj compile

use_clangxx: clang_make_obj clang_compile

make_obj:
	$(CXX) -c $(CXXFLAGS) $(SOURCES) -o $(OBJ) -static -lgmp -lgmpxx -D__int64="long long"

clang_make_obj:
	$(CXX_2) -c -o $(OBJ) $(CXXFLAGS) $(SOURCES) -static -lgmp -lgmpxx -D__int64="long long"

compile:
	$(CXX) -shared $(CXXFLAGS) $(OBJ) -o $(OUT) -Wl,--add-stdcall-alias

clang_compile:
	$(CXX_2) -shared -o $(OUT) $(OBJ) $(CXXFLAGS) -Wl,--add-stdcall-alias

clean:
	rm $(OBJ) && rm $(OUT)