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

        System.out.println("Mincí před: " + Arrays.toString(p_minci));
        while(stav != 0) {
            System.out.println("Jsem ve stavu: " + stav);
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
                        stav = 'F';
                    } else {
                        stav = 'A';
                    }
                    break;
                case 'F':
                    do {
                        System.out.println("Vhoďte mince: ");
                        while (!br.ready()) {
                            pauza(250);
                        }
                        vstup = Integer.parseInt(br.readLine());
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
                    } else {
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
                        stav = 'J';
                    } else {
                        stav = 'R';
                    }
                    break;
                case 'J':
                    do {
                        while (!br.ready()) {
                            Thread.sleep(250);
                            napoustet();
                            if(nadrz_plna >= 100) break;
                        }
                        if(nadrz_plna >= 100) break;
                        vstup = Integer.parseInt(br.readLine());
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
                            pauza(250);
                            ohrev();
                            if(teplota >= 80) break;
                        }
                        if(teplota >= 80) break;
                        vstup = Integer.parseInt(br.readLine());
                    } while (nadrz_plna < 100 || vstup != 0);
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
                    stav = 'P';
                    break;
                case 'P':
                    vratPenize();
                    stav = 'Q';
                    break;
                case 'Q':
                    reset();
                    // konec
                    stav = 0;
                    //stav = 'A';
                    break;
                case 'R':
                    vypis("Automat nemá na vrácení");
                    stav = 'P';
                    break;
                case 'S':
                    vypustitVodu();
                    stav = 'P';
                    break;
                default:
                    stav = 0;
                    break;
            }
        }
        System.out.println("Zbytek: " + Arrays.toString(pen_vrat));
        System.out.println("Mincí po: " + Arrays.toString(p_minci));
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
            System.out.println("Směsi nejsou na skladě");
            return false;
        }
    }

    /**
     * Metoda zvětší počet kostek cukru, které se přidají do nápoje o jednu, maximálně však na hodnotu MAX_CUKRU
     */
    public void plusC() { // 33
        if((this.cukr + 1) <= MAX_CUKRU) {
            this.cukr++;
        }
        System.out.println("Cukr: " + cukr);
    }

    /**
     * Metoda zmenší počet kostek, které se přidají do nápoje o jednu, minimálně však na hodnotu MIN_CUKRU
     */
    public void minusC() { // funkce 34
        if((this.cukr - 1) >= MIN_CUKRU) {
            this.cukr--;
        }
        System.out.println("Cukr: " + cukr);
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
                System.out.println("Peníze: " + penize);
                jeMince = true;
                break;
            case 2:
                penize += mince;
                p_minci[1]++;
                vhozene_mince[1]++;
                System.out.println("Peníze: " + penize);
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
                System.out.println("Peníze: " + penize);
                jeMince = true;
                break;
            case 20:
                penize += mince;
                p_minci[4]++;
                vhozene_mince[4]++;
                System.out.println("Peníze: " + penize);
                jeMince = true;
                break;
            case 50:
                penize += mince;
                p_minci[5]++;
                vhozene_mince[5]++;
                System.out.println("Peníze: " + penize);
                jeMince = true;
                break;
            default:
                System.out.println("Není mince");
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
                    System.out.println("Nedostatek penez nebo smesi pro nápoj 1");
                }
                break;
            case NAPOJ_2:
                if (penize >= this.c_k2 && this.p_smes2 > 0) {
                    penize -= this.c_k2;
                    dostatek_penez = true;
                } else {
                    System.out.println("Nedostatek penez nebo smesi pro nápoj 2");
                }
                break;
            case NAPOJ_3:
                if(penize >= this.c_k3 && this.p_smes3 > 0) {
                    penize -= this.c_k3;
                    dostatek_penez = true;
                } else {
                    System.out.println("Nedostatek penez nebo smesi pro nápoj 3");
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
        vypis("Mimo provoz");
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
     * Metoda přeruší program o počet ms
     * @param ms - doba, po kterou se program přeruší
     */
    public void pauza(int ms) {
        try {
            Thread.sleep(ms);
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
    public void vypis(String zprava) {
        vypis = zprava;
        System.out.println(vypis);
        //vypis = "";
    }
}