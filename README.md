# Projet TinyPet

## Introduction

Ce projet a été réalisé par Luca GARIC et Michaël LOPES en M1 MIAGE Classique.<br><br>

L’objectif de ce projet est de réaliser une application Web ressemblant à un site de pétition, c’est-à-dire la création et la signature de pétition.<br><br>

Ce document est divisé en plusieurs sections. Vous trouverez les liens de notre application, les fonctionnalités implémentées, le fonctionnement de l’application, les entités de notre base de données, les temps d’exécution de l’application, les fonctionnalités non implémentées et pour terminer une conclusion.

## Les liens

* Lien vers [l'application](https://projetwebcloud.ew.r.appspot.com/)
* Lien vers le [portail de l'API](https://endpointsportal.projetwebcloud.cloud.goog/)
* Lien vers le [Github](https://github.com/mlopesstage/ProjetWebCloud)

## Fonctionnalités implémentées

Ci-dessous la liste de toutes les implémentations : 
* Créer / Signer / **Fermer (seulement pour l’auteur de la pétition)** une pétition
* Se connecter (avec google oauth2)
* Afficher un classement des meilleures pétitions triées par date la plus récente *(cette page fait office de page d’accueil de l’application)*
* Consulter ses pétitions créées
* Consulter ses pétitions signées
* Afficher le détail d’une pétition
* Se déconnecter avec redirection vers la page d’accueil

## Fonctionnement

Nous avons donc travaillé avec un système d’endpoint dans une classe java. C’est dans cette classe que toutes nos requêtes sont effectuées, nous effectuons ensuite des appels REST dans les différentes vues codées en Mithrils dans notre application.<br>
Nous avons donc utilisé Mithril pour animer notre site. Cependant, nous avons fonctionné de manière à séparer les vues dans différents fichiers HTML et non pas en un fichier unique comme l’exige Mithril. Cette façon de procéder est dûe à notre méconnaissance de la technologie Mithril, nous étions partis très tôt dans le projet dans ce mode de fonctionnement de séparer les affichages en plusieurs vues différentes. Nous avons compris assez tard l’intérêt d’utiliser le framework Mithril.<br>
Concernant le fonctionnement général du site, nous avons tout d’abord mis un point d’honneur à gérer la sécurité, de ce fait nous avons géré le fait qu’un utilisateur déconnecté ne puisse pas accéder à des pages où il est censé être connecté.<br><br>

L’utilisateur déconnecté a donc accès à :
* L’accueil, où il peut consulter le classement des pétitions les plus signées
* Le détail d’une pétition **(cependant, il ne peut pas signer)**

![accueilTopPetition](images_readme/accueilTopPetition.png "accueilTopPetition")