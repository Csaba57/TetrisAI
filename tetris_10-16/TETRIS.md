# Tetris Kötelező Program
A feladata a Tetris játékot játszó ágnes implementálása.

A játék során fentről középről lehulló 4 egységből álló alakzatokat kell 
minél pontosabban egymáshoz illeszteni, úgy hogy csak a balra, jobbra és 
forgatás műveleteket hajthatjuk végre. Ha a pályán kialakul legalább egy 
alakzatokkal teljesen kitöltött sor, akkor az eltűnik. A játék addig tart, 
amíg meg nem telik a pálya.

További információ a játék Wikipédia oldalán
[angolul](https://en.wikipedia.org/wiki/Tetris)

## Szabályok
A játék kezdetekor megjelenik középen fent az első alakzat, amelyet 
jobbra, balra mozgatva vagy forgatva tudunk pozicionálni és le is tudjuk 
ejteni. 

Az alakzat folyamatosan esik lefele, az esés gyorsasága szintlépésenként 
növekszik, amíg le nem ér a pálya aljára, vagy rá nem esik egy már a pályán 
lévő elemre. Ekkor egy új alakzat jelenik meg a pálya tetején, amit ugyancsak 
el kell helyeznünk. Ha a pályán kialakul legalább egy teljesen kitöltött 
sor, akkor az eltűnik és a fölötte lévő elemek lejjebb csúsznak.
Az eső alakzat árnyéka látszik a pálya alján (hova érkezne, ha nem mozgatnánk 
azt).

A játék során tudjuk a következő pár elem alakját, valamint hogy egymás 
után nem következik ugyan az a két alakzat.

Kezdetben az alakzatok egy cellányi esése alatt 4 akciót hajthatunk végre, 
amely szintlépésenként csökken egyel, de nem megy egy alá. 10 kirakott 
soronként lépünk egy szintet.

A játék véget ér, ha megtelt a pálya, azaz nem tud új elem megjelenni a 
pálya tetején, mert azt már valamilyen alakzat elfoglalja vagy lejár a 
gondolkodási időnk, vagy 1000 elhelyezett alakzatnál tartunk.

## Pontozás
Minden eltűnt sor esetén pontot kapunk, amely értéke függ az egyszerre 
eltűnő sorok számától (max 4) és a játék szintjétől.

* 1 kirakott sor: szint * 40
* 2 kirakott sor: szint * 100
* 3 kirakott sor: szint * 300
* 4 kirakott sor: szint * 1200

További pontszerzési lehetőség az alakzat leejtésével érhető el, ami esetén 
annyi pontot kapunk, ahány sort esik az alakzat.

## Célok és Paraméterek
* Cél: minél több pont szerzése
* Pálya mérete: `20x10`, azaz `20` sor és `10` oszlop
* Előre látott alakzatok száma: `1`
* Lehetséges akciók:
  * `TetrisGame.LEFT`: balra mozgatás
  * `TetrisGame.RIGHT`: jobbra mozgatás
  * `TetrisGame.ROTATE`: balra forgatás
  * `TetrisGame.DROP`: leejtés
  * `TetrisGame.NONE`: nem csinálunk semmit
* Pálya lehetséges elemei:
  * `TetrisGame.EMPTY`: üres (`. `)
  * `TetrisGame.TAKEN`: kitöltött (`# `)
  * `TetrisGame.GHOST`: árnyék (`/ `)

## Keretrendszer
A megoldást `Java` nyelven kell megvalósítani, egy általunk definiált absztrakt
osztály megvalósítása által (részletek később). Az ehhez szükséges keretrendszer
a coospace felületről letölthető, használatát pedig a továbbiakban részletezzük.

Szükséges a `Java sdk 8` vagy újabb telepítése a fordításhoz és a kiértékeléshez.

### Játék indítása vizualizációs felülettel
* Véletlenszerű akciót választó ágenssel:
``java -jar game_engine.jar 10 game.tetris.TetrisGame 1234567890 1000 1 game.tetris.players.RandomPlayer``

### Paraméterek:
* `10`: debug paraméter (x frames/sec, 0: nincs gui, -x csak konzol kimenet)
* `game.tetris.TetrisGame`: játék logikát megvalósító osztály
* `1234567890`: random seed
* `1000`: rendelkezésre álló összidő (millisec)
* `1`: előre látható alakzatok száma
* `game.tetris.players.RandomPlayer`: a játékot vezérlő osztály

### Saját ágens készítése:
 * Hozzuk létre egy ``SamplePlayer.java`` állományt, a következő tartalommal:
``` java
import java.util.Random;

import game.tetris.TetrisAction;
import game.tetris.TetrisGame;
import game.tetris.TetrisPlayer;
import game.tetris.utils.TetrisState;

public class SamplePlayer extends TetrisPlayer {

  public SamplePlayer(int color, Random random, TetrisState state) {
    super(color, random, state);
  }
  
  @Override
  public TetrisAction getAction(long remainingTime) {
    int action = TetrisGame.actions[random.nextInt(TetrisGame.actions.length)];
    state.apply(action);
    return new TetrisAction(action);
  }
}
```
 * Fordítsuk le a file-t:
``javac -cp game_engine.jar SamplePlayer.java``
 * Értékeljük ki:
``java -jar game_engine.jar 0 game.tetris.TetrisGame 1234567890 1000 1 SamplePlayer``
 * Kimenet az output csatornán:
```sh
logfile: gameplay_xxxxxxxxx.data
0 SamplePlayer 124.0 1000000000
```
 * Egy játék visszanézése a logfile alapján (25fps):
``java -jar game_engine.jar 25 gameplay_xxxxxxxxx.data``

A játék kimenete:

* `0`: játékos azonosító
* `SamplePlayer`: játékos implementáló osztály neve
* `124.0`: elért pontszám
* `10000000000`: megmaradt gondolkodási idő nanomásodpercben

## Kiértékelés
A feladat beadása a coospace-en keresztül történik majd, a beadáshoz egyetlen
java file feltöltése szükséges ami a fentiek szerint a stratégia megvalósítását
tartalmazza. A keretrendszer használ véletlen döntéseket, tehát a random
seed a saját megvalósítás esetleges véletlen döntéseit befolyásolja és attól 
függetlenük a keretrendszerét is.

### Korlátok, határidők, követelmények
* Maximális gondolkodási idő: `1000 ms`
* Maximálisan felhasználható memória: `2G`
* A teljesítéshez legalább `300` pontot kell elérni a `10` játékból legalább `8` esetben
* `10` próbálkozás áll rendelkezésre
* Beküldési határidő: `2026. december 7. 23:59`

A fenti korlátoknak megfelelő futtatási paraméterezés lehet a következő:
``java -Xmx2G -jar game_engine.jar 0 game.tetris.TetrisGame 1234567890 1000 1 SamplePlayer``

A kiértékelés során 10 véletlen inicializáció lesz használva (random seed).

## További követelmények a megoldással szemben
A megoldásnak saját munkának kell lennie. Konzultáció, közös ötletelés megengedett,
de a megvalósítás önálló kell legyen. A megoldást tartalmazó forráskódnak minden
körülmények között ki kell elégítenie a következő követelményeket:

* A megoldás nem állhat előre legyártott lépéssorozat visszajátszásából
* A forráskódot ``Agent.java`` néven kell feltölteni
* A feltöltött forráskódnak le kell fordulnia és hibamentesen le kell futnia
* A feltöltött fájlt az ``iconv -f ascii -c`` paranccsal ASCII-vé konvertáljuk
  a fordítás előtt. Emiatt az ékezetes betűk és minden más nem-ascii karakter
  ki lesznek vágva, tehát jobb ezeket eleve kerülni. Javasolt az UTF8 kódolás.
* A megoldást tartalmazó osztálynak a ``game.tetris.TetrisPlayer``-ből kell
  származnia, ami a keretrendszer részét képezi
* Véletlen számok használata esetén kizárólag az örökölt ``random`` mezőt
  szabad használni, és a seed átállítása tilos
* A megoldást tartalmazó osztálynak részletes magyar osztálydokumentációt kell
  tartalmaznia, javadoc formátumban, illetve a kód dokumentációja is magyar kell,
  hogy legyen
* A kód nem használhat a keretrendszeren kívül semmilyen más osztálykönyvtárat
  (természetesen a JDK osztályain kívül)
* A megoldást tartalmazó osztály nem lehet csomagban
* A megoldásban nem lehet képernyőre írás
* A megoldás nem nyithat meg fájlt, nem indíthat új szálat
* Az implementált metódusoknak minden esetben vissza kell térniük (nem szerepelhet
  benne exit hívás például)
* A forráskód első sorában megadható egy nicknév és egy értesítési emailcím a
  következő formátumban:

    ```java
    ///Nicknevem,Vezeteknev.Keresztnev@stud.u-szeged.hu
    ```
  Ha meg van adva, a nicknév jelenik meg a ranglistában, egyébként pedig a Neptun
  azonosító. Ha meg van adva emailcím, egy tájékoztató emailt küldünk az ágens
  kiértékelése után, mely a ``{DATE}_out.txt`` (a program kimenete), ``{DATE}_log.txt``
  (játék logja), és ``meta.txt`` (eddigi beküldések státusza) állományok elérhetőségét
  tartalmazza. Emailcím megadása nélkül is megtekinthető a ranglistában a pontszám
  és a játék visszajátszható. Lehetőség van arra is, hogy nicknevet ne, csak emailt
  adjunk meg, ebben az esetben az első paramétert üresen kell hagyni, majd a vessző
  után az emailcímet megadni:

    ```java
    ///,Vezeteknev.Keresztnev@stud.u-szeged.hu
    ```
  Az email értesítő esetén érdemes hivatalos egyetemi emailcímet használni.
  (A gmail pl. spam folderbe teheti az értesítést.)
* Fenntartjuk a jogot, hogy bármilyen, fent nem listázott, de az etika szabályai
ellen történő vétséget szankcionáljunk; ha bárkinek kételyei vannak egy konkrét
dologgal kapcsolatban, inkább kérdezzen rá időben.

