Configuration de Prometheus avec Docker Compose 

Cet exemple présente la mise en place de Prometheus à l'aide de Docker Compose ainsi que l'explication de son fichier principal prometheus.yml, qui permet à Prometheus de collecter les métriques à partir des services configurés. 

Fichier Docker Compose pour Prometheus 

Exemple de fichier docker-compose.yml utilisé pour déployer Prometheus : 

version: '3.8' 

  

services: 

  prometheus: 

    image: prom/prometheus 

    container_name: prometheus 

    restart: unless-stopped 

    ports: 

      - "9090:9090" 

    volumes: 

      - ./docker/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml 

 

Explications : 

version: '3.8' : Spécifie la version de Docker Compose pour garantir la compatibilité avec les fonctionnalités récentes. 

services : Définit les conteneurs à lancer. Ici, un seul service est configuré, nommé prometheus. 

prometheus : 

image: prom/prometheus : Utilise l’image officielle prom/prometheus depuis Docker Hub, qui est une base de données de séries temporelles pour la surveillance et les alertes. 

container_name: prometheus : Définit le nom du conteneur comme "prometheus", facilitant son identification. 

restart: unless-stopped : Permet au conteneur de redémarrer automatiquement en cas d'échec, sauf s'il est arrêté manuellement. 

ports: "9090:9090" : Redirige le port 9090 du conteneur vers le port 9090 de la machine hôte, permettant d'accéder à l'interface web de Prometheus via http://localhost:9090. 

volumes : Permet de gérer la configuration de Prometheus en mappant un fichier local avec un fichier dans le conteneur. 

./docker/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml : Lie le fichier de configuration prometheus.yml stocké localement à l'emplacement /etc/prometheus/prometheus.yml dans le conteneur. 

Importance de la gestion des volumes 

Le mappage de volumes permet une flexibilité accrue. Le fichier de configuration prometheus.yml, stocké localement, peut être modifié sans nécessiter de reconstruction du conteneur. Toute modification dans ce fichier est immédiatement prise en compte par Prometheus, car il est directement utilisé à partir du volume. 

 

Fichier de Configuration Prometheus (prometheus.yml) 

Le fichier de configuration prometheus.yml contient les directives sur le fonctionnement de Prometheus, les métriques à collecter et les services surveillés. 

Exemple de configuration : 

global: 

  scrape_interval: 10s 

  evaluation_interval: 10s 

  

scrape_configs: 

  - job_name: 'product_service' 

    metrics_path: /actuator/prometheus 

    scrape_interval: 5s 

    static_configs: 

      - targets: ['192.168.0.160:8081'] 

        labels: 

          application: 'Product Service Application' 

 

Explications détaillées : 

global : Définit des paramètres globaux pour l’ensemble des cibles à surveiller. 

scrape_interval: 10s : Définit que Prometheus doit collecter les métriques de chaque service toutes les 10 secondes par défaut. 

evaluation_interval: 10s : Indique la fréquence à laquelle Prometheus évalue les règles configurées, qui peuvent générer des alertes ou effectuer des transformations sur les données collectées. 

scrape_configs : 

La section scrape_configs spécifie les services à surveiller et les détails pour récupérer leurs métriques. 

job_name: 'product_service' : Le nom du job, ici product_service, identifie le service à surveiller. Ce nom permet une identification facile des métriques dans les requêtes et les alertes. 

metrics_path: /actuator/prometheus : Précise le chemin à utiliser pour récupérer les métriques du service cible. Ici, il s'agit d'une application Spring Boot qui expose ses métriques sur /actuator/prometheus. 

scrape_interval: 5s : Définit que Prometheus collectera les métriques de ce service particulier toutes les 5 secondes, plus fréquemment que l’intervalle global. 

static_configs : Spécifie les adresses IP des services cibles. 

targets: ['192.168.0.160:8081'] : Indique que Prometheus doit récupérer les métriques à partir du service disponible à l’adresse 192.168.0.160 sur le port 8081. 

labels : Ajoute des étiquettes personnalisées aux métriques collectées. Ici, l'étiquette application avec la valeur Product Service Application est ajoutée, facilitant l'agrégation et la recherche des métriques associées à cette application. 

 

Conclusion 

Cette configuration permet à Prometheus de surveiller les services configurés, tels que Product Service Application, et de collecter des données en temps réel. Le mappage de volumes avec Docker Compose offre une gestion facile du fichier de configuration sans redémarrer le conteneur, rendant ce système de surveillance efficace pour maintenir une visibilité constante sur l'état et les performances des services en production. 

 

Configuration de Grafana Tempo avec Docker Compose 

Dans cette section, nous expliquons la configuration de Grafana Tempo, un système de traçage distribué open source utilisé pour collecter et analyser les traces dans une architecture de microservices. L'utilisation de Docker Compose simplifie le déploiement et la gestion de Tempo. 

Fichier Docker Compose pour Tempo 

Voici un exemple de fichier docker-compose.yml utilisé pour déployer Tempo : 

version: '3.8' 

  

services: 

  tempo: 

    image: grafana/tempo 

    container_name: tempo 

    command: ["-config.file=/etc/tempo.yaml"] 

    volumes: 

      - ./docker/tempo/tempo.yml:/etc/tempo.yaml:ro 

      - ./docker/tempo/tempo-data:/tmp/tempo 

    ports: 

      - "3200:3200" 

      - "9411:9411" # zipkin et server 

 

Explications détaillées : 

version: '3.8' : Définit la version de Docker Compose à utiliser, garantissant la compatibilité avec les fonctionnalités modernes. 

services : Section définissant les services déployés. Ici, nous avons un service nommé tempo. 

tempo : 

image: grafana/tempo : Utilise l’image officielle grafana/tempo depuis Docker Hub, qui est utilisée pour collecter et stocker les traces distribuées. 

container_name: tempo : Attribue au conteneur le nom tempo, facilitant son identification. 

command: ["-config.file=/etc/tempo.yaml"] : Spécifie la commande à exécuter pour démarrer Tempo avec le fichier de configuration fourni (/etc/tempo.yaml). 

volumes : Section permettant de monter des fichiers et répertoires locaux dans le conteneur : 

./docker/tempo/tempo.yml:/etc/tempo.yaml 

: Monte le fichier de configuration tempo.yml situé localement dans le répertoire ./docker/tempo/ à l’intérieur du conteneur à l'emplacement /etc/tempo.yaml. L'option ro signifie que le fichier est monté en lecture seule, empêchant toute modification depuis le conteneur. 

./docker/tempo/tempo-data:/tmp/tempo : Monte un répertoire local pour stocker les données de Tempo (les traces) dans /tmp/tempo dans le conteneur. 

ports : 

"3200:3200" : Le port 3200 du conteneur est redirigé vers le port 3200 de la machine hôte, permettant l’accès à l’interface HTTP de Tempo. 

"9411:9411" : Le port 9411 est utilisé pour recevoir les traces au format Zipkin, ce qui permet à Tempo d’être compatible avec des services envoyant des traces via Zipkin. 

Importance des volumes 

Le mappage des volumes permet à Tempo d'utiliser un fichier de configuration externe (tempo.yml) et de stocker les données localement. Toute modification du fichier de configuration tempo.yml peut être réalisée directement sur la machine hôte sans nécessiter la reconstruction ou le redémarrage du conteneur. 

Fichier de Configuration Tempo (tempo.yml) 

Le fichier de configuration tempo.yml contient les informations concernant la configuration du serveur Tempo, le stockage des traces et les protocoles de réception des traces. 

Exemple de configuration : 

server: 

  http_listen_port: 3200 

  

distributor: 

  receivers: 

    zipkin: 

  

storage: 

  trace: 

    backend: local 

    local: 

      path: /tmp/tempo/blocks 

 

Explications détaillées : 

server : 

http_listen_port: 3200 : Définit que Tempo écoute sur le port 3200 pour les requêtes HTTP. Cela correspond au port mappé dans Docker Compose pour permettre l’accès à l’interface web de Tempo. 

distributor : 

receivers : Spécifie les protocoles que Tempo accepte pour recevoir les traces. 

zipkin : Active le support du protocole Zipkin, permettant à Tempo de recevoir des traces envoyées au format Zipkin. Cela correspond au port 9411 mappé dans Docker Compose. 

storage : 

trace : Définit le backend de stockage des traces collectées par Tempo. 

backend: local : Indique que Tempo stockera les traces en local, sans utiliser de backend distant (comme AWS ou GCS). 

local : 

path: /tmp/tempo/blocks : Définit le chemin où les blocs de traces sont stockés. Ce chemin correspond au volume ./docker/tempo/tempo-data monté depuis la machine hôte dans Docker Compose. 

 

Conclusion 

La configuration de Tempo via Docker Compose permet une gestion simple et efficace du système de traçage distribué. Le mappage des volumes assure une flexibilité dans la gestion du fichier de configuration et le stockage des données. Tempo, associé à Zipkin, est capable de recevoir et de traiter les traces d’une architecture de microservices, fournissant ainsi une vue complète des opérations internes des services surveillés. Grâce à cette configuration, il est possible de surveiller les performances des services et de diagnostiquer les problèmes en analysant les traces distribuées. 

 

 

Zipkin et Tempo font-ils la même chose ? 

Oui et non. Les deux outils remplissent des rôles similaires en tant que systèmes de traçage distribués, mais ils ne sont pas identiques en termes de fonctionnalités et de conception. 

Zipkin est un système de traçage distribué mature qui a été conçu pour collecter, stocker et visualiser les traces distribuées. Il a son propre backend de stockage et sa propre interface web pour l'analyse des traces. 

Grafana Tempo, en revanche, se concentre uniquement sur le stockage et la recherche de traces à grande échelle. Il n'a pas d'interface de visualisation propre, mais il est conçu pour s'intégrer nativement avec Grafana, un outil puissant de visualisation et de surveillance. Tempo est optimisé pour l'évolutivité et peut gérer des volumes de traces bien plus importants que Zipkin, en stockant les traces de manière plus efficace, mais en se reposant sur Grafana pour la visualisation. 

Pourquoi utiliser Tempo au lieu de Zipkin ? 

Scalabilité : Tempo est conçu pour gérer une grande quantité de traces (sans échantillonnage) et pour être évolutif avec un faible coût de stockage, en utilisant un stockage d'objet distribué (par exemple, des systèmes comme S3). 

Compatibilité avec Grafana : Tempo s'intègre parfaitement avec Grafana pour visualiser les traces dans les tableaux de bord. Cela en fait un choix idéal si tu utilises déjà Grafana pour la surveillance et que tu souhaites unifier les logs, les métriques et les traces dans un seul endroit. 

Simplicité de déploiement : Tempo est conçu pour être facile à déployer et à configurer, avec un focus sur la simplicité du stockage local ou cloud, tandis que Zipkin peut nécessiter plus de configurations de backend pour le stockage et la visualisation. 

 

 

Il y a quelques scénarios spécifiques où cela pourrait être pertinent d’utiliser les deux outils : 

1. Migration progressive d'une solution à l'autre 

Si tu utilises déjà Zipkin dans ton environnement et que tu souhaites migrer progressivement vers Tempo, il peut être utile d’exécuter les deux systèmes simultanément pendant la phase de transition. Cela permet de continuer à collecter les traces via Zipkin, tout en testant Tempo dans un environnement réel. Durant cette phase, les traces peuvent être envoyées aux deux systèmes pour comparaison avant une migration complète vers Tempo. 

2. Scénarios de compatibilité avec des clients existants 

Si certaines de tes applications ou microservices sont fortement intégrés avec Zipkin, et qu'il serait coûteux ou complexe de les migrer vers un client ou un protocole différent (comme OpenTelemetry), il pourrait être intéressant de : 

Continuer à utiliser Zipkin pour ces services qui sont déjà configurés. 

Utiliser Tempo pour les nouveaux services ou ceux qui nécessitent une meilleure intégration avec Grafana. 

Dans ce cas, Tempo recevrait les traces au format Zipkin (comme le montre ta configuration avec receivers.zipkin), ce qui te permettrait de centraliser les traces dans un seul système, même si certaines parties de ton infrastructure sont encore configurées pour utiliser Zipkin. 

3. Besoin de fonctionnalités spécifiques 

Bien que Tempo soit idéal pour l'évolutivité et l'intégration avec Grafana, certaines fonctionnalités spécifiques de Zipkin pourraient justifier de maintenir les deux : 

Interface de visualisation indépendante : Zipkin possède sa propre interface web pour visualiser les traces. Si certains membres de ton équipe préfèrent l’interface native de Zipkin ou si des workflows critiques reposent sur cette interface, tu pourrais maintenir Zipkin uniquement pour la visualisation. 

Analyse avancée des traces : Zipkin permet une analyse directe des traces via son interface. Si tu as besoin d’une interface dédiée pour certaines équipes (sans dépendre uniquement de Grafana), Zipkin peut continuer à jouer ce rôle. 

4. Besoins de résilience et de redondance 

Dans des environnements critiques, où une redondance et une haute disponibilité sont essentielles, tu pourrais envisager d’avoir deux systèmes de traçage actifs en parallèle, où Zipkin et Tempo collecteraient les mêmes données en cas de panne ou de surcharge de l'un des deux systèmes. 

  

 

Configuration de Loki avec Docker Compose 

Dans cette section, nous allons examiner comment configurer Grafana Loki à l'aide de Docker Compose, ainsi que son intégration avec Logback pour la gestion des journaux. Nous allons également décrire en détail le fichier de configuration local-config.yaml utilisé pour Loki. 

Docker Compose pour Loki 

Voici un exemple de configuration Docker Compose pour déployer Loki : 

version: '3.8' 

  

services: 

  loki: 

    image: grafana/loki:main 

    container_name: loki 

    command: ["-config.file=/etc/loki/local-config.yaml"] 

    ports: 

      - "3100:3100" 

 

Explications détaillées : 

version: '3.8' : Cela spécifie la version de Docker Compose utilisée pour assurer la compatibilité avec les fonctionnalités récentes. 

services : Cette section permet de définir les conteneurs à lancer. Ici, nous avons un seul service, loki. 

loki : 

image: grafana/loki 

: Ceci utilise l'image officielle de Loki disponible sur Docker Hub. Loki est un système de journalisation conçu pour collecter et indexer les logs. 

container_name: loki : Cela définit le nom du conteneur en tant que "loki", facilitant son identification. 

command: ["-config.file=/etc/loki/local-config.yaml"] : Cette commande indique à Loki de charger sa configuration à partir du fichier spécifié. 

ports: "3100:3100" : Ce paramètre redirige le port 3100 du conteneur vers le port 3100 de la machine hôte. Ce port est utilisé pour accéder à l'API de Loki. 

Configuration de Loki (local-config.yaml) 

Voici un exemple de configuration XML pour un appender Logback qui envoie des logs à Loki : 

<?xml version="1.0" encoding="UTF-8"?> 

<configuration> 

    <jmxConfigurator/> 

    <include resource="org/springframework/boot/logging/logback/base.xml"/> 

    <springProperty scope="context" name="appName" source="spring.application.name"/> 

  

    <appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender"> 

        <http> 

            <url>http://localhost:3100/loki/api/v1/push</url> 

        </http> 

        <format> 

            <label> 

                <pattern>application=${appName},host=${HOSTNAME},level=%level</pattern> 

            </label> 

            <message> 

                <pattern>${FILE_LOG_PATTERN}</pattern> 

            </message> 

            <sortByTime>true</sortByTime> 

        </format> 

    </appender> 

  

    <root level="INFO"> 

        <appender-ref ref="LOKI"/> 

    </root> 

</configuration> 

 

Explications détaillées : 

<jmxConfigurator/> : Cela permet d'activer la configuration JMX pour le suivi des performances. 

<include resource="org/springframework/boot/logging/logback/base.xml"/> : Cela inclut la configuration de base de Logback pour l'application Spring Boot. 

<springProperty scope="context" name="appName" source="spring.application.name"/> : Cela définit une propriété Spring pour le nom de l'application, facilitant ainsi l'identification des logs. 

<appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender"> : Cela définit un nouvel appender nommé "LOKI" qui utilise Loki4jAppender pour envoyer des logs à Loki. 

<http> : Cette section contient la configuration HTTP pour Loki. 

<url>http://localhost:3100/loki/api/v1/push</url> : C'est l'URL à laquelle les logs seront envoyés. Cela pointe vers l'API de Loki pour l'envoi de logs. 

<format> : Cette section définit le format des logs envoyés à Loki. 

<label> : Ici, nous définissons des étiquettes pour les logs qui incluent le nom de l'application, l'hôte et le niveau de log. 

<message> : Le format du message de log est défini ici, utilisant le modèle de format de fichier. 

<sortByTime>true</sortByTime> : Cela indique que les logs doivent être triés par temps lors de leur envoi à Loki. 

<root level="INFO"> : Cela définit le niveau de log par défaut à INFO. 

<appender-ref ref="LOKI"/> : Cela indique que tous les logs au niveau racine (INFO et supérieur) doivent être envoyés à l'appender LOKI. 

Conclusion 

En intégrant Loki avec Docker Compose et Logback, nous avons la capacité de collecter et de gérer les logs d'application de manière efficace. La configuration de Loki avec un appender Logback permet d'envoyer des logs en temps réel vers Loki, où ils peuvent être indexés et consultés via l'interface de Grafana. Cela crée un système robuste pour la gestion des logs dans des environnements de production. 

 

 

 
