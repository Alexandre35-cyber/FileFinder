''
FileFinder
''

Ce logiciel permet une recherche récursive de fichiers par extensions
dans un répertoire. Le résultat s'affiche sous forme de tableau avec des liens cliquables.
Il faut configurer l'editeur par defaut dans /src/main/resources/config.properties

Ce logiciel est configuré pour être utilisé sous Windows. (Chemin de l'explorateur
et de l editeur des fichiers dans le fichier de configuration config.properties)

Les extensions de fichiers non ouverts (binaires) sont configurées dans ce fichier également.

Il faut installer maven et un jdk. 

Le build se lance avec la commande 'mvn clean compile assembly:single'.
Un jar est generé dans le répertoire target, il faut lancer l'application avec la
commande 'java -jar.\target\FileFinder-1.0-SNAPSHOT-jar-with-dependencies.jar'.
