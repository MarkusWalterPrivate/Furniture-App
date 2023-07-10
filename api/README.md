# Spring Boot RESTful API

> This is the user API for the Sopra Itestra Project
> It exposes REST resources under http://localhost:8080/api/v1/.
> See the a list of resources in the Swagger UI (http://localhost:8080/api/v1/swagger-ui) or OpenAPI documentation as JSON (http://localhost:8080/api/v1/api-docs).
NeuesTestPasswort
## Prerequisites

- Install a JDK in at least version 11.
- Make sure that `JAVA_HOME` is set correctly to the root directory of your JDK. You can check with this command: `echo %JAVA_HOME%` (or `echo $JAVA_HOME` on Linux / Git Bash)
- Make sure that the JDK `bin` folder is added to your `PATH`.
- You can also install Maven yourself, but if you use the Maven Wrapper scripts (`mvnw` or `mvnw.cmd`), this is not necessary, since Maven will be downloaded in this case.

## External Prerequisits

- MariaDB running with this configuration:
- Running on local host with port 3306
- Password/User: root
- Can be changed in line 15, 17,18 of application.properties in api/src/main/resources
- Will be changed in later version to use online database

## Usage

```bash
# build and package executable --> appears in target/sopraws20-api.jar
./mvnw clean install

# execute tests only
./mvnw clean test

# generate test coverage report (execute tests first) --> appears in target/site/jacoco/index.html
./mvnw jacoco:report

# build and package executable without running tests
./mvnw clean install -DskipTests

# run the created JAR file --> http://localhost:8080/api/v1/
java -jar ./target/sopraws20-api.jar

# for development: build and run in live-reload mode
./mvnw spring-boot:run
```

## Documentation

- General references: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle
- Application properties: https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html


## API Input/Output examples:
## WARNING: all GET and DELETE now send data like username, password, id via headers or url. Document NOT up to date

> Users


User creation:

- /users POST
- Input of username (5+ chars, unique) and password (10+ chars), output of user with empty roomList and searchQuery list
- Sends Error 400 if constraints not met
- Sends Error 409 (CONFLICT) if user name is already taken

- Request:

```bash
 {  
    "userName": "testUser",
    "password": "123435678990"
}
```

- Response:

```bash
 {
    "id": 54,
   	"authentication": {
    	"id": 53,
    	"userName": "testUser",
    	"password": "123435678990"
    },
    "searchQuerys": [],
    "rooms": []
}
```

User retrieval:

- /authentications/ Post
- input of username, password and id in url. 
- Data must match database
- sends Error 400 for bad requests
- sends error 403 if password, user or id dont fit together
- sends error 404 if user with id cannot be found

- Request: (/user/54)

```bash
 {   
    "userName": "testUser",
    "password": "123435678990"
}
```

- Response: (of newly created user)

```bash
 {
    "id": 54,
   	"authentication": {
    	"id": 53,
        "userName": "testUser",
    	"password": "123435678990"
    },
    "searchQuerys": [],
    "rooms": []
}
```

- Response: (more complex return)

```bash
{
    "id": 92,
    "authentication": {
        "id": 91,
        "userName": "testUser4",
        "password": "changedPassword"
    },
    "searchQuerys": [
        {
            "id": 100,
            "searchTerms": []
        },
        {
            "id": 117,
            "searchTerms": [
                {
                    "id": 115,
                    "searchTerm": "term"
                },
                {
                    "id": 116,
                    "searchTerm": "term2"
                }
            ]
        },
        {
            "id": 120,
            "searchTerms": [
                {
                    "id": 118,
                    "searchTerm": "term3"
                },
                {
                    "id": 119,
                    "searchTerm": "term4"
                }
            ]
        }
    ],
    "rooms": [
        {
            "id": 94,
            "name": "Living room",
            "furnitureItems": [
                {
                    "id": 97,
                    "name": "couch",
                    "color": "black",
                    "material": "leather",
                    "type": "couch"
                }
            ]
        },
        {
            "id": 95,
            "name": "Kitchen",
            "furnitureItems": []
        }
    ]
}
```

User deletion:

- /user/{id} PUT
- deltes user with all recources
- input of username, password and id in url.
- Data must match database
- sends error 400 for bad request
- sends error 403 if password, user or id dont fit together
- sends error 404 if user with id cannot be found

- Request: (user/54)

```bash
 {    
    "userName": "testUser",
    "password": "123435678990"
}
```

- Response Code 200 

>Authentication


Login:

- /authentications/ GET
- input of username and password
- Data must match a user in Database
- sends error 400 for bad request
- sends error 404 if user with matchinf data cannot be found cannot be found
- returns user with all Data

- Request: 

```bash
{ 
    "userName": "testUser4",
    "password": "testPassword"
} 
```

- Response: 

```bash
{
    "id": 92,
    "authentication": {
        "id": 91,
        "userName": "testUser4",
        "password": "changedPassword"
    },
    "searchQuerys": [],
    "rooms": [
        {
            "id": 93,
            "name": "Lvingroom",
            "furnitureItems": []
        }
    ]
}
```

Change Password:

- /authentications/{id} PUT
- input of oldAuthenitcation and new Authentiction each containing userName and password, and user id in URL
- Data of oldAuthentication must match database
- sends error 400 for bad request
- sends error 403 if password, user or id dont fit together
- sends error 404 if user with id cannot be found

- Request: 

```bash
{ 
    "oldAuthentication":{
        "userName": "testUser4",
        "password": "testPassword"
    },
    "newAuthentication" :{
        "userName": "testUser4",
        "password": "changedPassword"
    } 
} 
```

- Response: 

```bash
{
    "id": 91,
    "userName": "testUser4",
    "password": "changedPassword"
}
```



> Rooms


Get all Rooms:

- /rooms GET
- gets all rooms of that user
- requires userId, and authentication
- Data od userID and authentication must match database
- sends error 400 for bad request
- sends error 403 if userId, password or name do not fit together
- sends error 404 if user with ID cannot be found
- returns list of rooms

- Request:

```bash
{ 
    "userId": 92,
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }  
}  
```

- Response:

```bash
[
    {
        "id": 94,
        "name": "Living room",
        "furnitureItems": []
    },
    {
        "id": 95,
        "name": "Kitchen",
        "furnitureItems": []
    }
]
```

Room creation:

- /rooms POST
- creates a room with empty furniture list
- input of userId, room name and authentication of user
- Data of userId and authentication must match database
- sends error 400 for bad request
- sends error 403 if userId, password or name do not fit together
- sends error 404 if user with ID cannot be found

- Request:

```bash
{ 
    "userId": 56,
    "name": "testRoom",
    "authentication": {
        "userName": "testUser",
        "password": "89908571423357687"
    }
    
} 
```

- Response:

```bash
{
    "id": 58,
    "name": "testRoom",
    "furnitureItems": []
}
```

Room editing (update Room name or furniture list):

- /rooms/{id} PUT
- change some of rooms properties
- input of userId, authentication of user and roomName required
- input of FurnitureList optional
- Data of userId and authentication must match database
- sends error 400 for bad request
- sends error 403 if userId, password or name do not fit together
- sends error 404 if user with ID cannot be found or Room with id cannot be found

- Request:

```bash
{ 
   "authentication": {
    "userName": "testUser",
    "password": "89908571423357687"
     },
    "name": "EditRoom",
    "userId":"56"
} 
```

- Response:

```bash
{
    "id": 57,
    "name": "EditRoom",
    "furnitureItems": []
}
```

Room deletion:

- /rooms/{id} DELETE
- used to delte a room with all its associations
- input of username, password, userId and roomId in url.
- Data must match database
- sends error 400 for bad request
- sends error 403 if password, user or id dont fit together
- sends error 404 if user with id cannot be found

- Request: (user/54)

```bash
 { 
    "userName": "testUser",
    "password": "123435678990" 
}
```

- Response Code 200 


>Furniture


Furniture creation:

- /furnitures/ POST
- used to create a new furniture item
- Requires userID, roomID, authentication, color, name, material and type
- userID, roomID and authentication msut match Database
- sends 400 for bad request
- sends 403 if password, user or userId dont fit together
- sends 404 if userId or roomId could not be found

- Request:

```bash
{ 
    "userId": 92,
    "roomId": 94,
    "color" : "black",
    "name" : "test",
    "material": "leather",
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Respsonse:

```bash
{
    "id": 99,
    "name": "test",
    "color": "black",
    "material": "leather",
}
```

Furniture Editing:

- /furnitures/{id} PUT
- used to change furniture settings
- Requires userID, roomID, authentication, color, name, material and type
- userID, roomID and authentication msut match Database
- sends 400 for bad request
- sends 403 if password, user or userId dont fit together
- sends 404 if userId or roomId could not be found

- Request:

```bash
{ 
    "userId": 92,
    "roomId": 94,
    "color" : "green",
    "name" : "changedName",
    "material": "changedleather",
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Response:

```bash
{
    "id": 99,
    "name": "changedName",
    "color": "green",
    "material": "changedleather",
}
```

Furniture Deletion:

- /furnitures/{id} DELETE
- used to delete a single piece of furniture
- Requires authetication with username and password, userID, roomId and furnitureId in URL
- sends 400 for bad requests
- sends 403, if password, username and userID do not fit together
- sends 404 if userId, roomId or furnitureId cannot be found

- Request:

```bash
{ 
    "userId": 92,
    "roomId": 94,
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Response Code 200

> SearchQuerys

Get all SearchQuerys 

- /searchquerys GET
- used to get search history of user
- Requires userId and authentication with userName and password
- sends 400 for bad requests
- sends 403, if password, username and userID do not fit together
- sends 404 if userId cannot be found

- Request:

```bash
{ 
    "userId": 92,
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Response:

```bash
[
    {
        "id": 100,
        "searchTerm": String
    },
    {
        "id": 101,
        "searchTerm": String
    }
]
```

Get a SearchQuerys 

- /searchquerys/{id} GET
- used to get a single search history of user
- Requires userId and authentication with userName and password; searchQueryId in url
- sends 400 for bad requests
- sends 403, if password, username and userID do not fit together
- sends 404 if userId cannot be found

- Request:

```bash
{ 
    "userId": 92,
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Response:

```bash
[
    {
        "id": 100,
        "searchTerm": String
    },
    {
        "id": 101,
        "searchTerm": String
    }
]
```

Post new SearchQuery

- /searchquerys POST
- used to add SearchQuery to user
- Requires userId and authentication with userName and password
- sends 400 for bad requests
- sends 403, if password, username and userID do not fit together
- sends 404 if userId cannot be found

- Request:

```bash
{ 
    "userId": 92,
    "searchTerm": String,
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Response:

```bash
{
    "id": 105,
    "searchTerms": String
}

```

Delete SearchQuery

- /searchquerys/{id} DELETE
- used to delete a single searchQuery
- Requires userId and authentication with userName and password; searchQueryId in URL
- sends 400 for bad requests
- sends 403, if password, username and userID do not fit together
- sends 404 if userId or searchQueryId cannot be found

- Request:

```bash
{ 
    "userId": 92,
    "authentication": {
        "userName": "testUser4",
        "password": "changedPassword"
    }
} 
```

- Response code 200
