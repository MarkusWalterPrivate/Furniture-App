# SoPra WS21/22: Möbel App

Furniture search App that aims to help the users find the furniture they are looking for.
The furniture data was made available by an external API.
This App was developed as part of a partnership between University of Stuttgart and Itestra, where teams of up to three students each competed against each other.
Our two-man team finished in the top 3 with two unique features:
- User data was saved on our API, not client side via cookies /non-persistant storage
  - This allows users to switch devices without loosing data
- Users can configure rooms and add furniture with certain parameters, like color.
  - Users can select a room and ask for recommendations for new furniture. The most used value for each parameter will be 	recommended for the new furniture piece (e.g. the room has two black items and one red, it recommends getting black furniture)    

## Requirements
Each Critical feature was fulfilled, except the optional one. Our room recommendation system was an additional feature, allowing more search parameters to be pre-selected.

### Critical Features
- Furniture search
  - Users can use search parameters, like material or color, as search parameters
  - Searches will be sent to external API
- Display of search results
  - Searches will get displayed in the web application   
  - Parameters used in the search are displayed when the result is displayed
  - A link to an external site (e.g. a shop site) is made available on each furniture piece
- Saving of searches
  - Searches will get saved
  - Users can view and repeat previous searches
  - Users can edit the parameters from previous searches
  - Previous searches can be deleted individually or all at once
- Saving of search results
  - Search results (furniture pieces) will be saved
  - Users can view and delete saved search results
  - Optional: A saved result is linked to the search it was found with
- AI features
  - Users can upload or take pictures
  - At least one search parameter will be prefilled by the AI feature

### Additional Features
- Further search parameters are prefilled by AI
- Use of other user data to recommend search parameters
- Configurator for configurable furniture

## Learnings
While the backend was viewed as the better part of the project, the attempt of implementing a user management system from scratch was misguided. In later projects, Spring Security was used for much better results.

## Usage
The user can specify the furniture he is looking for by typing in the search field, or uploading an image of a similar piece. On mobile devices, it is also possible to take pictures right away.
>The image recognition AI only recognizes the furniture type and color.
>
>Further Usage instructions can be found in the [Template.README.md](./doc/Template.README.md)

### API and Frontend

Please refer to the README.md files in the respective folders for concrete instructions.
