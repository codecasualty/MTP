#!/bin/bash

echo "enter your subnet "

read subnet



# if [[ -z "$name" ]];then
#     echo "string is empty"
# else 
#     echo "your name is $name"
# fi
# 10.96.17.175
for IP in $(seq 154 156); do
    ping -c 2 $subnet.${IP}
done