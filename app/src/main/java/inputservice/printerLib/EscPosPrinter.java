package inputservice.printerLib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Minimal ESC/POS printer wrapper used to replace the old
 * rego.PrintLib.mobilePrinter dependency.  It exposes a very small
 * subset of the API used by the project and forwards commands
 * directly to a {@link BluetoothSocket}.
 */
public class EscPosPrinter {
    private BluetoothSocket socket;
    private OutputStream outputStream;

    public boolean LnitLib() {
        // Nothing to initialise for the generic layer
        return true;
    }

    public ArrayList<String> GetBondedDevices() {
        ArrayList<String> list = new ArrayList<>();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                list.add(device.getName());
                list.add(device.getAddress());
            }
        }
        return list;
    }

    public boolean ConnectDevice(ArrayList<String> params, int index) {
        if (params.size() <= index + 1) {
            return false;
        }
        String address = params.get(index + 1);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return false;
        }

        BluetoothDevice device = adapter.getRemoteDevice(address);
        adapter.cancelDiscovery();

        UUID spp = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        // Try regular, insecure and reflection based sockets in this order
        try {
            socket = device.createRfcommSocketToServiceRecord(spp);
            socket.connect();
        } catch (IOException primary) {
            closeSilently();
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(spp);
                socket.connect();
            } catch (IOException secondary) {
                closeSilently();
                try {
                    socket = (BluetoothSocket) device.getClass()
                            .getMethod("createRfcommSocket", int.class)
                            .invoke(device, 1);
                    socket.connect();
                } catch (Exception fallback) {
                    closeSilently();
                    return false;
                }
            }
        }

        try {
            outputStream = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            closeSilently();
            return false;
        }
    }

    public void CloseConnect() {
        closeSilently();
    }

    public void Reset() {
        write(new byte[]{0x1B, '@'});
    }

    public void SendString(String data) throws UnsupportedEncodingException {
        if (data == null) return;
        write(data.getBytes("UTF-8"));
    }

    public void SendBuffer(byte[] buffer, int length) {
        if (buffer == null || length <= 0) return;
        byte[] out = buffer;
        if (length < buffer.length) {
            out = new byte[length];
            System.arraycopy(buffer, 0, out, 0, length);
        }
        write(out);
    }

    public void FeedLines(int lines) {
        if (lines <= 0) return;
        write(new byte[]{0x1B, 'd', (byte) lines});
    }

    public void FormatString(boolean bold, boolean underline, boolean doubleHeight,
                             boolean doubleWidth, boolean smallFont) {
        // Bold
        write(new byte[]{0x1B, 0x45, (byte) (bold ? 1 : 0)});
        // Underline
        write(new byte[]{0x1B, 0x2D, (byte) (underline ? 1 : 0)});
        // Font size
        byte size = 0x00;
        if (doubleWidth) size |= 0x20;
        if (doubleHeight) size |= 0x10;
        write(new byte[]{0x1D, 0x21, size});
        // Small font is handled via ESC M command
        write(new byte[]{0x1B, 0x4D, (byte) (smallFont ? 1 : 0)});
    }

    public int QueryPrinterStatus() {
        // Not supported in the generic layer; assume ready
        return 0;
    }

    private void write(byte[] bytes) {
        if (outputStream == null || bytes == null) return;
        try {
            outputStream.write(bytes);
        } catch (IOException ignored) {
        }
    }

    private void closeSilently() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ignored) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
        outputStream = null;
        socket = null;
    }
}
