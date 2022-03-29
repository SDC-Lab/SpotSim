#!/bin/bash

for f in `ls *.xml`
do
	newname=`echo $f | sed 's/csv\.//g'`
	mv $f $newname
done
