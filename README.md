# A or B
The project is WIP.

It's a pretty simple game where you create a game with a title, and two buttons A or B that you can name whatever you want (f.ex. True and False) and then you can create a variable amount of rounds which are images/videos that are either A or B.

You can then start a game session (which creates a code) with the games that you have created and are saved to your account, and other people can then join the game session with said code.

Technology used:
* Backend: Micronaut (Kotlin), REST and Websocket
* Frontend: React, Typescript
* Auth: Keycloak
* Database: Postgres
* API-Docs: Swagger-UI
* Devops: Nginx, Docker, Terraform

## Environment Secrets
* PROD_AWS_ACCESS_KEY_ID = You can use the keys generated at the beginning, or generate new keys with policy that allows access only to that specific bucket.
* PROD_AWS_SECRET_ACCESS_KEY = Same as above..
* AWS_CLOUDFRONT_DISTRIBUTION_ID = You can get this value in AWS Console > CloudFront.
* BUCKET_NAME

## TODO List
* Frontend
  * Game Session start screen
    * Where the creator can view who has joined and where he can click to start the game sessio
  * Game round screen for the creator
  * Game round screen for the players
  * Game round end result screen for players
  * Game round end result screen for creator
* Backend
  * Rest-API endpoint for creating game session and returning it's ID.
  * Websocket: let the creator join his game session as the creator
  * Websocket: let the creator start the game session
  * Websocket: let the creator skip the remaining time of the round
  * Webscoket: let the creator go to the next round after the result screen
  * Websocket: let the user submit their answer A or B when a round is active
  * Websocket: Supply the user with round and result data in the correct order
* Devops
  * Github actions for testing and deployment
  * Terraform for deployment
  * Kubernetes
  * Docker containers for prod