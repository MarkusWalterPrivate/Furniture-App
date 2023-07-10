# Möbel App

Die App dient den Suchen und organisieren von Möbeln.
Hierbei wird mittels Algorithmenunterstützung den Nutzer ermöglicht schneller die gewünschten Objekte zu finden.
Dafür können Nutzer sich in der App Räume erstellenen und dort ihre vorhandenen Möbel ablegen. Diese Daten werden dann genutzt um die Suchergebnisse zu verbesser, in dem etwa Möbel mit der gleichen Farbe gewählt werden.


## Installation und Start

Siehe ReadMe in api bzw frontend unterordner

## Verwendung der App

Beschreibung der wichtigsten implementierten Anwendungsfälle


### Wichtiger Anwendungsfall 1: Suche

Um nach Möbeln zu suchen muss der User erst die Möbelart durch auswählen auf dem Dropdownmenü oder durch hichladen/aufnehmen eines Bildes bestimmen.
Daraufhin werden alle Möbel dieser Art angezeigt. Hier kann der User dann genauer bestimmen welche Eigenschaften das gesuchte Möbelstück haben soll. Wobei hier auch ein Bild gewählt werden kann um die gewünschte farbe zu bestimmen.

### Wichtiger Anwendungsfall 2: Registrierung und Accountverwaltung

Durch das klicken auf "Accountverwaltung" in der NavBar wird der User auf die Registrierungs seite geleitet. Hier kann der User sich registrieren, oder sein Account verwalten falls er bereits eingeloggt ist.
Falls der User bereits ein Account hat kann er sich durch das klicken auf "log in" in sein bestehendes Account einloggen.

### Wichtiger Anwendungsfall 3: Suchverlauf

Solange der User eingelogt ist werden alle getätigten Suchen im Suchverlauf gespeichert. Hier kann der User seine vergangenen Suchen einsehen, anpassen, wiederholen und löschen.

### Wichtiger Anwendungsfall 4: Raumverwaltung

Eingelogte User können durch klicken auf "Raumverwaltung" auf das Raum Feature zugreifen. Dieses Feature ermöglicht es dem User eigene Räume anzulegen und diese mit Möbeln zu füllen. Damit ist es dem User möglich nach zu bestimmten Räumen passende Möbel zu suchen (änliches Matterial und Farbe).

### Wichtiger Anwendungsfall 5: Möbel speichern

Der User kann einzelne Möbel aus seinen Suchergebnissen speichern. Durch das klicken auf das Sternsymbol wird das gewählte Möbelstück zu den Favoriten des Users hinzugefügt. Die Favoriten sind als Raum in der Raumverwaltung gespeichert, somit ist es dem User möglich die Raum-Features mit den von ihm gespeicherten Suchergebnissen zu nutzen.

## Changelog

Die Entwicklungsgeschichte befindet sich in [CHANGELOG.md](CHANGELOG.md).