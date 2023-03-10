module locallink.network {
    requires static lombok;
    requires javax.jmdns;
    requires org.apache.commons.collections4;

    exports fr.emevel.locallink.network;
    exports fr.emevel.locallink.network.server;
    exports fr.emevel.locallink.network.packets;
    exports fr.emevel.locallink.network.jmdns;
    exports fr.emevel.locallink.network.jmdns.client;
    exports fr.emevel.locallink.network.jmdns.server;
    exports fr.emevel.locallink.network.serial;
}