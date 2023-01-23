------------------------------------------------------------------------
Ceci est le fichier LISEZMOI (README) du projet.
------------------------------------------------------------------------

TITRE DU PROJET : Emulateur Traffic
BUT POURSUIVI : Simuler un Traffic simple dans une Ville avec fonctions Edition
VERSION ou DATE : Version 6
COMMENT INVOQUER CE PROJET :
AUTEURS : William Mesnil Anis Menaa
INSTRUCTIONS A USAGER :
Clic Gauche = Placement de Tile
Clic Droit = Suppression de Tile
Le Menu a Gauche propose les options de personalisation de Tile
Le bouton Rand sert a aleatoirement placer une Tile qui se connecte a ses voisins
La Generation des vehicules se fait sur les Tiles connectes au bord de ville

INFO COMPLEMENTAIRES :
Les Vehicules sont representes par des Rectangles pouvant avoir des Dimensions Negatives
Les Proportions du Calcul de la representation Graphique se base a partir du modele de tile 100X100 pixels
Le modele 1000X1000 a ete imagine comme reparti en 10 sections de 100X100 pixels de Largeur et Hauteur
Considerant ces bases, le centre de la Tile fait 34X34 pixels et se place equitablement entre les bords de Tile
Une Voiture fait donc au maximum moins que la largeur et hauteur des routes
Le centre recupere la partie entiere de division par 3 et accorde espace en trop au centre
Les decoupages de Tile getTileCenter, getTileLeft, getTileUp, getTileRight, getTileDown... est fait pour recuperer plus facilement niveau Graphique/Logique
Avec on recupere la route et ses morceaux de route pour affichage et tests de logique

REPRESENTATION DES ROUTES :
O________________
|    |  |  |    |
|    |CU|  |    |
|____|__|__|____|
|    |     | CR |
|____|  C  |____|
| CL |     |    |
|____|_____|____|
|    |  |  |    |
|    |  |CD|    |
|____|__|__|____|
O : gridPosition
CL : Circulation Left
CU : Circulation Up
CR : Circulation Right
CD : Circulation Down

DECOUPAGE DES TILES :
_________________
|    |  |  |    |
|    |UI|UO|    |
|____|__|__|____|
| LO |     | RI |
|____|  C  |____|
| LI |     | RO |
|____|_____|____|
|    |  |  |    |
|    |DO|DI|    |
|____|__|__|____|
C : Center
LI : Left IN
LO : Left OUT
UI : Up IN
UO : Up OUT
RI : Right IN
RO : Right OUT
DI : Down IN
DO : Down OUT

REPRESENTATION DES VOITURES :
O__________
|         |Hei
|_________|ght
   Width
O : carLeftRear

REPRESENTATION DE GRILLE :
La Grille[i][j] avec 0<i<GRID_WIDTH et 0<j<GRID_HEIGHT et X=GRID_WIDTH Y=GRID_HEIGHT
[0,0][i,0][X,0]
[0,j][i,j][X,j]
[0,Y][i,Y][X,Y]

NOTES SUR LA DETECTION COLLISION :
Comme on garde la Position de arriere gauche de Vehicule
Alors on ne peut pas utiliser la Methode Rectangle.intersects(Rectangle R)
Cette Methode de la Classe Rectangle implemente Algorithme standard et suppose
Origine comme coin superieur gauche ce qui ne fonctionne pas avec Dimensions 0 ou negatives

On a donc 3 solutions :
1)Normaliser le Rectangle pour correspondre aux attentes de la Methode
2)Utiliser des Math.min et Math.max pour identifier les Bords du Rectangle
3)Creer des Points pour chaque extremite du Rectangle et utiliser la Methode Rectangle.contains(Point p) sur tous

Ce Cas Implementation de Detection ne se limite pas a Java comme visible sur :
StackOverflow - How do negative sized rectangles intersect

Ainsi on a decide que la solution des Min Max etait plus generale et ajoute la Classe CollisionTools a cet effet
