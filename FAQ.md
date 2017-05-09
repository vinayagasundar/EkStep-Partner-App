What are things need to build a Partner App ?
1. Partner ID
2. Genie SDK

Where can i find documentation and example for building Partner App ?
1. https://github.com/ekstep/EkStep-Partner-App/wiki

What is Telementry ? How can i use it my app ?
https://community.ekstep.in/specifications-guides/53-telemetry-specification

How to create public & private key for Partner App and Why I need this ?

All the Partner data will be encrypted before sending into Genie. 
For this encryption we need these keys.

Windows
=======

Steps to Generate RSA public and private keys:

	1) Download openssl from this link:   					    
		http://www.indyproject.org/Sockets/fpc/OpenSSLforWin64.en.aspx
	2) Extract the downloaded folder and click on "openssl.exe" then it will open cmd prompt.		
	(Right click and Run as a Administrator)
	3) To generate an RSA Private key pair use: (Type below cmd in the cmd prompt)    
			genrsa -out rsa_1024_priv.pem 1024
	4) This private key file "rsa_1024_priv.pem" will be generated on your extracted folder.
	5) To generate the Public Key in PEM format use:(Type below cmd in the cmd prompt)    
			rsa -pubout -in rsa_1024_priv.pem -out rsa_1024_pub.pem
	6) This public key file "rsa_1024_pub.pem" will be generated on your extracted folder.


Linux 
=====

Ubuntu 
-----------

Install 
https://geeksww.com/tutorials/libraries/openssl/installation/installing_openssl_on_ubuntu_linux.php

	1) To generate an RSA Private key pair use: (Type below cmd in the terminal)    
			genrsa -out rsa_1024_priv.pem 1024
	2) This private key file "rsa_1024_priv.pem" will be generated on your extracted folder.
	3) To generate the Public Key in PEM format use:(Type below cmd in theterminal)    
			rsa -pubout -in rsa_1024_priv.pem -out rsa_1024_pub.pem
	4) This public key file "rsa_1024_pub.pem" will be generated on your extracted folder.

Mac
====
Install

brew install openssl

	1) To generate an RSA Private key pair use: (Type below cmd in the terminal)    
			genrsa -out rsa_1024_priv.pem 1024
	2) This private key file "rsa_1024_priv.pem" will be generated on your extracted folder.
	3) To generate the Public Key in PEM format use:(Type below cmd in theterminal)    
			rsa -pubout -in rsa_1024_priv.pem -out rsa_1024_pub.pem
	4) This public key file "rsa_1024_pub.pem" will be generated on your extracted folder.
