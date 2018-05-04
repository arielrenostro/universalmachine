package com.ariel.universalmachine.model;

public interface CancellableRunnable extends Runnable {

	void cancel();
}
