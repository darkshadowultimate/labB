package com.insubria.it.threads.gameThread.dictionary;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Loader {

	public Dictionary loadDictionaryFromFile(ZipInputStream dictionaryZipInputStream) throws IOException {
		Dictionary dictionary = new Dictionary();
		/*ZipFile zf = new ZipFile(file);*/

		/*Enumeration<? extends ZipEntry> entries = zf.entries();*/

		/*BufferedReader in = new BufferedReader(
			new InputStreamReader(
				zf.getInputStream(
					zf.getEntry("dictionaries/th_it_IT_v2.dat")),
					"ISO8859-15"
			)
		);*/

		ZipEntry entry;

		String entryName = "dictionaries/th_it_IT_v2.dat";

		while ((entry = dictionaryZipInputStream.getNextEntry()) != null) {
			if (entry.getName().equals(entryName)) {
				// process your file
				// stop looking for your file - you've already found it
				break;
			}
		}

		BufferedReader in = new BufferedReader(
			new InputStreamReader(
				dictionaryZipInputStream,
				"ISO8859-15"
			)
		);

		String temp;
		int numDefs = 0;
		Term lastTerm = null;
		while ((temp = in.readLine()) != null) {
			if (temp.startsWith("ISO8859-15"))
				continue;
			if (numDefs > 0) {
				analyzeDefinition(temp, lastTerm);
				numDefs--;
			} else {
				String[] result;
				result = temp.split("\\|");
				numDefs = Integer.parseInt(result[1]);
				lastTerm = new Term(result[0]);
				dictionary.addTerm(lastTerm);
			}
		}
		dictionaryZipInputStream.close();
		in.close();
		return dictionary;
	}

	public void analyzeDefinition(String s, Term t) {
		String[] result;
		result = s.split("\\|");
		Definition d = new Definition(getItemType(result[0]));
		d.setDefinition(result[0]);
		if (result.length > 1)
			for (int i = 1; i < result.length; i++)
				d.addSynonym(result[i]);

		t.addDefinition(d);
	}

	public ItemType getItemType(String s) {
		if (s.startsWith("(s.m."))
			return ItemType.sostantivo_maschile;
		else if (s.startsWith("(s.f."))
			return ItemType.sostantivo_femminile;
		else if (s.startsWith("(v."))
			return ItemType.verbo;
		else if (s.startsWith("(agg."))
			return ItemType.aggettivo;
		else if (s.startsWith("(avv."))
			return ItemType.avverbio;
		else if (s.startsWith("(cong."))
			return ItemType.congiunzione;
		else if (s.startsWith("(prep."))
			return ItemType.preposizione;
		else if (s.startsWith("(inter."))
			return ItemType.interiezione;
		else
			return ItemType.other;
	}

	/*public static void main(String[] args) {
		Loader loader = new Loader();
		//String file_dizionario = "dict_it.oxt";
		//File dizionario = new File(file_dizionario);

		try {

			ClassLoader classLoader = new Object() {}.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("dict_it.oxt");

			Dictionary d = loader.loadDictionaryFromFile(new ZipInputStream(inputStream));

			//for(String key: d.getKeys()) { System.out.println(d.getTerm(key)); }

			System.out.println(d.getSize());
			System.out.println(d.getTerm("studente"));
			System.out.println(d.getTerm("studentato"));
			System.out.println(d.getTerm("studio"));
			System.out.println(d.getTerm("stud"));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKey e) {
			e.printStackTrace();
		}
	}*/
}
