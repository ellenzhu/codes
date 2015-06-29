package cc.softwarefactory.lokki.android.espresso.utilities;

import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class MockDispatcher extends Dispatcher {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_PUT = "PUT";
    public static final String DEFAULT_USER_BASE_PATH = "/user/" + TestUtils.VALUE_TEST_USER_ID + "/";

    private Map<String, MockResponse> responses;
    private Map<String, RequestsHandle> requests;

    /*
     * Response dispatching is done in different thread than installing responses, so there's a
     * potential for race condition – thus a lock here.
     */
    private final Object dispatchLock = new Object();

    public MockDispatcher() throws JSONException {
        this.responses = new HashMap<>();
        this.requests = new HashMap<>();
        setDefaultResponses();
    }

    private void setDefaultResponses() throws JSONException {
        setDashboardResponse(new MockResponse().setBody(MockJsonUtils.getEmptyDashboardJson()));
        setPlacesResponse(new MockResponse().setBody(MockJsonUtils.getEmptyPlacesJson()));
        setUpdateLocationsResponse(new MockResponse().setResponseCode(200));
        setPostLocationResponse(new MockResponse().setResponseCode(200));
        setAllowPostResponse(new MockResponse().setResponseCode(200));
        setGcmTokenResponse(new MockResponse().setResponseCode(200));
    }

    @Override
    public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
        System.out.println("RECORDED REQUEST: " + recordedRequest.toString());
        String key = constructKey(recordedRequest.getMethod(), recordedRequest.getPath());

        synchronized (dispatchLock) {
            if (requests.containsKey(key)) {
                requests.get(key).addRequest(recordedRequest);
            }
            return responses.get(key);
        }
    }

    public RequestsHandle setDashboardResponse(MockResponse response) {
        return installResponse(METHOD_GET, DEFAULT_USER_BASE_PATH + "dashboard", response);
    }

    public RequestsHandle setUpdateLocationsResponse(MockResponse response) {
        return installResponse(METHOD_POST, DEFAULT_USER_BASE_PATH + "update/locations", response);
    }

    public RequestsHandle setPostLocationResponse(MockResponse response) {
        return installResponse(METHOD_POST, DEFAULT_USER_BASE_PATH + "location", response);
    }

    public RequestsHandle setAllowPostResponse(MockResponse response) {
        return installResponse(METHOD_POST, DEFAULT_USER_BASE_PATH + "allow", response);
    }

    public RequestsHandle setGcmTokenResponse(MockResponse response) {
        return installResponse(METHOD_POST, DEFAULT_USER_BASE_PATH + "gcmToken", response);
    }

    public RequestsHandle setPlacesResponse(MockResponse response) {
        return setPlacesResponse(response, METHOD_GET);
    }

    public RequestsHandle setPlacesResponse(MockResponse response, String method) {
        return installResponse(method, DEFAULT_USER_BASE_PATH + "places", response);
    }

    public RequestsHandle setPlacesDeleteResponse(MockResponse response, String placeId) {
        return installResponse(METHOD_DELETE, DEFAULT_USER_BASE_PATH + "places" + "/" + placeId, response);
    }

    public RequestsHandle setPlacesRenameResponse(MockResponse response, String placeId) {
        return installResponse(METHOD_PUT, DEFAULT_USER_BASE_PATH + "places" + "/" + placeId, response);
    }

    public RequestsHandle setSignUpResponse(MockResponse response) {
        return installResponse(METHOD_POST, "/signup", response);
    }

    public RequestsHandle installResponse(String method, String path, MockResponse response) {
        String key = constructKey(method, path);
        RequestsHandle requestsHandle;

        synchronized (dispatchLock) {
            if (!requests.containsKey(key)) {
                requestsHandle = new RequestsHandle();
                requests.put(key, requestsHandle);
            } else {
                requestsHandle = requests.get(key);
            }

            responses.put(key, response);
        }

        return requestsHandle;
    }

    private String constructKey(String method, String path) {
        return method + " " + path;
    }
}
