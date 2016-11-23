/**
 * Datum vytvoření: 23.11.2016.
 *
 * @author Martin Matas
 */
public class Aplikace {
    int max_cukru = 5, min_cukru = 0, cukr = 3;
    int p_kelimku = 20;
    int p_vody = 0; //cidlo_vody[0,1]
    int teplota = 0;    //cidlo_teploty[?]
    int poloha_kelimku = 0; // cidlo[0,1]
    int nadrz_plna = 0;  // cidlo_hladiny[0,1]
    String vypis = "";

    int p_smes1 = 20;
    int c_k1 = 20;

    int p_smes2 = 15;
    int c_k2 = 25;

    int p_smes3 = 20;
    int c_k3 = 15;

    int penize = 0; // stav vhozenych penez
    int[] druh_k = {1,2,3};

    // pocty korun v mincovniku
    int p_jedna = 100;
    int p_dva = 80;
    int p_peti = 50;
    int p_deseti = 40;
    int p_dvaceti = 30;
    int p_padesati = 20;
    int[] pen_vrat = {0,0,0,0,0,0}; //[p1,p2,p5,p10,p20,p50] pole pro vraceni

    public static void main(String[] args) {
        
    }
}
