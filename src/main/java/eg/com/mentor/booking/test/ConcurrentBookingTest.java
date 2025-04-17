package eg.com.mentor.booking.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentBookingTest {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 5; i++) {
			final int userId = i;
			executor.execute(() -> {
				try {
					String result = bookTicket(userId, LockType.OPTIMISTIC_LOCK);
					System.out.println("Thread " + userId + ": " + result);
				} catch (Exception e) {
					System.out.println("Thread " + userId + " failed: " + e.getMessage());
				}
			});
		}

		executor.shutdown();
	}

	private static String bookTicket(int userId, LockType lockType) throws Exception {
		URL url = getLockTypeURL(lockType);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		String jsonInputString = String.format("{\"firstName\":\"User%d\",\"lastName\":\"Test\"}", userId);

		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}

	private static URL getLockTypeURL(LockType lockType) throws MalformedURLException {
		URL url = null;
		switch (lockType) {
		case PESSIMISTIC_LOCK:
			url = new URL("http://localhost:8080/api/v1/booking/pessimistic/1");
			break;
		case OPTIMISTIC_LOCK:
			url = new URL("http://localhost:8080/api/v1/booking/optimistic/1");
			break;
		default:
			url = new URL("http://localhost:8080/api/v1/booking/no-lock/1");
			break;
		}
		return url;
	}

}
