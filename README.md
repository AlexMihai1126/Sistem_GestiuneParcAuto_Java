# Sistem de gestiune a Parcului Auto în limbajul Java

Dezvoltarea unui sistem pentru gestionarea unei flote de vehicule, care să permită utilizatorilor să înregistreze, să gestioneze și să urmărească vehiculele disponibile într-un parc auto.

* Utilizați conceptele de programare orientată obiect pentru a defini clasele pentru vehicule, clienți, tranzacții și întreținere.
* Implementați interfețe și abstracțizări pentru a gestiona diferitele tipuri de vehicule și clienți.
* Folosiți colecții Java pentru a administra listele de vehicule disponibile și clienți.
* Implementați o bază de date pentru a stoca informațiile despre vehicule, tranzacții, clienți și întreținere.

## Descrierea proiectului

Pentru acest proiect, am implementat o interfață grafică utilizând librăria Java Swing, ceea ce face interacțiunea cu aplicația mult mai intuitivă și mai ușor de utilizat în comparație cu linia de comandă.

### Structura proiectului

1. **Interfețe și clase**
   * Interfețele `Client` și `Vehicul` definesc comportamentul comun al claselor care le implementează. 
     * `ClientPersFizica` și `ClientPersJuridica` implementează interfața `Client`.
     * `Masina` și `Autoutilitara` implementează interfața `Vehicul`.
   * Clasele `Tranzactie` și `Intretinere` sunt folosite pentru a stoca datele tranzacțiilor și a întreținerii.

2. **Managementul datelor**
   * Datele sunt stocate în memorie utilizând colecții de tipul `ArrayList`.
   * Fiecare tip de obiect are un manager dedicat care se ocupă de actualizarea listelor în memorie și salvarea obiectelor în baza de date utilizând `PreparedStatement`. 
     * Clasele de tip `Manager` apelează clasa `DbManager` pentru gestionarea conexiunii la baza de date PostgreSQL.
     * `DbManager` este o clasă de tip Singleton, garantând existența unei singure instanțe în aplicație.
     * Clasele `Manager` sunt și ele de tip Singleton, din același motiv ca la `DbManager`.
   * Operațiile `CRUD (Create, Read, Update, Delete)` sunt implementate în fiecare manager, asigurând gestionarea datelor.

3. **Interfața grafică**
   * Interfața grafică este organizată folosind tab-uri, facilitând navigarea între diferitele tipuri de obiecte (clienți, vehicule, tranzacții și întreținere).
   * Fiecare panou are un layout similar, permițând utilizatorului să adauge noi obiecte prin completarea câmpurilor necesare.
     * Validarea datelor este realizată la introducere; în caz de erori, utilizatorul este notificat și nu se realizează salvarea până la corectarea acestora.
   * Datele sunt afișate sub formă de tabel:
     * Selectarea unui rând permite ștergerea acestuia printr-un buton dedicat.
     * Modificările pot fi realizate prin click pe o celulă, completarea cu datele noi, iar actualizarea se face în timp real, după validare.
