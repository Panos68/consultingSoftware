## Authentication 
We're using OAuth2 with Google as the service provider.

#### LOGIN (this endpoint is created automatically).
GET /oauth2/authorization/google

#### LOGOUT (this endpoint is created automatically).
POST /logout

#### USER ROLES
The first time a user logs in we check if the user already exists in the db (using email adress). 
If it doesn't exist we create a new user and assign it role "user".
To give a user admin rights we use the endpoint:
PUT /user/{id} with a UserDTO in the request body, eg:

{}