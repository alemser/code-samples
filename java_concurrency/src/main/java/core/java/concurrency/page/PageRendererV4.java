package core.java.concurrency.page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

/**
 * Based on article: https://www.infoq.com/articles/Functional-Style-Callbacks-Using-CompletableFuture.
 */
public class PageRendererV4 {
	
	static final int MAX_DOWNLOADS = 10;
	
	private ExecutorService executor;
	
	public PageRendererV4() {
		executor = Executors.newWorkStealingPool();
	}
	
	Function<ImageInfo, ImageData> infoToData = imageInfo -> { 
		   CompletableFuture<ImageData> imageDataFuture = 
		       CompletableFuture.supplyAsync(imageInfo::downloadImage, executor); 
		   try { 
		       return imageDataFuture.get(6, TimeUnit.SECONDS); 
		   } catch (InterruptedException e) { 
		       Thread.currentThread().interrupt(); 
		       imageDataFuture.cancel(true); 
		       return ImageData.createIcon(e); 
		   } catch (ExecutionException e) { 
		       throw new RuntimeException(e.getCause()); 
		   } catch (TimeoutException e) { 
		       return ImageData.createIcon(e); 
		   } 
		};	
	
	public void renderPage(CharSequence source) {
		List<ImageInfo> info = scanForImageInfo(source); 
	       info.forEach(imageInfo -> 
	           CompletableFuture.runAsync(() -> 
	               renderImage(infoToData.apply(imageInfo)), executor));
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
		new PageRendererV4().renderPage("The web page");
		Thread.currentThread().join(20000);
	}

}
