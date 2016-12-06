package ti;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Gen_dat {
	/* soubor, kam se maj� data vygenerovat*/
	static File file = new File("test.txt");
	/* list s hodnotami kombinac� vhozen�ch pen�z*/
	static ArrayList<int[]> list = new ArrayList<int[]>();
	/* pole povolen�ch minc�*/
	static int[] coins = { 1, 2, 5, 10, 20, 50 };
	/* pole po�t� kombinac�*/
	static int[] counts = new int[coins.length];
	static int genhod = 10;

	static Random r = new Random();

	/**
	 * Metoda na v�po�et v�ech mo�n�ch kombinaci vhozen�ch penez napr 50 k�
	 * 
	 * @param coins
	 *            v�ehny mo�n� mince, ktere lze vhodit
	 * @param counts
	 *            fin�ln� po�et kombinaci
	 * @param startIndex
	 *            za��te�n� index pole
	 * @param totalAmount
	 *            celkov� hodnota
	 */
	public static void naplnPen(int[] coins, int[] counts, int startIndex, int totalAmount) {
		if (startIndex >= coins.length)// we have processed all coins
		{
			int[] uloz = Arrays.copyOf(counts, counts.length);
			list.add(uloz);
			return;
		}

		if (startIndex == coins.length - 1) {
			if (totalAmount % coins[startIndex] == 0) {
				counts[startIndex] = totalAmount / coins[startIndex];

				naplnPen(coins, counts, startIndex + 1, 0);

			}
		} else {
			for (int i = 0; i <= totalAmount / coins[startIndex]; i++) {
				counts[startIndex] = i;
				naplnPen(coins, counts, startIndex + 1, totalAmount - coins[startIndex] * i);
			}
		}
	}
/**
 * metoda na z�pis do souboru, pouze se p�ipisuje na konec souboru
 * @param msg z�znam na p�ips�n�
 */
	public static void write(String msg) {
		FileWriter fileWriter;
		BufferedWriter bufferedWriter;
		try {
			fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(msg);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * hlavn� metoda, kter� generuje vstupn� data do souboru, pro ka�dou
	 * prom�nou proch�z�me alespo� 3 stavy(pro v�echny mo�nosti by byl soubor
	 * p��li� velky a stejn� by se minim�ln� na prvn� p�lce opakoval vystup mimo
	 * provoz) generator postupuje pomoc� for cyklu do dal��ch prom�nn�ch, na
	 * konci se nahodne vygeneruje hodnota vhozen�ch pen�z a simuluje se 10x
	 * vhozen� pen�z, poka�d� s jinou kombinac� vhozen�ch minc�
	 */
	public static void main(String[] args) throws Exception {
		StringBuilder s = new StringBuilder();
		for (int voda = 0; voda <= 1; voda++) {
			String vodav = voda + " ";
			s.append(vodav);
			for (int storno = 0; storno <= 1; storno++) {
				String stornov = storno + " ";
				s.append(stornov);
				for (int p_kelimku = 0; p_kelimku <= 2; p_kelimku++) {
					String p_kelimkuv = p_kelimku + " ";
					s.append(p_kelimkuv);
					for (int p_cukru = 0; p_cukru <= 6; p_cukru = p_cukru + 2) {
						String p_cukruv = p_cukru + " ";
						s.append(p_cukruv);
						for (int p_smesi1 = 0; p_smesi1 <= 2; p_smesi1++) {
							String p_smesi1v = p_smesi1 + " ";
							s.append(p_smesi1v);
							for (int p_jedna = 0; p_jedna <= 102; p_jedna = p_jedna + 34) {
								String p_jednav = p_jedna + " ";
								s.append(p_jednav);
								for (int p_dva = 0; p_dva <= 52; p_dva = p_dva + 26) {
									String p_dvav = p_dva + " ";
									s.append(p_dvav);
									for (int p_peti = 0; p_peti <= 22; p_peti = p_peti + 11) {
										String p_petiv = p_peti + " ";
										s.append(p_petiv);
										for (int p_deseti = 0; p_deseti <= 12; p_deseti = p_deseti + 6) {
											String p_desetiv = p_deseti + " ";
											s.append(p_desetiv);
											for (int p_dvaceti = 0; p_dvaceti <= 7; p_dvaceti = p_dvaceti + 3) {
												String p_dvacetiv = p_dvaceti + " ";
												s.append(p_dvacetiv);
												for (int p_padesati = 0; p_padesati <= 4; p_padesati = p_padesati + 2) {
													String p_padesativ = p_padesati + " ";
													s.append(p_padesativ);
													if (voda == 0 || storno == 1 || p_kelimku == 0 || p_smesi1 == 0) {
														genhod = 2;
													} else {
														genhod = 10;
													}
													for (int peniz = 0; peniz < genhod; peniz++) {
														int penize = r.nextInt(80); // stav
																					// vhozenych
																					// penez
																					// 80
																					// max
														list.clear();
														naplnPen(coins, counts, 0, penize);
														StringBuilder pen = new StringBuilder();
														int index = r.nextInt(list.size());
														for (int poc = 0; poc < list.get(index).length; poc++) {
															pen.append(list.get(index)[poc] + " ");
														}
														String penizv = pen.toString() + "\n";
														s.append(penizv);
														write(s.toString());
														s.setLength(s.length() - penizv.length());
													}
													s.setLength(s.length() - p_padesativ.length());
												}
												s.setLength(s.length() - p_dvacetiv.length());
											}
											s.setLength(s.length() - p_desetiv.length());
										}
										s.setLength(s.length() - p_petiv.length());
									}
									s.setLength(s.length() - p_dvav.length());
								}
								s.setLength(s.length() - p_jednav.length());
							}
							s.setLength(s.length() - p_smesi1v.length());
						}
						s.setLength(s.length() - p_cukruv.length());
					}
					s.setLength(s.length() - p_kelimkuv.length());
				}
				s.setLength(s.length() - stornov.length());
			}
			s.setLength(s.length() - vodav.length());
		}

	}

}
