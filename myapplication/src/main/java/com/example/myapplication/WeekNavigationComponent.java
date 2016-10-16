package com.example.myapplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class WeekNavigationComponent extends CustomComponent {

	private HorizontalLayout layout;
	private ScheduleComponent parent;
	
	public WeekNavigationComponent(LocalDateTime dt, ScheduleComponent parent){
		this.parent = parent;
		layout = new HorizontalLayout();
		layout.setWidth("750px");
        layout.setHeight("80px");
		initialize(dt);
		setCompositionRoot(layout);
	}
	
	public void initialize(LocalDateTime dt){
		Label weekLabel = new Label("Unknown week");
		Button previousButton = new Button("<<Previous", parent::onPreviousWeekButtonClick);
		Button nextButton = new Button("Next>>", parent::onNextWeekButtonClick);
		//previousButton.addL
		layout.addComponent(weekLabel);
		layout.addComponent(previousButton);
		layout.addComponent(nextButton);
		weekLabel.setSizeUndefined();
		previousButton.setSizeUndefined();
		nextButton.setSizeUndefined();
		layout.setComponentAlignment(weekLabel, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(previousButton, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(nextButton, Alignment.MIDDLE_CENTER);
        update(dt);
	}
	
	public void update(LocalDateTime dt){
		LocalDate date = dt.toLocalDate();
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
		int weekNumber = date.get(woy);
		Label weekLabel = (Label) layout.getComponent(0);
		weekLabel.setValue("Week " + weekNumber);
	}
	
}
