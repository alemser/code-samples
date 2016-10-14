package core.java.concurrency.page;

import java.util.Random;

public class ImageInfo {

	private String id;
	
	public ImageInfo(String id) {
		this.id = id;
	}
	
	public ImageData downloadImage() {
		int wait = new Random().nextInt(5)*1000;
		System.out.println("waiting "+ wait);
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("creating data");
		return new ImageData(("Concluding "+ id + " in " + wait  + " ms").getBytes());
	}
	
}
