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

if [[ "$#" -ge 2 ]]; then
	TMP_CONFIG="$(printf '[SAN]\nsubjectAltName=IP:'$2'\n')"
	
	for ip in ${@:3}; do
		TMP_CONFIG="$TMP_CONFIG $(printf '\nsubjectAltName=IP:'$ip)"
	done

	echo "*** Alternative names will be added to the config ***"
	echo "$TMP_CONFIG"
fi

echo
echo "*** Generating new key... ***"

CONFIG="$(sudo cat $CONFIG_PATH)$(printf '\n'$TMP_CONFIG)"

openssl genrsa -out "$NAME-priv-key.pem" 2048 > /dev/null; 
openssl req -subj "/CN=$NAME" -new -key "$NAME-priv-key.pem" -out "$NAME.csr" -reqexts SAN -config <(echo "$CONFIG") > /dev/null; 
openssl x509 -req -days 1825 -in "$NAME.csr" -CA ca.pem -CAkey ca-prib-key.pem -CAcreateserial -out "$NAME-cert.pem" -extensions v3_req -extfile <(echo "$CONFIG") > /dev/null;

echo "*** Generation keys ended. ***"