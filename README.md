## Énoncé

1-	Ajouter la dépendance Maven de Spring Security

2-	Personnaliser la configuration de Spring Security pour ajouter les restrictions suivantes avec la stratégie InMemoryAuthentication :

- Authentification avec le rôle USER pour pouvoir consulter les Patients.

- Authentification avec le rôle ADMIN pour pouvoir Ajouter, Editer, mettre à jour et supprimer des Patients.

- Permettre l’accès aux ressources statiques (/webjars/**) sans aucune authentification.

3-	Basculer de la stratégie InMemoryAuthentication vers JDBCAuthentication

