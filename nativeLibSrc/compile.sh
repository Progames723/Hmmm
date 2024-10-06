#! /bin/bash

read -p "Use windows Makefile?(Y/N): " windows
read -p "Use clang?(Y/N): " clang
read -p "Clean up?(Y/N): " cleanup

if [[ "$windows" == *[yY]* || "$windows" == *[yY][eE][sS]* ]]; then
    if [[ "$cleanup" == *[yY]* || "$cleanup" == *[yY][eE][sS]* ]]; then
        echo "deleting temporary windows compilation files"
        make -f ./windows/MakeFile clean
        exit
    fi
    if [[ "$clang" == *[yY]* || "$clang" == *[yY][eE][sS]* ]]; then
        echo "windows with clang++"
        make -f ./windows/MakeFile use_clangxx
    else
        echo "windows with g++"
        make -f ./windows/MakeFile
    fi
else
    if [[ "$cleanup" == *[yY]* || "$cleanup" == *[yY][eE][sS]* ]]; then
        echo "deleting temporary linux compilation files"
        make -f ./linux/MakeFile clean
        exit
    fi
    if [[ "$clang" == *[yY]* || "$clang" == *[yY][eE][sS]* ]]; then
        echo "linux with clang++"
        make -f ./linux/MakeFile use_clangxx
    else
        echo "linux with g++"
        make -f ./linux/MakeFile
    fi
fi