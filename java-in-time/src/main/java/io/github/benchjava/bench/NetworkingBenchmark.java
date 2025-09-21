package io.github.benchjava.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Networking — analogue for the .NET blog's "TLS/HTTP client small improvements" item.
 * <p>
 * The .NET post mentions incremental improvements in HttpClient/TLS that weren't
 * microbenchmarked in detail. This benchmark provides a rough Java analogue using
 * java.net.http.HttpClient over HTTPS to a small resource. It intentionally keeps
 * the work small so overheads like TLS handshake, header parsing, small I/O, etc.,
 * dominate. Note: results will vary with network conditions; prefer running against
 * a local HTTPS endpoint for stability if possible.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class NetworkingBenchmark {

    @Param({"https://example.com/"})
    public String url;

    private HttpClient client;
    private HttpRequest request;

    @Setup(Level.Trial)
    public void setup() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                // Default SSLContext/Parameters from JSSE (TLS 1.3 capable on modern JDKs)
                .build();
        request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
    }

    // Blog-aligned: Uri host extraction with IPv6 literal (CtorHost)
    @Benchmark
    public String CtorHost() {
        return URI.create("http://[2603:1020:201:10::10f]").getHost();
    }

    // Blog-aligned: Uri construction that requires normalization (Ctor)
    @Benchmark
    public URI Ctor() {
        return URI.create("http://some.host.with.ümlauts/");
    }

    // Blog-aligned analogue: read entire response content as byte[] (sync analogue of ReadAsByteArrayAsync)
    @Benchmark
    public int ResponseContentRead_ReadAsByteArrayAsync() throws IOException, InterruptedException {
        HttpResponse<byte[]> resp = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return resp.body() != null ? resp.body().length : 0;
    }

    // Blog-aligned analogue: read response as String (Java lacks headers-only completion option)
    @Benchmark
    public int ResponseHeadersRead_ReadAsStringAsync() throws IOException, InterruptedException {
        HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        return resp.body() != null ? resp.body().length() : 0;
    }

    // Blog-aligned analogue: simple headers add
    @Benchmark
    public int Add() {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://example.com"))
                .header("X-Custom", "Value")
                .build();
        // Return number of header entries to prevent DCE
        return req.headers().map().size();
    }

    // Blog-aligned analogue: get header values
    @Benchmark
    public Object GetValues() {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://example.com"))
                .header("X-Custom", "Value")
                .build();
        return req.headers().allValues("X-Custom");
    }

    // Blog-aligned name (optional in the post): HttpGetSmall
    // Returns body length to prevent DCE.
    @Benchmark
    public int HttpGetSmall() throws IOException, InterruptedException {
        HttpResponse<byte[]> resp = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        // Ensure a successful response; if not, still return the length to keep the work done.
        return resp.body() != null ? resp.body().length : 0;
    }
}
