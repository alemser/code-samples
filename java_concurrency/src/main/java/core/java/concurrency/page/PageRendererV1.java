package core.java.concurrency.page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Simulation of a page rendering, including images, using only Futures.
 * Based on article: https://www.infoq.com/articles/Functional-Style-Callbacks-Using-CompletableFuture.
 */
public class PageRendererV1 {

	private ExecutorService executor;
	
	public PageRendererV1() {
		executor = Executors.newFixedThreadPool(2);
	}
	
	public void renderPage(CharSequence source) {
		List<ImageInfo> info = scanForImageInfo(source);
		final Callable<List<ImageData>> task = () -> info.stream().map(ImageInfo::downloadImage)
				.collect(Collectors.toList());
		Future<List<ImageData>> images = executor.submit(task);
		renderText(source);
		try {
			final List<ImageData> imageDatas = images.get();
			imageDatas.forEach(this::renderImage);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			images.cancel(true);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
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
	
	public static void main(String[] args) {
		new PageRendererV1().renderPage("The web page");
	}

}
