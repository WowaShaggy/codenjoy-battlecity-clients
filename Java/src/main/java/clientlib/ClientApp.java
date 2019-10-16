package clientlib;



public class ClientApp {

    private final static String URL = "ws://codenjoy.com:80/codenjoy-contest/ws?user=n86uhxw9ri01nj8rm31t&code=2072159192629773949";

    public static void main(String[] args) {
        try {
            WebSocketRunner.run(URL, new SampleSolver());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
