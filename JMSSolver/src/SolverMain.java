import Solver.Solver;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.ArrayList;

public class SolverMain {

	public static void main(String[] args) {

		Solver solver = null;

		try {
			solver = new Solver();
			solver.start();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			if (solver != null){
				solver.stop();
			}
		}


	}

}
