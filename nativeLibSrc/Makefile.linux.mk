CXX = g++

CXX_2 = clang++

JAVA = $(shell echo "$$JAVA_HOME")

override CXXFLAGS += -I$(JAVA)/include -I$(JAVA)/include/linux -O3 -std=c++23 -fPIC

all: gxx_compile gxx_link

gxx_compile:
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_GMP.cpp -o libGMP.o -static -lgmp -lmpfr -lgmpxx
	$(CXX) -c $(CXXFLAGS) dev_progames723_hmmm_utils_MathUtil.cpp -o libMathUtil.o -static

gxx_link: 
	$(CXX) $(CXXFLAGS) -o libGMP.so libGMP.o -shared
	$(CXX) $(CXXFLAGS) -o libMathUtil.so libMathUtil.o -shared

clean:
	rm *.o
	rm *.dll
	rm *.so