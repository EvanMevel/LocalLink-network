package fr.emevel.locallink.network;

import javax.jmdns.JmDNS;
import java.io.IOException;
import java.net.InetAddress;

public class LinkJmdns {

    public static final String SERVICE_TYPE = "_locallink._tcp.local.";

    public static JmDNS initJmdnsLocal() throws IOException {
        InetAddress localHost = InetAddressUtils.getLocalHostLANAddress();
        return JmDNS.create(localHost, localHost.getHostName());
    }

}
