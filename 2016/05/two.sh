#!/bin/bash

input=$1
i=5833678
matches=0
until [[ $matches -ge 24 ]]
do
	if [[ $(echo -n $input$i | md5sum -) =~ ^0{5,}.* ]]
	then
		echo -n $input$i | md5sum -
		echo $input$i >> output2.txt
		((matches+=1))
	fi
	((i+=1))
done

