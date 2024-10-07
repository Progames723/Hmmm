

CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "${JAVA_HOME}")

override CXXFLAGS += -I$(JAVA)/include -I$(JAVA)/include/linux -O3

SOURCES = $(shell find .. -name '*.cpp' -print | tr '\n' ' ')
HEADERS = $(shell find .. -name '*.h' -print | tr '\n' ' ')
OBJ = temp.o
OUT = libHmmm.so

.PHONY: all

.DEFAULT: all

all: use_gxx

use_gxx: make_obj compile

use_clangxx: clang_make_obj clang_compile

make_obj:
	$(CXX) -c $(CXXFLAGS) $(SOURCES) -o $(OBJ) -static -fPIC -lgmp -lgmpxx

clang_make_obj:
	$(CXX_2) -c -o $(OBJ) $(CXXFLAGS) $(SOURCES) -fPIC -static -lgmp -lgmpxx

compile:
	$(CXX) -shared $(CXXFLAGS) $(OBJ) -o $(OUT)

clang_compile:
	$(CXX_2) -shared -o $(OUT) $(OBJ) $(CXXFLAGS)

clean:
	rm $(OBJ) && rm $(OUT)