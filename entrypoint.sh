#!/bin/bash

echo "Inicializando MySQL..."

if [ ! -d "/var/lib/mysql/mysql" ]; then
    mysqld --initialize-insecure --user=mysql
fi

echo "Iniciando MySQL..."

mysqld --user=mysql --daemonize

echo "Aguardando MySQL..."

until mysqladmin ping -h 127.0.0.1 --silent; do
    sleep 2
done

echo "MySQL iniciado!"

mysql -u root <<EOF
CREATE DATABASE IF NOT EXISTS classica;

CREATE USER IF NOT EXISTS 'classica'@'localhost' IDENTIFIED BY 'classica123';

GRANT ALL PRIVILEGES ON classica.* TO 'classica'@'localhost';

FLUSH PRIVILEGES;
EOF

echo "Banco e usuário configurados!"

echo "Iniciando backend..."

java -jar /app/app.jar