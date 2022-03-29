#!/bin/bash

for f in `ls *.xml`
do
	cat $f | sed 's/list/tree-set/g' > $f.bak
	cat $f.bak | sed '/<tree-set>/a\
<comparator class="org.cloudbus.spotsim.cloud.spothistory.SpotPriceRecord$2"\/>' > $f.bak2
	mv $f.bak2 $f
	rm -f $f.bak
done
