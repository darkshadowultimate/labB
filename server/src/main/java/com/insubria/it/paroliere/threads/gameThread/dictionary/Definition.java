package com.insubria.it.paroliere.threads.gameThread.dictionary;

import java.util.HashSet;
import java.util.Set;

public class Definition {
	private ItemType type;
	private String definition;
	private Set<String> synonymSet;
	
	public Definition(ItemType type) {
		super();
		this.type = type;
		this.definition="";
		this.synonymSet=new HashSet<String>();
	}
	
	public boolean addSynonym(String synonym) {
		return synonymSet.add(synonym);
	}
	
	public void setDefinition(String definition) {
		this.definition=definition;
	}
	
	public String toString(){
		String ret=definition+ " ";
		for(String synonym : synonymSet) {
			ret=  ret+ " "+synonym+",";
		}
		return ret;
	}

	public ItemType getType() {
		return type;
	}
}
