import Producer.Producer;

public class ProducerMain {
	
	public static void main(String[] args) {
		Producer producer = null;
		try {
			producer = new Producer();
			producer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (producer != null) {
				producer.stop();
			}
		}
		
	}
	
}
