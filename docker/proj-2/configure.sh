#!/bin/bash

SW_MASTER_IP=0.0.0.0;
IP_RegEx="([0-9]{1,3}\.?){4}";
PORT_RegEx="[0-9]+";

if [ $# -gt 0 ] && [[ "$1" =~ [a-z]+ ]]; then
	SW_MASTER_IP=$(docker-machine ls | egrep "$1.*\(master\)" | sed -r "s/.*(tcp|upd)\:\/\/($IP_RegEx\:$PORT_RegEx).*/\2/");
	if [ -n $SW_MASTER_IP ]; then
		echo "This is swarm master ip: $SW_MASTER_IP";
		
	fi	
else
	echo "First argument is not a word";
fi

# if [ -n $1 ] && ([ "$1" = "a" ] || [ "$1" \> "a" ]); then
# 	contaner="$1";
# 	echo "Configuring nginx on the server";
# 	service=$(docker ps -a --format='{{.Names}}'| grep -i "$contaner");
# 	for s in $service; do
# 		if [ -n "$(which docker-machine)" ]; then
# 			machine=$(docker-machine ls | grep -i "$s");
# 			echo "$machine";
# 		fi
# 	done
# else
# 	echo -e "You have not mentioned any argument to confiugre\nExiting...";
# fi