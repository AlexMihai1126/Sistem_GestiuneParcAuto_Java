# Sistem de Gestiune a Parcului Auto
 
Dezvoltarea unui sistem pentru gestionarea unei flote de vehicule, care să permită utilizatorilor să înregistreze, să gestioneze și să urmărească vehiculele disponibile într-un parc auto.

* Utilizați conceptele de programare orientată obiect pentru a defini clasele pentru vehicule, clienți, tranzacții și întreținere.
* Implementați interfețe și abstracțizări pentru a gestiona diferitele tipuri de vehicule și clienți.
* Folosiți colecții Java pentru a administra listele de vehicule disponibile și clienți.
* Implementați o bază de date pentru a stoca informațiile despre vehicule, tranzacții, clienți și întreținere.

Pentru acest proiect, am implementat o interfață grafică cu librăria Java Swing, făcând interacțiunea cu aplicația mult mai simplă decât utilizarea liniei de comandă.

Am început prin implementarea interfețelor ```Client``` și ```Vehicul``` care definesc un comportament comun al claselor ce le implementează - ```ClientPersFizica``` și ```ClientPersJuridica``` implementează prima interfață, iar ```Masina``` si ```Autoutilitara``` pe a doua.

Am mai creat două clase, ```Tranzactie``` și ```Intretinere``` pentru a stoca datele necesare.

Datele sunt stocate în memorie utilizând liste (```ArrayList```), iar fiecare tip de obiect are un manager care se ocupă de actualizarea listelor din memorie și salvarea obiectelor în baza de date (făcând apeluri la clasa ```DbManager``` care se ocupă de conexiunea cu baza de date de tipul PostgreSQL, este o clasă Singleton deoarece vrem o singură instanță în aplicație), iar operațiile CRUD sunt implementate pentru fiecare tip de manager în parte. Managerele sunt și ele niște clase de tip Singleton care ne asigură faptul că putem avea doar câte un singur manager din fiecare tip per aplicație).

Interfața grafică are taburi ce ne permit navigarea între tipurile de obiecte - ambele tipuri de clienți, ambele tipuri de vehicule, tranzacții și întreținere. În fiecare panou avem un layout similar, ce ne permite să introducem obiecte noi completând câmpurile cu datele necesare (acestea fac și validarea datelor - în caz contrar aplicația ne indică ce nu este corect și nu face salvarea obiectului nou până la remedierea erorilor de validare). Datele sunt afișate sub formă de tabel, selectarea unui rând ne permite să apăsăm pe butonul de ștergere din partea de sus (dacă nu avem un rând selectat apare un mesaj de eroare), iar făcând click pe o celulă din tabel putem modifica datele (acestea se scriu în timp real în baza de date), dar numai după ce se face validarea (în caz contrar nu se va face nicio scriere). 
