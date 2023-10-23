# projet_test_springboot
Montée en compétences Spring boot

Pour executer ce projet les prérequis sont les suivants :
Posséder une BDD mysql (cf log dans application.properties)
Au niveau de la base de données locale, créer un schéma qui s'appel "schema_test" (pas besoin de créer de table Spring boot gère la génération de celles-ci)
Lancer mySql et démarrer l'instance de la base de données (si au lancement de l'instance un message d'erreur apparaît, vérifier que le process MySQL80 est bien démarré dans l'onglet Service du gestionnaire de tâche, si ce n'est pas le cas => clic droit => démarrer)

Télécharger l'IDE Spring Tool Suite et importer le projet
Une fois le projet importé, clic droit sur la classe TestApplication => Run As => Spring boot app
Une fois les actions executées, par défaut, un server locale de BDD utilisera le port 3306 et l'application Spring Boot utiliseras le port 8080

Nb : 
La base de données est vidée à chaque lancement de l'application et quelques données sont générées (cf TestApplication.run())
Il est nécessaire de se loguer pour chaque requête envoyé au server (admin@gmail.com - CHANGE_MOI), sous POSTMAN sous la requête dans Autorization mettre basic auth avec les identifiants de connexions
