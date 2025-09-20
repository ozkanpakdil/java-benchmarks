package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class NetworkingBenchmark {

    private DatagramSocket server;
    private int port;
    private Thread serverThread;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        server = new DatagramSocket(0, InetAddress.getByName(null)); // loopback
        port = server.getLocalPort();
        serverThread = new Thread(() -> {
            byte[] buf = new byte[256];
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    server.receive(p);
                    // echo back
                    DatagramPacket out = new DatagramPacket(p.getData(), p.getLength(), p.getAddress(), p.getPort());
                    server.send(out);
                }
            } catch (Exception ignored) { }
        }, "udp-echo-server");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        if (serverThread != null) serverThread.interrupt();
        if (server != null) server.close();
    }

    @Benchmark
    public int udp_echo_roundtrip() throws Exception {
        try (DatagramSocket client = new DatagramSocket()) {
            byte[] payload = new byte[] {1,2,3,4,5,6,7,8};
            DatagramPacket req = new DatagramPacket(payload, payload.length, InetAddress.getByName(null), port);
            client.send(req);
            byte[] buf = new byte[32];
            DatagramPacket resp = new DatagramPacket(buf, buf.length);
            client.receive(resp);
            return resp.getLength();
        }
    }
}
