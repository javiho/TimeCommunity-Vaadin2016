package com.example.myapplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

//KORJATTAVA TÄMÄN LUOKAN ONGELMAT
//DUMMY-SIVUT LUOTAVA JA NAVIGAATIO NIILLE TÄSTÄ SIVUSTA

/*
 * Assumes that the first day of a week is Monday.
 */
public class ScheduleComponent extends CustomComponent{
	
	private Course[] courses;
	private LocalDateTime displayedWeek;
	
	private int firstDataColumn = 1;
	private int firstDataRow = 1;
	private LocalTime firstHourOfDay = LocalTime.of(8, 0);
	private LocalTime lastHourOfDay = LocalTime.of(23, 0);
	private GridLayout eventsLayout;
	private WeekNavigationComponent weekNavigationComponent;
	VerticalLayout mainLayout;
	private final int weekDaysCount = 7;

	public ScheduleComponent(Course[] courses){
		this.courses = courses;
		this.mainLayout = new VerticalLayout();
        this.eventsLayout = new GridLayout(8, 16);
        this.displayedWeek = LocalDateTime.now();
        this.weekNavigationComponent = new WeekNavigationComponent(displayedWeek, this);
        eventsLayout.setSizeFull();
        mainLayout.setSizeFull();
        weekNavigationComponent.setSizeFull();
        //setSizeFull();
        eventsLayout.setWidth("750px");
        eventsLayout.setHeight("750px");
        //weekNavigationComponent.setWidth("750px"); EI TOIMI
        //weekNavigationComponent.setHeight("80px"); EI TOIMI
		updateEventsLayout(LocalDate.of(2016, Month.OCTOBER, 10), this.courses);
		mainLayout.addComponent(weekNavigationComponent);
		mainLayout.addComponent(eventsLayout);
		setCompositionRoot(mainLayout);
    }
	
	public void onPreviousWeekButtonClick(Button.ClickEvent e){
		LocalDateTime previousWeek = this.displayedWeek.plusWeeks(-1);
		updateTime(previousWeek);
	}
	
	public void onNextWeekButtonClick(Button.ClickEvent e){
		LocalDateTime nextWeek = this.displayedWeek.plusWeeks(1);
		updateTime(nextWeek);
	}
	
	private void updateTime(LocalDateTime weekStartDt){
		LocalDate weekStartDate = weekStartDt.toLocalDate();
		this.displayedWeek = weekStartDt;
		updateEventsLayout(weekStartDate, courses);
		weekNavigationComponent.update(weekStartDt);
	}
	
	/*
	 * MISTÄ TÄTÄ KUTSUTAAN?
	 */
	private void updateCourses(Course[] courses){
		//TODO
	}
	
	/*
	 * OVERLAPPAAMISTA EI OTETA VIELÄ HUOMIOON
	 * ENTÄ JOS ALKAA ENNEN ENSIMMÄISTÄ KELLONAIKAA?
	 * ENTÄ JOS ENSIMMÄINEN KELLONAIKA MUUTTUU?
	 * PITÄISIKÖ MÄÄRITELLÄ VIIMEINEN KELLONAIKA, KUN KERRAN ENSIMMÄINENKIN ON MÄÄRITELTY?
	 * MIKÄ ON VIIKON ENSIMMÄINEN PÄIVÄ?
	 * Pre-condition: none of the CourseSessions in courses must span from one day to another.
	 */
	private void updateEventsLayout(LocalDate weekStartDate, Course[] courses){
		GridLayout el = eventsLayout;
		el.removeAllComponents();
		LocalDate[] weekDays = new LocalDate[weekDaysCount];
		for(int i = 0; i < weekDays.length; i++){
			weekDays[i] = weekStartDate.plusDays(i);
		}
		//Add column titles
		int weekDayIndex = 0;
		for(int c = firstDataColumn; c < el.getColumns(); c++){
			el.addComponent(new Label(weekDays[weekDayIndex].toString()), c, 0);
			weekDayIndex++;
		}
		//Add row titles
		int firstHourInt = firstHourOfDay.getHour();
		for(int r = firstDataRow; r < el.getRows(); r++){
			int hour = firstHourInt + (r - firstDataRow);
			el.addComponent(new Label(hour + " - " + (hour + 1)), 0, r);
		}
		fillDataAreaWithEmptyLabels();
		//Add course sessions
		CourseSession[] courseSessions = getLecturesInWeek(weekStartDate, courses);
		LocalDateTime firstDtOfWeek = LocalDateTime.of(weekDays[0], firstHourOfDay);
		LocalDateTime lastDtOfWeek = LocalDateTime.of(weekDays[weekDays.length - 1], lastHourOfDay);
		Panel p;
		CourseSession cs;
		LocalDateTime csStart;
		LocalDateTime csEnd;
		int row;
		int endRow;
		int col;
		for(int i = 0; i < courseSessions.length; i++){
			cs = courseSessions[i];
			csStart = cs.getTime();
			if(!shouldBeInDisplayedArea(csStart, firstDtOfWeek, lastDtOfWeek)){
				continue;
			}
			row = getRowByDateTime(csStart, firstDtOfWeek, lastDtOfWeek);
			col = getColumnByDateTime(csStart ,firstDtOfWeek, lastDtOfWeek);
			csEnd = csStart.plusHours(cs.getDurationHours() - 1);
			endRow = getRowByDateTime(csEnd, firstDtOfWeek, lastDtOfWeek);
			p = createCellContent(cs.getCourse().getName());
			clearGridLayoutArea(el, col, row, col, endRow);
			el.addComponent(p, col, row, col, endRow);
		}
	}
	
	private void fillDataAreaWithEmptyLabels(){
		GridLayout el = eventsLayout;
		Panel p;
		for(int c = firstDataColumn; c < el.getColumns(); c++){
			for(int r = firstDataRow; r < el.getRows(); r++){
				p = createCellContent("");
				el.addComponent(p, c, r);
			}
		}
	}
	
	private boolean shouldBeInDisplayedArea(LocalDateTime target, LocalDateTime firstDateTime,
			LocalDateTime lastDateTime){
		LocalDateTime ldt = target.truncatedTo(ChronoUnit.MINUTES);
		return getRowByDateTime(ldt, firstDateTime, lastDateTime) != -1 &&
				getColumnByDateTime(ldt, firstDateTime, lastDateTime) != -1;
	}
	
	/*
	 * firstDateTime is first time of first date which is displayed
	 * (e.g. 8 o'clock in a certain Monday).
	 * lastDateTime is the starting date time of the last date time which is displayed
	 * (e.g. 23:00 in a certain Sunday).
	 * Returns -1 is there is not appropriate row.
	 */
	private int getRowByDateTime(LocalDateTime dateTime, LocalDateTime firstDateTime,
			LocalDateTime lastDateTime){
		LocalTime time = dateTime.toLocalTime();
		LocalTime firstTime = firstDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
		LocalTime lastTime = lastDateTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
		int row = firstDataRow;
		for(LocalTime lt = firstTime; lt.compareTo(lastTime) <= 0; lt = lt.plusHours(1)){
			if(lt.equals(time)){
				return row;
			}
			row++;
		}
		return -1;
	}
	
	private int getColumnByDateTime(LocalDateTime dateTime, LocalDateTime firstDateTime,
			LocalDateTime lastDateTime){
		LocalDate date = dateTime.toLocalDate();
		LocalDate firstDate = firstDateTime.toLocalDate();
		LocalDate lastDate = lastDateTime.toLocalDate();
		int col = firstDataColumn;
		for(LocalDate ld = firstDate; ld.compareTo(lastDate) <= 0; ld = ld.plusDays(1)){
			if(ld.equals(date)){
				return col;
			}
			col++;
		}
		return -1;
	}
	
	private CourseSession[] getLecturesInWeek(LocalDate weekStartDate, Course[] courses){
		LocalDateTime weekStartTime = weekStartDate.atTime(0, 0);
		LocalDateTime weekEndTime = weekStartTime.plusDays(weekDaysCount);
		ArrayList<CourseSession> courseSessions = new ArrayList<CourseSession>();
		Course course;
		ArrayList<CourseSession> courseSessionsOfOneCourse;
		for(int i = 0; i < courses.length; i++){
			course = courses[i];
			courseSessionsOfOneCourse = course.getCourseSessionsInInterval(weekStartTime,
					weekEndTime);
			courseSessions.addAll(courseSessionsOfOneCourse);
		}
		return courseSessions.toArray(new CourseSession[0]);
	}
	
	private Panel createCellContent(String labelContent){
		Panel p = new Panel();
		Label l = new Label(labelContent);
		p.setContent(l);
		p.setSizeFull();
		l.setSizeFull();
		return p;
	}
	
	/*
	 * Pre-condition: row1 <= row2 && col1 <= col2.
	 */
	private void clearGridLayoutArea(GridLayout gl, int col1, int row1, int col2, int row2){
		if(row1 > row2 || col1 > col2){
			throw new Error("Erroneous parameters");
		}
		for(int c = col1; c <= col2; c++){
			for(int r = row1; r <= row2; r++){
				gl.removeComponent(c, r);
			}
		}
	}
	
}
