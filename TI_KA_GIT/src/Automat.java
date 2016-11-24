/**
 * Datum vytvoření: 24.11.2016.
 *
 * @author Martin Matas
 */
public class Automat {
    final int MIN_CUKRU = 0;
    final int CUKR_START_VAL = 3;
    final int MAX_POVOLENO_CUKRU = 5;
    final int MAX_CUKRU;

    final int NAPOJ_1 = 1;
    final int NAPOJ_2 = 2;
    final int NAPOJ_3 = 3;

    // inicializuji se pri vytvoreni podle cidla
    int druh_k;
    int cukr; // defaultni hodnota cukru
    int p_vody = 1; //cidlo_vody[0,1]
    int teplota = 0;    //cidlo_teploty[?]
    int poloha_kelimku = 0; // cidlo[0,1]
    int nadrz_plna = 0;  // cidlo_hladiny[0,1]

    // resetuji se
    String vypis;
    int penize; // stav vhozenych penez
    int[] pen_vrat; //[p1,p2,p5,p10,p20,p50] pole pro vraceni

    // zasoby automatu a cena smesi
    int p_kelimku;
    int p_cukru;
    int p_smes1;
    int c_k1;
    int p_smes2;
    int c_k2;
    int p_smes3;
    int c_k3;
    // pocty korun v mincovniku
    int[] p_minci;
    /*int p_jedna;
    int p_dva;
    int p_peti;
    int p_deseti;
    int p_dvaceti;
    int p_padesati;*/

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
    }

    /**
     * Při vytvoření instance se nastaví maximální možná hodnota cukru na MAX_POVOLENO_CUKRU, pokud není tolik
     * cukru na skladě, nastaví se na hodnotu cukrSkladem
     *
     * @param cukrSkladem - množství cukru (kostek) skladem
     * @return - maximální hranice počtu kostek cukru (max 5) podle zásob cukru
     */
    private int getMaxCukru(int cukrSkladem) {
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
    private int getCukrStartVal(int cukrSkladem) {
        int cukrDefault = CUKR_START_VAL;
        if(cukrSkladem < CUKR_START_VAL) cukrDefault = cukrSkladem;

        return cukrDefault;
    }

    /**
     * Metoda zkontroluje jestli je na skladě alespoň jedna směs ze všech nabízených
     *
     * @return - směs skladem = true, směs není skladem = false
     */
    public boolean zkontrolujSmesi() {
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
    public void plusC() {
        if((this.cukr + 1) <= MAX_CUKRU) {
            this.cukr++;
        }
    }

    /**
     * Metoda zmenší počet kostek, které se přidají do nápoje o jednu, minimálně však na hodnotu MIN_CUKRU
     */
    public void minusC() {
        if((this.cukr - 1) >= MIN_CUKRU) {
            this.cukr--;
        }
    }

    /**
     * Metoda přičte vhozenou minci k celkové sumě, kterou uživatel do automatu už naházel
     *
     * @param mince - mince 1,2,5,10,20 nebo 50
     * @return - je to mince = true, není to mince = false
     */
    public boolean vhozeniMince(int mince) {
        boolean jeMince = false;

        switch(mince) {
            case 1:
                penize += mince;
                p_minci[0]++;
                jeMince = true;
                break;
            case 2:
                penize += mince;
                p_minci[1]++;
                jeMince = true;
                break;
            case 5:
                penize += mince;
                p_minci[2]++;
                jeMince = true;
                break;
            case 10:
                penize += mince;
                p_minci[3]++;
                jeMince = true;
                break;
            case 20:
                penize += mince;
                p_minci[4]++;
                jeMince = true;
                break;
            case 50:
                penize += mince;
                p_minci[5]++;
                jeMince = true;
                break;
            default:
                System.out.println("Není mince");
                break;
        }

        return jeMince;
    }

    public boolean zkontolujVstupy() {
        return (
                (druh_k == NAPOJ_1 && penize >= this.c_k1 && this.p_smes1 > 0) ||
                (druh_k == NAPOJ_2  && penize >= this.c_k2 && this.p_smes2 > 0) ||
                (druh_k == NAPOJ_3 && penize >= this.c_k3 && this.p_smes3 > 0)
        );
    }

    public boolean init_pen_vrat() {
        boolean presne = false;
        int[] druhy_minci = {1,2,5,10,20,50};
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
}
