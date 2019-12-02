#!/bin/bash

input=$1
i=24090052
matches=0
until [[ $matches -ge 36 ]]
do
	if [[ $(echo -n $input$i | md5sum -) =~ ^0{5,}.* ]]
	then
		echo $input$i >> output3.txt
		echo -n $input$i | md5sum - >> output3.txt
		((matches+=1))
	fi
	((i+=1))
done

