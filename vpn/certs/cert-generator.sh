#!/bin/bash


NAME="$1"

if [[ -z "$NAME" ]]; then
	echo "Please provide name of the certificate as first agrument"
	exit 1;
fi

CONFIG_PATH="/usr/lib/ssl/openssl.cnf"

while [[ ! -e "$CONFIG_PATH" ]]; 
do
	echo "Cannot find openssl default configuration."
	read -i  "Please provide the route to the configuration file: " CONFIG_PATH;
done

TMP_CONFIG=""

array_index=1;

if [[ "$#" -ge 2 ]]; then
	# TMP_CONFIG="$(printf '[alt_names]\n')"
	TMP_CONFIG="\nsubjectAltName = "
	
	for ip in ${@:2}; do
		TMP_CONFIG="$TMP_CONFIG""IP:$ip"
		if [[ $array_index -lt $(($# - 1)) ]]; then
			TMP_CONFIG="$TMP_CONFIG,"
		fi
		array_index=$(($array_index + 1))
	done

	echo "*** Alternative names will be added to the config ***"
	printf "$TMP_CONFIG"
fi

CONFIG=$(sed 's/\#\s*x509_extensions\s*=\s*v3_req/x509_extensions = v3_req/;' $CONFIG_PATH | sed 's/\[ v3_req \]/\[ v3_req \]\n'"$TMP_CONFIG"'/')

if [[ -z "$CONFIG" ]]; then
	exit 1;
fi

echo
echo
echo "*** Generating new key... ***"

openssl genrsa -out "$NAME-priv-key.pem" 2048; 
openssl req -subj "/CN=$NAME" -new -key "$NAME-priv-key.pem" -out "$NAME.csr" -config <(echo "$CONFIG"); 
openssl x509 -req -days 1825 -in "$NAME.csr" -CA ca.pem -CAkey ca-priv-key.pem -CAcreateserial -out "$NAME-cert.pem" -extensions v3_req -extfile <(echo "$CONFIG");

echo "*** Generation keys ended ***"