#where did this go

CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "${JAVA_HOME}")

override CXXFLAGS += -I"$(JAVA)/include" -I"$(JAVA)/include/linux" -I"$(HEADERS)" -O3 -std=c++23

SOURCES = $(shell find .. -name '*.cpp' -print | tr '\n' ' ')
HEADERS = $(shell [[ -d ./src/main/headers ]] && echo "./src/main/headers" || echo "../src/main/headers")
OBJ = $(shell find .. -name '*.o' -print | tr '\n' ' ')
OUT = libhmmm.dll

make_obj:
	$(CXX) -c $(CXXFLAGS) $(SOURCES) -static -fPIC -lmpfr -lgmp -lgmpxx -llibstdc++

clang_make_obj:
	$(CXX_2) -c $(CXXFLAGS) $(SOURCES) -fPIC -static -lmpfr -lgmp -lgmpxx -llibstdc++

compile:
	$(CXX) -shared $(CXXFLAGS) $(OBJ) -o $(OUT) -lmpfr -lgmp -lgmpxx -llibstdc++

clang_compile:
	$(CXX_2) -shared -o $(OUT) $(OBJ) $(CXXFLAGS) -lmpfr -lgmp -lgmpxx -llibstdc++

clean:
	rm $(OBJ) && rm $(OUT)