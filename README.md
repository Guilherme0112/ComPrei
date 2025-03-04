<p>Baixe o repositório</p>
<p>Instale o Docker</p>
<p>Após iniciar o dokcer digite</p> <code>docker network create my-network</code>
<p>Depois de criar sua rede, rode o MySQL com: </p><code>docker run -p 3306:3306 --network=mynetwork --name  mysql -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=loja  mysql:latest</code>
<p>Agore dê build no Spring: </p><code>docker build -t comprei .</code>
<p>Rode o Spring: </p><code>docker run -p 8080:8080 --network=mynetwork --name comprei comprei</code>