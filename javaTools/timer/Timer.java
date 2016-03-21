package timer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Timer {

	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("Supply the timestamp file path");
			System.exit(85);
		}
		
		String timestamp = args[0];
		Map <Integer, Date> submitted = new HashMap<Integer, Date>();
		Map <Integer, Date> retrieved = new HashMap<Integer, Date>();
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(timestamp));
		String line;
		while ((line = br.readLine()) != null) {
			// If line is a comment or empty, ignore it.
			if(line.startsWith("submitted")) {
				Date date = parseDateRepresentation(line);
				int job = parseJob(line);
				submitted.put(job,date);
			}
			else if(line.startsWith("retrieving")) {
				Date date = parseDateRepresentation(line);
				int job = parseJob(line);
				retrieved.put(job, date);
			}

		}
		br.close();
		for(int i : submitted.keySet()) {
			if(retrieved.containsKey(i)) {
				System.out.println("total time for job " + i + " was " + dateDifference(retrieved.get(i),submitted.get(i)));
			}
			else {
				System.out.println("i never retrieved output for job " + i);
			}
		}


	}

	private static int parseJob(String line)
	{
		return Integer.parseInt(line.substring(0,line.indexOf(":") - 1).replaceAll("[\\D]", ""));
	}
	private static Date parseDateRepresentation(String line)
	{
		Date date = null;
		try {
			date = parseDate(line.substring(line.indexOf(":") + 1).trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return date;
	}

	private static Difference dateDifference(Date end, Date start)
	{
		return new Difference(end,start);
	}

	private static Date parseDate(String dateRepresentation) throws ParseException
	{

		DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
		Date date = format.parse(dateRepresentation);
		return date;
	}

	static class Difference {
		private long hours;
		private long minutes;
		private long seconds;
		private long days;
		public Difference(Date end, Date start)
		{
			long temp = (end.getTime() - start.getTime());
			if(temp < 0) temp = -temp;
			this.days = temp / (1000*3600*24);
			temp -= days*(1000*3600*24);
			this.hours = temp / (1000*3600);
			temp -= hours*(1000*3600);
			this.minutes = temp / (1000*60);
			temp -= minutes*(1000*60);
			this.seconds = temp / 1000;
		}

		public long getHours() {
			return hours;
		}
		public long getMinutes() {
			return minutes;
		}
		public long getSeconds() {
			return seconds;
		}
		public long getDays() {
			return days;
		}

		@Override
		public String toString() {
			return "days=" + days + ", hours=" + hours + ", minutes=" + minutes + ", seconds=" + seconds;
		}



	}


}
