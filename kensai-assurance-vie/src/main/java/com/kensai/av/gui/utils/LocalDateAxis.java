package com.kensai.av.gui.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.chart.Axis;
import javafx.util.Duration;
import javafx.util.StringConverter;

import com.sun.javafx.charts.ChartLayoutAnimator;

public class LocalDateAxis extends Axis<LocalDate> {
	private enum Interval {
		DECADE(ChronoUnit.DECADES, 1), 
		YEAR(ChronoUnit.YEARS, 1), 
		MONTH_6(ChronoUnit.MONTHS, 6), 
		MONTH_3(ChronoUnit.MONTHS, 3), 
		MONTH_1(ChronoUnit.MONTHS, 1), 
		WEEK(ChronoUnit.WEEKS, 1), 
		DAY(ChronoUnit.DAYS, 1), 
		HOUR_12(ChronoUnit.HOURS, 12), 
		HOUR_6(ChronoUnit.HOURS, 6), 
		HOUR_3(ChronoUnit.HOURS, 3), 
		HOUR_1(ChronoUnit.HOURS, 1), 
		MINUTE_15(ChronoUnit.MINUTES, 15), 
		MINUTE_5(ChronoUnit.MINUTES, 5), 
		MINUTE_1(ChronoUnit.MINUTES, 1), 
		SECOND_15(ChronoUnit.SECONDS, 15), 
		SECOND_5(ChronoUnit.SECONDS, 5), 
		SECOND_1(ChronoUnit.SECONDS, 1), 
		MILLISECOND(ChronoUnit.MILLIS, 1);

		private final ChronoUnit INTERVAL;
		private final int AMOUNT;

		private Interval(final ChronoUnit INTERVAL, final int AMOUNT) {
			this.INTERVAL = INTERVAL;
			this.AMOUNT = AMOUNT;
		}
	}

	private final LongProperty currentLowerBound;
	private final LongProperty currentUpperBound;
	private final ObjectProperty<StringConverter<LocalDate>> tickLabelFormatter;
	private LocalDate minDate;
	private LocalDate maxDate;
	private ObjectProperty<LocalDate> lowerBound;
	private ObjectProperty<LocalDate> upperBound;
	private ChartLayoutAnimator animator;
	private Object currentAnimationID;
	private Interval actualInterval;

	// ******************** Constructors **************************************
	public LocalDateAxis() {
		this("", LocalDate.now(), LocalDate.now().plusDays(1), true);
	}

	public LocalDateAxis(final boolean AUTO_RANGING) {
		this("", LocalDate.now(), LocalDate.now().plusDays(1), AUTO_RANGING);
	}

	public LocalDateAxis(final LocalDate LOWER_BOUND, final LocalDate UPPER_BOUND) {
		this("", LOWER_BOUND, UPPER_BOUND, true);
	}

	public LocalDateAxis(final LocalDate LOWER_BOUND, final LocalDate UPPER_BOUND, final boolean AUTO_RANGING) {
		this("", LOWER_BOUND, UPPER_BOUND, AUTO_RANGING);
	}

	public LocalDateAxis(final String AXIS_LABEL, final LocalDate LOWER_BOUND, final LocalDate UPPER_BOUND, final boolean AUTO_RANGING) {
		super();
		if (LOWER_BOUND.isAfter(UPPER_BOUND))
			throw new IllegalArgumentException("Lower bound must be before upper bound!!!");
		currentLowerBound = new SimpleLongProperty(this, "currentLowerBound");
		currentUpperBound = new SimpleLongProperty(this, "currentUpperBound");
		tickLabelFormatter = new ObjectPropertyBase<StringConverter<LocalDate>>() {
			@Override
			protected void invalidated() {
				if (!isAutoRanging()) {
					invalidateRange();
					requestAxisLayout();
				}
			}

			@Override
			public Object getBean() {
				return this;
			}

			@Override
			public String getName() {
				return "tickLabelFormatter";
			}
		};
		lowerBound = new ObjectPropertyBase<LocalDate>(LOWER_BOUND) {
			@Override
			protected void invalidated() {
				if (!isAutoRanging()) {
					invalidateRange();
					requestAxisLayout();
				}
			}

			@Override
			public Object getBean() {
				return LocalDateAxis.this;
			}

			@Override
			public String getName() {
				return "lowerBound";
			}
		};
		upperBound = new ObjectPropertyBase<LocalDate>(UPPER_BOUND) {
			@Override
			protected void invalidated() {
				if (!isAutoRanging()) {
					invalidateRange();
					requestAxisLayout();
				}
			}

			@Override
			public Object getBean() {
				return LocalDateAxis.this;
			}

			@Override
			public String getName() {
				return "upperBound";
			}
		};
		animator = new ChartLayoutAnimator(this);
		actualInterval = Interval.DECADE;
		setLabel(AXIS_LABEL);
		setAutoRanging(AUTO_RANGING);
	}

	// ******************** Methods *******************************************
	@Override
	protected Object getRange() {
		return new Object[] { getLowerBound(), getUpperBound() };
	}

	@Override
	protected void setRange(final Object RANGE, final boolean ANIMATED) {
		Object[] range = (Object[]) RANGE;
		LocalDate oldLowerBound = getLowerBound();
		LocalDate oldUpperBound = getUpperBound();
		LocalDate lower = (LocalDate) range[0];
		LocalDate upper = (LocalDate) range[1];
		setLowerBound(lower);
		setUpperBound(upper);

		if (ANIMATED) {
			animator.stop(currentAnimationID);
			currentAnimationID = animator.animate(new KeyFrame(Duration.ZERO, new KeyValue(currentLowerBound, toMillis(oldLowerBound)), new KeyValue(
				currentUpperBound, toMillis(oldUpperBound))), new KeyFrame(Duration.millis(700), new KeyValue(currentLowerBound, toMillis(lower)),
				new KeyValue(currentUpperBound, toMillis(upper))));

		} else {
			currentLowerBound.set(toMillis(getLowerBound()));
			currentUpperBound.set(toMillis(getUpperBound()));
		}
	}

	@Override
	public void invalidateRange(final List<LocalDate> LIST) {
		super.invalidateRange(LIST);

		Collections.sort(LIST);
		if (LIST.isEmpty()) {
			minDate = maxDate = LocalDate.now();
		} else if (LIST.size() == 1) {
			minDate = maxDate = LIST.get(0);
		} else if (LIST.size() > 1) {
			minDate = LIST.get(0);
			maxDate = LIST.get(LIST.size() - 1);
		}
	}

	@Override
	protected Object autoRange(final double LENGTH) {
		if (isAutoRanging()) {
			return new Object[] { minDate, maxDate };
		} else {
			if (null == getLowerBound() || null == getUpperBound()) {
				throw new IllegalArgumentException("If autoRanging is false, a lower and upper bound must be set.");
			}
			return getRange();
		}
	}

	@Override
	public double getZeroPosition() {
		return 0;
	}

	@Override
	public double getDisplayPosition(LocalDate date) {
		final double length = getSide().isHorizontal() ? getWidth() : getHeight();

		// Get the difference between the max and min date.
		double diff = currentUpperBound.get() - currentLowerBound.get();

		// Get the actual range of the visible area.
		// The minimal date should start at the zero position, that's why we subtract it.
		double range = length - getZeroPosition();

		// Then get the difference from the actual date to the min date and divide it by the total difference.
		// We get a value between 0 and 1, if the date is within the min and max date.
		double d = (toMillis(date) - currentLowerBound.get()) / diff;

		// Multiply this percent value with the range and add the zero offset.
		if (getSide().isVertical()) {
			return getHeight() - d * range + getZeroPosition();
		} else {
			return d * range + getZeroPosition();
		}
	}

	@Override
	public LocalDate getValueForDisplay(double displayPosition) {
		final double length = getSide().isHorizontal() ? getWidth() : getHeight();

		// Get the difference between the max and min date.
		double diff = currentUpperBound.get() - currentLowerBound.get();

		// Get the actual range of the visible area.
		// The minimal date should start at the zero position, that's why we subtract it.
		double range = length - getZeroPosition();
		Instant instant;
		if (getSide().isVertical()) {
			// displayPosition = getHeight() - ((date - lowerBound) / diff) * range + getZero
			// date = displayPosition - getZero - getHeight())/range * diff + lowerBound
			instant = Instant.ofEpochMilli((long) ((displayPosition - getZeroPosition() - getHeight()) / -range * diff + currentLowerBound.get()));
		} else {
			// displayPosition = ((date - lowerBound) / diff) * range + getZero
			// date = displayPosition - getZero)/range * diff + lowerBound
			instant = Instant.ofEpochMilli((long) ((displayPosition - getZeroPosition()) / range * diff + currentLowerBound.get()));
		}
		return LocalDate.ofEpochDay(instant.toEpochMilli());
	}

	@Override
	public boolean isValueOnAxis(LocalDate date) {
		return toMillis(date) > currentLowerBound.get() && toMillis(date) < currentUpperBound.get();
	}

	@Override
	protected List<LocalDate> calculateTickValues(final double VALUE, final Object RANGE) {
		Object[] range = (Object[]) RANGE;
		LocalDate lower = (LocalDate) range[0];
		LocalDate upper = (LocalDate) range[1];

		List<LocalDate> dateList = new ArrayList<>();
		LocalDate calendar = LocalDate.now();

		// The preferred gap which should be between two tick marks.
		double averageTickGap = 100;
		double averageTicks = VALUE / averageTickGap;

		List<LocalDate> previousDateList = new ArrayList<>();

		Interval previousInterval = Interval.values()[0];

		// Starting with the greatest interval, add one of each calendar unit.
		for (Interval interval : Interval.values()) {
			// Reset the calendar.
			calendar = LocalDate.from(lower);
			// Clear the list.
			dateList.clear();
			previousDateList.clear();
			actualInterval = interval;

			// Loop as long we exceeded the upper bound.
			while (calendar.isBefore(upper)) {
				dateList.add(calendar);
				calendar = calendar.plus(interval.AMOUNT, interval.INTERVAL);
			}

			// Then check the size of the list. If it is greater than the amount of ticks, take that list.
			if (dateList.size() > averageTicks) {
				calendar = LocalDate.from(lower);
				// Recheck if the previous interval is better suited.
				while (calendar.isBefore(upper) || calendar.isEqual(upper)) {
					previousDateList.add(calendar);
					calendar = calendar.plus(previousInterval.AMOUNT, previousInterval.INTERVAL);
				}
				break;
			}

			previousInterval = interval;
		}
		if (previousDateList.size() - averageTicks > averageTicks - dateList.size()) {
			dateList = previousDateList;
			actualInterval = previousInterval;
		}

		// At last add the upper bound.
		dateList.add(upper);

		//TODO
//		List<LocalDate> evenDateList = makeDatesEven(dateList, calendar);
		List<LocalDate> evenDateList = dateList;
		
		// If there are at least three dates, check if the gap between the lower date and the second date is at least half
		// the gap of the second and third date.
		// Do the same for the upper bound.
		// If gaps between dates are to small, remove one of them.
		// This can occur, e.g. if the lower bound is 25.12.2013 and years are shown. Then the next year shown would be
		// 2014 (01.01.2014) which would be too narrow to 25.12.2013.
		if (evenDateList.size() > 2) {

			LocalDate secondDate = evenDateList.get(1);
			LocalDate thirdDate = evenDateList.get(2);
			LocalDate lastDate = evenDateList.get(dateList.size() - 2);
			LocalDate previousLastDate = evenDateList.get(dateList.size() - 3);

			// If the second date is too near by the lower bound, remove it.

			if (secondDate.toEpochDay() - lower.toEpochDay() < thirdDate.toEpochDay() - secondDate.toEpochDay()) {
				evenDateList.remove(secondDate);
			}

			// If difference from the upper bound to the last date is less than the half of the difference of the previous
			// two dates,
			// we better remove the last date, as it comes to close to the upper bound.
			if (upper.toEpochDay() - lastDate.toEpochDay() < ((lastDate.toEpochDay() - previousLastDate.toEpochDay() * 0.5))) {
				evenDateList.remove(lastDate);
			}
		}
		return evenDateList;
	}

	@Override
	protected void layoutChildren() {
		if (!isAutoRanging()) {
			currentLowerBound.set(toMillis(getLowerBound()));
			currentUpperBound.set(toMillis(getUpperBound()));
		}
		super.layoutChildren();
	}

	@Override
	protected String getTickMarkLabel(LocalDate date) {

		StringConverter<LocalDate> converter = getTickLabelFormatter();
		if (converter != null) {
			return converter.toString(date);
		}

		DateTimeFormatter dateTimeFormat;
		LocalDate calendar = LocalDate.from(date);

		if (actualInterval.INTERVAL == ChronoUnit.YEARS && calendar.getMonthValue() == 0 && calendar.getDayOfMonth() == 1) {
			dateTimeFormat = DateTimeFormatter.ofPattern("yyyy");
		} else if (actualInterval.INTERVAL == ChronoUnit.MONTHS && calendar.getDayOfMonth() == 1) {
			dateTimeFormat = DateTimeFormatter.ofPattern("MMMM yy");
		} else {
			switch (actualInterval.INTERVAL) {
			case DAYS:
			case WEEKS:
			default:
				dateTimeFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
				break;
			case HOURS:
			case MINUTES:
				dateTimeFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
				break;
			case SECONDS:
				dateTimeFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
				break;
			case MILLIS:
				dateTimeFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
				break;
			}
		}

		return dateTimeFormat.format(date);
	}

	public final LocalDate getLowerBound() {
		return lowerBound.get();
	}

	public final void setLowerBound(LocalDate date) {
		lowerBound.set(date);
	}

	public final ObjectProperty<LocalDate> lowerBoundProperty() {
		return lowerBound;
	}

	public final LocalDate getUpperBound() {
		return upperBound.get();
	}

	public final void setUpperBound(LocalDate date) {
		upperBound.set(date);
	}

	public final ObjectProperty<LocalDate> upperBoundProperty() {
		return upperBound;
	}

	public final StringConverter<LocalDate> getTickLabelFormatter() {
		return tickLabelFormatter.getValue();
	}

	public final void setTickLabelFormatter(StringConverter<LocalDate> value) {
		tickLabelFormatter.setValue(value);
	}

	public final ObjectProperty<StringConverter<LocalDate>> tickLabelFormatterProperty() {
		return tickLabelFormatter;
	}

	@Override
	public double toNumericValue(final LocalDate DATE) {
		return toMillis(DATE);
	}

	@Override
	public LocalDate toRealValue(final double VALUE) {
		return toLocalDate((long) VALUE);
	}

	private long toMillis(final LocalDate DATE_TIME) {
		return DATE_TIME.toEpochDay();
	}

	private LocalDate toLocalDate(final long MILLIS) {
		return LocalDate.ofEpochDay(MILLIS);
	}
}
