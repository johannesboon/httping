package httping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.net.http.HttpClient.Version;

public class HttpPingTest {
	
	private static final String EXPECTED_RESPONSE = "Available\n";
	
	public static void main(String[] args) throws IOException, InterruptedException {

		
		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
		List<Double> responseTimes = new ArrayList<>(10);
		for(int i=0; i<10; i++) {
			Thread.sleep(250);
			long start = System.nanoTime();
			String response = getResponse(client, "https://www.ov-chipkaart.nl/status.txt");
			if(!EXPECTED_RESPONSE.equals(response))
				throw new RuntimeException("Unexpected response: " + response);
			long stop = System.nanoTime();
			responseTimes.add((stop-start)/1_000_000.0);
		}
		
		System.out.println("Response times:");
		responseTimes.stream().forEach(System.out::println);
	}

	private static String getResponse(HttpClient client, String uri) throws IOException, InterruptedException {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.GET()
				.build();
		HttpResponse<String> resp = client.send(req, BodyHandlers.ofString());
		String respBody = resp.body();
		return respBody;
	}

}
