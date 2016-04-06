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

if [[ "$#" -ge 2 ]]; then
	TMP_CONFIG="$(sudo cat $CONFIG_PATH)"
	TMP_CONFIG="$TMP_CONFIG $(printf '\n[SAN]\nsubjectAltName=IP:'$2'\n')"
	
	CONFIG=''

	for ip in ${@:2}; do
		CONFIG="$TMP_CONFIG $(printf '\nsubjectAltName=IP:'$ip)"
	done

	echo 'New config'
	echo "$CONFIG" | tail -n $(( $# - 1 ))

	echo 
	echo "*** Generating new key... ***"
	openssl genrsa -out "$NAME-priv-key.pem" 2048; 
	openssl req -subj "/CN=$NAME" -new -key "$NAME-priv-key.pem" -out "$NAME.csr" -reqexts SAN -config <(echo "$CONFIG"); 
	openssl x509 -req -days 1825 -in "$NAME.csr" -CA ca.pem -CAkey ca-prib-key.pem -CAcreateserial -out "$NAME-cert.pem" -extensions v3_req -extfile <(echo "$CONFIG"); 
	openssl rsa -in "$NAME-priv-key.pem" -out "$NAME-priv-key.pem";
fi