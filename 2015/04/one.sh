#!/bin/bash

input=$1
i=0
#d="0000000af0000c9"
until [[ $d =~ ^0{5,}.* ]]
do
	#echo Checking $input$i
	d=$(echo -n $input$i | md5sum -)
	((i+=1))
done
((i-=1))
echo -n $input$i | md5sum -
echo $input$i

