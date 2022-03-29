#!/bin/bash

cp=`ls lib/*`

cp1=`echo $cp | sed 's/\ /:/g'`

java -cp ./class:${cp1} org.cloudbus.replica.Main
