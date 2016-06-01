package com.avorona.helper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created by avorona on 29.04.16.
 */
public class IpHelper {
    private static final Pattern IP4_PATTERN = Pattern.compile("[0-9]{1,4}\\.([0-9]{1,4}\\.){2}[0-9]{1,4}");

    public static String ipForIface(String iface) throws SocketException, NoSuchInterfaceException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface i = networkInterfaces.nextElement();
            if (iface.equals(i.getName())) {
                Enumeration<InetAddress> addrs = i.getInetAddresses();
                while (addrs.hasMoreElements()){
                    InetAddress addr = addrs.nextElement();
                    if (IP4_PATTERN.matcher(addr.getHostAddress()).matches()) {
                        return addr.getHostAddress();
                    }
                }
            }
        }
        throw new NoSuchInterfaceException("No interface with name: " + iface);
    }
}
