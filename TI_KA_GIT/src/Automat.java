import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Datum vytvoření: 24.11.2016.
 *
 * @author Martin Matas
 */
public class Automat {
    final int MIN_CUKRU = 0;
    final int CUKR_START_VAL = 3;
    final int MAX_POVOLENO_CUKRU = 5;
    int MAX_CUKRU;

    final int NAPOJ_1 = 1;
    final int NAPOJ_2 = 2;
    final int NAPOJ_3 = 3;

    // inicializuji se pri vytvoreni podle cidla
    int druh_k = -1;
    int cukr; // defaultni hodnota cukru
    int p_vody; //cidlo_vody[0,1]
    int teplota;    //cidlo_teploty[?]
    int poloha_kelimku; // cidlo[0,1]
    int nadrz_plna;  // cidlo_hladiny[0-100]

    // resetuji se
    String vypis;
    int penize; // stav vhozenych penez

    int[] druhy_minci = {1,2,5,10,20,50};
    int[] p_minci;
    int[] pen_vrat; //[p1,p2,p5,p10,p20,p50] pole pro vraceni
    int[] vhozene_mince;

    // zasoby automatu a cena smesi
    int p_kelimku;
    int p_cukru;
    int p_smes1;
    int c_k1;
    int p_smes2;
    int c_k2;
    int p_smes3;
    int c_k3;

    private int vstup = -1;
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public Automat(int p_vody, int teplota, int poloha_kelimku, int nadrz_plna, int p_kelimku, int p_cukru, int p_smes1, int c_k1, int p_smes2, int c_k2, int p_smes3, int c_k3, int p_jedna, int p_dva, int p_peti, int p_deseti, int p_dvaceti, int p_padesati) {
        this.MAX_CUKRU = getMaxCukru(p_cukru);
        this.cukr = getCukrStartVal(p_cukru);

        this.p_vody = p_vody;
        this.teplota = teplota;
        this.poloha_kelimku = poloha_kelimku;
        this.nadrz_plna = nadrz_plna;
        this.p_kelimku = p_kelimku;
        this.p_cukru = p_cukru;

        this.p_smes1 = p_smes1;
        this.c_k1 = c_k1;
        this.p_smes2 = p_smes2;
        this.c_k2 = c_k2;
        this.p_smes3 = p_smes3;
        this.c_k3 = c_k3;

        this.p_minci = new int[6];
        this.p_minci[0] = p_jedna;
        this.p_minci[1] = p_dva;
        this.p_minci[2] = p_peti;
        this.p_minci[3] = p_deseti;
        this.p_minci[4] = p_dvaceti;
        this.p_minci[5] = p_padesati;

        this.vypis = "";
        this.penize = 0; // stav vhozenych penez
        this.pen_vrat = new int[6];//[p1,p2,p5,p10,p20,p50] pole pro vraceni
        this.vhozene_mince = new int[6];
    }

    public void startKA() throws Exception {
        char stav = 'A';
        boolean smesi_skladem = false, kontrola_vstupu = false;

        System.out.println("Nápojový automat byl spuštěn.");

        while(stav != 0) {
            switch (stav) {
                case 'A':
                    getMaxCukru(p_cukru);
                    getCukrStartVal(p_cukru);
                    stav = 'B';
                    break;
                case 'B':
                    if(p_kelimku > 0) {
                        stav = 'C';
                    } else {
                        mimoProvoz();
                        stav = 'A';
                    }
                    break;
                case 'C':
                    if(p_vody == 1) {
                        stav = 'D';
                    } else {
                        mimoProvoz();
                        stav = 'A';
                    }
                    break;
                case 'D':
                    // slou by sem dat rovnou testovani na true a false
                    smesi_skladem = zkontrolujSmesi();
                    stav = 'E';
                    break;
                case 'E':
                    if(smesi_skladem) {
                        System.out.println("-------------------------------------------------------");
                        vypis = "Automat připraven, navod k obsluze:\n 1) Vhazujte mince 1, 2, 5, 10, 20, 50 Kč\n 2) Výši vhozené částky kontrolujte na displeji\n 3) Pro přidání cukru zadejte 61, pro ubrání 60\n 4) Pro zvolení nápoje 1,2 nebo 3 zadejte: 51,52 nebo 53\n 5) Pro storno zadejte 0\n 6) Po výzvě odeberte nápoj";
                        System.out.println(vypis);
                        System.out.println("-------------------------------------------------------");
                        stav = 'F';
                    } else {
                        stav = 'A';
                    }
                    break;
                case 'F':
                    do {
                        while (!br.ready()) {
                            pauza();
                        }
                        try {
                            vstup = Integer.parseInt(br.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Špatný vstup!");
                        }
                    } while (vstup == -1);
                    if(vstup == 51 || vstup == 52 || vstup == 53) {
                        druh_k = vstup%50;
                        kontrola_vstupu = zkontolujVstupy();
                        stav = 'G';
                        break;
                    } else if(vstup == 60) { // plus
                        minusC();
                        stav = 'F';
                        break;
                    } else if(vstup == 61) { // plus
                        plusC();
                        stav = 'F';
                        break;
                    } else if(vstup > 0 && vstup <= 50) {
                        vhozeniMince(vstup);
                        stav = 'F';
                        break;
                    } else if(vstup == 0){
                        storno();
                        stav = 'S';
                        break;
                    } else {
                        System.out.println("Špatný vstup!");
                        stav = 'F';
                        break;
                    }
                case 'G':
                    if(kontrola_vstupu) {
                        stav = 'H';
                    } else {
                        stav = 'F';
                    }
                    break;
                case 'H':
                    init_pen_vrat();
                    stav = 'I';
                    break;
                case 'I':
                    if(penize == 0) {
                        System.out.println("Nápoj se připravuje, vyčkejte prosím");
                        stav = 'J';
                    } else {
                        stav = 'R';
                    }
                    break;
                case 'J':
                    do {
                        while (!br.ready()) {
                            pauza();
                            napoustet();
                            if(nadrz_plna >= 100) break;
                        }
                        if(nadrz_plna >= 100) break;

                        try {
                            vstup = Integer.parseInt(br.readLine());
                        } catch (NumberFormatException e) {
                            e.getMessage();
                        }
                    } while (nadrz_plna < 100 || vstup != 0);
                    if(vstup == 0) {
                        storno();
                        stav = 'S';
                        break;
                    } else {
                        stav = 'K';
                        break;
                    }
                case 'K':
                    do {
                        while (!br.ready()) {
                            pauza();
                            ohrev();
                            if(teplota >= 80) break;
                        }
                        if(teplota >= 80) break;

                        try {
                            vstup = Integer.parseInt(br.readLine());
                        } catch (NumberFormatException e) {
                            e.getMessage();
                        }
                    } while (teplota < 80 || vstup != 0);
                    if(vstup == 0) {
                        storno();
                        stav = 'S';
                    } else {
                        stav = 'L';
                    }
                    break;
                case 'L':
                    vyhoditKelimek();
                    stav = 'M';
                    break;
                case 'M':
                    if(poloha_kelimku == 0) {
                        stav = 'S';
                    } else {
                        stav = 'N';
                    }
                    break;
                case 'N':
                    smichat();
                    stav = 'O';
                    break;
                case 'O':
                    napln();
                    System.out.println("Odeberte nápoj");
                    stav = 'P';
                    break;
                case 'P':
                    vratPenize();
                    stav = 'Q';
                    break;
                case 'Q':
                    reset();
                    // konec
                    //stav = 0;
                    stav = 'A';
                    break;
                case 'R':
                    vypis();
                    stav = 'P';
                    break;
                case 'S':
                    System.out.println("Stornováno");
                    vypustitVodu();
                    stav = 'P';
                    break;
                default:
                    stav = 0;
                    break;
            }
        }
    }

    /**
     * Při vytvoření instance se nastaví maximální možná hodnota cukru na MAX_POVOLENO_CUKRU, pokud není tolik
     * cukru na skladě, nastaví se na hodnotu cukrSkladem
     *
     * @param cukrSkladem - množství cukru (kostek) skladem
     * @return - maximální hranice počtu kostek cukru (max 5) podle zásob cukru
     */
    int getMaxCukru(int cukrSkladem) { // funkce 31
        int maxCukru = MAX_POVOLENO_CUKRU;
        if(cukrSkladem < MAX_POVOLENO_CUKRU) maxCukru = cukrSkladem;

        return maxCukru;
    }

    /**
     * Při vytvoření se nastaví defaultní hodnota kostek cukru na 3, pokud není tolik kostek skadem, upraví se
     * hodnota na hodnotu cukrSkladem
     *
     * @param cukrSkladem - množství cukru (kostek} skladem
     * @return - defaultní hodnota cukru, která se nastaví pro automat a zobrazí na display
     */
    int getCukrStartVal(int cukrSkladem) {
        int cukrDefault = CUKR_START_VAL;
        if(cukrSkladem < CUKR_START_VAL) cukrDefault = cukrSkladem;

        return cukrDefault;
    }

    /**
     * Metoda zkontroluje jestli je na skladě alespoň jedna směs ze všech nabízených
     *
     * @return - směs skladem = true, směs není skladem = false
     */
    public boolean zkontrolujSmesi() { // funkce 32
        if (this.p_smes1 > 0 || this.p_smes2 > 0 || this.p_smes3 > 0) {
            return true;
        } else {
            vypis = "Směsi nejsou dostupné";
            System.out.println(vypis);
            return false;
        }
    }

    /**
     * Metoda zvětší počet kostek cukru, které se přidají do nápoje o jednu, maximálně však na hodnotu MAX_CUKRU
     */
    public void plusC() { // 33
        if ((this.cukr + 1) <= MAX_CUKRU) {
            this.cukr++;
        }
        if (MAX_CUKRU == 0) {
            vypis = "Nedostatek cukru";
            System.out.println(vypis);
        } else {
            vypis = "Cukr: " + cukr;
            System.out.println(vypis);
        }
    }

    /**
     * Metoda zmenší počet kostek, které se přidají do nápoje o jednu, minimálně však na hodnotu MIN_CUKRU
     */
    public void minusC() { // funkce 34
        if((this.cukr - 1) >= MIN_CUKRU) {
            this.cukr--;
        }
        vypis = "Cukr: " + cukr;
        System.out.println(vypis);
    }

    /**
     * Metoda přičte vhozenou minci k celkové sumě, kterou uživatel do automatu už naházel
     *
     * @param mince - mince 1,2,5,10,20 nebo 50
     * @return - je to mince = true, není to mince = false
     */
    public boolean vhozeniMince(int mince) { // funkce 35
        boolean jeMince = false;

        switch(mince) {
            case 1:
                penize += mince;
                p_minci[0]++;
                vhozene_mince[0]++;
                vypis = "Peníze: " + penize;
                System.out.println(vypis);
                jeMince = true;
                break;
            case 2:
                penize += mince;
                p_minci[1]++;
                vhozene_mince[1]++;
                vypis = "Peníze: " + penize;
                System.out.println(vypis);
                jeMince = true;
                break;
            case 5:
                penize += mince;
                p_minci[2]++;
                vhozene_mince[2]++;
                System.out.println("Peníze: " + penize);
                jeMince = true;
                break;
            case 10:
                penize += mince;
                p_minci[3]++;
                vhozene_mince[3]++;
                vypis = "Peníze: " + penize;
                System.out.println(vypis);
                jeMince = true;
                break;
            case 20:
                penize += mince;
                p_minci[4]++;
                vhozene_mince[4]++;
                vypis = "Peníze: " + penize;
                System.out.println(vypis);
                jeMince = true;
                break;
            case 50:
                penize += mince;
                p_minci[5]++;
                vhozene_mince[5]++;
                vypis = "Peníze: " + penize;
                System.out.println(vypis);
                jeMince = true;
                break;
            default:
                vypis = "Není mince";
                System.out.println(vypis);
                break;
        }

        return jeMince;
    }

    /**
     * Metoda podle zvolého nápoje zkontroluje jestli má uživatel dostatek peněz na nápoj a jestli je dostatek směsi
     * pro zvolený nápoj
     *
     * @return vhodil dost peněz a směsi je dostatek = true, nedostatek směsi nebo peněz = false
     */
    public boolean zkontolujVstupy() { // mam zde kontrolovat i smes? bude se kontrolovat uz predtim ... funkce 36
        boolean dostatek_penez = false;

        switch (druh_k) {
            case NAPOJ_1:
                if(penize >= this.c_k1 && this.p_smes1 > 0) {
                    penize -= this.c_k1;
                    dostatek_penez = true;
                } else {
                    if(this.p_smes1 > 0) {
                        vypis = "Nedostatek penez pro nápoj 1";
                    } else {
                        vypis = "Nedostatek smesi pro nápoj 1";
                    }
                    System.out.println(vypis);
                }
                break;
            case NAPOJ_2:
                if (penize >= this.c_k2 && this.p_smes2 > 0) {
                    penize -= this.c_k2;
                    dostatek_penez = true;
                } else {
                    if(this.p_smes2 > 0) {
                        vypis = "Nedostatek penez pro nápoj 2";
                    } else {
                        vypis = "Nedostatek smesi pro nápoj 2";
                    }
                    System.out.println(vypis);
                }
                break;
            case NAPOJ_3:
                if(penize >= this.c_k3 && this.p_smes3 > 0) {
                    penize -= this.c_k3;
                    dostatek_penez = true;
                } else {
                    if(this.p_smes3 > 0) {
                        vypis = "Nedostatek penez pro nápoj 3";
                    } else {
                        vypis = "Nedostatek smesi pro nápoj 3";
                    }
                    System.out.println(vypis);
                }
                break;
            default:
                break;
        }
        return dostatek_penez;
    }

    /**
     * Metoda spočte kolik mincí jaké hodnoty má vrátit za požadovaný nápoj
     *
     * @return automat má na vrácení = true, automat nemá na vrácení = false
     */
    public boolean init_pen_vrat() { // funkce 37
        boolean presne = false;
        int pocet;

        for (int i = (druhy_minci.length - 1); i >= 0 ; i--) {
            pocet = penize/druhy_minci[i];
            if(pocet > p_minci[i]) continue;
            pen_vrat[i] = pocet;
            penize %= druhy_minci[i];
        }

        if(penize == 0) presne = true;

        return presne;
    }

    /**
     * Metoda vypíše informaci mimo provoz
     */
    public void mimoProvoz() { // funce 38
        vypis = "Mimo provoz";
        System.out.println(vypis);
        //vypis = "";
    }

    /**
     * Metoda přeruší program na 250 ms.
     */
    public void pauza() { // funkce 39
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda napustí vodu do nádrže
     */
    public void napoustet() { // funkce 40
        nadrz_plna += 10;
    }

    /**
     * Metoda ukončí prováděnou čínost automatu
     */
    public void storno() { // funkce 41
        System.arraycopy(vhozene_mince,0,pen_vrat,0,pen_vrat.length);
    }

    /**
     * Metoda ohřeje vodu v nádrži
     */
    public void ohrev() { // funkce 42
        teplota += 5;
    }


    /**
     * Metoda vysune kelímek do pozice pro naplnění
     */
    public void vyhoditKelimek() {
        poloha_kelimku = 1;
        p_kelimku--;
    }

    /**
     * Metoda spíchá požadovanou směs a cukr s vodou v nádrži
     */
    public void smichat() {
        switch (druh_k) {
            case NAPOJ_1:
                p_smes1--;
                p_cukru -= cukr;
                break;
            case NAPOJ_2:
                p_smes2--;
                p_cukru -= cukr;
                break;
            case NAPOJ_3:
                p_smes3--;
                p_cukru -= cukr;
                break;
        }
    }

    /**
     * Metoda naplní kelímek hotovým nápojem
     */
    public void napln() {
        nadrz_plna = 0;
    }

    /**
     * Metoda vypustí nádrž s vodou
     */
    public void vypustitVodu() {
        while(nadrz_plna == 0) {
            nadrz_plna -= 10;
        }
    }

    /**
     * Metoda vrací zbylé peníze
     */
    public void vratPenize() {
        for (int i = 0; i < pen_vrat.length; i++) {
            if(pen_vrat[i] == 0) continue;
            System.out.printf("Vracím %dx %d Kč\n",pen_vrat[i],druhy_minci[i]);
            p_minci[i] -= pen_vrat[i];
            pen_vrat[i] = 0;
        }
    }

    /**
     * Metoda vrátí automat do původního stavu
     */
    public void reset() {
        vypis = "";
        penize = 0;

        // inicializuji se pri vytvoreni podle cidla
        druh_k = -1;
        MAX_CUKRU = getMaxCukru(cukr);
        cukr = getCukrStartVal(p_cukru);
        p_vody = 1;
        teplota = 0;
        poloha_kelimku = 0;
        nadrz_plna = 0;

        Arrays.fill(pen_vrat,0);
        Arrays.fill(vhozene_mince,0);
    }

    /**
     * Metoda vypíše informaci
     */
    public void vypis() {
        vypis = "Automat nemá na vrácení";
        System.out.println(vypis);
        System.arraycopy(vhozene_mince,0,pen_vrat,0,pen_vrat.length);
        //vypis = "";
    }
}