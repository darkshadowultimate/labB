package com.insubria.it.server.threads.gameThread.dictionary;

public class InvalidKey extends Exception {

		private static final long serialVersionUID = 1L;
		
		public InvalidKey(){
			super("Parola non contenuta nel dizionario");
		}

	}
