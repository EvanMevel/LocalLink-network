package fr.emevel.locallink.network.jmdns.server;

import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Represent a thread holding a JmDNSServer instance.
 */
public class JmDNSServerThread {

    private final JmDNSServer jmNDS;
    private final JmDNSRunnable jmDNSRunnable;
    private final Thread jmDNSThread;

    public JmDNSServerThread(int port) {
        this.jmNDS = new JmDNSServer(port);
        this.jmDNSRunnable = new JmDNSRunnable();
        jmDNSThread = new Thread(jmDNSRunnable);
    }

    public void start() {
        jmDNSThread.start();
    }

    public void stop() {
        synchronized (jmDNSRunnable) {
            jmDNSRunnable.notify();
        }
    }

    @NoArgsConstructor
    public class JmDNSRunnable implements Runnable {

        @Override
        public void run() {
            try {
                jmNDS.registerService();
                System.out.println("Started JmNDS server");
                synchronized (this) {
                    this.wait();
                }
                System.out.println("Stopping JmNDS server");
                jmNDS.stop();
                System.out.println("Stopped JmNDS server");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
