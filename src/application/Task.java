package application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Task {
	private Boolean completed;
	private Date dueDate;
	private String dueDateString;
	private String title;
	private Integer percent;
	private String description;
	
	public Task(String inString) throws ParseException{
		//				    new Task("21/02/2018|GUI Laboratory 1|15|JavaFX Laboratory"),
		String delims = "[|]";
		String[] tokens = inString.split(delims);
		
	    DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
	    this.dueDate =  df.parse(tokens[0]);  		
		
	    this.dueDateString = tokens[0];
		this.title = tokens[1];
		this.percent = Integer.parseInt(tokens[2]);
		
		this.completed = (this.percent == 100);
		this.description = tokens[3];
	}
	

    public Task(Boolean completed, Date dueDate, String dueDateString, String title, Integer percent,
			String description) {
		super();
		this.completed = completed;
		this.dueDate = dueDate;
		this.dueDateString = dueDateString;
		this.title = title;
		this.percent = percent;
		this.description = description;
	}


	public final BooleanProperty isCompletedProperty() {
    	BooleanProperty completedProperty = new SimpleBooleanProperty();
    	completedProperty.set(completed);
		return completedProperty;
    }
	
	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Date getDueDate() {
		return dueDate;
	}
	
	public String getDueDateString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(dueDate);   
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDueDateString(String dueDateString) {
		this.dueDateString = dueDateString;
	}
	
}
