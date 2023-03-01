package fr.emevel.locallink.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;

public class InetAddressUtils {

    private static InetAddress getAddressFromInterfaces() throws SocketException {
        InetAddress candidateAddress = null;

        Iterable<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

        for (NetworkInterface networkInterface : interfaces) {
            Iterable<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
            for (InetAddress inetAddress : inetAddresses) {
                if (!inetAddress.isLoopbackAddress()) {
                    if (inetAddress.isSiteLocalAddress()) {
                        return inetAddress;
                    } else if (candidateAddress == null) {
                        candidateAddress = inetAddress;
                    }
                }
            }
        }
        // We did not find a site-local address, but we may have found some other non-loopback address.
        // Server might have a non-site-local address assigned to its NIC (or it might be running
        // IPv6 which deprecates the "site-local" concept).
        return candidateAddress;
    }

    /**
     * We use this function because {@link InetAddress#getLocalHost} returns a wrong value based on network interfaces
     * This method list all network interfaces and their addresses until it finds THE correct local address
     * @return THE local address
     * @throws UnknownHostException
     */
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress interfaceAddress = getAddressFromInterfaces();
            if (interfaceAddress != null) {
                return interfaceAddress;
            }

            // We did not find THE local address through iteration. This is weird. We TRY the InetAddress#getLocalHost
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            if (e instanceof UnknownHostException) {
                throw (UnknownHostException) e;
            }
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}
