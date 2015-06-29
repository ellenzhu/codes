package cc.softwarefactory.lokki.android.espresso.utilities;

import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestsHandle {
    private final int DEFAULT_WAIT_TIMEOUT = 1000;
    private List<RecordedRequest> requests = Collections.synchronizedList(new ArrayList<RecordedRequest>());
    private CountDownLatch requestLatch = new CountDownLatch(1);

    void addRequest(RecordedRequest request) {
        requests.add(request);
        requestLatch.countDown();
    }

    /**
     * Return a read-only list of requests on this RequestHandle.
     */
    public List<RecordedRequest> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    /**
     * Block current thread until there's at least one request on this RequestHandle.
     *
     * @throws InterruptedException if current thread is interrupted
     * @throws TimeoutException if default timeout is reached
     */
    public void waitUntilAnyRequests() throws InterruptedException, TimeoutException {
        boolean reachedZero = requestLatch.await(DEFAULT_WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
        if (!reachedZero) {
            throw new TimeoutException("Timeout reached and still not any requests");
        }
    }
}
