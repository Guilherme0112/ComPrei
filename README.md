<p>Baixe o repositório</p>
<p>Instale o Docker</p>
<p>Após iniciar o docker digite <code>docker network create my-network</code></p>
<p>Depois de criar sua rede, rode o MySQL com: <code>docker run -p 3306:3306 --network=mynetwork --name  mysql -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=loja  mysql:latest</code></p>
<p>Agore dê build no Spring: <code>docker build -t comprei .</code></p>
<p>Rode o Spring: <code>docker run -p 8080:8080 --network=mynetwork --name comprei comprei</code></p>