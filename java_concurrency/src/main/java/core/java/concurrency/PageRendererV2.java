package core.java.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import core.java.concurrency.page.ImageData;
import core.java.concurrency.page.ImageInfo;

/**
 * Simulation of a page rendering, including images, using Futures and the CompletionService.
 * This sample takes advantage of completion service in order to process images as it becomes available.
 * Based on article: https://www.infoq.com/articles/Functional-Style-Callbacks-Using-CompletableFuture.
 */
public class PageRendererV2 {

	private ExecutorService executor;
	
	public PageRendererV2() {
		executor = Executors.newFixedThreadPool(2);
	}
	
	public void renderPage(CharSequence source) throws Exception {
		List<ImageInfo> info = scanForImageInfo(source);
		CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);
		info.forEach(imageInfo-> completionService.submit(imageInfo::downloadImage));
		renderText(source);
		
		for (int i = 0; i < info.size(); i++) {
			Future<ImageData> imageFuture = completionService.take();
			renderImage(imageFuture.get());
		}
		executor.shutdown();
	}
	
	private List<ImageInfo> scanForImageInfo(CharSequence source) {
		ArrayList<ImageInfo> infos = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			infos.add(new ImageInfo("ID"+i));
		}
		return infos;
	}
	
	public void renderText(CharSequence source) {
		System.out.println("Text: " + source);
	}
	
	public void renderImage(ImageData id) {
		System.out.println(new String(id.getData()));
	}
	
	public static void main(String[] args) throws Exception {
		new PageRendererV2().renderPage("The web page");
	}

}
