package core.java.concurrency.page;

public class ImageData {

	private byte[] data;

	public ImageData(byte[] data) {
		super();
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public static ImageData createIcon(Throwable e) {
		return new ImageData("Error".getBytes());
	}
	
}
