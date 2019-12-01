#!/bin/bash

input=$1
i=0
matches=0
until [[ $matches -ge 8 ]]
do
	if [[ $(echo -n $input$i | md5sum -) =~ ^0{5,}.* ]]
	then
		echo -n $input$i | md5sum -
		echo $input$i >> output.txt
		((matches+=1))
	fi
	((i+=1))
done

