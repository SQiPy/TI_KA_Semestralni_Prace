package ti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Random;
import java.util.regex.Pattern;

public class Tester {
	/* v�stupn� soubor testu */
	static File fileout = new File("vystup.txt");
	/* vstupn� soubor s daty */
	static File file = new File("test.txt");

	Pattern p = Pattern.compile("\\w+");
	static Random r = new Random();

	/**
	 * metoda na z�pis do souboru, pouze se p�ipisuje na konec souboru
	 * 
	 * @param msg
	 *            z�znam na p�ips�n�
	 */
	public static void write(String msg) {
		FileWriter fileWriter;
		BufferedWriter bufferedWriter;
		try {
			fileWriter = new FileWriter(fileout.getAbsoluteFile(), true);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(msg);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * metoda na testov�n� automatu, ze souboru se nastav� hodnoty automatu a
	 * pot� se vytvo�� instance, na kter� je testov�ny data
	 * 
	 * @param file
	 *            soubor s vstupn�my daty
	 */
	public static void cteniSouboru(File file) {
		if (file != null) {
			// nacteni dat
			BufferedReader read;
			try {
				// cteni souboru
				read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
				String radek;
				while ((radek = read.readLine()) != null) {
					String[] splitStr = radek.split(" ");
					int[] cisla = new int[splitStr.length];
					for (int i = 0; i < splitStr.length; i++) {
						cisla[i] = Integer.parseInt(splitStr[i]);
					}
					int p_vody = cisla[0];
					int teplota = 0;
					int poloha_kelimku = 0;
					int nadrz_plna = 0;
					int storno = cisla[1];
					int p_kelimku = cisla[2];
					int p_cukru = cisla[3];
					int p_smes1 = cisla[4];
					int p_jedna = cisla[5];
					int p_dva = cisla[6];
					int p_peti = cisla[7];
					int p_deseti = cisla[8];
					int p_dvaceti = cisla[9];
					int p_padesati = cisla[10];
					int penize_1 = cisla[11];
					int penize_2 = cisla[12];
					int penize_5 = cisla[13];
					int penize_10 = cisla[14];
					int penize_20 = cisla[15];
					int penize_50 = cisla[16];
					int c_k1 = 20;
					int p_smes2 = 10;
					int c_k2 = 15;
					int p_smes3 = 10;
					int c_k3 = 20;
					int druh_k = r.nextInt(2)+1;
					Automat a = new Automat(p_vody, teplota, poloha_kelimku, nadrz_plna, p_kelimku, p_cukru, p_smes1,
							c_k1, p_smes2, c_k2, p_smes3, c_k3, p_jedna, p_dva, p_peti, p_deseti, p_dvaceti, p_padesati,
							penize_1, penize_2, penize_5, penize_10, penize_20, penize_50, storno, druh_k);
					try {
						a.startKA();
					} catch (Exception e) {
						e.printStackTrace();
					}
					a.vypisTest.setLength(a.vypisTest.length() - 2);
					a.vypisTest.append("\n");
					StringBuilder vystup = a.vypisTest;
					write(vystup.toString());
				}
				read.close();
				// osetreni podminek
			} catch (IOException | UncheckedIOException e) {
				System.out.println("Nastala chyba p�i �ten� souboru s daty.");
			}
		}
	}

	public static void main(String[] args) {
		cteniSouboru(file);
	}

}
