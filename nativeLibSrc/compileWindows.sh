cd src

g++ -c -I./include/windows/x64/include -I./include/windows/x64/include/win32 dev_progames723_hmmm_utils_MathUtil.cpp -o dev_progames723_hmmm_utils_MathUtil.o -m 64

g++ -shared -o ../build/windows/x64/mathUtil.dll dev_progames723_hmmm_utils_MathUtil.o -Wl,--add-stdcall-alias

g++ -c -I./include/windows/x86/include -I./include/windows/x86/include/win32 dev_progames723_hmmm_utils_MathUtil.cpp -o dev_progames723_hmmm_utils_MathUtil.o -m 32

g++ -shared -o ../build/windows/x86/mathUtil.dll dev_progames723_hmmm_utils_MathUtil.o -Wl,--add-stdcall-alias