#sudo apt update && sudo apt upgrade -y

#sudo apt install docker.io docker-compose -y

 # Colores para los mensajes
 GREEN='\033[0;32m'
 YELLOW='\033[1;33m'
 NC='\033[0m' 

 echo -e "${YELLOW} ELIMINANDO MAQUINAS DOCKER ANTERIORES"
 echo ""

sudo docker-compose down

echo -e "${GREEN} MAQUINA DETENIDA"
echo ""


echo -e "${YELLOW} LEVANTANDO BASE DE DATOS"
echo ""

sudo docker-compose -f "docker-compose.yml" up --build -d "mysql-db"

if [ $? -ne 0 ]; then
    echo -e "\033[0;31mError: Fallo al levantar la base de datos. Revisa los logs con 'docker compose logs'${NC}"
    exit 1
fi


echo -e "${GREEN} BASE DE DATOS LEVANTADA"
echo ""

sleep 10

echo -e "${YELLOW} LEVANTANDO CATALOGO APP"
echo ""

sudo docker-compose -f "docker-compose.yml" up --build -d "inventario-app"

if [ $? -ne 0 ]; then
    echo -e "\033[0;31mError: Fallo al levantar catalogo app. Revisa los logs con 'docker compose logs'${NC}"
    exit 1
fi

echo -e "${GREEN} CATALOGO APP LEVANTADO"
sudo docker-compose ps

