package Model;

import org.jfree.chart.axis.Timeline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class CustomTimeline implements Timeline, Cloneable, Serializable {

    /** Defines a day segment size in ms. */
    public static final long DAY_SEGMENT_SIZE = 2* 6* 6* 1000;

    /** Defines a one hour segment size in ms. */
    public static final long HOUR_SEGMENT_SIZE = 6* 6* 1000;

    /** Defines a 15-minute segment size in ms. */
    public static final long FIFTEEN_MINUTE_SEGMENT_SIZE = 1* 6* 1000;

    /** Defines a one-minute segment size in ms. */
    public static final long MINUTE_SEGMENT_SIZE = 6* 1000;

    private ArrayList<CustomTimeline.Segment> segmentList;
    private long startTime = -1;

    public CustomTimeline(){
        this.segmentList = new ArrayList<>();
    }

    public void addSegment(Date d){
        Date end = d;
        end.setDate(end.getDate()+1);
        this.segmentList.add(new Segment(this.segmentList.size(),d.getTime(),end.getTime()));
        if(this.startTime == -1){
            this.startTime = d.getTime();
        }
    }

    @Override
    public long toTimelineValue(long millisecond) {
        return millisecond;
    }

    @Override
    public long toTimelineValue(Date date) {
        return date.getTime();
    }

    @Override
    public long toMillisecond(long timelineValue) {
        return timelineValue;
    }

    @Override
    public boolean containsDomainValue(long millisecond) {
        return this.segmentList.get(0).getSegmentStart() >= millisecond && this.segmentList.get(this.segmentList.size()-1).getSegmentEnd() <= millisecond;
    }

    @Override
    public boolean containsDomainValue(Date date) {
        return this.segmentList.get(0).getSegmentStart() >= date.getTime() && this.segmentList.get(this.segmentList.size()-1).getSegmentEnd() <= date.getTime();
    }

    @Override
    public boolean containsDomainRange(long fromMillisecond, long toMillisecond) {
        return this.segmentList.get(0).getSegmentStart() >= fromMillisecond && this.segmentList.get(this.segmentList.size()-1).getSegmentEnd() <= toMillisecond;
    }

    @Override
    public boolean containsDomainRange(Date fromDate, Date toDate) {
        return this.segmentList.get(0).getSegmentStart() >= fromDate.getTime() && this.segmentList.get(this.segmentList.size()-1).getSegmentEnd() <= toDate.getTime();
    }

    public class Segment implements Comparable, Cloneable, Serializable {

        /**
         * The segment number.
         */
        protected long segmentNumber;

        /**
         * The segment start.
         */
        protected long segmentStart;

        /**
         * The segment end.
         */
        protected long segmentEnd;

        /**
         * A reference point within the segment.
         */
        protected long millisecond;

        /**
         * Protected constructor only used by sub-classes.
         */
        protected Segment() {
            // empty
        }

        /**
         * Creates a segment for two given dates and the segment number.
         *
         * @param segmentNumber The number of the Segment.
         * @param start The start date of the Segment.
         * @param end The end date of the Segment.
         */
        protected Segment(int segmentNumber, long start, long end) {
            this.segmentStart = start;
            this.segmentEnd = end;
            this.millisecond = this.segmentStart / 2 + this.segmentEnd / 2;
            this.segmentNumber = segmentNumber;
        }


        /**
         * Returns the segment number of this segment. Segments start at 0.
         *
         * @return The segment number.
         */
        public long getSegmentNumber() {
            return this.segmentNumber;
        }

        /**
         * Returns always one (the number of segments contained in this
         * segment).
         *
         * @return The segment count (always for this class).
         */
        public long getSegmentCount() {
            return 1;
        }

        /**
         * Gets the start of this segment in ms.
         *
         * @return The segment start.
         */
        public long getSegmentStart() {
            return this.segmentStart;
        }

        /**
         * Gets the end of this segment in ms.
         *
         * @return The segment end.
         */
        public long getSegmentEnd() {
            return this.segmentEnd;
        }

        /**
         * Returns the millisecond used to reference this segment (always
         * between the segmentStart and segmentEnd).
         *
         * @return The millisecond.
         */
        public long getMillisecond() {
            return this.millisecond;
        }

        /**
         * Returns a {@link java.util.Date} that represents the reference point
         * for this segment.
         *
         * @return The date.
         */
        public Date getDate() {
            return new Date(this.millisecond);
        }

        /**
         * Returns true if a particular millisecond is contained in this
         * segment.
         *
         * @param millis the millisecond to verify.
         * @return <code>true</code> if the millisecond is contained in the
         * segment.
         */
        public boolean contains(long millis) {
            return (this.segmentStart <= millis && millis <= this.segmentEnd);
        }

        /**
         * Returns <code>true</code> if an interval is contained in this
         * segment.
         *
         * @param from the start of the interval.
         * @param to   the end of the interval.
         * @return <code>true</code> if the interval is contained in the
         * segment.
         */
        public boolean contains(long from, long to) {
            return (this.segmentStart <= from && to <= this.segmentEnd);
        }

        /**
         * Returns <code>true</code> if a segment is contained in this segment.
         *
         * @param segment the segment to test for inclusion
         * @return <code>true</code> if the segment is contained in this
         * segment.
         */
        public boolean contains(CustomTimeline.Segment segment) {
            return contains(segment.getSegmentStart(), segment.getSegmentEnd());
        }

        /**
         * Returns <code>true</code> if this segment is contained in an
         * interval.
         *
         * @param from the start of the interval.
         * @param to   the end of the interval.
         * @return <code>true</code> if this segment is contained in the
         * interval.
         */
        public boolean contained(long from, long to) {
            return (from <= this.segmentStart && this.segmentEnd <= to);
        }

        /**
         * Returns a segment that is the intersection of this segment and the
         * interval.
         *
         * @param from the start of the interval.
         * @param to   the end of the interval.
         * @return A segment.
         */
        public CustomTimeline.Segment intersect(long from, long to) {
            if (from <= this.segmentStart && this.segmentEnd <= to) {
                return this;
            } else {
                return null;
            }
        }

        /**
         * Returns <code>true</code> if this segment is wholly before another
         * segment.
         *
         * @param other the other segment.
         * @return A boolean.
         */
        public boolean before(CustomTimeline.Segment other) {
            return (this.segmentEnd < other.getSegmentStart());
        }

        /**
         * Returns <code>true</code> if this segment is wholly after another
         * segment.
         *
         * @param other the other segment.
         * @return A boolean.
         */
        public boolean after(CustomTimeline.Segment other) {
            return (this.segmentStart > other.getSegmentEnd());
        }

        /**
         * Tests an object (usually another <code>Segment</code>) for equality
         * with this segment.
         *
         * @param object The other segment to compare with us
         * @return <code>true</code> if we are the same segment
         */
        public boolean equals(Object object) {
            if (object instanceof CustomTimeline.Segment) {
                CustomTimeline.Segment other = (CustomTimeline.Segment) object;
                return (this.segmentNumber == other.getSegmentNumber()
                        && this.segmentStart == other.getSegmentStart()
                        && this.segmentEnd == other.getSegmentEnd()
                        && this.millisecond == other.getMillisecond());
            } else {
                return false;
            }
        }

        /**
         * Returns a copy of ourselves or <code>null</code> if there was an
         * exception during cloning.
         *
         * @return A copy of this segment.
         */
        public CustomTimeline.Segment copy() {
            try {
                return (CustomTimeline.Segment) this.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        /**
         * Will compare this Segment with another Segment (from Comparable
         * interface).
         *
         * @param object The other Segment to compare with
         * @return -1: this < object, 0: this.equal(object) and
         * +1: this > object
         */
        public int compareTo(Object object) {
            CustomTimeline.Segment other = (CustomTimeline.Segment) object;
            if (this.before(other)) {
                return -1;
            } else if (this.after(other)) {
                return +1;
            } else {
                return 0;
            }
        }

        /**
         * Calculate the segment number relative to the segment group. This
         * will be a number between and segmentsGroup-1. This value is
         * calculated from the segmentNumber. Special care is taken for
         * negative segmentNumbers.
         *
         * @return The segment number.
         */
        private long getSegmentNumberRelativeToGroup() {
            return this.segmentNumber;
        }


        /**
         * Increments the internal attributes of this segment by a number of
         * segments.
         *
         * @param n Number of segments to increment.
         */
        public void inc(long n) {
            this.segmentNumber += n;
            long m = n * CustomTimeline.DAY_SEGMENT_SIZE;
            this.segmentStart += m;
            this.segmentEnd += m;
            this.millisecond += m;
        }

        /**
         * Increments the internal attributes of this segment by one segment.
         * The exact time incremented is segmentSize.
         */
        public void inc() {
            inc(1);
        }

        /**
         * Decrements the internal attributes of this segment by a number of
         * segments.
         *
         * @param n Number of segments to decrement.
         */
        public void dec(long n) {
            this.segmentNumber -= n;
            long m = n * CustomTimeline.DAY_SEGMENT_SIZE;
            this.segmentStart -= m;
            this.segmentEnd -= m;
            this.millisecond -= m;
        }

        /**
         * Decrements the internal attributes of this segment by one segment.
         * The exact time decremented is segmentSize.
         */
        public void dec() {
            dec(1);
        }

        /**
         * Moves the index of this segment to the beginning if the segment.
         */
        public void moveIndexToStart() {
            this.millisecond = this.segmentStart;
        }

        /**
         * Moves the index of this segment to the end of the segment.
         */
        public void moveIndexToEnd() {
            this.millisecond = this.segmentEnd;
        }

        @Override
        public String toString() {
            return "Segment{" +
                    "segmentNumber=" + segmentNumber +
                    ", segmentStart=" + segmentStart +
                    ", segmentEnd=" + segmentEnd +
                    ", millisecond=" + millisecond +
                    '}';
        }
    }
}
