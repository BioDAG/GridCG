#!/bin/bash

cd jdl_collection/

sum=0
for i in `ls | grep steremma`; do
	str=$(grep "blast runtime is" "$i/std.out" | grep -o '[0-9]*' | cut -d " " -f 2);
	arr=($str)
	echo ${arr[1]}
	((sum+=${arr[1]}))
done
echo "sum is: $sum"
cd ..
