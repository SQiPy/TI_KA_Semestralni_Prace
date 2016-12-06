package ti;

import java.util.Arrays;

/**
 * Datum vytvoøení: 24.11.2016.
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
	int p_vody; // cidlo_vody[0,1]
	int teplota; // cidlo_teploty[?]
	int poloha_kelimku; // cidlo[0,1]
	int nadrz_plna; // cidlo_hladiny[0-100]

	// resetuji se
	StringBuilder vypisTest;
	String vypis;
	int penize; // stav vhozenych penez

	int[] druhy_minci = { 1, 2, 5, 10, 20, 50 };
	int[] p_minci;
	int[] pen_vrat; // [p1,p2,p5,p10,p20,p50] pole pro vraceni
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

	int penize_1, penize_2, penize_5, penize_10, penize_20, penize_50;
	int storno;
	boolean nahazeno = false;

	public Automat(int p_vody, int teplota, int poloha_kelimku, int nadrz_plna, int p_kelimku, int p_cukru, int p_smes1,
			int c_k1, int p_smes2, int c_k2, int p_smes3, int c_k3, int p_jedna, int p_dva, int p_peti, int p_deseti,
			int p_dvaceti, int p_padesati, int penize_1, int penize_2, int penize_5, int penize_10, int penize_20,
			int penize_50, int storno, int druh_k) {
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

		this.penize_1 = penize_1;
		this.penize_2 = penize_2;
		this.penize_5 = penize_5;
		this.penize_10 = penize_10;
		this.penize_20 = penize_20;
		this.penize_50 = penize_50;
		this.storno = storno;

		this.vypisTest = new StringBuilder();
		this.vypis = "";
		this.penize = 0; // stav vhozenych penez
		this.pen_vrat = new int[6];// [p1,p2,p5,p10,p20,p50] pole pro vraceni
		this.vhozene_mince = new int[6];
		this.druh_k = druh_k;
	}

	public void startKA() throws Exception {
		char stav = 'A';
		boolean smesi_skladem = false, kontrola_vstupu = false;

		// System.out.println("Mincí pøed: " + Arrays.toString(p_minci));
		vypisTest.append("pred: " + Arrays.toString(p_minci) + ", ");
		while (stav != 0) {
			switch (stav) {
			case 'A':
				getMaxCukru(p_cukru);
				getCukrStartVal(p_cukru);
				stav = 'B';
				break;
			case 'B':
				if (p_kelimku > 0) {
					stav = 'C';
				} else {
					mimoProvoz();
					vypisTest.append("!KELIMIKY, ");
					stav = '0';
				}
				break;
			case 'C':
				if (p_vody == 1) {
					stav = 'D';
				} else {
					mimoProvoz();
					vypisTest.append("!VODA, ");
					stav = '0';
				}
				break;
			case 'D':
				// slou by sem dat rovnou testovani na true a false
				smesi_skladem = zkontrolujSmesi();
				stav = 'E';
				break;
			case 'E':
				if (smesi_skladem) {
					stav = 'F';
				} else {
					vypisTest.append("!SMES, ");
					stav = '0';
				}
				break;
			case 'F':
				// vypis = "Automat pøipraven";
				vypisTest.append("pripraven" + ", ");
				for (int i = 0; i < penize_1; i++) {
					vhozeniMince(1);
				}
				for (int i = 0; i < penize_2; i++) {
					vhozeniMince(2);
				}
				for (int i = 0; i < penize_5; i++) {
					vhozeniMince(5);
				}
				for (int i = 0; i < penize_10; i++) {
					vhozeniMince(10);
				}
				for (int i = 0; i < penize_20; i++) {
					vhozeniMince(20);
				}
				for (int i = 0; i < penize_50; i++) {
					vhozeniMince(50);
				}
				vypisTest.append("Penize: " + penize + ", ");
				if (storno == 1) {
					storno();
					stav = 'S';
					break;
				}
				kontrola_vstupu = zkontolujVstupy();
				stav = 'G';
				break;
			case 'G':
				if (kontrola_vstupu) {
					stav = 'H';
				} else {
					stav = '0';
				}
				break;
			case 'H':
				init_pen_vrat();
				stav = 'I';
				break;
			case 'I':
				if (penize == 0) {

					stav = 'J';
				} else {
					stav = 'R';
				}
				break;
			case 'J':
				do {
					napoustet();
					// if(nadrz_plna >= 100) break;
				} while (nadrz_plna <= 100);
				stav = 'K';
				break;
			case 'K':
				do {
					ohrev();
					// if(teplota >= 80) break;
				} while (teplota <= 80);
				stav = 'L';
				break;
			case 'L':
				vyhoditKelimek();
				stav = 'M';
				break;
			case 'M':
				if (poloha_kelimku == 0) {
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
				vypisTest.append("UVARENO, ");
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
				// stav = 'A';
				break;
			case 'R':
				vypis();
				stav = 'P';
				break;
			case 'S':
				vypisTest.append("STORNO, ");
				vypustitVodu();
				stav = 'P';
				break;
			default:
				stav = 0;
				break;
			}
		}
		// System.out.println("Zbytek: " + Arrays.toString(pen_vrat));
		// vypisTest.append("Zbytek: " + Arrays.toString(pen_vrat) + ", ");
		// System.out.println("Mincí po: " + Arrays.toString(p_minci));
		vypisTest.append("Mincí po: " + Arrays.toString(p_minci) + ", ");
	}

	/**
	 * Pøi vytvoøení instance se nastaví maximální možná hodnota cukru na
	 * MAX_POVOLENO_CUKRU, pokud není tolik cukru na skladì, nastaví se na
	 * hodnotu cukrSkladem
	 *
	 * @param cukrSkladem
	 *            - množství cukru (kostek) skladem
	 * @return - maximální hranice poètu kostek cukru (max 5) podle zásob cukru
	 */
	int getMaxCukru(int cukrSkladem) { // funkce 31
		int maxCukru = MAX_POVOLENO_CUKRU;
		if (cukrSkladem < MAX_POVOLENO_CUKRU)
			maxCukru = cukrSkladem;

		return maxCukru;
	}

	/**
	 * Pøi vytvoøení se nastaví defaultní hodnota kostek cukru na 3, pokud není
	 * tolik kostek skadem, upraví se hodnota na hodnotu cukrSkladem
	 *
	 * @param cukrSkladem
	 *            - množství cukru (kostek} skladem
	 * @return - defaultní hodnota cukru, která se nastaví pro automat a zobrazí
	 *         na display
	 */
	int getCukrStartVal(int cukrSkladem) {
		int cukrDefault = CUKR_START_VAL;
		if (cukrSkladem < CUKR_START_VAL)
			cukrDefault = cukrSkladem;

		return cukrDefault;
	}

	/**
	 * Metoda zkontroluje jestli je na skladì alespoò jedna smìs ze všech
	 * nabízených
	 *
	 * @return - smìs skladem = true, smìs není skladem = false
	 */
	public boolean zkontrolujSmesi() { // funkce 32
		if (this.p_smes1 > 0 || this.p_smes2 > 0 || this.p_smes3 > 0) {
			return true;
		} else {
			vypis = "Smìsi nejsou dostupné";
			// System.out.println(vypis);
			vypisTest.append(vypis + ", ");
			return false;
		}
	}

	/**
	 * Metoda zvìtší poèet kostek cukru, které se pøidají do nápoje o jednu,
	 * maximálnì však na hodnotu MAX_CUKRU
	 */
	public void plusC() { // 33
		if ((this.cukr + 1) <= MAX_CUKRU) {
			this.cukr++;
		}
		if (MAX_CUKRU == 0) {
			vypis = "Nedostatek cukru";
			// System.out.println(vypis);
			vypisTest.append(vypis + ", ");
		} else {
			vypis = "Cukr: " + cukr;
			// System.out.println(vypis);
			vypisTest.append(vypis + ", ");
		}
	}

	/**
	 * Metoda zmenší poèet kostek, které se pøidají do nápoje o jednu, minimálnì
	 * však na hodnotu MIN_CUKRU
	 */
	public void minusC() { // funkce 34
		if ((this.cukr - 1) >= MIN_CUKRU) {
			this.cukr--;
		}
		vypis = "Cukr: " + cukr;
		// System.out.println(vypis);
		vypisTest.append(vypis + ", ");
	}

	/**
	 * Metoda pøiète vhozenou minci k celkové sumì, kterou uživatel do automatu
	 * už naházel
	 *
	 * @param mince
	 *            - mince 1,2,5,10,20 nebo 50
	 * @return - je to mince = true, není to mince = false
	 */
	public boolean vhozeniMince(int mince) { // funkce 35
		boolean jeMince = false;

		switch (mince) {
		case 1:
			penize += mince;
			p_minci[0]++;
			vhozene_mince[0]++;
			// vypis = "Peníze: " + penize;
			// System.out.println(vypis);

			jeMince = true;
			break;
		case 2:
			penize += mince;
			p_minci[1]++;
			vhozene_mince[1]++;
			// vypis = "Peníze: " + penize;
			// System.out.println(vypis);

			jeMince = true;
			break;
		case 5:
			penize += mince;
			p_minci[2]++;
			vhozene_mince[2]++;
			// System.out.println("Peníze: " + penize);

			jeMince = true;
			break;
		case 10:
			penize += mince;
			p_minci[3]++;
			vhozene_mince[3]++;
			// vypis = "Peníze: " + penize;
			// System.out.println(vypis);

			jeMince = true;
			break;
		case 20:
			penize += mince;
			p_minci[4]++;
			vhozene_mince[4]++;
			// vypis = "Peníze: " + penize;
			// System.out.println(vypis);
			// vypisTest.append(vypis + ", ");
			jeMince = true;
			break;
		case 50:
			penize += mince;
			p_minci[5]++;
			vhozene_mince[5]++;
			// vypis = "Peníze: " + penize;
			// System.out.println(vypis);

			jeMince = true;
			break;
		default:
			// vypis = "Není mince";
			// System.out.println(vypis);
			break;
		}

		return jeMince;
	}

	/**
	 * Metoda podle zvolého nápoje zkontroluje jestli má uživatel dostatek penìz
	 * na nápoj a jestli je dostatek smìsi pro zvolený nápoj
	 *
	 * @return vhodil dost penìz a smìsi je dostatek = true, nedostatek smìsi
	 *         nebo penìz = false
	 */
	public boolean zkontolujVstupy() { // mam zde kontrolovat i smes? bude se
										// kontrolovat uz predtim ... funkce 36
		boolean dostatek_penez = false;

		switch (druh_k) {
		case NAPOJ_1:
			if (penize >= this.c_k1 && this.p_smes1 > 0) {
				penize -= this.c_k1;
				dostatek_penez = true;
			} else {
				if(p_smes1 > 0){
					vypis = "MALO PENEZ PRO NAPOJ1";
				}else{
					vypis = "MALO SMESI PRO NAPOJ1";
				}
				vypisTest.append(vypis + ", ");
			}
			break;
		case NAPOJ_2:
			if (penize >= this.c_k2 && this.p_smes2 > 0) {
				penize -= this.c_k2;
				dostatek_penez = true;
			} else {
				if(p_smes2 > 0){
					vypis = "MALO PENEZ PRO NAPOJ2";
				}else{
					vypis = "MALO SMESI PRO NAPOJ2";
				}
				vypisTest.append(vypis + ", ");
			}
			break;
		case NAPOJ_3:
			if (penize >= this.c_k3 && this.p_smes3 > 0) {
				penize -= this.c_k3;
				dostatek_penez = true;
			} else {
				if(p_smes3 > 0){
					vypis = "MALO PENEZ PRO NAPOJ3";
				}else{
					vypis = "MALO SMESI PRO NAPOJ3";
				}
				vypisTest.append(vypis + ", ");
			}
			break;
		default:
			break;
		}
		return dostatek_penez;
	}

	/**
	 * Metoda spoète kolik mincí jaké hodnoty má vrátit za požadovaný nápoj
	 *
	 * @return automat má na vrácení = true, automat nemá na vrácení = false
	 */
	public boolean init_pen_vrat() { // funkce 37
		boolean presne = false;
		int pocet;

		for (int i = (druhy_minci.length - 1); i >= 0; i--) {
			pocet = penize / druhy_minci[i];
			if (pocet > p_minci[i])
				continue;
			pen_vrat[i] = pocet;
			penize %= druhy_minci[i];
		}

		if (penize == 0)
			presne = true;

		return presne;
	}

	/**
	 * Metoda vypíše informaci mimo provoz
	 */
	public void mimoProvoz() { // funce 38
		vypis = "Mimo provoz";
		// System.out.println(vypis);
		vypisTest.append(vypis + ", ");
		// vypis = "";
	}

	/**
	 * Metoda pøeruší program na 250 ms.
	 */
	public void pauza() { // funkce 39
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda pøeruší program o poèet ms
	 * 
	 * @param ms
	 *            - doba, po kterou se program pøeruší
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
	 * Metoda ukonèí provádìnou èínost automatu
	 */
	public void storno() { // funkce 41
		System.arraycopy(vhozene_mince, 0, pen_vrat, 0, pen_vrat.length);
	}

	/**
	 * Metoda ohøeje vodu v nádrži
	 */
	public void ohrev() { // funkce 42
		teplota += 5;
	}

	/**
	 * Metoda vysune kelímek do pozice pro naplnìní
	 */
	public void vyhoditKelimek() {
		poloha_kelimku = 1;
		p_kelimku--;
	}

	/**
	 * Metoda spíchá požadovanou smìs a cukr s vodou v nádrži
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
		while (nadrz_plna == 0) {
			nadrz_plna -= 10;
		}
	}

	/**
	 * Metoda vrací zbylé peníze
	 */
	public void vratPenize() {
		vypisTest.append("Vracím: ");
		for (int i = 0; i < pen_vrat.length; i++) {
			if (pen_vrat[i] == 0)
				continue;
			// System.out.printf("Vracím %dx %d
			// Kè\n",pen_vrat[i],druhy_minci[i]);
			vypisTest.append(pen_vrat[i] + "x " + druhy_minci[i] + ",  ");
			p_minci[i] -= pen_vrat[i];
			pen_vrat[i] = 0;
		}
	}

	/**
	 * Metoda vrátí automat do pùvodního stavu
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

		Arrays.fill(pen_vrat, 0);
		Arrays.fill(vhozene_mince, 0);
		vypisTest.append("restart, ");
	}

	/**
	 * Metoda vypíše informaci
	 */
	public void vypis() {
		vypis = "Automat nemá na vrácení";
		// System.out.println(vypis);
		vypisTest.append(vypis + ", ");
		System.arraycopy(vhozene_mince, 0, pen_vrat, 0, pen_vrat.length);
		// vypis = "";
	}
}