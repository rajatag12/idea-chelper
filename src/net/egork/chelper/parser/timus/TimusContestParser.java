package net.egork.chelper.parser.timus;

import net.egork.chelper.parser.ContestParser;
import net.egork.chelper.parser.StringParser;
import net.egork.chelper.parser.TaskParser;
import net.egork.chelper.util.FileUtilities;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Egor Kulikov (egor@egork.net)
 */
public class TimusContestParser extends TimusParser implements ContestParser {
	public static final TimusContestParser INSTANCE = new TimusContestParser();

	public Collection<String> parse(String id) {
		String mainPage;
		try {
			mainPage = FileUtilities.getWebPageContent("http://acm.timus.ru/problemset.aspx?space=" + id);
		} catch (IOException e) {
			return Collections.emptyList();
		}
		List<String> tasks = new ArrayList<String>();
		StringParser parser = new StringParser(mainPage);
		int index = 1;
		while (true) {
			try {
				parser.advance(true, "<A HREF=\"problem.aspx?space=" + id + "&amp;num=" + index);
				tasks.add(id + " " + index++);
			} catch (ParseException e) {
				break;
			}
		}
		return tasks;		
	}

	public TaskParser getTaskParser() {
		return TimusTaskParser.INSTANCE;
	}
}