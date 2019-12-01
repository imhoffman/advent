#!/bin/bash

input=$1
i=346386
until [[ $d =~ ^0{6,}.* ]]
do
	#echo Checking $input$i
	d=$(echo -n $input$i | md5sum -)
	((i+=1))
done
((i-=1))
echo -n $input$i | md5sum -
echo $input$i

