#! /bin/bash

read -p "Use windows Makefile?(Y/N): " windows
read -p "Use clang?(Y/N): " clang
read -p "Clean up?(Y/N): " cleanup

if [[ "$windows" == *[yY]* || "$windows" == *[yY][eE][sS]* ]]; then
    if [[ "$cleanup" == *[yY]* || "$cleanup" == *[yY][eE][sS]* ]]; then
        echo "deleting temporary windows compilation files"
        make -f ./windows/Makefile clean
        exit
    fi
    if [[ "$clang" == *[yY]* || "$clang" == *[yY][eE][sS]* ]]; then
        echo "windows with clang++"
        make -f ./windows/Makefile make_obj
        make -f ./windows/Makefile compile
    else
        echo "windows with g++"
        make -f ./windows/Makefile make_obj
        make -f ./windows/Makefile compile
    fi
else
    if [[ "$cleanup" == *[yY]* || "$cleanup" == *[yY][eE][sS]* ]]; then
        echo "deleting temporary linux compilation files"
        make -f ./linux/Makefile clean
        exit
    fi
    if [[ "$clang" == *[yY]* || "$clang" == *[yY][eE][sS]* ]]; then
        echo "linux with clang++"
        make -f ./linux/Makefile make_obj
        make -f ./linux/Makefile compile
    else
        echo "linux with g++"
        make -f ./linux/Makefile make_obj
        make -f ./linux/Makefile compile
    fi
fi