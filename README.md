# Application Yog'app

## Installation : 

Le projet neccessite deux répertoires différents : 
* Un répertoire pour le front-end
* Un répertoire pour le back-end

### Installation des outils (Linux) : 

#### Java & Maven : 

* Installation de java : `sudo apt install openjdk-8-jdk`

* Télécharger la version de maven 3.9.9 sur le site https://maven.apache.org/download.cgi

* installer maven dans le répertoire /opt : 

`cd ~/Téléchargements`

`unzip apache-maven-3.9.9-bin.zip`

`cd apache-maven-3.9.9-bin/`    

`sudo mv apache-maven-3.9.9 /opt`

* ajouter java & maven au path : 

`gedit ~/.profile`

Ajouter les lignes suivantes en fin de fichier : 

`#java path`

`export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64/"`

`#maven path`

`export MAVEN_HOME="/opt/apache-maven-3.8.8/"`

`export M2_HOME="/opt/apache-maven-3.8.8/"`

`export PATH=${M2_HOME}/bin:${PATH}`

#### Mysql : 

`sudo apt install mysql-server`

`sudo mysql -u root -p`

`CREATE DATABASE yoga;`

`CREATE USER 'user'@'localhost' IDENTIFIED BY 'user';`

`GRANT ALL ON *.* to 'user'@'localhost';`

Réaliser la combinaison CTRL + D, puis : 

`mysql -u root -p yoga < script.sql`

### Installer API back-end : 

Depuis un terminal ou depuis vscode cloner le dossier distant https://github.com/ZaeRon007/Projet5_DevFullStack.git

Monter dans le répertoire : `cd Projet5_DevFullStack/back`

Enfin, compilez l'application : `mvn compile`

### Installer API Front-end : 

Monter dans le répertoire : `cd Projet5_DevFullStack/front`

Enfin, compilez l'application : `npm install`

## Tester les APIs : 

### Tester l'API back-end : 

`cd Projet5_DevFullStack/back`

`mvn clean test`

Pour observer le coverage total, ouvrir le fichier ci-dessous avec un navigateur : `/target/site/jacoco/index.html`

### Tester l'API front-end : 

`cd Projet5_DevFullStack/front`

#### Test unitaire & intégration :

Pour lancer les tests : `./node_modules/jest/bin/jest.js` 
Pour vérifier le coverage ajouter l'option `--collect-coverage`

#### Test end to end : 

Lancer l'API back-end 

Pour lancer les tests : `npm run e2e`. Cypress s'ouvre, il nous indique de choisir un navigateur et une liste de test s'affiche. Le test All.cy.ts regroupe l'ensemble des tests.

Pour observer le coverage des tests end to end : `npm run e2e:coverage`

## Lancer l'application : 

Pour lancer l'application il est nécessaire de démarrer l'API front-end ainsi que le back-end.

### Lancer API back-end : 

`cd Projet5_DevFullStack/back`

`mvn spring-boot:run`

### Lancer API front-end : 

`cd Projet5_DevFullStack/front`

`ng serve`
