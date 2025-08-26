package inputservice.printerLib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/*
 * The original implementation relied on the proprietary
 * rego.PrintLib.mobilePrinter library.  A new generic
 * ESC/POS implementation is provided in {@link EscPosPrinter}
 * which communicates directly with the Bluetooth socket and
 * emits raw ESC/POS commands.
 */

//import rego.PrintLib.mobilePrinter;
// Replaced by our own generic layer

// Generic ESC/POS printer implementation provides a minimal API
// compatible with the old mobilePrinter class to ease the transition.

class Printer {
        protected EscPosPrinter mobileprint = null;
	public static boolean libStatus = false;
	public static boolean connected = false;
	public static boolean keepConnection = false;
	private String mPrinterName = null;
	public static final String MPT3 = "MPT-III";
	public static final String MPT2 = "MPT-II";
	public static final String INPUTSERVICE = "INPUTSERVICE";
	public static final String A7LIGHT = "Leopardo A7 Light";
	public static final String MAC = "00:02:5B";
	public static boolean isA7Light = false;

        public Printer(EscPosPrinter mp) {
                if (mp == null) {
                        this.mobileprint = new EscPosPrinter();
                } else {
                        this.mobileprint = mp;
                }
                libStatus = this.mobileprint.LnitLib();
        }

        public Printer() {
                this.mobileprint = new EscPosPrinter();
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

        /* The original implementation restricted the list of printers to
         * a handful of known devices and checked for a specific MAC
         * address prefix.  In order to support any generic ESC/POS
         * printer we now simply expose the list of paired devices and
         * let the caller decide which one to use.  The helper methods
         * below therefore no longer enforce name or MAC filters. */

        private boolean knownPrinter(String printerName, String macaddress) {
                // Any printer is considered valid now
                return true;
        }

        private int knownPrinter(ArrayList<String> strList, String printerName) {
                for (int i = 0; i < strList.size(); i += 2) {
                        if (strList.get(i).equals(printerName)) {
                                return i;
                        }
                }
                return -1;
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

                                this.mobileprint = new EscPosPrinter();
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
                        this.mobileprint = new EscPosPrinter();
                        this.mobileprint.LnitLib();
                        ArrayList<String> btList = getPairedDevices();

                        // Try each paired device until a connection succeeds
                        for (int i = 0; i < btList.size(); i += 2) {
                                ArrayList<String> candidate = new ArrayList<>();
                                candidate.add(btList.get(i));
                                candidate.add(btList.get(i + 1));
                                if (mobileprint.ConnectDevice(candidate, 0)) {
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
                        this.mobileprint = new EscPosPrinter();
                        this.mobileprint.LnitLib();
                        ArrayList<String> strList = getPairedDevices();

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

        public EscPosPrinter getMobilePrinter() {
                return mobileprint;
        }

        /**
         * Returns a flat ArrayList with pairs of [name, address] for all
         * bonded Bluetooth devices.  This allows callers to present the
         * available printers to the user and choose one manually.
         */
        public ArrayList<String> getPairedDevices() {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                ArrayList<String> btList = new ArrayList<>();
                if (adapter != null) {
                        Set<BluetoothDevice> devices = adapter.getBondedDevices();
                        Iterator<BluetoothDevice> it = devices.iterator();
                        while (it.hasNext()) {
                                BluetoothDevice device = it.next();
                                btList.add(device.getName());
                                btList.add(device.getAddress());
                        }
                }
                if (btList.isEmpty()) {
                        Log.d("REGOLIB", "GetBondedDevices --> no pairedDevices");
                }
                return btList;
        }

}
