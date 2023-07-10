# Change Log

All notable changes to this project will be documented in this file based on the process of [keepachangelog](https://keepachangelog.com/en/1.0.0/).
This project adheres currently NOT REALLY to [Semantic Versioning](http://semver.org/) despite the formatting of the version numbers.

## [0.1.0] - 2021-11-19

### Added

- added Context diagram
- added sketches
- added Class diagram
- added something else

### Changed

- changed Zeitabrechnung
- changed Entwurf.md to detail our project.

### Deprecated

- deprecated something
- deprecated something else

### Removed

- removed old sketches
- removed something else

### Fixed

- fixed sketch filenames to fit Entwurf.md
- fixed something else

### Security

- made some security relevant changes
- made other security relevant changes

##  [0.2.0] - 2021-12-19

### Added

- added Api Classes for Users, Authentication, Rooms, Furntiture, SearchQuery, SearchTerm (See Class Diagram)
- added persistancy for above mentioned classes
- added API controller for above mentionend classes, execpt SearchTerm
- added DTO objects: RoomCreationDTO, SearhQueryCreationDTO, UserRequestDTO, AuthenticationChangeDTO, FurnitureCreationDTO: All DTO include authentication data for security reasons and links to the parent structure (e.g. Furniture links to user and room with ID)
- added logik to controller: e.g. Deleting a user now deletes all of the users recources too
- Added Frontend vues

### Changed

- changed aplication.properties to use local DB
- changed Class diagram for updated design
- Changed Zeitabrechnung
- Changed Changelod (obvoiusly....)
- 

### Deprecated

- deprecated something
- deprecated something else

### Removed

- removed Cat related APi recources
- removed Cat related tests
- removed something else

### Fixed

- fixed sketch filenames to fit Entwurf.md

### Security

- made some security relevant changes
- made other security relevant changes

##  [0.3.0] - 2022-01-30

### Added

- Added image upload functuon
- Added image recognition function, to recognize type and color of fruniture on the uploeaded image.
- Added funtion to send search requests to restAPI
- Added view to show results of the search request
- Added view to show details of single furniture pieces
- Added search history view
- Added view to modify, create and view rooms
- Added saved furniture view


### Changed

- changed Zeitabrechnung
- changed Readme
- changed documentation
- changed About page

### Removed

- Removed unused/deprecated files

### Fixed

- fixed Definition of Done
- fixed Entwurf.md
- fixed errors that came up while testing

### Security

##  [0.2.0] - 2021-12-19(TEMPLATE for new versions)

### Added


### Changed


### Deprecated


### Removed


### Fixed


### Security


[0.1.0]: https://sopra.informatik.uni-stuttgart.de/sopra-ws21/project17-itestra/-/tree/v0.1/
[0.2.0]: https://sopra.informatik.uni-stuttgart.de/sopra-ws21/project17-itestra/-/tree/v0.2/