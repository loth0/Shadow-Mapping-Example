package com.base.util;

import com.base.main.MainComponent;

public class Core {
	private boolean isRunning;
	private int width;
	private int height;
	private double frameTime;
	private double time;
	private static double passedTime;
	private static double delta = 0;

	public Core(int width, int height, double framerate) {
		this.isRunning = false;
		this.width = width;
		this.height = height;
		this.frameTime = 1.0 / framerate;
	}

	public void createWindow(String title) {
		Window.createWindow(width, height, title);
		MainComponent.setUp();
	}

	public void start() {
		if (isRunning)
			return;

		run();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}

	private void run() {
		isRunning = true;

		int frames = 0;
		float frameCounter = 0;

		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		double oldTime = 0;

		while (isRunning) {
			boolean render = false;

			double startTime = Time.getTime();
			passedTime = startTime - lastTime;
			lastTime = startTime;
			time += passedTime;
			double newTime = startTime;

			unprocessedTime += passedTime;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				unprocessedTime -= frameTime;

				if (Window.isCloseRequested())
					stop();

				if (frameCounter >= 1.0) {
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				delta = newTime - oldTime;
				oldTime = newTime;
				MainComponent.render();
				Window.render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		cleanUp();
	}

	private void cleanUp() {
		MainComponent.cleanUp();
		Window.dispose();
	}

	public float getDelta() {
		return (float)delta;
	}
	
	public float getTime() {
		return (float) (time * 1000);
	}
}
