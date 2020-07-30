# Implementierung Referat und Ausarbeitung WP Computergrafik

## Aufgabenstellung
3.1.4 Eigener Renderer für 3D-Meshes\
Implementierung der Kameratransformation zur Darstellung von 3D-Objekten.
* Eingabe: TriangleMesh und virtuelle Kamera.
* Ausgabe: Rendering des Objektes als Drahtgittermodell auf 2D Zeichenfläche.

### Quellen
* Folien Transformationen
* https://www.youtube.com/watch?v=ih20l3pJoeU

### Teilaufgaben:
* Kameratransformation
* Backface-Culling (nur Flächen, die zur Kamera zeigen) werden gezeichnet
* (optional) Verdeckungsrechnung, dann zusätlichen ⭐
* (optional) Kamerasteuerung (Rotation, Zoom) + Rasterisierung, dann zusätzlichen ⭐

## Beginn
09.06.2020

## Abgabe
31.07.2020

## Step-by-step
###### Ziel
Der Zweck des Renderers ist gegebene 3-dimensionale Daten in 2-dimensionale Formen zu konvertieren. (Projektion)
###### Datenstruktur
Diese Formen (Meshes) sind gruppierte Dreiecke (Polygone),
die aus 3 Kanten (Edges) und 3 Punkten (Vertices) zusammengesetzt sind.\
Da das Dreieck die simpelste 2-dimensionale Form ist, lässt sich mit ihr jede beliebige Form darstellen.
###### Projektion
Da die gegebenen Objekte dreidimensional und die Zeichenfläche zweidimensional ist, müssen wir diese auf
den zweidimensionalen Raum projezieren.
Die Zeichenfläche (`Canvas`) hat eine Breite (``width``) und eine Höhe (`height`). Da 3D-Objekte in allen Größen
und Formen eingelesen werden können, ist es nützlich die Zeichenfläche in einen normalisierten Anzeigeraum zu
überführe (hier: zwischen _-1_ und _1_).\
D.h. nach der Partitionierung befinden sich die Koordinaten (0|0) in der mitte der Fläche.\
Weil sowohl die Breite, als auch die Höhe der Zeichenfläche variieren kann, ist es nötig Objekte innerhalb der
Fläche dementsprechend zu skalieren (Seitenverhältnis / "aspect ratio").\
Den Anzeigeraum zu normalisieren hat einen entscheidenden Vorteil: Objekte innerhalb des Bereiches (zwischen _-1_ und _1_)
werden für den Beobachter korrekt angezeigt und Objekte ausßerhalb müssen nicht angezeigt werden.\
Um das Sichtfeld des menschlichen Auges, das Dinge im Bereich des Bildwinkels ("field of view") erkennt, simulieren
zu können, wird eine zusätzliche Skalierung gebraucht (je weiter gleichgroße Objekte vom Beobachter entfernt sind,
desto kleiner erscheinen sie; oder desto mehr Raum (= mehr Objekte) kann dieser wahrnehmen). Der Winkel (``fieldOfView``)
des Blickfeldes bestimmt hierbei wieviel Raum gesehen werden kann ("zoom")

###### Rendering Pipeline
Anwendung → Geometrie → Rasterung → Zeichenfläche

1 Anwendung
* Änderungen der Szene durch Benutzerinteraktion / Animation
* Aufgaben: Kollisionserkennung, Animation, Datenverwaltung (Octree, Szenengraph, Speicheroptimierung)
* Parallelisieren auf Mehrkernprozessoren
* Primitive (Polygone) werden weitergegeben

2 Geometrie
* Operationen mit Polygonen und deren Eckpunkten

→ Modell- & Kameratransformationen → Beleuchtung → Projektion → Clipping → Window-Viewport-Transformation →\
↑ Objektkoordinaten &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
↑ Kamerakoordinaten &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
↑ Clipping-Koordinaten &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
↑ Flächenkoordinaten

3 Rasterung

4  Zeichenfläche

## Tasks
- [ ] GUI (Renderer)
    - [ ] Selektieren eines Models
        - [ ] Auswählen (FileChooser)
        - [x] Einlesen: ``ObjReader.read(...)``
    - [ ] Steuerung (key input)
    - [ ] [optional] UI (Slider, Parameter, FileChooser usw.)
- [ ] Szene
    - [x] Liste der Polygone
    - [ ] Lichtquellen
- [x] Knoten: ``Vertex``
    - [x] Position (Vektor): ``position``
    - [x] Normale: ``normal``
    - [x] Farbe: ``color``
- [ ] Edge
    - [ ] Punkte(e) (Vertex)
- [x] Dreieck: `Trianlge`
    - [x] Knoten-Indizes
    - [x] Reflektion (Farbe): ``color``
    - [x] Normale: ``normal``
- [x] Dreiecksnetz: ``TriangleMesh``
    - [x] Knoten: ``vertices``
    - [x] Dreiecke: ``triangles``
    - [x] Texturkoordinaten: ``textureCoordinates``
    - [x] Helper: ``TriangleMeshTools``
- [x] Vektor: `Vector2f, Vector3f, Vector4f`
    - [x] Koordinaten: `x, y, z`
    - [x] Länge: `length()`
    - [x] Helper: (Punktprodukt, Kreuzprodukt, usw.)
- [x] Transformationsmatrix
    - [x] Matrix: ``Matrix4f``
- [ ] Tests
- [x] Pipeline Design Pattern (https://medium.com/@deepakbapat/the-pipeline-design-pattern-in-java-831d9ce2fe21)
- [x] Grafik-Pipeline (https://de.wikipedia.org/wiki/Grafikpipeline)
- [ ] Monkey3Engine Features nachbauen

# Base Framework for "Einführung in die Computergrafik"

## Install

IntelliJ Import -> Gradle project

## General usage

### Main Application (used for all exercises) 

* `wpcg.ComputergraphicsApplication`
* for each exercise:
    * create Scene = class implementing interface `wpcg.base.scene`
* set scene in `wpcg.ComputergraphicsApplication.main()`
* Camera
    * Rotate: left mouse button press + move
    * Zoom: Mouse wheel oder middle mouse button + move

### 2D Main Application: 

* `wpcg.ComputergraphicsApplication2D`
* with example content `wpcg.base.canvas2d.Canvas2D` 
            
