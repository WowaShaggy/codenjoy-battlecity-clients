package clientlib;



public class ClientApp {

    private final static String URL = "ws://dojorena.io:80/codenjoy-contest/ws?user=uc01ezdppwlnq08l65p5&code=6016728173321878399";

    public static void main(String[] args) {
        try {
            WebSocketRunner.run(URL, new SampleSolver());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
