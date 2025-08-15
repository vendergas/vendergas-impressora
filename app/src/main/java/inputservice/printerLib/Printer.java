package inputservice.printerLib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import rego.PrintLib.mobilePrinter;

class Printer {
	protected mobilePrinter mobileprint = null;
	public static boolean libStatus = false;
	public static boolean connected = false;
	public static boolean keepConnection = false;
	private String mPrinterName = null;
        public static final String MPT3 = "MPT-III";
        public static final String MPT2 = "MPT-II";
        public static final String INPUTSERVICE = "INPUTSERVICE";
        public static final String A7LIGHT = "Leopardo A7 Light";
        public static final String GSMTP8 = "GS-MTP8";
	public static final String MAC = "00:02:5B";
	public static boolean isA7Light = false;

	public Printer(mobilePrinter mp) {
		if (mp == null) {
			this.mobileprint = new mobilePrinter();
		} else {
			this.mobileprint = mp;
		}
		libStatus = this.mobileprint.LnitLib();
	}

	public Printer() {
		this.mobileprint = new mobilePrinter();
		libStatus = this.mobileprint.LnitLib();
	}

	@SuppressWarnings("unused")
	private boolean connectPrv(String printerName, String macaddress,
			boolean keepC) {
		ArrayList<String> strList;
		strList = this.mobileprint.GetBondedDevices();// não funciona sem esse

		// comando antes
		if (libStatus) {
			if (keepConnection == false) {

				strList = new ArrayList<String>();

				connected = !disconnect();// se desconectou retorna contrário =
											// falso(nao conectado)

				strList.add(0, printerName);
				strList.add(1, macaddress);

				if (this.mobileprint.ConnectDevice(strList, 0)) {
					connected = true;
					keepConnection = keepC;
					return true;
				}
			}
		} else {
			this.mobileprint.LnitLib();
			if (keepConnection == false) {

				strList = new ArrayList<String>();

				connected = !disconnect();// se desconectou retorna contrário =
											// falso(nao conectado)

				strList.add(0, printerName);
				strList.add(1, macaddress);

				if (this.mobileprint.ConnectDevice(strList, 0)) {
					connected = true;
					keepConnection = keepC;
					return true;
				}
			}
		}
		return false;
	}

        /* default MPT-III || MPT-II || INPUTSERVICE || GS-MTP8 */

	// -1 = unknown
	private int knownPrinter(ArrayList<String> strList) {
		int known = -1;

		for (int i = 0; i < strList.size(); ++i) {

			if (0 == i % 2) {
				isA7Light = strList.get(i).equals(Printer.A7LIGHT);

                                if (
                                                strList.get(i).equals(Printer.MPT2)
                                                || strList.get(i).equals(Printer.MPT3)
                                                || strList.get(i).equals(Printer.INPUTSERVICE)
                                                || strList.get(i).equals(Printer.A7LIGHT)
                                                || strList.get(i).equals(Printer.GSMTP8)
                                ) {

					//if (strList.get(i + 1).substring(0, 8).equals(MAC)) {

						known = i;
						return known;
					//}
				}

			}

		}

		return known;

	}

	private boolean knownPrinter(String printerName, String macaddress) {
		boolean known = false;

                if (printerName.equals(Printer.MPT2)
                                || printerName.equals(Printer.MPT3)
                                || printerName.equals(Printer.INPUTSERVICE)
                                || printerName.equals(Printer.GSMTP8)) {

                        if (printerName.equals(Printer.GSMTP8)
                                        || macaddress.substring(0, 8).equals(MAC)) {

                                return true;

                        }
                }

                return known;

        }

	private int knownPrinter(ArrayList<String> strList, String printerName) {
		int known = -1;

		for (int i = 0; i < strList.size(); ++i) {

			if (0 == i % 2) {

                                if (strList.get(i).equals(printerName)) {

                                        if (printerName.equals(Printer.GSMTP8)
                                                        || strList.get(i + 1).substring(0, 8).equals(MAC)) {

                                                known = i;
                                                return known;
                                        }
                                }

			}

		}

		return known;

	}

	public boolean connect(String printerName, String macaddress, boolean keepC) {

		if (knownPrinter(printerName, macaddress)) {
			ArrayList<String> strList;
			strList = this.mobileprint.GetBondedDevices();// não funciona sem
															// esse

			// comando antes

			if (libStatus) {
				this.mobileprint.LnitLib();
			}
			if (keepConnection == false) {

				strList = new ArrayList<String>();

				connected = !disconnect();// se desconectou retorna contrário =
											// falso(nao conectado)

				this.mobileprint = new mobilePrinter();
				this.mobileprint.LnitLib();

				strList.add(0, printerName);
				strList.add(1, macaddress);

				if (this.mobileprint.ConnectDevice(strList, 0)) {
					connected = true;
					keepConnection = keepC;
					return true;
				}

			}
		}
		return false;
	}

	public boolean connectA6(String printerName, String macaddress,
			boolean keepC) {
		ArrayList<String> strList;
		strList = this.mobileprint.GetBondedDevices();// não funciona sem
														// esse
		// comando antes

		if (libStatus) {
			this.mobileprint.LnitLib();
		}
		if (keepConnection == false) {

			strList = new ArrayList<String>();

			connected = !disconnect();// se desconectou retorna contrário =
										// falso(nao conectado)

			strList.add(0, printerName);
			strList.add(1, macaddress);

			if (this.mobileprint.ConnectDevice(strList, 0)) {
				connected = true;
				keepConnection = keepC;
				return true;
			}

		}
		return false;
	}

	public boolean connect(boolean keepC) {
		if (libStatus) {
			this.mobileprint.LnitLib();
		}
		if (keepConnection == false) {
			this.mobileprint = new mobilePrinter();
			this.mobileprint.LnitLib();
			this.mobileprint.GetBondedDevices();

			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			Set<BluetoothDevice> devices = adapter.getBondedDevices();
			ArrayList btList = new ArrayList();

			if (devices.size() > 0) {
				Iterator var4 = devices.iterator();
				while(var4.hasNext()) {
					BluetoothDevice device = (BluetoothDevice)var4.next();
					btList.add(device.getName());
					btList.add(device.getAddress());
				}
			} else {
				Log.d("REGOLIB", "GetBondedDevices --> no pairedDevices");
			}

			int printerMac = knownPrinter(btList);
			if (printerMac != -1) {
				if (mobileprint.ConnectDevice(btList, printerMac)) {
					connected = true;
					keepConnection = keepC;
					return true;
				}
			}

		}
		return false;
	}

	/* by name of the printer/bluetooth device */

	public boolean connect(String printerName, boolean keepC) {
		if (libStatus) {
			this.mobileprint.LnitLib();
		}
		if (keepConnection == false) {
			this.mobileprint = new mobilePrinter();
			this.mobileprint.LnitLib();
			ArrayList<String> strList = mobileprint.GetBondedDevices();

			int printerMac = knownPrinter(strList, printerName);
			if (printerMac != -1) {
				if (mobileprint.ConnectDevice(strList, printerMac)) {
					connected = true;
					keepConnection = keepC;
					return true;
				}
			}

		}
		return false;
	}

	public boolean disconnect() {
		if (connected) {
			mobileprint.CloseConnect();
		}
		keepConnection = false;
		// mobileprint.FreeLib(true);
		return true;
	}

	public String getPrinterName() {
		return mPrinterName;
	}

	public mobilePrinter getMobilePrinter() {
		return mobileprint;
	}

}
