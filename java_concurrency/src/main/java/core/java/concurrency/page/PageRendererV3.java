package core.java.concurrency.page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Simulation of a page rendering, including images, using Futures and the CompletableFuture.
 * This sample takes advantage of completable future in order to render images in a no callback.
 * The sample is completely non blocking.
 * Based on article: https://www.infoq.com/articles/Functional-Style-Callbacks-Using-CompletableFuture.
 */
public class PageRendererV3 {
	
	static final int MAX_DOWNLOADS = 10;
	
	public void renderPage(CharSequence source) {
		List<ImageInfo> info = scanForImageInfo(source);
		info.forEach(imageInfo-> CompletableFuture.supplyAsync(imageInfo::downloadImage).thenAccept(this::renderImage));
		renderText(source);		
	}
	
	private List<ImageInfo> scanForImageInfo(CharSequence source) {
		ArrayList<ImageInfo> infos = new ArrayList<>();
		for (int i = 0; i < MAX_DOWNLOADS; i++) {
			infos.add(new ImageInfo("ID"+i));
		}
		return infos;
	}
	
	public void renderText(CharSequence source) {
		System.out.println("Text: " + source);
	}
	
	public String renderImage(ImageData id) {
		System.out.println(new String(id.getData()));
		return new String(id.getData());
	}
	
	public void onComplete() throws Throwable {
		System.out.println("Finish..");
	}
	
	public static void main(String[] args) throws Exception {
		new PageRendererV3().renderPage("The web page");
		Thread.currentThread().join(20000);
	}

}
