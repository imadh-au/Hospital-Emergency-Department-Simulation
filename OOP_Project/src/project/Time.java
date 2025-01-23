package project;

public class Time {
	 private int mins;
	 private int hours;

	 public Time(int mins, int hours) {
	      this.mins = mins;
	      this.hours = hours;
	 }

	 public Time() {
	     mins=0;
	     hours=0;
	 }
	 
	 public Time addMin(int min) {
		 mins += min;
		 hours = hours+mins/60;
		 mins%=60;
		 return new Time(hours,mins);
	 }

	 public Time add(Time t) {
	     int totalMinutes = mins + t.mins;
	     int totalHours = hours + t.hours + (totalMinutes / 60);
	     totalMinutes %= 60;
	     return new Time(totalMinutes, totalHours);
	 }
	 public void sethours(int h) {hours=h;}
	 public void setmins(int m) {mins=m;}
	 public int gethours() {return hours;}
	 public int getmins() {return mins;}
	 public boolean equals(Time t) {
	     return mins == t.mins && hours == t.hours;
	 }

	 public String display() {
	     return ("Hours: " + hours + ", Minutes: " + mins);
	 }
}
