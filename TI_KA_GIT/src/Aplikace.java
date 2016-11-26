import java.util.Arrays;

/**
 * Datum vytvoření: 23.11.2016.
 *
 * @author Martin Matas
 */
public class Aplikace {

    public static void main(String[] args) {
        int p_vody = 1;
        int teplota = 0;
        int poloha_kelimku = 0;
        int nadrz_plna = 0;
        int p_kelimku = 10;
        int p_cukru = 10;
        int p_smes1 = 20;
        int c_k1 = 20;
        int p_smes2 = 10;
        int c_k2 = 15;
        int p_smes3 = 20;
        int c_k3 = 20;
        int p_jedna = 100;
        int p_dva = 60;
        int p_peti = 0;
        int p_deseti = 30;
        int p_dvaceti = 20;
        int p_padesati = 0;

        Automat a = new Automat(p_vody,teplota,poloha_kelimku,nadrz_plna,p_kelimku,p_cukru,p_smes1,c_k1,p_smes2,c_k2,p_smes3,c_k3,p_jedna,p_dva,p_peti,p_deseti,p_dvaceti,p_padesati);

        a.vhozeniMince(50);
        a.vhozeniMince(20);
        a.vhozeniMince(50);
        a.vhozeniMince(10);
        System.out.println("Počty mincí v mincovníku: " + Arrays.toString(a.p_minci));
        System.out.println("Kolik peněz je: " + a.penize);
        a.druh_k = 1;
        a.zkontolujVstupy();
        System.out.println("Kolik je po kontrole: " + a.penize);
        a.init_pen_vrat();
        System.out.println("Kolik peněz zbylo: " + a.penize);
        System.out.println("Počty mincí k vrácení: " + Arrays.toString(a.pen_vrat));

    }
    
    //commit
}
