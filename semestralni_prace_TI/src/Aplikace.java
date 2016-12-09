
import java.util.InputMismatchException;
import java.util.Scanner;

public class Aplikace {
    static char stav;
    static int p_vody;
    static int teplota;
    static int poloha_kelimku;
    static int nadrz_plna;
    static int p_kelimku;
    static int p_cukru;
    static int p_smes1;
    static int c_k1;
    static int p_smes2;
    static int c_k2;
    static int p_smes3;
    static int c_k3;
    static int p_jedna;
    static int p_dva;
    static int p_peti;
    static int p_deseti;
    static int p_dvaceti;
    static int p_padesati;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Chcete nastavit interní vstupy ? y/n");
        System.out.println("-------------------------------------------------------");
        do {
            try {
                stav = sc.next().toLowerCase().charAt(0);
                break;
            } catch (InputMismatchException e) {
                System.err.println("Prosím zadejte správnou hodnotu! ");
                sc.next().toLowerCase().charAt(0);
                continue;
            }
        } while (true);

        if (stav == 'n') {
            p_vody = 1;
            teplota = 0;
            poloha_kelimku = 0;
            nadrz_plna = 0;
            p_kelimku = 10;
            p_cukru = 15;
            p_smes1 = 20;
            c_k1 = 20;
            p_smes2 = 10;
            c_k2 = 15;
            p_smes3 = 10;
            c_k3 = 20;
            p_jedna = 100;
            p_dva = 60;
            p_peti = 0;
            p_deseti = 30;
            p_dvaceti = 20;
            p_padesati = 0;
        } else {
            do {
                try {
                    System.out.println("Zadejte zda automat ma přísun vody [0/1]");
                    p_vody = sc.nextInt();
                    System.out.println("Zadejte teplotu vstupní vody:");
                    teplota = sc.nextInt();
                    System.out.println("Zadejte polohu kelimku [0=>v pořádku / 1=>zaseknutý]");
                    poloha_kelimku = sc.nextInt();
                    System.out.println("Zadejte jak je plná nádrž [0=>prázdná / 100=>plná]");
                    nadrz_plna = sc.nextInt();
                    System.out.println("Zadejte počet kelímků");
                    p_kelimku = sc.nextInt();
                    System.out.println("Zadejte počet cukru");
                    p_cukru = sc.nextInt();
                    System.out.println("Zadejte počet směsi 1");
                    p_smes1 = sc.nextInt();
                    System.out.println("Zadejte počet směsi 2");
                    p_smes2 = sc.nextInt();
                    System.out.println("Zadejte počet směsi 3");
                    p_smes3 = sc.nextInt();
                    System.out.println("Zadejte cenu nápoje 1");
                    c_k1 = sc.nextInt();
                    System.out.println("Zadejte cenu nápoje 2");
                    c_k2 = sc.nextInt();
                    System.out.println("Zadejte cenu nápoje 3");
                    c_k3 = sc.nextInt();
                    System.out.println("Zadejte kolik má automat 1 Kč mincí");
                    p_jedna = sc.nextInt();
                    System.out.println("Zadejte kolik má automat 2 Kč mincí");
                    p_dva = sc.nextInt();
                    System.out.println("Zadejte kolik má automat 5 Kč mincí");
                    p_peti = sc.nextInt();
                    System.out.println("Zadejte kolik má automat 10 Kč mincí");
                    p_deseti = sc.nextInt();
                    System.out.println("Zadejte kolik má automat 20 Kč mincí");
                    p_dvaceti = sc.nextInt();
                    System.out.println("Zadejte kolik má automat 50 Kč mincí");
                    p_padesati = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.err.println("Prosím zadejte číslo! ");
                    continue;
                }
            } while (true);
        }

        Automat a = new Automat(p_vody,teplota,poloha_kelimku,nadrz_plna,p_kelimku,p_cukru,p_smes1,c_k1,p_smes2,c_k2,p_smes3,c_k3,p_jedna,p_dva,p_peti,p_deseti,p_dvaceti,p_padesati);

        try {
            a.startKA();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
