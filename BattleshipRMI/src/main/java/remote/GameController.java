package remote;

import java.io.Serializable;
import java.rmi.Remote;

public interface GameController extends Remote, Serializable, Runnable {
}
