#!/bin/bash

IN=$1
dir=runtime_est

rm -rf $dir
mkdir $dir

cat $IN | while read line
do
	userid=`echo $line | awk '{print($12)}'`
	runtime=`echo $line | awk '{print($4)}'`
	echo $runtime >> ${dir}/${userid}.txt
done
