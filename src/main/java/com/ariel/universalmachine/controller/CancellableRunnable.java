package com.ariel.universalmachine.controller;

public interface CancellableRunnable extends Runnable {

	void cancel();
}
