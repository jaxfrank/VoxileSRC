package com.jaxfrank.voxile.util.multiThread;

public interface ThreadCompleteListener {
	void notifyOfThreadComplete(final Runnable thread);
}
